package edu.cmu.cs.lti.ark.fn.data.prep.formats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javax.annotation.concurrent.Immutable;
import java.util.List;

import static com.google.common.collect.ImmutableList.copyOf;
import static edu.cmu.cs.lti.ark.fn.data.prep.formats.AllLemmaTags.*;
import static edu.cmu.cs.lti.ark.util.IntRanges.xrange;

/**
 * Represents one sentence in conll format
 * Conll sentences are one token per line. Sentences are separated by blank lines.
 *
 * @author sthomson@cs.cmu.edu
 */
@Immutable
public class Sentence {
	private final ImmutableList<Token> tokens;

	public Sentence(Iterable<Token> tokens) {
		this.tokens = copyOf(tokens);
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public static Sentence fromAllLemmaTagsArray(String[][] parse) {
		List<Token> toks = Lists.newArrayList();
		for(int i : xrange(parse[0].length)) {
			final Token token =
					new Token(i,
							parse[PARSE_TOKEN_ROW][i],
							parse[PARSE_LEMMA_ROW][i],
							parse[PARSE_POS_ROW][i].substring(0, 1),
							parse[PARSE_POS_ROW][i],
							null, null, null, null, null);
			toks.add(token);
		}
		return new Sentence(toks);
	}

	public String[][] toAllLemmaTagsArray() {
		final int length = tokens.size();
		String[][] result = new String[NUM_PARSE_ROWS][length];
		for(Token token : tokens) {
			final Integer id = token.getId();
			assert id != null;
			for(int row : xrange(NUM_PARSE_ROWS)) result[row][id] = "";
			result[PARSE_TOKEN_ROW][id] = token.getForm();
			result[PARSE_POS_ROW][id] = token.getPostag() == null ? "_": token.getPostag();
			result[PARSE_NE_ROW][id] = "O";
			result[PARSE_DEPREL_ROW][id] = token.getDeprel() == null ? "_" : token.getDeprel();
			result[PARSE_HEAD_ROW][id] = token.getHead() == null ? "0" : token.getHead().toString();
			result[PARSE_LEMMA_ROW][id] = token.getLemma() == null ? "_" : token.getLemma();
		}
		return result;
	}
}

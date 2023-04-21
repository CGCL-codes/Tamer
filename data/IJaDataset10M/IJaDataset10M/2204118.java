package regex.matchers;

import regex.MatchResult;

/**
 * The interface for classes that can match part of a character sequence. Instances should 
 * <em>be stateless</em> with respect to the character sequence they're matched against, so 
 * that they may be reused for matching against several character sequences.
 * 
 * @author Daniel Kristensen
 */
public interface Matcher {

    /**
	 * Tries to match starting from startIndex. Returns the number of characters matched,
	 * or -1 if no match was found (0 means a match of the empty String).
	 */
    int match(CharSequence toMatch, int startIndex, MatchResult matchResult);
}

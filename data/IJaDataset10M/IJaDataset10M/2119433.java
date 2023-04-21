package net.sf.jtypogrify.token;

import java.util.List;

/**
 * Tokenizes HTML text for processing.
 *
 * @author Andy Marek (andrew.marek@aurora.org)
 * @version Jun 3, 2007
 */
public interface Tokenizer {

    /**
	 * Parses {@code text} into many tokens according to the rules of the implementing class.
	 *
	 * @param text The text to parse which will often be HTML.
	 * @return Return the tokens as a {@link List} of {@link Token} objects.
	 */
    List<Token> parse(String text);
}

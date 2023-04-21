package org.macchiato.tokenfilter;

import org.macchiato.tokenizer.Token;
import org.macchiato.tokenizer.Tokenizer;

/**
 * Filter tokens which have less than specified characters.
 *
 * @author fdietz
 */
public class MinTokenLengthFilter extends AbstractTokenizerFilter {

    private int length;

    /**
	 * @param tokenizer
	 */
    public MinTokenLengthFilter(Tokenizer tokenizer, int length) {
        super(tokenizer);
        this.length = length;
    }

    /**
	 * @see org.macchiato.tokenizer.Tokenizer#nextToken()
	 */
    public Token nextToken() {
        Token token = getTokenizer().nextToken();
        if (token.equals(Token.EOF)) return token;
        if (token.equals(Token.SKIP)) return token;
        if (token.getString().length() < length) return Token.SKIP;
        return token;
    }
}

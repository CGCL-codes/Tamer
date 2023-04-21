package org.scribble.lang.java.parser;

import org.scribble.parser.Token;
import org.scribble.parser.TokenType;

/**
 * This class represents the token list.
 *
 */
public class TokenList {

    /**
	 * This is the default constructor.
	 */
    public TokenList() {
    }

    /**
	 * This method returns the next available token.
	 * 
	 * @return The next token
	 */
    public Token nextToken() {
        Token ret = null;
        if (m_currentToken < m_tokens.size()) {
            ret = m_tokens.get(m_currentToken++);
        } else {
            ret = new Token("", TokenType.EndOfFile, 0, 0);
        }
        return (ret);
    }

    /**
	 * This method peeks a specified number of tokens ahead.
	 * 
	 * @param num The number of lookahead
	 * @return The token
	 */
    public Token lookahead(int num) {
        Token ret = null;
        if (m_currentToken + num < m_tokens.size()) {
            ret = m_tokens.get(m_currentToken + num);
        } else {
            ret = new Token("", TokenType.EndOfFile, 0, 0);
        }
        return (ret);
    }

    /**
	 * This method returns whether there are more tokens
	 * available.
	 * 
	 * @return Whether more tokens are available
	 */
    public boolean moreTokens() {
        return (m_currentToken < m_tokens.size());
    }

    /**
	 * This method returns the list of tokens.
	 * 
	 * @return The list of tokens
	 */
    public java.util.List<Token> getTokens() {
        return (m_tokens);
    }

    private java.util.List<Token> m_tokens = new java.util.Vector<Token>();

    private int m_currentToken = 0;
}

package com.aptana.ide.lexer.codebased;

import com.aptana.ide.lexer.ILexer;
import com.aptana.ide.lexer.ILexerBuilder;
import com.aptana.ide.lexer.ITokenList;
import com.aptana.ide.lexer.LexerException;
import com.aptana.ide.lexer.TokenList;

/**
 * @author Kevin Lindsey
 */
public class CodeBasedLexerBuilder implements ILexerBuilder {

    private ILexer _lexer;

    /**
	 * CodeBasedLexerBuilder
	 */
    public CodeBasedLexerBuilder() {
        this._lexer = this.createLexer();
    }

    /**
	 * @see com.aptana.ide.lexer.ILexerBuilder#getTokens(java.lang.String)
	 */
    public ITokenList getTokens(String language) {
        ITokenList result = null;
        if (this._lexer != null) {
            result = this._lexer.getTokens(language);
        }
        return result;
    }

    /**
	 * @see com.aptana.ide.lexer.ILexerBuilder#addTokenList(com.aptana.ide.lexer.ITokenList)
	 */
    public void addTokenList(ITokenList list) {
        this._lexer.addLanguage(list);
    }

    /**
	 * @see com.aptana.ide.lexer.ILexerBuilder#buildLexer()
	 */
    public ILexer buildLexer() throws LexerException {
        this._lexer.seal();
        return this._lexer;
    }

    /**
	 * createLexer
	 *
	 * @return ILexer
	 */
    protected ILexer createLexer() {
        return new CodeBasedLexer();
    }

    /**
	 * createTokenList
	 *
	 * @param language
	 * @return ITokenList
	 */
    protected ITokenList createTokenList(String language) {
        return new TokenList(language);
    }
}

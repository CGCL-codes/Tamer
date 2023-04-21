package com.aptana.ide.editor.scriptdoc.tests;

import com.aptana.ide.editor.scriptdoc.lexing.ScriptDocTokenTypes;
import com.aptana.ide.editor.scriptdoc.parsing.ScriptDocMimeType;
import com.aptana.ide.editor.scriptdoc.parsing.ScriptDocParser;
import com.aptana.ide.lexer.ILexer;
import com.aptana.ide.lexer.LexerException;
import com.aptana.ide.lexer.TokenCategories;
import com.aptana.ide.lexer.tests.TestTokenBase;

/**
 * @author Kevin Lindsey
 */
public class TestPunctuatorTokens extends TestTokenBase {

    /**
	 * @see com.aptana.ide.lexer.tests.TestTokenBase#createLexer()
	 */
    protected ILexer createLexer() throws Exception {
        ScriptDocParser parser = new ScriptDocParser();
        return parser.getLexer();
    }

    /**
	 * @see com.aptana.ide.lexer.tests.TestTokenBase#getLanguage()
	 */
    protected String getLanguage() {
        return ScriptDocMimeType.MimeType;
    }

    /**
	 * testLCurly
	 * 
	 * @throws LexerException
	 */
    public void testLCurly() throws LexerException {
        this.lexer.setGroup("documentation");
        this.lexemeTest("{", TokenCategories.PUNCTUATOR, ScriptDocTokenTypes.LCURLY);
    }

    /**
	 * testDollaLCurly
	 * 
	 * @throws LexerException
	 */
    public void testDollaLCurly() throws LexerException {
        this.lexer.setGroup("documentation");
        this.lexemeTest("${", TokenCategories.PUNCTUATOR, ScriptDocTokenTypes.DOLLAR_LCURLY);
    }

    /**
	 * testPound
	 * 
	 * @throws LexerException
	 */
    public void testPound() throws LexerException {
        this.lexer.setGroup("documentation");
        this.lexemeTest("#", TokenCategories.PUNCTUATOR, ScriptDocTokenTypes.POUND);
    }

    /**
	 * testLBracket
	 * 
	 * @throws LexerException
	 */
    public void testLBracket() throws LexerException {
        this.lexer.setGroup("documentation");
        this.lexemeTest("[", TokenCategories.PUNCTUATOR, ScriptDocTokenTypes.LBRACKET);
    }

    /**
	 * testRBracket
	 * 
	 * @throws LexerException
	 */
    public void testRBracket() throws LexerException {
        this.lexer.setGroup("documentation");
        this.lexemeTest("]", TokenCategories.PUNCTUATOR, ScriptDocTokenTypes.RBRACKET);
    }

    /**
	 * testRCurly
	 * 
	 * @throws LexerException
	 */
    public void testRCurly() throws LexerException {
        this.lexer.setGroup("identifier");
        this.lexemeTest("}", TokenCategories.PUNCTUATOR, ScriptDocTokenTypes.RCURLY);
    }

    /**
	 * testEllipsis
	 * 
	 * @throws LexerException
	 */
    public void testEllipsis() throws LexerException {
        this.lexer.setGroup("identifier");
        this.lexemeTest("...", TokenCategories.PUNCTUATOR, ScriptDocTokenTypes.ELLIPSIS);
    }

    /**
	 * testIdentiferLBracket
	 * 
	 * @throws LexerException
	 */
    public void testIdentiferLBracket() throws LexerException {
        this.lexer.setGroup("identifier");
        this.lexemeTest("[", TokenCategories.PUNCTUATOR, ScriptDocTokenTypes.LBRACKET);
    }

    /**
	 * testIdentiferRBracket
	 * 
	 * @throws LexerException
	 */
    public void testIdentiferRBracket() throws LexerException {
        this.lexer.setGroup("identifier");
        this.lexemeTest("]", TokenCategories.PUNCTUATOR, ScriptDocTokenTypes.RBRACKET);
    }

    /**
	 * testComma
	 * 
	 * @throws LexerException
	 */
    public void testComma() throws LexerException {
        this.lexer.setGroup("identifier");
        this.lexemeTest(",", TokenCategories.PUNCTUATOR, ScriptDocTokenTypes.COMMA);
    }

    /**
	 * testPipe
	 * 
	 * @throws LexerException
	 */
    public void testPipe() throws LexerException {
        this.lexer.setGroup("identifier");
        this.lexemeTest("|", TokenCategories.PUNCTUATOR, ScriptDocTokenTypes.PIPE);
    }

    /**
	 * testForwardSlash
	 * 
	 * @throws LexerException
	 */
    public void testForwardSlash() throws LexerException {
        this.lexer.setGroup("identifier");
        this.lexemeTest("/", TokenCategories.PUNCTUATOR, ScriptDocTokenTypes.FORWARD_SLASH);
    }
}

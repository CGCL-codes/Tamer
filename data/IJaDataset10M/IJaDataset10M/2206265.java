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
public class TestDelimiterTokens extends TestTokenBase {

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
	 * testStartDocumentation
	 */
    public void testStartDocumentation() {
        this.lexemeTest("/**", TokenCategories.DELIMITER, ScriptDocTokenTypes.START_DOCUMENTATION);
    }

    /**
	 * testEndDocumentation
	 * 
	 * @throws LexerException
	 */
    public void testEndDocumentation() throws LexerException {
        this.lexer.setGroup("documentation");
        this.lexemeTest("*/", TokenCategories.DELIMITER, ScriptDocTokenTypes.END_DOCUMENTATION);
    }

    /**
	 * testStar
	 * 
	 * @throws LexerException
	 */
    public void testStar() throws LexerException {
        this.lexer.setGroup("indent");
        this.lexemeTest("*", TokenCategories.DELIMITER, ScriptDocTokenTypes.STAR);
    }
}

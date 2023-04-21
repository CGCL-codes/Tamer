package com.aptana.ide.lexer.tests;

import junit.framework.TestCase;
import com.aptana.ide.lexer.ILexer;
import com.aptana.ide.lexer.ITokenList;
import com.aptana.ide.lexer.Lexeme;
import com.aptana.ide.lexer.LexerBuilder;
import com.aptana.ide.lexer.ascii.AsciiLexerBuilder;
import com.aptana.ide.lexer.ascii.AsciiTokenList;

/**
 * @author Kevin Lindsey
 */
public class TestStatements extends TestCase {

    private static String LANGUAGE = "unit-tests";

    private static String GROUP = "default";

    private ILexer _lexer;

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        LexerBuilder lexerBuilder = new AsciiLexerBuilder();
        ITokenList tokenList = new AsciiTokenList(LANGUAGE);
        lexerBuilder.addTokenList(tokenList);
        tokenList.add("category", "type", "aaa", GROUP, GROUP);
        tokenList.add("category", "type", "bbb", GROUP, GROUP);
        tokenList.add("category", "type", "ccc", GROUP, GROUP);
        this._lexer = lexerBuilder.buildLexer();
        this._lexer.setLanguageAndGroup(LANGUAGE, GROUP);
    }

    /**
	 * typingTests
	 * 
	 * @param source
	 * @return Lexeme
	 */
    protected Lexeme parseTest(String source) {
        this._lexer.setSource(source);
        return this._lexer.getNextLexeme();
    }

    /**
	 * Test simple assignment
	 * 
	 * @throws Exception
	 */
    public void testCharacter() throws Exception {
        Lexeme lexeme;
        lexeme = this.parseTest("a");
        assertNull(lexeme);
        lexeme = this.parseTest("aa");
        assertNull(lexeme);
        lexeme = this.parseTest("aaa");
        assertEquals(lexeme.getText(), "aaa");
        lexeme = this.parseTest("aaaa");
        assertEquals(lexeme.getText(), "aaa");
    }
}

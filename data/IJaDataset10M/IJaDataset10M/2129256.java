package com.aptana.ide.editor.html.tests;

import com.aptana.ide.editor.html.lexing.HTMLTokenTypes;
import com.aptana.ide.editor.html.parsing.HTMLMimeType;
import com.aptana.ide.editor.html.parsing.HTMLParser;
import com.aptana.ide.lexer.ILexer;
import com.aptana.ide.lexer.TokenCategories;
import com.aptana.ide.lexer.tests.TestTokenBase;

/**
 * @author Kevin Lindsey
 *
 */
public class TestPunctuatorTokens extends TestTokenBase {

    /**
	 * @see com.aptana.ide.lexer.tests.TestTokenBase#createLexer()
	 */
    protected ILexer createLexer() throws Exception {
        HTMLParser parser = new HTMLParser();
        return parser.getLexer();
    }

    /**
	 * @see com.aptana.ide.lexer.tests.TestTokenBase#getLanguage()
	 */
    protected String getLanguage() {
        return HTMLMimeType.MimeType;
    }

    /**
	 * testATTLISTPunctuator
	 */
    public void testATTLISTPunctuator() {
        this.lexemeTest("<!ATTLIST", TokenCategories.PUNCTUATOR, HTMLTokenTypes.ATTLIST_DECL);
    }

    /**
	 * testCDATAEndPunctuator
	 */
    public void testCDATAEndPunctuator() {
        this.lexemeTest("]]>", TokenCategories.PUNCTUATOR, HTMLTokenTypes.CDATA_END);
    }

    /**
	 * testCDATAStartPunctuator
	 */
    public void testCDATAStartPunctuator() {
        this.lexemeTest("<![CDATA[", TokenCategories.PUNCTUATOR, HTMLTokenTypes.CDATA_START);
    }

    /**
	 * testDOCTYPEPunctuator
	 */
    public void testDOCTYPEPunctuator() {
        this.lexemeTest("<!DOCTYPE", TokenCategories.PUNCTUATOR, HTMLTokenTypes.DOCTYPE_DECL);
    }

    /**
	 * testELEMENTPunctuator
	 */
    public void testELEMENTPunctuator() {
        this.lexemeTest("<!ELEMENT", TokenCategories.PUNCTUATOR, HTMLTokenTypes.ELEMENT_DECL);
    }

    /**
	 * testEndTagPunctuator
	 */
    public void testEndTagPunctuator() {
        this.lexemeTest("</element", TokenCategories.PUNCTUATOR, HTMLTokenTypes.END_TAG);
    }

    /**
	 * testENTITYPunctuator
	 */
    public void testENTITYPunctuator() {
        this.lexemeTest("<!ENTITY", TokenCategories.PUNCTUATOR, HTMLTokenTypes.ENTITY_DECL);
    }

    /**
	 * testEqualPunctuator
	 */
    public void testEqualPunctuator() {
        this.lexemeTest("=", TokenCategories.PUNCTUATOR, HTMLTokenTypes.EQUAL);
    }

    /**
	 * testGreaterPunctuator
	 */
    public void testGreaterPunctuator() {
        this.lexemeTest(">", TokenCategories.PUNCTUATOR, HTMLTokenTypes.GREATER_THAN);
    }

    /**
	 * testLBracketPunctuator
	 */
    public void testLBracketPunctuator() {
        this.lexemeTest("[", TokenCategories.PUNCTUATOR, HTMLTokenTypes.LBRACKET);
    }

    /**
	 * testNOTATIONPunctuator
	 */
    public void testNOTATIONPunctuator() {
        this.lexemeTest("<!NOTATION", TokenCategories.PUNCTUATOR, HTMLTokenTypes.NOTATION_DECL);
    }

    /**
	 * testPercentOpenPunctuator
	 */
    public void testPercentOpenPunctuator() {
        this.lexemeTest("<%", TokenCategories.PUNCTUATOR, HTMLTokenTypes.PERCENT_OPEN);
    }

    /**
	 * testPIOpenPunctuator
	 */
    public void testPIOpenPunctuator() {
        this.lexemeTest("<?instruction", TokenCategories.PUNCTUATOR, HTMLTokenTypes.PI_OPEN);
    }

    /**
	 * testPlusPunctuator
	 */
    public void testPlusPunctuator() {
        this.lexemeTest("+", TokenCategories.PUNCTUATOR, HTMLTokenTypes.PLUS);
    }

    /**
	 * testQuestionPunctuator
	 */
    public void testQuestionPunctuator() {
        this.lexemeTest("?", TokenCategories.PUNCTUATOR, HTMLTokenTypes.QUESTION);
    }

    /**
	 * testQuestionGreaterPunctuator
	 */
    public void testQuestionGreaterPunctuator() {
        this.lexemeTest("?>", TokenCategories.PUNCTUATOR, HTMLTokenTypes.QUESTION_GREATER_THAN);
    }

    /**
	 * testRBracketPunctuator
	 */
    public void testRBracketPunctuator() {
        this.lexemeTest("]", TokenCategories.PUNCTUATOR, HTMLTokenTypes.RBRACKET);
    }

    /**
	 * testSlashGreaterPunctuator
	 */
    public void testSlashGreaterPunctuator() {
        this.lexemeTest("/>", TokenCategories.PUNCTUATOR, HTMLTokenTypes.SLASH_GREATER_THAN);
    }

    /**
	 * testStarPunctuator
	 */
    public void testStarPunctuator() {
        this.lexemeTest("*", TokenCategories.PUNCTUATOR, HTMLTokenTypes.STAR);
    }

    /**
	 * testStartTagPunctuator
	 */
    public void testStartTagPunctuator() {
        this.lexemeTest("<element", TokenCategories.PUNCTUATOR, HTMLTokenTypes.START_TAG);
    }

    /**
	 * testATTLISTPunctuator
	 */
    public void testXMLPunctuator() {
        this.lexemeTest("<?xml", TokenCategories.PUNCTUATOR, HTMLTokenTypes.XML_DECL);
    }
}

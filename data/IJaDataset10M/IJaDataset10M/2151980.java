package com.aptana.ide.editor.xml.tests;

import com.aptana.ide.editor.xml.lexing.XMLTokenTypes;
import com.aptana.ide.editor.xml.parsing.XMLMimeType;
import com.aptana.ide.editor.xml.parsing.XMLParser;
import com.aptana.ide.lexer.ILexer;
import com.aptana.ide.lexer.LexerException;
import com.aptana.ide.lexer.TokenCategories;
import com.aptana.ide.lexer.tests.TestTokenBase;

/**
 * @author Kevin Lindsey
 */
public class TestLiteralTokens extends TestTokenBase {

    /**
	 * @see com.aptana.ide.lexer.tests.TestTokenBase#createLexer()
	 */
    protected ILexer createLexer() throws Exception {
        XMLParser parser = new XMLParser();
        return parser.getLexer();
    }

    /**
	 * @see com.aptana.ide.lexer.tests.TestTokenBase#getLanguage()
	 */
    protected String getLanguage() {
        return XMLMimeType.MimeType;
    }

    /**
	 * testName
	 */
    public void testName() {
        this.lexemeTest("name", TokenCategories.LITERAL, XMLTokenTypes.NAME);
    }

    /**
	 * testDashedName
	 */
    public void testDashedName() {
        this.lexemeTest("dashed-name", TokenCategories.LITERAL, XMLTokenTypes.NAME);
    }

    /**
	 * testSingleQuotedString
	 */
    public void testSingleQuotedString() {
        this.lexemeTest("'string'", TokenCategories.LITERAL, XMLTokenTypes.STRING);
    }

    /**
	 * testDoubleQuotedString
	 */
    public void testDoubleQuotedString() {
        this.lexemeTest("\"string\"", TokenCategories.LITERAL, XMLTokenTypes.STRING);
    }

    /**
	 * testEntityRef
	 */
    public void testEntityRef() {
        this.lexemeTest("&ref;", TokenCategories.LITERAL, XMLTokenTypes.ENTITY_REF);
    }

    /**
	 * testDecimalCharRef
	 */
    public void testDecimalCharRef() {
        this.lexemeTest("&#13;", TokenCategories.LITERAL, XMLTokenTypes.CHAR_REF);
    }

    /**
	 * testHexadecimalCharRef
	 */
    public void testHexadecimalCharRef() {
        this.lexemeTest("&#x0A;", TokenCategories.LITERAL, XMLTokenTypes.CHAR_REF);
    }

    /**
	 * testPercentRef
	 */
    public void testPercentRef() {
        this.lexemeTest("%ref;", TokenCategories.LITERAL, XMLTokenTypes.PE_REF);
    }

    /**
	 * testCDATAText
	 * 
	 * @throws LexerException
	 */
    public void testCDATAText() throws LexerException {
        this.lexer.setGroup("cdata-section");
        this.lexemeTest("this\ris\nsome\r\nCDATA]]>", TokenCategories.LITERAL, XMLTokenTypes.CDATA_TEXT);
    }

    /**
	 * testProcessingInstruction
	 *
	 * @throws LexerException
	 */
    public void testProcessingInstruction() throws LexerException {
        this.lexer.setGroup("processing-instruction");
        this.lexemeTest("processing instruction?>", TokenCategories.LITERAL, XMLTokenTypes.PI_TEXT);
    }

    /**
	 * testProcessingInstruction
	 *
	 * @throws LexerException
	 */
    public void testText() throws LexerException {
        this.lexer.setGroup("text");
        this.lexemeTest("this is some text", TokenCategories.LITERAL, XMLTokenTypes.TEXT);
    }
}

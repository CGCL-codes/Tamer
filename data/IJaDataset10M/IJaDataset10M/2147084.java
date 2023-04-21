package org.chimaira.xion;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class XionParser {

    private XionParserPI parser;

    private Reader input;

    private XionExpressionHandler handler;

    private XionLexerCmp lexerCmp;

    private XionParserCmp parserCmp;

    public XionParser() {
        lexerCmp = new XionLexerCmp();
        parserCmp = new XionParserCmp();
        parserCmp.lexer = lexerCmp.lexer;
        this.parser = parserCmp.parser;
        this.handler = parserCmp.handler;
    }

    /**
 	 * �C�x���g�n���h����ݒ肷��B
 	 * �C���^�[�t�F�[�X���݊��Ƃ��邽�߂̃O���[�R�[�h�B
	 */
    public void setExpressionHandler(XionExpressionHandler handler) {
        this.handler = handler;
        this.parserCmp.handler = this.handler;
    }

    /**
	 * Xion�̕�����\���̃p�[�W���O���s���B
	 */
    public void parse(Reader input) throws IOException, XionLexException, XionParseException, XionHandleException {
        this.input = input;
        this.lexerCmp.setInput(this.input);
        this.parser.parse();
    }

    public void parse(InputStream input) throws IOException, XionLexException, XionParseException, XionHandleException {
        this.parse(new InputStreamReader(input));
    }
}

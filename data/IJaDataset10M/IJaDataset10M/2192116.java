package com.aptana.ide.editor.js.parsing.nodes;

import com.aptana.ide.io.SourceWriter;
import com.aptana.ide.lexer.Lexeme;
import com.aptana.ide.parsing.IParseState;

/**
 * @author Kevin Lindsey
 */
public class JSPrimitiveNode extends JSParseNode {

    private char _quote;

    /**
	 * JSPrimitiveNode
	 * 
	 * @param typeIndex
	 * @param parseState
	 * @param startLexeme
	 */
    public JSPrimitiveNode(int typeIndex, IParseState parseState, Lexeme startLexeme) {
        super(typeIndex, parseState, startLexeme);
        String text = null;
        if (startLexeme != null) {
            text = startLexeme.getText();
        } else {
            if (typeIndex == JSParseNodeTypes.NULL) {
                text = "null";
            } else {
                throw new IllegalArgumentException(Messages.JSPrimitiveNode_StartLexemeMustNotBeNull);
            }
        }
        if (typeIndex == JSParseNodeTypes.IDENTIFIER) {
            if (text.length() >= 2) {
                char first = text.charAt(0);
                char last = text.charAt(text.length() - 1);
                if (first == last && (first == '"' || first == '\"')) {
                    text = text.substring(1, text.length() - 1);
                    this._quote = first;
                }
            }
        }
        this.setAttribute("text", text);
    }

    /**
	 * @see com.aptana.ide.parsing.nodes.IParseNode#getText()
	 */
    public String getText() {
        return this.getAttribute("text");
    }

    /**
	 * @see com.aptana.ide.editor.js.parsing.nodes.JSParseNode#getSource(com.aptana.ide.io.SourceWriter)
	 */
    public void getSource(SourceWriter writer) {
        switch(this.getTypeIndex()) {
            case JSParseNodeTypes.FALSE:
            case JSParseNodeTypes.NULL:
            case JSParseNodeTypes.TRUE:
            case JSParseNodeTypes.NUMBER:
            case JSParseNodeTypes.REGULAR_EXPRESSION:
            case JSParseNodeTypes.THIS:
                writer.print(this.getAttribute("text"));
                break;
            case JSParseNodeTypes.IDENTIFIER:
                if (this._quote != '\0') {
                    writer.print(this._quote).print(this.getAttribute("text")).print(this._quote);
                } else {
                    writer.print(this.getAttribute("text"));
                }
                break;
            default:
                break;
        }
    }
}

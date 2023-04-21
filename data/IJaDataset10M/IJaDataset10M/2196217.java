package com.aptana.ide.editor.scriptdoc.parsing.nodes;

import com.aptana.ide.lexer.Lexeme;
import com.aptana.ide.io.SourceWriter;
import com.aptana.ide.parsing.IParseState;

/**
 * @author Robin Debreuil
 */
public class ScriptDocDescriptionNode extends ScriptDocParseNode {

    private String _text;

    /**
	 * A node that holds descriptions for a scriptDoc element.
	 * @param typeIndex
	 * @param parseState
	 * @param startLexeme
	 */
    public ScriptDocDescriptionNode(int typeIndex, IParseState parseState, Lexeme startLexeme) {
        super(typeIndex, parseState, startLexeme);
    }

    /**
	 * @see com.aptana.ide.parsing.nodes.IParseNode#getText()
	 */
    public String getText() {
        return (this._text == null) ? "" : this._text;
    }

    /**
	 * Sets the inner text of the description.
	 * @param text
	 */
    public void setText(String text) {
        this._text = text;
    }

    /**
	 * @see com.aptana.ide.parsing.nodes.IParseNode#getSource(com.aptana.ide.io.SourceWriter)
	 */
    public void getSource(SourceWriter writer) {
        writer.print(this.getText());
    }
}

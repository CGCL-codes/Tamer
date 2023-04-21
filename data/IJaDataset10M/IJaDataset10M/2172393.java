package com.aptana.ide.editor.scriptdoc.parsing.nodes;

import com.aptana.ide.lexer.Lexeme;
import com.aptana.ide.io.SourceWriter;
import com.aptana.ide.parsing.IParseState;

/**
 * @author Robin Debreuil
 */
public class ScriptDocAliasNode extends ScriptDocParseNode {

    /**
	 * A node for the @alias tag.
	 * @param typeIndex
	 * @param parseState
	 * @param startLexeme
	 */
    public ScriptDocAliasNode(int typeIndex, IParseState parseState, Lexeme startLexeme) {
        super(typeIndex, parseState, startLexeme);
    }

    /**
	 * @see com.aptana.ide.parsing.nodes.IParseNode#getSource(com.aptana.ide.io.SourceWriter)
	 */
    public void getSource(SourceWriter writer) {
        writer.print("@alias ");
        ScriptDocKindNode kind = (ScriptDocKindNode) this.getChild(0);
        writer.print(kind.getAttribute("name"));
    }
}

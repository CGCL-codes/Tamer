package com.aptana.ide.editor.scriptdoc.parsing.nodes;

import com.aptana.ide.lexer.Lexeme;
import com.aptana.ide.io.SourceWriter;
import com.aptana.ide.parsing.IParseState;

/**
 * @author Robin Debreuil
 */
public class ScriptDocMethodNode extends ScriptDocParseNode {

    /**
	 * A node for the @method tag.
	 * @param typeIndex
	 * @param parseState
	 * @param startLexeme
	 */
    public ScriptDocMethodNode(int typeIndex, IParseState parseState, Lexeme startLexeme) {
        super(typeIndex, parseState, startLexeme);
    }

    /**
	 * @see com.aptana.ide.parsing.nodes.IParseNode#getSource(com.aptana.ide.io.SourceWriter)
	 */
    public void getSource(SourceWriter writer) {
        writer.print("@method ");
    }
}

package com.aptana.ide.editor.css.parsing.nodes;

import com.aptana.ide.io.SourceWriter;
import com.aptana.ide.lexer.Lexeme;
import com.aptana.ide.parsing.IParseState;
import com.aptana.ide.parsing.nodes.IParseNode;

/**
 * @author Kevin Lindsey
 */
public class CSSPageNode extends CSSParseNode {

    /**
	 * @param parseState
	 * @param startLexeme
	 */
    public CSSPageNode(IParseState parseState, Lexeme startLexeme) {
        super(CSSParseNodeTypes.PAGE, parseState, startLexeme);
    }

    /**
	 * @see com.aptana.ide.parsing.nodes.ParseNodeBase#getSource(com.aptana.ide.io.SourceWriter)
	 */
    public void getSource(SourceWriter writer) {
        writer.printWithIndent(this.getName());
        if (this.hasAttribute("name")) {
            writer.print(":").print(this.getAttribute("name"));
        }
        IParseNode declarations = this.getChild(0);
        if (declarations.hasChildren()) {
            writer.println(" {").increaseIndent();
            declarations.getSource(writer);
            writer.decreaseIndent().println("}");
        } else {
            writer.println(" {}");
        }
    }
}

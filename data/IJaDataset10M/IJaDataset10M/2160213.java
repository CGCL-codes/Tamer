package com.agentfactory.afapl2.compiler.parser.node;

import com.agentfactory.afapl2.compiler.parser.LexicalAnalyser;
import com.agentfactory.afapl2.compiler.parser.LexicalAnalyserException;
import com.agentfactory.afapl2.compiler.parser.ParseTreeException;
import com.agentfactory.afapl2.compiler.parser.ParserHelper;
import com.agentfactory.afapl2.compiler.parser.token.Token;

/**
 *
 * @author Administrator
 */
public class NextNode extends ModalNode {

    private ModalNode node;

    /** Creates a new instance of ProgramNode */
    public NextNode() {
    }

    @Override
    public void construct(LexicalAnalyser analyser) throws LexicalAnalyserException, ParseTreeException {
        ParserHelper.punctuationCheck("(", "Syntax Error in NEXT", "expected ( but found", analyser);
        Token token = analyser.getNextToken();
        Node temp = Node.create(token, analyser);
        if (temp instanceof ModalNode) {
            node = (ModalNode) temp;
        } else {
            throw new LexicalAnalyserException(analyser.getSource(), token.getLine(), "Invalid use of NEXT with: " + temp);
        }
        ParserHelper.punctuationCheck(")", "Syntax Error in NEXT" + node, "expected ) but found", analyser);
    }

    public Node getContents() {
        return node;
    }

    @Override
    public String toString() {
        return "NEXT(" + node.toString() + ")";
    }
}

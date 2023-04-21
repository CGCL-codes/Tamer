package com.agentfactory.afapl2.compiler.parser.node;

import com.agentfactory.afapl2.compiler.parser.LexicalAnalyser;
import com.agentfactory.afapl2.compiler.parser.LexicalAnalyserException;
import com.agentfactory.afapl2.compiler.parser.ParseTreeException;
import com.agentfactory.afapl2.compiler.parser.ParserHelper;

/**
 *
 * @author Administrator
 */
public class EqualNode extends ModalNode {

    private FOSNode left;

    private FOSNode right;

    /** Creates a new instance of ProgramNode */
    public EqualNode() {
    }

    @Override
    public void construct(LexicalAnalyser analyser) throws LexicalAnalyserException, ParseTreeException {
        ParserHelper.punctuationCheck("(", "Syntax Error in EQUAL", "expected ( but found", analyser);
        left = FOSNode.createFOS(analyser.getNextToken(), analyser);
        ParserHelper.punctuationCheck(",", "Syntax Error in EQUAL(" + left, "expected , but found", analyser);
        right = FOSNode.createFOS(analyser.getNextToken(), analyser);
        ParserHelper.punctuationCheck(")", "Syntax Error in EQUAL(" + left + "," + right, "expected ) but found", analyser);
    }

    @Override
    public String toString() {
        return "EQUAL(" + left + "," + right + ")";
    }

    public FOSNode getLeft() {
        return left;
    }

    public FOSNode getRight() {
        return right;
    }
}

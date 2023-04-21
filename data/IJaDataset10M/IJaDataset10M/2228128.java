package com.google.clearsilver.jsilver.syntax;

import com.google.clearsilver.jsilver.syntax.analysis.DepthFirstAdapter;
import com.google.clearsilver.jsilver.syntax.node.AMultipleCommand;
import com.google.clearsilver.jsilver.syntax.node.AOptimizedMultipleCommand;

/**
 * Visitor that can be applied to the AST to optimize it by replacing nodes with more efficient
 * implementations than the default SableCC generated versions.
 */
public class SyntaxTreeOptimizer extends DepthFirstAdapter {

    /**
   * Replace AMultipleCommand nodes with AOptimizedMultipleCommands, which iterates over children
   * faster.
   */
    @Override
    public void caseAMultipleCommand(AMultipleCommand originalNode) {
        super.caseAMultipleCommand(originalNode);
        originalNode.replaceBy(new AOptimizedMultipleCommand(originalNode));
    }
}

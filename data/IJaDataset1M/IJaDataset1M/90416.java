package com.enerjy.analyzer.java.rules;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import com.enerjy.common.util.IterableIterator;

/**
 * JAVA0032 Switch statement missing default
 */
public class T0032 extends RuleBase {

    @Override
    public boolean visit(CompilationUnit unit) {
        NodeLookup nodes = getNodeLookup(unit);
        for (SwitchStatement node : nodes.getNodes(SwitchStatement.class)) {
            boolean bHaveDefault = false;
            for (Statement stmt : new IterableIterator<Statement>(node.statements())) {
                if (ASTNode.SWITCH_CASE == stmt.getNodeType()) {
                    SwitchCase sc = (SwitchCase) stmt;
                    if (null == sc.getExpression()) {
                        bHaveDefault = true;
                    }
                }
            }
            if (!bHaveDefault) {
                addProblem(node.getExpression());
            }
        }
        return false;
    }
}

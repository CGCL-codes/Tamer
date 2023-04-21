package com.enerjy.analyzer.java.rules;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import com.enerjy.analyzer.java.IFixCapability;
import com.enerjy.analyzer.java.fixes.RemoveNode;

/**
 * JAVA0053 Unused label ''{0}''
 */
public class T0053 extends RuleBase {

    private static final String USED_LABEL = "usedLabel";

    @Override
    public boolean visit(CompilationUnit unit) {
        NodeLookup nodes = getNodeLookup(unit);
        for (BreakStatement node : nodes.getNodes(BreakStatement.class)) {
            record(node.getLabel());
        }
        for (ContinueStatement node : nodes.getNodes(ContinueStatement.class)) {
            record(node.getLabel());
        }
        for (LabeledStatement node : nodes.getNodes(LabeledStatement.class)) {
            if (null == node.getProperty(makeUnique(USED_LABEL))) {
                addProblem(node.getLabel(), node.getLabel().getIdentifier());
            }
        }
        return false;
    }

    @Override
    public IFixCapability getFixCapability() {
        return new RemoveNode();
    }

    private void record(SimpleName name) {
        if (null == name) {
            return;
        }
        String label = name.getIdentifier();
        ASTNode node = name.getParent();
        while (null != node) {
            switch(node.getNodeType()) {
                case ASTNode.METHOD_DECLARATION:
                case ASTNode.INITIALIZER:
                    return;
                case ASTNode.LABELED_STATEMENT:
                    {
                        LabeledStatement stmt = (LabeledStatement) node;
                        if (stmt.getLabel().getIdentifier().equals(label)) {
                            node.setProperty(makeUnique(USED_LABEL), Boolean.TRUE);
                            return;
                        }
                        break;
                    }
                default:
                    break;
            }
            node = node.getParent();
        }
    }
}

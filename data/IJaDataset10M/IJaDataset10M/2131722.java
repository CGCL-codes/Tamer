package com.enerjy.analyzer.java.rules;

import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;

/**
 * JAVA0170 Caught exception ''{0}'' not derived from java.lang.Exception
 */
public class T0170 extends RuleBase {

    @Override
    public boolean visit(CompilationUnit unit) {
        ITypeBinding exception = resolveType("Ljava/lang/Exception;");
        if (null == exception) {
            return false;
        }
        NodeLookup nodes = getNodeLookup(unit);
        for (CatchClause node : nodes.getNodes(CatchClause.class)) {
            ITypeBinding check = node.getException().getType().resolveBinding();
            if (null == check) {
                continue;
            }
            if (!check.getTypeDeclaration().isAssignmentCompatible(exception)) {
                addProblem(node.getException(), check.getQualifiedName());
            }
        }
        return false;
    }
}

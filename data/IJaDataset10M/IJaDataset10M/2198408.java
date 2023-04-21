package com.enerjy.analyzer.java.rules;

import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * JAVA0026 Finalize method with parameters
 */
public class T0026 extends RuleBase {

    public boolean visit(CompilationUnit unit) {
        NodeLookup nodes = getNodeLookup(unit);
        List<MethodDeclaration> methods = nodes.getNodes(ASTNode.METHOD_DECLARATION);
        for (MethodDeclaration decl : methods) {
            if ("finalize".equals(decl.getName().getIdentifier()) && (0 != decl.parameters().size())) {
                addProblem(decl.getName());
            }
        }
        return false;
    }
}

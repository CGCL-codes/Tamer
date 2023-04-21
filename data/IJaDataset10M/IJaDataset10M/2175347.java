package com.enerjy.analyzer.java.rules;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * JAVA0061 Inaccessible member ''{0}'' in anonymous class
 */
public class T0061 extends RuleBase {

    @Override
    public boolean visit(CompilationUnit unit) {
        NodeLookup nodes = getNodeLookup(unit);
        for (AnonymousClassDeclaration node : nodes.getNodes(AnonymousClassDeclaration.class)) {
            ITypeBinding binding = node.resolveBinding();
            if (null == binding) {
                continue;
            }
            for (IVariableBinding field : binding.getDeclaredFields()) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    continue;
                }
                VariableDeclarationFragment fragment = (VariableDeclarationFragment) unit.findDeclaringNode(field);
                addProblem(fragment, field.getName());
            }
        }
        for (MethodDeclaration node : nodes.getNodes(MethodDeclaration.class)) {
            ASTNode parent = node.getParent();
            if ((null == parent) || (ASTNode.ANONYMOUS_CLASS_DECLARATION != parent.getNodeType())) {
                continue;
            }
            IMethodBinding binding = node.resolveBinding();
            if (null == binding) {
                continue;
            }
            if (!Modifier.isPrivate(binding.getModifiers()) && !RuleUtils.isMethodOverride(binding)) {
                addProblem(node.getName(), node.getName().getIdentifier());
            }
        }
        return false;
    }
}

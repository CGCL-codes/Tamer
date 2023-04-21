package org.cesar.flip.flipex.ajdt.validators.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * The visitor for an  Method Invocation.
 * 
 * @author Fernando Calheiros (fernando.calheiros@cesar.org.br)
 * 
 */
public class MethodInvocationCollectorVisitor extends ASTVisitor {

    private MethodInvocation invocation;

    @Override
    public boolean visit(MethodInvocation node) {
        this.invocation = node;
        return super.visit(node);
    }

    /**
	 * Returns the method invocation.
	 * 
	 * @return the method invocarion.
	 */
    public MethodInvocation getMethodInvocation() {
        return this.invocation;
    }
}

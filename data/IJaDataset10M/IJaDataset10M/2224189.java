package com.ibm.wala.refactoring.rtwq;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

/**
 * this visitor finds the SimpleName ASTNode that matches varName in the method
 * methodName 
 * It is used to find the selection needed to invoke InlineMethod refactoring
 * It saves the variable's first occurence in the method 
 * 
 * @author Alexander Libov
 *
 */
public class InlineFinderVisitor extends ASTVisitor {

    private String varName;

    private String methodName;

    public SimpleName selectedVarFirstOccurence = null;

    private int blockPosition;

    public InlineFinderVisitor(String varName, String methodName, int blockPosition) {
        this.varName = varName;
        this.methodName = methodName;
        this.blockPosition = blockPosition;
    }

    /**
	 * only visiting the needed method
	 */
    public boolean visit(MethodDeclaration node) {
        return node.getName().getIdentifier().equals(methodName);
    }

    /**
	 * 
	 * saving the first node that matches
	 * @param node the node to visit
	 * @return <code>true</code> if the children of this node should be
	 * visited, and <code>false</code> if the children of this node should
	 * be skipped
	 */
    public boolean visit(SimpleName node) {
        if (node.getIdentifier().equals(varName)) {
            if (selectedVarFirstOccurence == null && node.getStartPosition() > blockPosition) {
                selectedVarFirstOccurence = node;
            }
        }
        return true;
    }
}

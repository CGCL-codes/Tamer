package com.stateofflow.eclipse.tane.hidedelegate.model.chain.node;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class ChainNodeFactory {

    public ChainNode createNode(final ASTNode node) {
        switch(node.getNodeType()) {
            case ASTNode.FIELD_ACCESS:
                return new FieldAccessNode((FieldAccess) node);
            case ASTNode.METHOD_INVOCATION:
                return new MethodInvocationNode((MethodInvocation) node);
        }
        throw new IllegalArgumentException("Unexpected node type : " + node);
    }
}

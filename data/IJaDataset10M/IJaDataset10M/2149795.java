package com.google.gdt.eclipse.platform.jdt.dom;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * A minimal proxy version of {@link org.eclipse.jdt.core.dom.NodeFinder}.
 */
public class NodeFinder {

    public static ASTNode perform(ASTNode root, int start, int length) {
        return org.eclipse.jdt.core.dom.NodeFinder.perform(root, start, length);
    }
}

package org.jmlspecs.jml6.boogie.translation;

import org.eclipse.jdt.core.dom.ASTNode;

public class NoBindingsException extends Exception {

    ASTNode node;

    public NoBindingsException(ASTNode node) {
        super("Required bindings could not be found for node: " + node);
        this.node = node;
    }
}

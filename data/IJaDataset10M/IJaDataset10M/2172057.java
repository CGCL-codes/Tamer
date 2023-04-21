package org.jaqlib.core.syntaxtree;

/**
 * Placeholder for null {@link SyntaxTreeNode}s. The {@link #visit(Object)}
 * method of this class always returns true.
 * 
 * @author Werner Fragner
 * 
 * @param <T>
 */
public class NullSyntraxTreeNode<T> implements SyntaxTreeNode<T> {

    public boolean visit(T element) {
        return true;
    }

    public void appendLogString(StringBuilder sb) {
    }
}

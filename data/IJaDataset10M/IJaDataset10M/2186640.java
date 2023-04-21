package com.smartitengineering.javasourcetreeparser;

import com.sun.source.tree.Tree.Kind;

/**
 *
 * @author imyousuf
 */
public interface NodeTraversalListener<E extends Kind> {

    public void notifyAboutNode(NodeTraversalEvent<E> event);

    public void notifyEndOfNodeParsing(NodeTraversalEvent<E> event);

    public E getTreeKind();
}

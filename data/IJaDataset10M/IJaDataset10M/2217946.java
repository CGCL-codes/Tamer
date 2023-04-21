package com.lts.swing.tree.mvc;

/**
 * An underlying application tree-like data structure.
 * 
 * @author cnh
 *
 */
public interface ApplicationTree {

    public ApplicationNode getRoot();

    public void addNodeTo(ApplicationNode parent, ApplicationNode child);

    public void removeNodeFrom(ApplicationNode parent, ApplicationNode child);

    public void changeNode(ApplicationNode node);

    public void addApplicationTreeListener(ApplicationTreeListener listener);

    public void removeApplicationTreeListener(ApplicationTreeListener listener);
}

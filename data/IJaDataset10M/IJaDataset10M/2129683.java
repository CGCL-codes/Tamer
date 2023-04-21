package com.germinus.xpression.tree;

/**
 *
 * @author agonzalez
 */
public interface PrintableTreeNode {

    public Object getNode();

    public Integer getFinishedLevels();

    public boolean isAncestor();

    public boolean isLastSibiling();

    public boolean isFirstSibiling();
}

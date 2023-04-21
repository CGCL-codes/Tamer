package org.personalsmartspace.lm.c45.api.platform;

import javax.swing.tree.DefaultMutableTreeNode;

public interface IDecisionTree {

    /**
     * @return the parameterName related to the decision tree
     */
    public String getParameterName();

    /**
     * @return the root node of the DecisionTree
     */
    public DefaultMutableTreeNode getRoot();
}

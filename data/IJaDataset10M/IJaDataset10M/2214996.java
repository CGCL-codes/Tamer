package game.evolution.treeEvolution;

import java.lang.reflect.Method;
import java.io.Serializable;

/**
 * Object to carry all support data for all available objects.
 */
public class NodeInformation implements Serializable {

    public TreeNode template;

    public Method[] getMethods;

    public Method[] setMethods;

    public double[] minVal;

    public double[] maxVal;

    public int depth;

    public NodeInformation[] canMutateTo;

    public NodeInformation[] canMutateToLeaf;

    public NodeInformation(TreeNode node, int depth) {
        fillEmptyReferences(node);
        template = node;
        this.depth = depth;
        getMethods = new Method[0];
        setMethods = new Method[0];
        canMutateTo = null;
        canMutateToLeaf = null;
        minVal = null;
        maxVal = null;
    }

    private void fillEmptyReferences(TreeNode node) {
        if (node.templateNode == null) node.templateNode = this;
        if (node instanceof InnerTreeNode) {
            InnerTreeNode innerNode = (InnerTreeNode) node;
            for (int i = 0; i < innerNode.getNodesNumber(); i++) {
                fillEmptyReferences(innerNode.getNode(i));
            }
        }
    }
}

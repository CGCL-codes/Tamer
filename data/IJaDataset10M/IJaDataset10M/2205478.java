package lablog.lib.db.entity.base;

import java.util.*;
import javax.persistence.Transient;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * A super class for entities that have tree like relations.
 *
 * @param <N> The node type (e.g. Directory)
 * @param <L> The leaf type (e.g. File)
 */
public abstract class EntityTreeNode<N extends EntityTreeNode<N, L>, L extends EntityTreeLeaf<N, L>> extends EntityTreeLeaf<N, L> implements Iterable<N> {

    protected static final short CLASSID = 4;

    public static final long serialVersionUID = createSerialUID(DB_VERSION, MODULE, CLASSID);

    private List<N> nodeList = new LinkedList<N>();

    private List<L> leafList = new LinkedList<L>();

    /**
    * Get all children, that can contain other nodes or leafs.
    * The method should return null and should be annotated with
    * {@link Transient} for leaf nodes (e.g. File or Property) 
    * 
    * @return A list of child nodes.
    */
    public abstract Set<N> getNodeChildren();

    /**
    * Sets all children, that can contain other nodes or leafs.
    * The method should have an empty implementation and should be annotated with
    * {@link Transient} for leaf nodes (e.g. File or Property)
    * 
    * @param nodeChildren A list of child nodes.
    */
    public abstract void setNodeChildren(Set<N> nodeChildren);

    /**
    * Get all leaf children.
    * 
    * @return A list of child leafs.
    */
    public abstract Set<L> getLeafChildren();

    /**
    * Sets all leaf children. 
    * 
    * @param leafChildren A list of child leafs.
    */
    public abstract void setLeafChildren(Set<L> leafChildren);

    /**
    * Adds child to the receiver at index. child will be messaged with setParent.
    * {@link EntityTreeNode#setParent(MutableTreeNode)} will be messaged on the child.
    * The index can be ignored, because entities are sorted by their alphabetical
    * order. 
    * 
    * @param node The child to add.
    * @param index The index where the child should be added (ignored)
    */
    @Override
    public abstract void insert(MutableTreeNode node, int index);

    /**
    * Returns true if the node allows children.
    * The Method should return !isLeaf(). 
    * 
    * @return true if children are allowed, false otherwise.
    */
    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    /**
    * Returns true if the node is a leaf.
    * The Method should return !getAllowsChildren().
    * 
    * @return true if the node is a leaf, false otherwise.
    */
    @Override
    public boolean isLeaf() {
        return false;
    }

    /**
    * Returns the children of the node as an {@link Enumeration}. 
    */
    @Override
    public Enumeration<EntityTreeLeaf<?, ?>> children() {
        Vector<EntityTreeLeaf<?, ?>> children = new Vector<EntityTreeLeaf<?, ?>>(getChildCount());
        children.addAll(getNodeChildren());
        children.addAll(getLeafChildren());
        return children.elements();
    }

    /**
    * Returns the child node at the given index. 
    * 
    * @param index The index of the child.
    */
    @Override
    public EntityTreeLeaf<N, L> getChildAt(int index) {
        nodeList = EntityHelper.toSortedList(getNodeChildren(), nodeList);
        int nodeCount = nodeList != null ? nodeList.size() : 0;
        if (index >= 0 && index < nodeCount) {
            return nodeList.get(index);
        } else {
            leafList = EntityHelper.toSortedList(getLeafChildren(), leafList);
            int leafCount = leafList != null ? leafList.size() : 0;
            if (index >= nodeCount && index < leafCount + nodeCount) {
                return leafList.get(index - nodeCount);
            } else {
                return null;
            }
        }
    }

    /**
    * Returns the number of children the node contains.
    * 
    * @return The number of children.
    */
    @Override
    public int getChildCount() {
        Set<N> nodes = getNodeChildren();
        int nodeCount = nodes != null ? nodes.size() : 0;
        Set<L> leafs = getLeafChildren();
        int leafCount = leafs != null ? leafs.size() : 0;
        return nodeCount + leafCount;
    }

    /**
    * Returns the index of child in the nodes children. 
    * If the node does not contain the child, -1 will be returned. 
    * 
    * @return The index of the child or -1.
    */
    @Override
    public int getIndex(TreeNode node) {
        nodeList = EntityHelper.toSortedList(getNodeChildren(), nodeList);
        if (nodeList != null && nodeList.contains(node)) {
            return nodeList.indexOf(node);
        } else {
            int nodeCount = nodeList != null ? nodeList.size() : 0;
            leafList = EntityHelper.toSortedList(getLeafChildren(), leafList);
            if (leafList != null && leafList.contains(node)) {
                return nodeCount + leafList.indexOf(node);
            } else {
                return -1;
            }
        }
    }

    /**
    * Removes the child at the given index from the node.
    * {@link EntityTreeNode#setParent(MutableTreeNode)} will be messaged on the child. 
    * 
    * @param index The index of the child.
    */
    @Override
    public void remove(int index) {
        Set<N> nodes = getNodeChildren();
        int nodeCount = nodes != null ? nodes.size() : 0;
        if (nodes != null && index >= 0 && index < nodeCount) {
            nodeList = EntityHelper.toSortedList(getNodeChildren(), nodeList);
            N node = nodeList.get(index);
            nodes.remove(node);
            if (node != null) node.setParent(null);
        } else {
            Set<L> leafs = getLeafChildren();
            int leafCount = leafs != null ? leafs.size() : 0;
            if (leafs != null && index >= nodeCount && index < leafCount + nodeCount) {
                leafList = EntityHelper.toSortedList(getLeafChildren(), leafList);
                L node = leafList.get(index - nodeCount);
                nodes.remove(node);
                if (node != null) node.setParent(null);
            }
        }
    }

    /**
    * Removes the child from the node. 
    * {@link EntityTreeNode#setParent(MutableTreeNode)} will be messaged on the child.
    * 
    * @param node The child to remove.
    */
    @Override
    public void remove(MutableTreeNode node) {
        Set<N> nodes = getNodeChildren();
        if (nodes != null && nodes.contains(node)) {
            nodes.remove(node);
            node.setParent(null);
        } else {
            Set<L> leafs = getLeafChildren();
            if (leafs != null && leafs.contains(node)) {
                getLeafChildren().remove(node);
                node.setParent(null);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public TreeNodeIterator<N> iterator() {
        return new TreeNodeIterator<N>((N) this);
    }
}

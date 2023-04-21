package de.uniwue.tm.cev.data.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Abstrakter Node
 * 
 * @author Marco Nehmeier
 */
public abstract class CEVAbstractTreeNode implements ICEVTreeNode {

    private ICEVTreeNode parent;

    private ArrayList<ICEVTreeNode> children;

    /**
   * Konstruktor
   */
    public CEVAbstractTreeNode() {
        this(null);
    }

    /**
   * Konstruktor
   * 
   * @param parent
   *          Elternknoten
   */
    public CEVAbstractTreeNode(ICEVTreeNode parent) {
        this.parent = parent;
        children = new ArrayList<ICEVTreeNode>();
    }

    public void addChild(ICEVTreeNode child) {
        children.add(child);
    }

    public ICEVTreeNode[] getChildren() {
        return children.toArray(new ICEVTreeNode[] {});
    }

    public Iterator<ICEVTreeNode> getChildrenIterator() {
        return children.iterator();
    }

    public ICEVTreeNode getParent() {
        return parent;
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public void getNodes(LinkedList<ICEVTreeNode> list) {
        Iterator<ICEVTreeNode> iter = getChildrenIterator();
        while (iter.hasNext()) {
            ICEVTreeNode node = iter.next();
            list.add(node);
            node.getNodes(list);
        }
    }

    public void sort(Comparator<ICEVTreeNode> cp) {
        Collections.sort(children, cp);
    }
}

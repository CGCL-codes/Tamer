package org.neodatis.btree;

import java.util.Iterator;
import org.neodatis.OrderByConstants;
import org.neodatis.odb.ODBRuntimeException;
import org.neodatis.odb.core.NeoDatisError;

/**
 * @author olivier
 * 
 */
public abstract class AbstractBTreeIterator implements Iterator {

    private IBTree btree;

    /** The current node where the iterator is */
    protected IBTreeNode currentNode;

    /** The current key in the current node where the iterator is */
    private int currentKeyIndex;

    /** The number of returned keys */
    private int nbReturnedKeys;

    /**
	 * The number of returned elements ; it may be different from the number of
	 * keys in the case f multileValues btree where a key can contain more than
	 * one value
	 */
    protected int nbReturnedElements;

    private OrderByConstants orderByType;

    public AbstractBTreeIterator(IBTree tree, OrderByConstants orderByType) {
        this.btree = tree;
        this.currentNode = tree.getRoot();
        this.orderByType = orderByType;
        if (orderByType.isOrderByDesc()) {
            this.currentKeyIndex = currentNode.getNbKeys();
        } else {
            this.currentKeyIndex = 0;
        }
    }

    public abstract Object getValueAt(IBTreeNode node, int currentIndex);

    public boolean hasNext() {
        return nbReturnedElements < btree.getSize();
    }

    public Object next() {
        if (currentKeyIndex > currentNode.getNbKeys() || nbReturnedElements >= btree.getSize()) {
            throw new ODBRuntimeException(NeoDatisError.NO_MORE_OBJECTS_IN_COLLECTION);
        }
        if (orderByType.isOrderByDesc()) {
            return nextDesc();
        }
        return nextAsc();
    }

    protected Object nextAsc() {
        while (!currentNode.isLeaf()) {
            currentNode = currentNode.getChildAt(currentKeyIndex, true);
            currentKeyIndex = 0;
        }
        if (currentKeyIndex < currentNode.getNbKeys()) {
            nbReturnedKeys++;
            nbReturnedElements++;
            Object nodeValue = getValueAt(currentNode, currentKeyIndex);
            currentKeyIndex++;
            return nodeValue;
        }
        IBTreeNode child = null;
        while (currentKeyIndex >= currentNode.getNbKeys()) {
            child = currentNode;
            currentNode = currentNode.getParent();
            currentKeyIndex = indexOfChild(currentNode, child);
        }
        nbReturnedElements++;
        nbReturnedKeys++;
        Object value = getValueAt(currentNode, currentKeyIndex);
        currentKeyIndex++;
        return value;
    }

    protected Object nextDesc() {
        while (!currentNode.isLeaf()) {
            currentNode = currentNode.getChildAt(currentKeyIndex, true);
            currentKeyIndex = currentNode.getNbKeys();
        }
        if (currentKeyIndex > 0) {
            nbReturnedElements++;
            nbReturnedKeys++;
            currentKeyIndex--;
            Object nodeValue = getValueAt(currentNode, currentKeyIndex);
            return nodeValue;
        }
        IBTreeNode child = null;
        while (currentKeyIndex == 0) {
            child = currentNode;
            currentNode = currentNode.getParent();
            currentKeyIndex = indexOfChild(currentNode, child);
        }
        nbReturnedElements++;
        nbReturnedKeys++;
        currentKeyIndex--;
        Object value = getValueAt(currentNode, currentKeyIndex);
        return value;
    }

    private int indexOfChild(IBTreeNode parent, IBTreeNode child) {
        if (parent == null) {
            System.out.println("parent is null");
        }
        for (int i = 0; i < parent.getNbChildren(); i++) {
            if (parent.getChildAt(i, true).getId() == child.getId()) {
                return i;
            }
        }
        throw new RuntimeException("parent " + parent + " does not have the specified child : " + child);
    }

    public void remove() {
    }

    public void reset() {
        this.currentNode = btree.getRoot();
        if (orderByType.isOrderByDesc()) {
            this.currentKeyIndex = currentNode.getNbKeys();
        } else {
            this.currentKeyIndex = 0;
        }
        nbReturnedElements = 0;
        nbReturnedKeys = 0;
    }
}

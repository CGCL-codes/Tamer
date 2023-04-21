package model.abstractions.file;

import java.util.LinkedList;

/**
 * Describes a directory.
 * GoF: Composite Composite.
 * @author falk
 *
 */
public abstract class AbstractDirectory extends AbstractFileNode {

    /**
	 * This list stores the child items.
	 */
    private LinkedList childrenList = new LinkedList();

    /**
	 * Number of children.
	 */
    private int childrenCount = 0;

    /**
	 * Returns a child.
	 * @param Number of the child.
	 * @return
	 */
    public AbstractFileNode getChild(int i) {
        AbstractFileNode child = null;
        if ((i < childrenCount) && (i > -1)) {
            child = (AbstractFileNode) childrenList.get(i);
        }
        return child;
    }

    /**
	 * Adds a new file node to the list if it is not in the list.
	 * @param fileNode The node to be added.
	 */
    public void addChild(AbstractFileNode fileNode) {
        if (!childrenList.contains(fileNode)) {
            childrenList.add(fileNode);
            childrenCount++;
        }
    }

    /**
	 * Removes a file node frpm the list if it is in the list.
	 * @param fileNode The note to be removed.
	 */
    public void delChild(AbstractFileNode fileNode) {
        if (childrenList.contains(fileNode)) {
            childrenList.remove(fileNode);
            childrenCount--;
        }
    }

    /**
	 * 
	 * @return Number of children.
	 */
    public int Count() {
        return childrenCount;
    }

    /**
	 * Returns allways false.
	 * It means that this class can have child items.
	 * 
	 * @return false
	 */
    public boolean isLeaf() {
        return false;
    }

    /**
	 * Clears the children list.
	 *
	 */
    public void clear() {
        childrenList.clear();
        childrenCount = 0;
    }
}

package co.edu.unal.ungrid.image.dicom.core;

import java.util.Collection;
import java.util.Enumeration;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.tree.TreeNode;

/**
 * 
 */
public abstract class DicomDirectoryRecord implements Comparable<DicomDirectoryRecord>, TreeNode {

    /**
	 * @uml.property name="parent"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    DicomDirectoryRecord parent;

    /**
	 * @uml.property name="children"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     elementType="co.edu.unal.ungrid.image.dicom.dicom.DicomDirectoryRecord"
	 */
    Collection<TreeNode> children;

    /**
	 * @uml.property name="array"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
    TreeNode[] array;

    /**
	 * @uml.property name="list"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    AttributeList list;

    /**
	 * @uml.property name="uid"
	 */
    protected String uid;

    /**
	 * @uml.property name="stringValue"
	 */
    protected String stringValue;

    /**
	 * @uml.property name="integerValue"
	 */
    protected int integerValue;

    /**
	 * @param obj
	 */
    public int compareTo(DicomDirectoryRecord obj) {
        return 0;
    }

    /**
	 * @param obj
	 */
    public boolean equals(Object obj) {
        return (obj instanceof DicomDirectoryRecord ? compareTo((DicomDirectoryRecord) obj) == 0 : false);
    }

    /***/
    protected abstract void makeStringValue();

    /***/
    protected abstract void makeIntegerValue();

    /**
	 * @uml.property name="stringValue"
	 */
    protected String getStringValue() {
        return stringValue;
    }

    /**
	 * @uml.property name="integerValue"
	 */
    protected int getIntegerValue() {
        return integerValue;
    }

    /***/
    protected final String getUIDForComparison() {
        return uid;
    }

    /**
	 * @param record
	 */
    protected final int compareToByStringValue(DicomDirectoryRecord record) {
        if (this.getClass().equals(record.getClass())) {
            int uidComparison = getUIDForComparison().compareTo(record.getUIDForComparison());
            {
                int strComparison = toString().compareTo(record.toString());
                if (strComparison == 0) {
                    return uidComparison;
                } else {
                    return strComparison;
                }
            }
        } else {
            return toString().compareTo(record.toString());
        }
    }

    /**
	 * @param record
	 */
    protected final int compareToByIntegerValue(DicomDirectoryRecord record) {
        if (this.getClass().equals(record.getClass())) {
            int uidComparison = getUIDForComparison().compareTo(record.getUIDForComparison());
            if (uidComparison == 0) {
                return 0;
            } else {
                int intComparison = getIntegerValue() - record.getIntegerValue();
                if (intComparison == 0) {
                    int strComparison = toString().compareTo(record.toString());
                    if (strComparison == 0) {
                        return uidComparison;
                    } else {
                        return strComparison;
                    }
                } else {
                    return intComparison;
                }
            }
        } else {
            return toString().compareTo(record.toString());
        }
    }

    /**
	 * <p>
	 * Returns the parent node of this node.
	 * </p>
	 * 
	 * @return the parent node, or null if the root
	 */
    public TreeNode getParent() {
        return parent;
    }

    /**
	 * <p>
	 * Returns the child at the specified index.
	 * </p>
	 * 
	 * @param index
	 *            the index of the child to be returned, numbered from 0
	 * @return the child <code>TreeNode</code> at the specified index
	 */
    public TreeNode getChildAt(int index) {
        int n = children.size();
        if (array == null) {
            array = children.toArray(new TreeNode[n]);
        }
        return (index < n ? array[index] : null);
    }

    /**
	 * <p>
	 * Returns the index of the specified child from amongst this node's
	 * children, if present.
	 * </p>
	 * 
	 * @param child
	 *            the child to search for amongst this node's children
	 * @return the index of the child, or -1 if not present
	 */
    public int getIndex(TreeNode child) {
        int n = children.size();
        if (array == null) {
            array = (TreeNode[]) (children.toArray(new TreeNode[n]));
        }
        for (int i = 0; i < n; ++i) {
            if (getChildAt(i).equals(child)) {
                return i;
            }
        }
        return -1;
    }

    /**
	 * <p>
	 * Always returns true, since children may always be added.
	 * </p>
	 * 
	 * @return always true
	 */
    public boolean getAllowsChildren() {
        return true;
    }

    /**
	 * <p>
	 * Returns true if the receiver is a leaf (has no children).
	 * </p>
	 * 
	 * @return true if the receiver is a leaf
	 */
    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    /**
	 * <p>
	 * Return the number of children that this node contains.
	 * </p>
	 * 
	 * @return the number of children, 0 if none
	 */
    public int getChildCount() {
        return children == null ? 0 : children.size();
    }

    /**
	 * <p>
	 * Returns the children of this node as an
	 * {@link java.util.Enumeration Enumeration}.
	 * </p>
	 * 
	 * @return the children of this node
	 */
    public Enumeration<TreeNode> children() {
        return (children == null ? null : new Vector<TreeNode>(children).elements());
    }

    /**
	 * @param p
	 * @param l
	 */
    public DicomDirectoryRecord(DicomDirectoryRecord p, AttributeList l) {
        parent = p;
        list = l;
        makeIntegerValue();
        makeStringValue();
    }

    /**
	 * @param child
	 */
    public void addChild(DicomDirectoryRecord child) {
        if (children == null) {
            children = new TreeSet<TreeNode>();
        }
        children.add(child);
        array = null;
    }

    /**
	 * @param child
	 */
    public void removeChild(DicomDirectoryRecord child) {
        children.remove(child);
        array = null;
    }

    /**
	 * @param sibling
	 * @exception DicomException
	 */
    public void addSibling(DicomDirectoryRecord sibling) throws DicomException {
        if (parent == null) {
            throw new DicomException("Internal error - root node with sibling");
        } else {
            parent.addChild(sibling);
        }
    }

    /***/
    public AttributeList getAttributeList() {
        return list;
    }
}

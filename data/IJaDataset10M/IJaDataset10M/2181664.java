package ncclient;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * @author Svedin
 */
public class ContactTreeNode extends DefaultMutableTreeNode implements Comparable {

    protected Vector<ContactTreeNode> children;

    protected Contact contact;

    protected ContactTreeNode parent;

    protected boolean allowsChildren;

    protected final Enumeration<ContactTreeNode> EMPTY_ENUMERATION = children.elements();

    /** Creates a new instance of ContactNode */
    public ContactTreeNode(Contact contact) {
        this.contact = contact;
        this.allowsChildren = false;
        new Vector<ContactTreeNode>();
    }

    public ContactTreeNode(Contact contact, boolean allowsChildren) {
        this.contact = contact;
        this.allowsChildren = allowsChildren;
        new Vector<ContactTreeNode>();
    }

    public void insert(ContactTreeNode child, int index) {
        children.add(index, child);
    }

    public void insert(ContactTreeNode child) {
        if (allowsChildren) {
            Enumeration<ContactTreeNode> en = children.elements();
            ContactTreeNode node;
            while (en.hasMoreElements()) {
                node = en.nextElement();
                if (child.compareTo(node) < 0) {
                } else if (child.compareTo(node) == 0) {
                }
            }
        }
    }

    public Contact getUserObject() {
        return contact;
    }

    public int compareTo(Object o) {
        if (!(o instanceof ContactTreeNode)) {
            return -1;
        }
        ContactTreeNode node = (ContactTreeNode) o;
        return contact.compareTo(node.getUserObject());
    }

    public void remove(int index) {
        children.remove(index);
    }

    public void remove(ContactTreeNode node) {
        int index = children.indexOf(node);
        if (index >= 0) {
            remove(index);
        }
    }

    public void setUserObject(Contact contact) {
        this.contact = contact;
    }

    public void removeFromParent() {
        if (parent != null) {
            parent.remove(this);
            parent = null;
        }
    }

    public void setParent(ContactTreeNode newParent) {
        removeFromParent();
        parent = newParent;
    }

    public ContactTreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    public int getChildCount() {
        return children.size();
    }

    public ContactTreeNode getParent() {
        return parent;
    }

    public int getIndex(ContactTreeNode node) {
        return children.indexOf(node);
    }

    public boolean getAllowsChildren() {
        return allowsChildren;
    }

    public boolean isLeaf() {
        return !allowsChildren;
    }

    public Enumeration children() {
        if (allowsChildren) {
            return children.elements();
        }
        return EMPTY_ENUMERATION;
    }
}

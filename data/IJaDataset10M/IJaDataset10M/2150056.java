package net.datao.swing;

import net.datao.datamodel.OClass;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Vector;
import java.util.Collection;
import java.util.List;

public class OClassTreeNode extends DefaultMutableTreeNode {

    OClassTreeNode(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
        prepareChildren();
    }

    protected void prepareChildren() {
        if (children != null) return;
        if (!(userObject instanceof OClass)) throw new RuntimeException("Trying to fill the OModelTreeModel with no OClass");
        OClass c = (OClass) userObject;
        children = createNodesForClasses(c.getSubOClasses());
    }

    public OClassTreeNode createNodeForClass(OClass c) {
        OClassTreeNode node = new OClassTreeNode(c, true);
        node.setParent(this);
        return node;
    }

    public Vector<OClassTreeNode> createNodesForClasses(Collection<OClass> cc) {
        Vector<OClassTreeNode> sscc = new Vector<OClassTreeNode>();
        for (OClass c : cc) sscc.add(createNodeForClass(c));
        return sscc;
    }

    public List getChildrenAsList() {
        return (List) children;
    }

    public OClass getOClass() {
        if (userObject instanceof OClass) return (OClass) userObject; else throw new RuntimeException("Sorry, but this OClassTreeNode holds a non-OClass object: " + userObject.toString());
    }

    public String toString() {
        if (userObject == null) {
            return null;
        } else {
            return userObject.toString();
        }
    }
}

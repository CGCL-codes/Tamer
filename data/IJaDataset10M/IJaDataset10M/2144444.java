package jshomeorg.simplytrain.toolset;

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author js
 */
public class comparableDefaultMutableTreeNode extends DefaultMutableTreeNode implements java.lang.Comparable {

    Object updatedUserObject = null;

    public comparableDefaultMutableTreeNode(Object o) {
        super(o);
        updatedUserObject = o;
    }

    public comparableDefaultMutableTreeNode(Object o, boolean b) {
        super(o, b);
        updatedUserObject = o;
    }

    public int compareTo(Object o) {
        if (o instanceof comparableDefaultMutableTreeNode) {
            comparableDefaultMutableTreeNode o2 = (comparableDefaultMutableTreeNode) o;
            try {
                return ((java.lang.Comparable) getUserObject()).compareTo((java.lang.Comparable) o2.getUserObject());
            } catch (ClassCastException e) {
                return 0;
            }
        }
        return 0;
    }

    public void setUserObject(Object userObject) {
        updatedUserObject = userObject;
    }

    public void setUserObject_real(Object userObject) {
        super.setUserObject(userObject);
    }

    public Object getUpdatedUserObject() {
        return updatedUserObject;
    }

    public comparableDefaultMutableTreeNode findObject(Object u) {
        if (getUserObject() != null && u != null && getUserObject().equals(u)) {
            return this;
        }
        if (getUserObject() == u) {
            return this;
        }
        Enumeration e = children();
        while (e.hasMoreElements()) {
            comparableDefaultMutableTreeNode e2 = (comparableDefaultMutableTreeNode) e.nextElement();
            comparableDefaultMutableTreeNode ret = e2.findObject(u);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }
}

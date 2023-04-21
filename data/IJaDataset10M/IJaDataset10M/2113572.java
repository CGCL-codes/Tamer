package org.argouml.ui.explorer.rules;

import org.argouml.ui.explorer.WeakExplorerNode;

/**
 * This class is a support class for The Navigation panel Go Rules.
 * Don't confuse it with anything to do with GEF nodes or the like.
 *
 * @author  alexb
 * @since argo 0.13.4, Created on 21 March 2003, 23:18
 */
public class AssociationsNode implements WeakExplorerNode {

    /**
     * The parent.
     */
    private Object parent;

    /**
     * Creates a new instance of AssociationsNode.
     *
     * @param theParent the parent object
     */
    public AssociationsNode(Object theParent) {
        this.parent = theParent;
    }

    /**
     * @return the parent
     */
    public Object getParent() {
        return parent;
    }

    public String toString() {
        return "Associations";
    }

    public boolean subsumes(Object obj) {
        return obj instanceof AssociationsNode;
    }
}

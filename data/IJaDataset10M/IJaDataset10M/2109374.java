package org.dmd.features.extgwt.client;

import java.util.ArrayList;
import org.dmd.dmc.DmcHierarchicObjectName;
import org.dmd.dmr.shared.base.generated.dmo.HierarchicObjectDMO;

/**
 * The DmoExtGWTTreeNode extends the basic ExtGWTWrapperBase to provide access to fully qualified
 * names for objects. This supports creating hierarchies of object instances. Originally, the
 * class implemented the GXT TreeModel, but it turns out that GXT automatically wraps Model objects
 * with a TreeModel class when they're added to a TreeStore, so there was no need duplicate the functionality
 * here.
 * <P>
 * However, this class does provide a basic mechanism for creating a hierarchy of objects based on the FQN.
 */
public class DmoExtGWTTreeNode<DMO extends HierarchicObjectDMO> extends DmoExtGWTWrapperBase<DMO> {

    DmoExtGWTTreeNode<DMO> parent;

    @SuppressWarnings("unchecked")
    ArrayList<DmoExtGWTTreeNode> children;

    protected DmoExtGWTTreeNode() {
    }

    protected DmoExtGWTTreeNode(HierarchicObjectDMO obj) {
        super(obj);
    }

    public DmcHierarchicObjectName getFQN() {
        return (core.getFQN());
    }

    public DmcHierarchicObjectName getParentFQN() {
        return (core.getFQN().getParentName());
    }

    @SuppressWarnings("unchecked")
    public void addChild(DmoExtGWTTreeNode c) {
        if (children == null) children = new ArrayList<DmoExtGWTTreeNode>();
        children.add(c);
        c.parent = this;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<DmoExtGWTTreeNode> getChildren() {
        return (children);
    }

    @SuppressWarnings("unchecked")
    public void removeChild(DmoExtGWTTreeNode c) {
        if (children.remove(c)) {
            c.parent = null;
        }
    }

    @SuppressWarnings("unchecked")
    public DmoExtGWTTreeNode getParentObject() {
        return (parent);
    }
}

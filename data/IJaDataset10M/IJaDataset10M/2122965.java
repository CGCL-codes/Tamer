package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.SpecificationFacets;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.runtime.context.NakedObjectsContext;

/**
 * Specification for a tree node that will display a closed object as a root node or within an object. This
 * will indicate that the created view can be opened if: one of it fields is a collection; it is set up to
 * show objects within objects and one of the fields is an object but it is not a lookup.
 * 
 * @see org.nakedobjects.plugins.dnd.viewer.tree.OpenObjectNodeSpecification for displaying an open collection as
 *      part of an object.
 */
class ClosedObjectNodeSpecification extends NodeSpecification {

    private final boolean showObjectContents;

    public ClosedObjectNodeSpecification(final boolean showObjectContents) {
        this.showObjectContents = showObjectContents;
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        return content.isObject() && content.getNaked() != null;
    }

    @Override
    public int canOpen(final Content content) {
        final NakedObject object = ((ObjectContent) content).getObject();
        final NakedObjectAssociation[] fields = object.getSpecification().getAssociations(NakedObjectAssociationFilters.dynamicallyVisible(NakedObjectsContext.getAuthenticationSession(), object));
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isOneToManyAssociation()) {
                return CAN_OPEN;
            }
            if (showObjectContents && fields[i].isOneToOneAssociation() && !(SpecificationFacets.isBoundedSet(object.getSpecification()))) {
                return CAN_OPEN;
            }
        }
        return CANT_OPEN;
    }

    @Override
    protected View createNodeView(final Content content, final ViewAxis axis) {
        final View treeLeafNode = new LeafNodeView(content, this, axis);
        return treeLeafNode;
    }

    public String getName() {
        return "Object tree node - closed";
    }
}

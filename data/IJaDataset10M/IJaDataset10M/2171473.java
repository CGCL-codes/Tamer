package org.nakedobjects.plugins.dnd.viewer.list;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.border.ExpandableViewBorder;

public class ListWithExpandableElementsSpecification extends AbstractListSpecification {

    public String getName() {
        return "Expanding List";
    }

    public View createSubview(final Content content, final ViewAxis axis, int fieldNumber) {
        ViewRequirement requirement = new ViewRequirement(content, ViewRequirement.CLOSED | ViewRequirement.SUBVIEW);
        return Toolkit.getViewFactory().createView(requirement);
    }

    @Override
    public View decorateSubview(View subview) {
        return new ExpandableViewBorder(subview);
    }
}

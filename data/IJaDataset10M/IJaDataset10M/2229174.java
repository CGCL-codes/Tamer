package org.nakedobjects.plugins.dnd.viewer.lookup;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.viewer.view.simple.TextView;

class DropDownValueOverlaySpecification extends DropDownObjectOverlaySpecification {

    @Override
    public View createSubview(final Content content, final ViewAxis lookupAxis, int fieldNumber) {
        final TextView icon = new TextView(content, this, lookupAxis);
        return new DropDownSelection(icon);
    }

    @Override
    public String getName() {
        return "Value Drop Down Overlay";
    }
}

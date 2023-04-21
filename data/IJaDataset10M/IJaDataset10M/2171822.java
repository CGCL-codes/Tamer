package com.google.gdt.eclipse.designer.gxt.gef.policy;

import com.google.gdt.eclipse.designer.gxt.model.layout.AbsoluteLayoutInfo;
import com.google.gdt.eclipse.designer.model.widgets.WidgetInfo;

/**
 * {@link SelectionLayoutEditPolicy} for {@link AbsoluteLayoutInfo}.
 * 
 * @author scheglov_ke
 * @coverage ExtGWT.gef.policy
 */
public final class AbsoluteSelectionEditPolicy extends AnchorSelectionEditPolicy {

    public AbsoluteSelectionEditPolicy(WidgetInfo widget) {
        super(widget);
    }
}

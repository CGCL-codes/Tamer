package com.allen_sauer.gwt.dnd.client.drop;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.util.DragClientBundle;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;

/**
 * A {@link DropController} for instances of {@link FlowPanel}.
 */
public class FlowPanelDropController extends AbstractInsertPanelDropController {

    /**
   * @param dropTarget
   */
    public FlowPanelDropController(FlowPanel dropTarget) {
        super(dropTarget);
    }

    @Override
    protected LocationWidgetComparator getLocationWidgetComparator() {
        return LocationWidgetComparator.BOTTOM_RIGHT_COMPARATOR;
    }

    @Override
    protected Widget newPositioner(DragContext context) {
        HTML positioner = new HTML("&#x203B;");
        positioner.addStyleName(DragClientBundle.INSTANCE.css().flowPanelPositioner());
        return positioner;
    }
}

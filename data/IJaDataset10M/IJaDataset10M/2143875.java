package com.csci.finalproject.agileassistant.client.Backlog;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractInsertPanelDropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.DragClientBundle;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.csci.finalproject.agileassistant.client.Notecard;
import com.csci.finalproject.agileassistant.client.Postit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class BacklogDropController extends AbstractInsertPanelDropController {

    public BacklogDropController(InsertPanel dropTarget) {
        super((InsertPanel) dropTarget);
    }

    @Override
    protected LocationWidgetComparator getLocationWidgetComparator() {
        return LocationWidgetComparator.BOTTOM_HALF_COMPARATOR;
    }

    @Override
    protected Widget newPositioner(DragContext context) {
        SimplePanel outer = new SimplePanel();
        outer.addStyleName(DragClientBundle.INSTANCE.css().positioner());
        RootPanel.get().add(outer, -500, -500);
        outer.setWidget(new Label("x"));
        int width = context.draggable.getOffsetWidth();
        int height = context.draggable.getOffsetHeight();
        SimplePanel inner = new SimplePanel();
        inner.setPixelSize(width - DOMUtil.getHorizontalBorders(outer), height - DOMUtil.getVerticalBorders(outer));
        outer.setWidget(inner);
        return outer;
    }

    @Override
    public void onDrop(DragContext context) {
        super.onDrop(context);
        if (context.draggable.getClass() == Notecard.class) {
            Notecard nc = (Notecard) context.draggable;
            for (Postit p : nc.getPostits()) {
                p.removeFromParent();
            }
        }
    }

    @Override
    public void onPreviewDrop(DragContext context) throws VetoDragException {
        AbstractBacklog bl = (AbstractBacklog) dropTarget;
        if (!bl.getProject().moveIsPermissable(context)) {
            Window.alert("You do not have permission to do this!");
            throw new VetoDragException();
        }
        super.onPreviewDrop(context);
    }
}

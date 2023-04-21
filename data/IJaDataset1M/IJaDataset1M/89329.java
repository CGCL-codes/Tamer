package uk.ac.lkl.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.GridConstrainedDropController;
import com.allen_sauer.gwt.dnd.client.util.DragClientBundle;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Dropped widgets look selected
 * 
 * @author Ken Kahn
 *
 */
public class GridConstrainedSelectionStyleDropController extends GridConstrainedDropController {

    public GridConstrainedSelectionStyleDropController(AbsolutePanel dropTarget, int gridX, int gridY) {
        super(dropTarget, gridX, gridY);
    }

    @Override
    public void onDrop(DragContext context) {
        super.onDrop(context);
        for (Widget widget : context.selectedWidgets) {
            widget.addStyleName(DragClientBundle.INSTANCE.css().selected());
        }
    }
}

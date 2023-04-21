package sourceforge.pebblesframewor.gwt.client.window;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class PebPickupDragController extends PickupDragController {

    public PebPickupDragController(AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
        super(boundaryPanel, allowDroppingOnBoundaryPanel);
    }

    @Override
    protected void restoreSelectedWidgetsLocation() {
    }

    @Override
    protected void restoreSelectedWidgetsStyle() {
    }

    @Override
    protected void saveSelectedWidgetsLocationAndStyle() {
    }
}

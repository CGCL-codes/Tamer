package net.sf.freecol.client.gui.action;

import java.awt.event.ActionEvent;
import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.GUI;

/**
 * An action for zooming in on the main map.
 */
public class ZoomInAction extends FreeColAction {

    public static final String id = "zoomInAction";

    /**
     * Creates a new <code>ZoomInAction</code>.
     *
     * @param freeColClient The main controller object for the client.
     * @param gui 
     */
    ZoomInAction(FreeColClient freeColClient, GUI gui) {
        super(freeColClient, gui, id);
    }

    /**
     * Checks if this action should be enabled.
     *
     * @return <code>true</code> if the mapboard is selected
     *      and can be zoomed onto.
     */
    @Override
    protected boolean shouldBeEnabled() {
        if (!super.shouldBeEnabled()) {
            return false;
        }
        if (!gui.isMapboardActionsEnabled()) return false;
        float oldScaling = gui.getMapScale();
        return oldScaling < 1.0;
    }

    /**
     * Applies this action.
     *
     * @param e The <code>ActionEvent</code>.
     */
    public void actionPerformed(ActionEvent e) {
        gui.scaleMap(1 / 4f);
        update();
        freeColClient.getActionManager().getFreeColAction(ZoomOutAction.id).update();
    }
}

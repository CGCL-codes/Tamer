package net.sf.freecol.client.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.panel.ColopediaPanel;

/**
 * 
 */
public class ColopediaResourceAction extends MapboardAction {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(ColopediaResourceAction.class.getName());

    public static final String id = "colopediaResourceAction";

    /**
     * Creates this action.
     * @param freeColClient The main controller object for the client.
     */
    ColopediaResourceAction(FreeColClient freeColClient) {
        super(freeColClient, "menuBar.colopedia.resource", null, KeyEvent.VK_R);
    }

    /**
     * Checks if this action should be enabled.
     * 
     * @return true if this action should be enabled.
     */
    protected boolean shouldBeEnabled() {
        return true;
    }

    /**
     * Returns the id of this <code>Option</code>.
     * 
     * @return "colopediaResourceAction"
     */
    public String getId() {
        return id;
    }

    /**
     * Applies this action.
     * @param e The <code>ActionEvent</code>.
     */
    public void actionPerformed(ActionEvent e) {
        freeColClient.getCanvas().showColopediaPanel(ColopediaPanel.PanelType.RESOURCES);
    }
}

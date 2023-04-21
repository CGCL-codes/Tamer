package net.sf.freecol.client.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.panel.ColopediaPanel;

/**
 *
 */
public class ColopediaNationAction extends MapboardAction {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(ColopediaNationAction.class.getName());

    public static final String id = "colopediaNationAction";

    /**
     * Creates this action.
     * @param freeColClient The main controller object for the client.
     */
    ColopediaNationAction(FreeColClient freeColClient) {
        super(freeColClient, "menuBar.colopedia.nation", null, KeyEvent.VK_N);
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
     * @return "colopediaNationAction"
     */
    public String getId() {
        return id;
    }

    /**
     * Applies this action.
     * @param e The <code>ActionEvent</code>.
     */
    public void actionPerformed(ActionEvent e) {
        freeColClient.getCanvas().showColopediaPanel(ColopediaPanel.PanelType.NATIONS);
    }
}

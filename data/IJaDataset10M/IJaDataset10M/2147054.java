package net.sf.freecol.client.gui.action;

import java.awt.event.ActionEvent;
import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.GUI;

/**
 * An action for reconnecting to the server.
 */
public class ReconnectAction extends FreeColAction {

    public static final String id = "reconnectAction";

    /**
     * Creates a new <code>DeclareIndependenceAction</code>.
     *
     * @param freeColClient The main controller object for the client.
     * @param gui 
     */
    ReconnectAction(FreeColClient freeColClient, GUI gui) {
        super(freeColClient, gui, id);
    }

    /**
     * Applies this action.
     *
     * @param e The <code>ActionEvent</code>.
     */
    public void actionPerformed(ActionEvent e) {
        freeColClient.getConnectController().reconnect();
    }
}

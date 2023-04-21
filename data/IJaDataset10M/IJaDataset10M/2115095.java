package net.sf.freecol.client.gui.action;

import java.awt.event.ActionEvent;
import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.GUI;

/**
 * An action for editing trade routes.
 */
public class TradeRouteAction extends MapboardAction {

    public static final String id = "tradeRouteAction";

    /**
     * Creates this action.
     * @param freeColClient The main controller object for the client.
     * @param gui 
     */
    TradeRouteAction(FreeColClient freeColClient, GUI gui) {
        super(freeColClient, gui, id);
    }

    /**
     * Applies this action.
     * @param e The <code>ActionEvent</code>.
     */
    public void actionPerformed(ActionEvent e) {
        gui.showTradeRouteDialog(null, null);
    }
}

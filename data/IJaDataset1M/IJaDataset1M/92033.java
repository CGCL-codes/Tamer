package org.imajie.server.web.imajiematch.matchsServers.cartridges;

import org.imajie.server.web.imajiematch.matchsServers.common.CardPanel;
import org.imajie.server.web.imajiematch.matchsServers.common.FrameTimer;
import org.imajie.server.web.imajiematch.matchsServers.format.CartridgeFile;

/** Cartridge picker and launcher window.
 * <p>
 * Represents the window that is visible right after start-up. It has
 * a left-hand pane with a list of cartridges in the current directory
 * and a right-hand pane with details of the selected cartridge.
 * @see CartridgeDetails
 * @see CartridgeList
 */
public class CartridgeSelector {

    /** contents of left pane */
    CartridgeList list = new CartridgeList(this);

    /** contents of right pane */
    CartridgeDetails details = new CartridgeDetails(list);

    /** timer to handle navigation updates
	 * @see overview
	 */
    FrameTimer refresher;

    /** switcher between "please select" label and actual details display */
    CardPanel detailsPanel = new CardPanel();

    private static final String CMD_CHANGEDIR = "changeDir";

    public CartridgeSelector() {
    }

    /** Ensures that details pane is visible and updates it with information for <code>cf</code> */
    public void showDetails(CartridgeFile cf) {
        detailsPanel.show("details");
        details.showDetails(cf);
    }
}

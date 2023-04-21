package org.simbrain.network.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.network.gui.dialogs.network.KwtaNetworkDialog;

/**
 * New Kwta network action.
 */
public final class NewKwtaNetworkAction extends AbstractAction {

    /** Network panel. */
    private final NetworkPanel networkPanel;

    /**
     * Create a new new Kwta network action with the specified
     * network panel.
     *
     * @param networkPanel networkPanel, must not be null
     */
    public NewKwtaNetworkAction(final NetworkPanel networkPanel) {
        super("Kwta Network");
        if (networkPanel == null) {
            throw new IllegalArgumentException("networkPanel must not be null");
        }
        this.networkPanel = networkPanel;
    }

    /** @see AbstractAction */
    public void actionPerformed(final ActionEvent event) {
        KwtaNetworkDialog dialog = new KwtaNetworkDialog(networkPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}

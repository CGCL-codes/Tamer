package org.simbrain.network.gui.dialogs.connect;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import org.simbrain.network.connections.AllToAll;
import org.simbrain.network.gui.NetworkPanel;

/**
 * <b>AllToAllPanel</b> creates a dialog for setting preferences of all to all
 * neuron connections.
 *
 * @author ztosi
 * @author jyoshimi
 */
public class AllToAllPanel extends AbstractConnectionPanel {

    private ExcitatoryInhibitoryPropertiesPanel eipPanel;

    /** Allow self connection check box. */
    private JCheckBox allowSelfConnect = new JCheckBox();

    private boolean sourceContainsTarget;

    /**
     * This method is the default constructor.
     *
     * @param connection type
     */
    public AllToAllPanel(final AllToAll connection, final NetworkPanel networkPanel) {
        super(connection, networkPanel);
        if (networkPanel.getSelectedModelNeurons().equals(networkPanel.getSourceModelNeurons())) {
            sourceContainsTarget = true;
        } else {
            sourceContainsTarget = false;
        }
        layoutPanel();
    }

    private void layoutPanel() {
        eipPanel = new ExcitatoryInhibitoryPropertiesPanel(connection);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        int offset = 0;
        if (sourceContainsTarget) {
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 5, 0, 10);
            JPanel allowSelfConnectPanel = new JPanel();
            FlowLayout ASCPFL = new FlowLayout(FlowLayout.LEFT);
            allowSelfConnectPanel.setLayout(ASCPFL);
            allowSelfConnectPanel.add(new JLabel("Allow Self-Connections: "));
            allowSelfConnectPanel.add(allowSelfConnect);
            this.add(allowSelfConnectPanel, gbc);
            gbc.gridy = 1;
            gbc.gridwidth = 3;
            this.add(new JSeparator(), gbc);
            offset = 2;
        }
        gbc.gridy += offset;
        gbc.gridwidth = 3;
        gbc.gridheight = 9;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        this.add(eipPanel, gbc);
    }

    /**
     * {@inheritDoc}
     */
    public void commitChanges() {
        ((AllToAll) connection).setAllowSelfConnection(allowSelfConnect.isSelected());
        eipPanel.commitChanges();
    }

    /**
     * {@inheritDoc}
     */
    public void fillFieldValues() {
    }
}

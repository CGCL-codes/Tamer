package net.sourceforge.simplegamenet.specs.tools;

import javax.swing.*;
import net.sourceforge.simplegamenet.specs.gui.GameSettingsPanel;
import net.sourceforge.simplegamenet.util.proportionlayout.ProportionConstraints;
import net.sourceforge.simplegamenet.util.proportionlayout.ProportionLayout;

public class TwoPlayerGameSettingsPanel extends GameSettingsPanel {

    private JLabel maximumConnectionsAmountLabel;

    private JComboBox maximumConnectionsAmountComboBox;

    public TwoPlayerGameSettingsPanel() throws IllegalArgumentException {
        this(2, 2, 8);
    }

    public TwoPlayerGameSettingsPanel(int defaultMaximumConnectionsAmount) throws IllegalArgumentException {
        this(defaultMaximumConnectionsAmount, 2, defaultMaximumConnectionsAmount);
    }

    public TwoPlayerGameSettingsPanel(int defaultMaximumConnectionsAmount, int minimumMaximumConnectionsAmount, int maximumMaximumConnectionsAmount) throws IllegalArgumentException {
        ProportionLayout layout = new ProportionLayout();
        layout.appendColumn(0, ProportionLayout.NO_PROPORTION);
        layout.appendColumn(10);
        layout.appendColumn(0, 1.0);
        layout.appendRow(0, 1.0);
        layout.appendRow(0, ProportionLayout.NO_PROPORTION);
        layout.appendRow(0, 1.0);
        setLayout(layout);
        maximumConnectionsAmountLabel = new JLabel("Maximum amount of connections:");
        maximumConnectionsAmountComboBox = new JComboBox();
        for (int i = minimumMaximumConnectionsAmount; i <= maximumMaximumConnectionsAmount; i++) {
            Integer integer = new Integer(i);
            maximumConnectionsAmountComboBox.addItem(integer);
            if (i == defaultMaximumConnectionsAmount) {
                maximumConnectionsAmountComboBox.setSelectedItem(integer);
            }
        }
        add(maximumConnectionsAmountLabel, new ProportionConstraints(0, 1));
        add(maximumConnectionsAmountComboBox, new ProportionConstraints(2, 1));
    }

    public int getMaximumConnectionsAmount() {
        return ((Integer) maximumConnectionsAmountComboBox.getSelectedItem()).intValue();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        maximumConnectionsAmountComboBox.setEnabled(enabled);
    }
}

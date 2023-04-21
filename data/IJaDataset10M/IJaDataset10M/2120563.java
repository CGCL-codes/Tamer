package org.pybaldaj.gui;

import javax.swing.*;

/**
 * @author g3n1uss
 *
 */
@SuppressWarnings("serial")
public class InitSettingsPanel extends JPanel {

    public InitSettingsPanel() {
        this.buildPanel();
    }

    public void buildPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel settingsLabel = new JLabel("Settings");
        settingsLabel.setAlignmentY(CENTER_ALIGNMENT);
        add(settingsLabel);
        SettingsPanel ServerPortPanel = new SettingsPanel();
        ServerPortPanel.setAlignmentY(CENTER_ALIGNMENT);
        add(ServerPortPanel);
    }
}

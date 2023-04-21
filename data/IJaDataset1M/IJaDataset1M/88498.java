package org.edencrm.client;

import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JCheckBox;

public class WindowSettingsPanel extends JPanel {

    public WindowSettingsPanel(EdenCRMClient parent) {
        this.parent = parent;
        init();
    }

    public boolean isModified() {
        return modified;
    }

    public void revert() {
        Preferences preferences = parent.getPreferences();
        useUsersWindowSettingsCheckBox.setSelected(preferences.getUseUsersWindowSettings());
    }

    public void save() {
        Preferences preferences = parent.getPreferences();
        preferences.setUseUsersWindowSettings(useUsersWindowSettingsCheckBox.isSelected());
    }

    private void init() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        useUsersWindowSettingsCheckBox = new JCheckBox("Use my window settings");
        useUsersWindowSettingsCheckBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                modified = true;
            }
        });
        gbl.setConstraints(useUsersWindowSettingsCheckBox, gbc);
        add(useUsersWindowSettingsCheckBox);
        JPanel p = new JPanel();
        gbc.weighty = 1;
        gbl.setConstraints(p, gbc);
        add(p);
        revert();
    }

    private EdenCRMClient parent;

    private boolean modified = false;

    private JCheckBox useUsersWindowSettingsCheckBox;
}

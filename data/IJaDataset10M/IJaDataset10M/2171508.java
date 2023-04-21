package com.tmo;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PreferenceGui extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    JPanel jPanel;

    public PreferenceGui() {
        this.getContentPane().setLayout(new BorderLayout());
        jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
    }

    public void addBooleanField(String field, boolean value) {
        JCheckBox jCheckBox = new JCheckBox(field, value);
        jCheckBox.addActionListener(this);
        jPanel.add(jCheckBox);
    }

    public void showWindow(boolean val) {
        this.getContentPane().add(jPanel, BorderLayout.CENTER);
        this.pack();
        setVisible(val);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Logger.log("PreferenceGui: actionPerformed " + actionEvent.getActionCommand());
        JCheckBox jCheckBox = (JCheckBox) actionEvent.getSource();
        Preferences.getInstance().notifyPreferenceChanged(actionEvent.getActionCommand(), jCheckBox.getModel().isSelected());
    }
}

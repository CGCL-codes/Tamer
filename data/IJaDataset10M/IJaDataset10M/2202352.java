package com.intel.gui.editors.keystore2;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import com.intel.gpe.client2.common.panels.wrappers.OKCancelPanel;
import com.intel.gpe.client2.defaults.preferences.INode;
import com.intel.gui.controls2.configurable.ConfigurablePanel;
import com.intel.gui.controls2.configurable.IConfigurable;

public class PasswordPanel extends ConfigurablePanel implements OKCancelPanel {

    private char[] password;

    private JPasswordField passwordField = new JPasswordField(15);

    private String prompt;

    public PasswordPanel(IConfigurable parent, INode name) {
        super(parent, name);
        this.prompt = "Enter Password:";
        buildComponents();
    }

    public PasswordPanel(IConfigurable parent, INode name, String prompt) {
        super(parent, name);
        this.prompt = prompt;
        buildComponents();
    }

    /**
     * Takes current textfield entry as password
     * 
     * @return true if dialog can be disposed
     */
    public boolean applyValues() {
        password = passwordField.getPassword();
        return true;
    }

    /**
     * Add a password field to the parent dialog
     */
    private void buildComponents() {
        JLabel passwordLabel = new JLabel(prompt);
        JPanel passwordPanel = new JPanel();
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        passwordPanel.setBorder(new EmptyBorder(20, 5, 10, 5));
        add(passwordPanel, BorderLayout.CENTER);
    }

    /**
     * Sets password to null
     * 
     * @return true if dialog can be disposed
     */
    public boolean cancelValues() {
        password = null;
        return true;
    }

    /**
     * Get the result from the dialog
     * 
     * @return null if cancel was pressed and a char array if ok was pressed
     */
    public char[] getPassword() {
        return password;
    }

    /**
     * Put focus into password field
     */
    public void setVisible(boolean visible) {
        if (visible) {
            passwordField.requestFocus();
        }
        setVisible(true);
    }

    public Component getComponent() {
        return this;
    }
}

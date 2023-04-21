package org.ozoneDB.adminGui.feature.account.users;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WorkUserPanel extends JPanel {

    /** The text field used to enter the account name. */
    private JTextField userNameText = new JTextField();

    /** The text field used to enter the account password. */
    private JTextField userPasswordText = new JTextField();

    /**
     * Default constructor.
     */
    public WorkUserPanel() {
        super();
        init();
    }

    /**
     * This method initializes the input panel which gets the account information.
     */
    private void init() {
        this.setLayout(new GridBagLayout());
        this.add(new JLabel("User Name:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
        this.add(userNameText, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 150, 0));
        this.add(new JLabel("User Password:"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
        this.add(userPasswordText, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 150, 0));
    }

    /**
     * This method returns the account name.
     *
     * @return String - account name.
     */
    public String getUserName() {
        return userNameText.getText();
    }

    /**
     * This method returns the account password.
     *
     * @return String - account password.
     */
    public String getUserPassword() {
        return userPasswordText.getText();
    }
}

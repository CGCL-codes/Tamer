package de.buelowssiege.jaymail.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import de.buelowssiege.jaymail.bin.jayMail;
import de.buelowssiege.utils.gui.ExtendedJPanel;

/**
 * Description of the Class
 * 
 * @author Maximilian Schwerin
 * @created 17. Juli 2002
 */
public class OutServerEdit extends ExtendedJPanel implements ActionListener {

    private JTextField server = new JTextField(40);

    private JComboBox protocol = new JComboBox(jayMail.OUT_SERVER_PROTOCOLS);

    private JTextField port = new JTextField(jayMail.IN_SERVER_DEFAULT_PORTS[0] + "", 40);

    private JTextField login = new JTextField(40);

    private JPasswordField pass = new JPasswordField(40);

    private JCheckBox useESMTP = new JCheckBox(jayMail.getString("label.esmtp"));

    private JCheckBox useSMTPAfterPOP = new JCheckBox(jayMail.getString("label.smtpafterpop"));

    private JLabel loginLabel = new JLabel(jayMail.getString("label.login"));

    private JLabel passLabel = new JLabel(jayMail.getString("label.pass"));

    private boolean POP3Selected = true;

    /**
     * This is the constructor for the <code>OutServerEdit</code>
     */
    public OutServerEdit() {
        setBorder(new EmptyBorder(8, 8, 8, 8));
        addHeaderBar(jayMail.getString("header.dividers.preferences.login"));
        addLabelInput(loginLabel, login);
        addLabelInput(passLabel, pass);
        addHeaderBar(jayMail.getString("header.dividers.preferences.server"));
        addLabelInput(jayMail.getString("label.server"), server);
        addLabelInput(jayMail.getString("label.port"), port);
        addLabelInput(jayMail.getString("label.protocol"), protocol);
        addInputComponent(useESMTP);
        addInputComponent(useSMTPAfterPOP);
        fillRemainder();
        protocol.addActionListener(this);
        useESMTP.addActionListener(this);
        useSMTPAfterPOP.addActionListener(this);
        port.setText(jayMail.OUT_SERVER_DEFAULT_PORTS[0] + "");
        useSMTPAfterPOP.setEnabled(false);
    }

    /**
     * Its selfexplaining isnt it! :-)
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == protocol || ae.getSource() == useESMTP || ae.getSource() == useSMTPAfterPOP) {
            setEnabled();
        }
    }

    /**
     * Sets the enabled attribute of the OutServerEdit object
     */
    public void setEnabled() {
        port.setText(jayMail.OUT_SERVER_DEFAULT_PORTS[protocol.getSelectedIndex()] + "");
        login.setEnabled(useESMTP.isSelected() && !useSMTPAfterPOP.isSelected());
        loginLabel.setEnabled(useESMTP.isSelected() && !useSMTPAfterPOP.isSelected());
        pass.setEnabled(useESMTP.isSelected() && !useSMTPAfterPOP.isSelected());
        passLabel.setEnabled(useESMTP.isSelected() && !useSMTPAfterPOP.isSelected());
        useESMTP.setEnabled(!useSMTPAfterPOP.isSelected());
        useSMTPAfterPOP.setEnabled(!useESMTP.isSelected() && POP3Selected);
        if (!POP3Selected) {
            useESMTP.setEnabled(true);
            useSMTPAfterPOP.setSelected(false);
        }
        useSMTPAfterPOP.setEnabled(false);
    }

    /**
     * Gets the server attribute of the OutServerEdit object
     */
    public JTextField getServer() {
        return (server);
    }

    /**
     * Gets the port attribute of the OutServerEdit object
     */
    public JTextField getPort() {
        return (port);
    }

    /**
     * Gets the protocol attribute of the OutServerEdit object
     */
    public JComboBox getProtocol() {
        return (protocol);
    }

    /**
     * Gets the login attribute of the OutServerEdit object
     */
    public JTextField getLogin() {
        return (login);
    }

    /**
     * Gets the pass attribute of the OutServerEdit object
     */
    public JPasswordField getPass() {
        return (pass);
    }

    /**
     * Gets the eSMTP attribute of the OutServerEdit object
     */
    public JCheckBox getESMTP() {
        return (useESMTP);
    }

    /**
     * Gets the sMTPAfterPOP attribute of the OutServerEdit object
     */
    public JCheckBox getSMTPAfterPOP() {
        return (useSMTPAfterPOP);
    }

    /**
     * Gets the pOP3Selected attribute of the OutServerEdit object
     */
    public void isPOP3Selected(boolean selected) {
        POP3Selected = selected;
    }
}

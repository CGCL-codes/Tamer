package com.intel.gpe.client2.common.gpe4gtk.security;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import com.intel.gpe.client2.common.i18n.Messages;
import com.intel.gpe.client2.common.i18n.MessagesKeys;
import com.intel.gpe.client2.defaults.preferences.INode;
import com.intel.gui.controls2.configurable.IConfigurable;
import com.intel.util.gui.LayoutTools;

/**
 * @version $Id: PutToMyProxyPanel.java,v 1.10 2007/02/20 15:46:50 dizhigul Exp $
 * @author Dmitry Petrov
 */
public class PutToMyProxyPanel extends MyProxyAccountPanel {

    private static final long serialVersionUID = 2002651456166775307L;

    private JLabel labelLifetime;

    private JTextField textFieldLifetime;

    private JLabel labelPassphrase;

    private JPasswordField textFieldPassphrase;

    private JLabel labelPassphraserepeat;

    private JPasswordField textFieldPassphraserepeat;

    private String passphrase = null;

    private int lifetime;

    public PutToMyProxyPanel(IConfigurable parent, INode name, String title, String defaultServer, int defaultPort) {
        super(parent, name, title, defaultServer, defaultPort);
    }

    protected void buildComponents() {
        JPanel panel = new JPanel(new BorderLayout(0, 25));
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc;
        serverPanel = new JPanel(gbl);
        labelServerName = new JLabel(Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_MyProxy_server_name));
        gbc = LayoutTools.makegbc(0, 0, 1, 0, false);
        gbc.insets = new Insets(15, 20, 7, 22);
        gbl.setConstraints(labelServerName, gbc);
        serverPanel.add(labelServerName);
        textFieldServerName = new JTextField("", 20);
        gbc = LayoutTools.makegbc(1, 0, 1, 100, true);
        gbc.insets = new Insets(15, 0, 7, 20);
        gbl.setConstraints(textFieldServerName, gbc);
        serverPanel.add(textFieldServerName);
        labelServerPort = new JLabel(Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_MyProxy_server_port));
        gbc = LayoutTools.makegbc(0, 1, 1, 0, false);
        gbc.insets = new Insets(0, 20, 20, 22);
        gbl.setConstraints(labelServerPort, gbc);
        serverPanel.add(labelServerPort);
        textFieldServerPort = new JTextField("", 20);
        gbc = LayoutTools.makegbc(1, 1, 1, 100, true);
        gbc.insets = new Insets(0, 0, 20, 20);
        gbl.setConstraints(textFieldServerPort, gbc);
        serverPanel.add(textFieldServerPort);
        userPanel = new JPanel(gbl);
        labelUsername = new JLabel(Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_Username));
        gbc = LayoutTools.makegbc(0, 0, 1, 0, false);
        gbc.insets = new Insets(15, 20, 7, 10);
        gbl.setConstraints(labelUsername, gbc);
        userPanel.add(labelUsername);
        textFieldUsername = new JTextField("", 20);
        gbc = LayoutTools.makegbc(1, 0, 1, 100, true);
        gbc.insets = new Insets(15, 0, 7, 20);
        gbl.setConstraints(textFieldUsername, gbc);
        userPanel.add(textFieldUsername);
        labelPassphrase = new JLabel(Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_Passphrase));
        gbc = LayoutTools.makegbc(0, 1, 1, 0, false);
        gbc.insets = new Insets(15, 20, 7, 10);
        gbl.setConstraints(labelPassphrase, gbc);
        userPanel.add(labelPassphrase);
        textFieldPassphrase = new JPasswordField("", 20);
        gbc = LayoutTools.makegbc(1, 1, 1, 100, true);
        gbc.insets = new Insets(15, 0, 7, 20);
        gbl.setConstraints(textFieldPassphrase, gbc);
        userPanel.add(textFieldPassphrase);
        labelPassphraserepeat = new JLabel(Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_Repeat_passphrase));
        gbc = LayoutTools.makegbc(0, 2, 1, 0, false);
        gbc.insets = new Insets(15, 20, 7, 10);
        gbl.setConstraints(labelPassphraserepeat, gbc);
        userPanel.add(labelPassphraserepeat);
        textFieldPassphraserepeat = new JPasswordField("", 20);
        gbc = LayoutTools.makegbc(1, 2, 1, 100, true);
        gbc.insets = new Insets(15, 0, 7, 20);
        gbl.setConstraints(textFieldPassphraserepeat, gbc);
        userPanel.add(textFieldPassphraserepeat);
        labelLifetime = new JLabel(Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_Lifetime_in_days));
        gbc = LayoutTools.makegbc(0, 3, 1, 0, false);
        gbc.insets = new Insets(15, 20, 7, 10);
        gbl.setConstraints(labelLifetime, gbc);
        userPanel.add(labelLifetime);
        textFieldLifetime = new JTextField("", 20);
        gbc = LayoutTools.makegbc(1, 3, 1, 100, true);
        gbc.insets = new Insets(15, 0, 7, 20);
        gbl.setConstraints(textFieldLifetime, gbc);
        userPanel.add(textFieldLifetime);
        Border etched = BorderFactory.createEtchedBorder();
        TitledBorder title1 = BorderFactory.createTitledBorder(etched, Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_Server));
        TitledBorder title2 = BorderFactory.createTitledBorder(etched, Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_Credential_Info));
        title1.setTitleFont(new Font("Dialog", Font.ITALIC, 12));
        title2.setTitleFont(new Font("Dialog", Font.ITALIC, 12));
        serverPanel.setBorder(title1);
        userPanel.setBorder(title2);
        panel.add(serverPanel, BorderLayout.NORTH);
        panel.add(userPanel, BorderLayout.CENTER);
        this.add(panel);
    }

    public boolean applyValues() {
        if (super.applyValues()) {
            if (((String) textFieldLifetime.getText()).equals("") || (textFieldPassphrase.getPassword().length == 0) || (textFieldPassphraserepeat.getPassword().length == 0)) {
                this.resetValues();
                this.setErrorColor();
                JOptionPane.showMessageDialog(this, Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_The_____fields_can_not_be_empty), Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_The_____fields_can_not_be_emptyPutToMyProxyPanel_ERROR), JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!equalsPasswords(textFieldPassphraserepeat.getPassword(), textFieldPassphrase.getPassword())) {
                this.resetValues();
                this.setErrorColor();
                JOptionPane.showMessageDialog(this, Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_Passphrases_are_not_the_same), Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_Passphrases_are_not_the_samePutToMyProxyPanel_ERROR), JOptionPane.ERROR_MESSAGE);
                return false;
            }
            try {
                lifetime = Integer.parseInt(textFieldLifetime.getText());
            } catch (NumberFormatException e) {
                this.resetValues();
                textFieldLifetime.setForeground(Color.RED);
                JOptionPane.showMessageDialog(this, Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_Lifetime_must_be_a_number), Messages.getString(MessagesKeys.common_gpe4gtk_security_PutToMyProxyPanel_Lifetime_must_be_a_numberPutToMyProxyPanel_ERROR), JOptionPane.ERROR_MESSAGE);
                return false;
            }
            passphrase = new String(textFieldPassphrase.getPassword());
            return true;
        } else {
            return false;
        }
    }

    private static boolean equalsPasswords(char[] password1, char[] password2) {
        if (password1.length != password2.length) {
            return false;
        } else {
            for (int i = 0; i < password1.length; i++) {
                if (password1[i] != password2[i]) return false;
            }
        }
        return true;
    }

    protected void setErrorColor() {
        super.setErrorColor();
        if (textFieldLifetime.getText().equals("")) {
            textFieldLifetime.setForeground(Color.RED);
        } else {
            textFieldLifetime.setForeground(Color.BLACK);
        }
        if (textFieldPassphrase.getPassword().length == 0) {
            textFieldPassphrase.setForeground(Color.RED);
        } else {
            textFieldPassphrase.setForeground(Color.BLACK);
        }
        if (textFieldPassphraserepeat.getPassword().length == 0) {
            textFieldPassphraserepeat.setForeground(Color.RED);
        } else {
            textFieldPassphraserepeat.setForeground(Color.BLACK);
        }
        if (!equalsPasswords(textFieldPassphraserepeat.getPassword(), textFieldPassphrase.getPassword())) {
            textFieldPassphrase.setForeground(Color.RED);
            textFieldPassphraserepeat.setForeground(Color.RED);
        } else {
            textFieldPassphrase.setForeground(Color.BLACK);
            textFieldPassphraserepeat.setForeground(Color.BLACK);
        }
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public int getLifetime() {
        return lifetime;
    }
}

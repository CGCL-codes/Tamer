package com.signaturedomains.urc;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.signaturedomains.gri.Registry;

public class CreateContactPanel extends JPanel implements DomainTool {

    private static final String TITLE = "Create Contact";

    private static Log log = LogFactory.getLog(CreateContactPanel.class);

    private Registry registry;

    private JLabel nameLabel;

    private JTextField nameField;

    private JLabel securityPhraseLabel;

    private JTextField securityPhraseField;

    public CreateContactPanel(Registry registry) {
        this.registry = registry;
        init();
    }

    public Component getComponent() {
        return this;
    }

    public String getTitle() {
        return TITLE;
    }

    public void execute() {
        Thread t = new Thread() {

            public void run() {
                URCContact contact = new URCContact();
                contact.setName(nameField.getText());
                contact.setSecurityPhrase(securityPhraseField.getText());
                try {
                    registry.createContact(contact);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private void init() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameLabel = new JLabel("Name");
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbl.setConstraints(nameLabel, gbc);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setColumns(32);
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(nameField, gbc);
        add(nameField);
        securityPhraseLabel = new JLabel("Security Phrase");
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbl.setConstraints(securityPhraseLabel, gbc);
        add(securityPhraseLabel);
        securityPhraseField = new JTextField();
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(securityPhraseField, gbc);
        add(securityPhraseField);
    }
}

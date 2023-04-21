package de.iqcomputing.flap.rules;

import java.awt.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import org.jdom.*;
import de.iqcomputing.flap.*;
import de.iqcomputing.jdom.*;

public class HeaderAction extends Action {

    protected JPanel panel;

    protected String header;

    protected String value;

    protected JTextField tfHeader;

    protected JTextField tfValue;

    public HeaderAction() {
        this.header = "";
        this.value = "";
    }

    public HeaderAction(String header, String value) {
        this.header = header;
        this.value = value;
    }

    public static HeaderAction constructHeaderAction(Element action, int propertiesVersion) {
        return new HeaderAction(JDOMTools.getElementContentText(action.getChild("header")), JDOMTools.getElementContentText(action.getChild("value")));
    }

    public void apply(Message msg, RuleApplicationFlags flags) throws RuleException {
        if (!flags.stop()) {
            if (!(msg instanceof MimeMessage)) throw new RuleException("cannot add header \"" + header + ": " + value + "\" to " + "non-MIME message");
            try {
                ((MimeMessage) msg).addHeader(header, value);
                flags.setModified();
            } catch (MessagingException e) {
                throw new RuleException(e.getMessage());
            }
        }
    }

    public Element actionElement() {
        Element action = new Element("action");
        Element e;
        action.setAttribute(new Attribute("type", "header"));
        e = new Element("header");
        e.addContent(header);
        action.addContent(e);
        e = new Element("value");
        e.addContent(value);
        action.addContent(e);
        return action;
    }

    public Object clone() throws CloneNotSupportedException {
        HeaderAction act = (HeaderAction) super.clone();
        act.header = header;
        act.value = value;
        return act;
    }

    public static String getDisplayName() {
        return "Add message header";
    }

    public String getDisplayNameWithOptions() {
        return "Add message header: " + header + " = " + value;
    }

    public Component getComponent() {
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        Component c;
        panel = new JPanel();
        panel.setLayout(gb);
        c = new JLabel("Header:");
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 3, 3);
        gb.setConstraints(c, gbc);
        panel.add(c);
        tfHeader = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 3, 0);
        gb.setConstraints(tfHeader, gbc);
        panel.add(tfHeader);
        c = new JLabel("Value:");
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 3);
        gb.setConstraints(c, gbc);
        panel.add(c);
        tfValue = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        gb.setConstraints(tfValue, gbc);
        panel.add(tfValue);
        return panel;
    }

    public void fillGUIComponents() {
        tfHeader.setText(header);
        tfValue.setText(value);
    }

    public boolean applyGUIOptions() {
        header = tfHeader.getText();
        value = tfValue.getText();
        if (header.trim().length() > 0) return true;
        JOptionPane.showMessageDialog(panel, "You did not specify a header.", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}

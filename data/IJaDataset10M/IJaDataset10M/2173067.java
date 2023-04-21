package org.xngr.service.example.address.editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The panel that allows the user to edit an example address element.
 * 
 * @version $Revision: 1.1.1.1 $, $Date: 2002/02/16 16:08:45 $
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class AddressEditorPanel extends JPanel {

    private static final long serialVersionUID = 6820451486841087816L;

    private Element element = null;

    private JTextField lastnameField = null;

    private JTextField firstnameField = null;

    private JTextField streetField = null;

    private JTextField cityField = null;

    private JTextField zipField = null;

    private JTextField countryField = null;

    private JTextField phoneField = null;

    private JTextField mobileField = null;

    private JTextField emailField = null;

    private JTextField faxField = null;

    /**
	 * Constructs the panel.
	 */
    public AddressEditorPanel() {
        super(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.insets = new Insets(2, 5, 0, 0);
        gbc.anchor = GridBagConstraints.EAST;
        firstnameField = createTextField();
        firstnameField.setPreferredSize(new Dimension(180, 21));
        add("First name", firstnameField, gbc);
        lastnameField = createTextField();
        add("Last name", lastnameField, gbc);
        addSeparator(gbc);
        streetField = createTextField();
        add("Street", streetField, gbc);
        cityField = createTextField();
        add("City", cityField, gbc);
        zipField = createTextField();
        add("Zip", zipField, gbc);
        countryField = createTextField();
        add("Country", countryField, gbc);
        addSeparator(gbc);
        phoneField = createTextField();
        add("Phone", phoneField, gbc);
        mobileField = createTextField();
        add("Mobile", mobileField, gbc);
        emailField = createTextField();
        add("E-mail", emailField, gbc);
        faxField = createTextField();
        add("Fax", faxField, gbc);
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    /**
	 * Sets the element for the editor.
	 * 
	 * @param element
	 *            the address element.
	 */
    public void setElement(Element element) {
        this.element = element;
        lastnameField.setText(getValue(element, "lastname"));
        firstnameField.setText(getValue(element, "firstname"));
        streetField.setText(getValue(element, "street"));
        cityField.setText(getValue(element, "city"));
        zipField.setText(getValue(element, "zip"));
        countryField.setText(getValue(element, "country"));
        phoneField.setText(getValue(element, "phone"));
        mobileField.setText(getValue(element, "mobile"));
        emailField.setText(getValue(element, "email"));
        faxField.setText(getValue(element, "fax"));
    }

    /**
	 * Returns the currently edited element.
	 * 
	 * @return the currently edited element.
	 */
    public Element getElement() {
        commit();
        return element;
    }

    /**
	 * Sets the information filled in in the panel in the element.
	 */
    public void commit() {
        if (element != null) {
            setValue(element, "lastname", lastnameField.getText());
            setValue(element, "firstname", firstnameField.getText());
            setValue(element, "street", streetField.getText());
            setValue(element, "city", cityField.getText());
            setValue(element, "zip", zipField.getText());
            setValue(element, "country", countryField.getText());
            setValue(element, "phone", phoneField.getText());
            setValue(element, "mobile", mobileField.getText());
            setValue(element, "email", emailField.getText());
            setValue(element, "fax", faxField.getText());
        }
    }

    /**
	 * Refreshes/resets the information in the panel.
	 */
    public void refresh() {
        setElement(element);
    }

    private void add(String text, JTextField field, GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel(text + ":"), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(field, gbc);
    }

    private void addSeparator(GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(createSeparator(), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(createSeparator(), gbc);
    }

    private JPanel createSeparator() {
        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(10, 10));
        return separator;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        return field;
    }

    private String getValue(Element parent, String name) {
        String result = "";
        NodeList list = parent.getElementsByTagName(name);
        if (list.getLength() > 0) {
            result = list.item(0).getTextContent();
        }
        return result;
    }

    private void setValue(Element parent, String name, String value) {
        NodeList list = parent.getElementsByTagName(name);
        if (list.getLength() > 0) {
            list.item(0).setTextContent(value);
        }
    }
}

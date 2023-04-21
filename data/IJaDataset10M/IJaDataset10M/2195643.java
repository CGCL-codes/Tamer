package com.loribel.tools.xml.swing;

import gb.fwk.gui.editor.GB_TextFieldEditor;
import org.w3c.dom.Node;

/**
 * Component to represent an attribute of an Element in a textField.
 *
 * @author Gr�gory Borelli
 * @version 2003/09/25 - 17:43:48 - gen 7.11
 */
public class GB_AttributeTextField extends GB_TextFieldEditor {

    /**
     * The XML attribute to represent in this component.
     */
    private Node attribute;

    /**
     * Constructor of GB_AttributeTextField with parameter(s).
     *
     * @param a_attribute Node - the XML attribute to represent in this component
     */
    public GB_AttributeTextField(Node a_attribute) {
        super();
        attribute = a_attribute;
        init();
    }

    /**
     * Get the XML attribute to represent in this component.
     *
     * @return Node - <tt>attribute</tt>
     */
    public Node getAttribute() {
        return attribute;
    }

    /**
     * Method init.
     * <br />
     */
    private void init() {
        String l_value = attribute.getNodeValue();
        if (l_value == null) {
            l_value = "";
        }
        this.setText(l_value);
    }

    /**
     * Update the value of node from this textField content.
     */
    protected void updateValueFromComponentLive() {
        super.updateValueFromComponentLive();
        String l_newValue = this.getText();
        attribute.setNodeValue(l_newValue);
    }
}

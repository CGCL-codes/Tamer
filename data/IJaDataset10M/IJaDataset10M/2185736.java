package issrg.editor2.configurations;

import issrg.editor2.OIDinputTextField;
import issrg.utils.gui.xml.NodeItemList;
import issrg.utils.gui.xml.XMLChangeListener;
import issrg.utils.gui.xml.XMLEditor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Christian Azzopardi
 */
public class RoleTypeParametersConfiguration extends NodeItemList implements XMLChangeListener, KeyListener, ActionListener, ItemListener {

    /**
     * Panels needed to place the component on, and get the GUI right. 
     */
    private JPanel eastPanel, westPanel, envParamConfigPanel;

    /**
     * Labels that will accompany the JTextFields, and will inform the user
     * what input is required.
     */
    private JLabel oidLabel, typeLabel, dataTypeLabel;

    /**
     * TextFields which refer to the user's input.
     */
    private OIDinputTextField oidTextField;

    private JTextField typeTextField;

    /**
     * Drop Down List that allows users to select the data Type for the RoleType.
     */
    private JComboBox dataTypeField;

    /**
     * A description of what the dialog does (to fill up space).
     */
    private JTextArea descriptionText;

    /**
     * Loads the Resource Strings and Labels for everything to display correctly.
     */
    ResourceBundle rbl = ResourceBundle.getBundle("issrg/editor2/PEComponent_i18n");

    String oidLabelCaption = rbl.getString("RoleType_Parameters_ConfigDialog_Label_OID");

    String typeLabelCaption = rbl.getString("RoleType_Parameters_ConfigDialog_Label_Type");

    String dataTypeLabelCaption = rbl.getString("RoleType_Parameters_ConfigDialog_Label_Data_Type");

    String westBorderCaption = rbl.getString("RoleType_Parameters_ConfigDialog_Border_West");

    String eastBorderCaption = rbl.getString("RoleType_Parameters_ConfigDialog_Border_East");

    String textBorderCaption = rbl.getString("RoleType_Parameters_ConfigDialog_Border_TextArea");

    String textCaption = rbl.getString("RoleType_Parameters_ConfigDialog_TextArea_Contents");

    String addButtonCaption = rbl.getString("RoleType_Parameters_ConfigDialog_Button_Add");

    String delButtonCaption = rbl.getString("RoleType_Parameters_ConfigDialog_Button_Delete");

    String repButtonCaption = rbl.getString("RoleType_Parameters_ConfigDialog_Button_Replace");

    String errorHeader = rbl.getString("ErrorHeader");

    String infoHeader = rbl.getString("InfoHeader");

    String roleTypesParamsConfigError1 = rbl.getString("RoleType_Parameters_ConfigDialog_ErrorMSG1");

    String roleTypesParamsConfigError2 = rbl.getString("RoleType_Parameters_ConfigDialog_ErrorMSG2");

    String roleTypesParamsConfigError4 = rbl.getString("RoleType_Parameters_ConfigDialog_ErrorMSG3");

    String roleTypesParamsConfigError3 = rbl.getString("oidTF_LOST_FOCUS_MSG_ERROR");

    ResourceBundle rb = ResourceBundle.getBundle("issrg/editor2/PEIFConditions_i18n");

    String type1 = rb.getString("type1");

    String type2 = rb.getString("type2");

    String type3 = rb.getString("type3");

    String type4 = rb.getString("type4");

    String type5 = rb.getString("type5");

    String type6 = rb.getString("type6");

    String type7 = rb.getString("type7");

    /** Creates a new instance of RoleTypeParametersConfiguration */
    public RoleTypeParametersConfiguration(XMLEditor that) {
        super(that);
        this.setLayout(new BorderLayout());
        this.add(getContentPanel(), BorderLayout.CENTER);
        setCaption("ADD_BUTTON", addButtonCaption);
        setCaption("DELETE_BUTTON", delButtonCaption);
        setCaption("REPLACE_BUTTON", repButtonCaption);
    }

    /**
     * Method that creates a GUI Panel with the required fields to create
     * and modify an Environment Variable
     *
     * @return     a JPanel with the Display for the Environment Parameters 
     *             configuration.
     */
    public JPanel getContentPanel() {
        eastPanel = super.getContentPanel();
        westPanel = new JPanel();
        envParamConfigPanel = new JPanel();
        oidLabel = new JLabel(oidLabelCaption);
        typeLabel = new JLabel(typeLabelCaption);
        dataTypeLabel = new JLabel(dataTypeLabelCaption);
        oidTextField = new OIDinputTextField();
        typeTextField = new JTextField(15);
        dataTypeField = new JComboBox();
        dataTypeField.addItemListener(this);
        dataTypeField.addItem("");
        dataTypeField.addItem(type3);
        dataTypeField.addItem(type4);
        dataTypeField.addItem(type5);
        dataTypeField.addItem(type6);
        dataTypeField.addItem(type1);
        dataTypeField.addItem(type2);
        dataTypeField.addItem(type7);
        oidTextField.addKeyListener(this);
        typeTextField.addKeyListener(this);
        oidTextField.addActionListener(this);
        oidTextField.setActionCommand("oid_tf");
        typeTextField.addActionListener(this);
        typeTextField.setActionCommand("type_tf");
        descriptionText = new JTextArea(textCaption);
        descriptionText.setPreferredSize(new Dimension(250, 130));
        descriptionText.setFont(new Font("Dialog", 0, 14));
        descriptionText.setForeground(Color.BLACK);
        descriptionText.setBackground(new Color(236, 233, 216));
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setEditable(false);
        westPanel.setLayout(new GridBagLayout());
        envParamConfigPanel.setLayout(new GridBagLayout());
        constraints.insets = new Insets(0, 5, 0, 5);
        addComponent(typeLabel, westPanel, 0, 0, 1, 1, GridBagConstraints.LINE_END, 0, 0, GridBagConstraints.NONE);
        addComponent(typeTextField, westPanel, 0, 1, 1, 1, GridBagConstraints.LINE_END, 1, 0, GridBagConstraints.HORIZONTAL);
        constraints.insets = new Insets(5, 5, 5, 5);
        addComponent(oidLabel, westPanel, 1, 0, 1, 1, GridBagConstraints.LINE_END, 0, 0, GridBagConstraints.NONE);
        addComponent(oidTextField, westPanel, 1, 1, 1, 1, GridBagConstraints.LINE_END, 1, 0, GridBagConstraints.HORIZONTAL);
        constraints.insets = new Insets(0, 5, 0, 5);
        addComponent(dataTypeLabel, westPanel, 2, 0, 1, 1, GridBagConstraints.LINE_END, 0, 0, GridBagConstraints.NONE);
        addComponent(dataTypeField, westPanel, 2, 1, 1, 1, GridBagConstraints.LINE_END, 1, 0, GridBagConstraints.HORIZONTAL);
        addComponent(eastPanel, envParamConfigPanel, 0, 1, 1, 3, GridBagConstraints.CENTER, 0.5, 1, GridBagConstraints.BOTH);
        eastPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), eastBorderCaption));
        addComponent(westPanel, envParamConfigPanel, 1, 0, 1, 1, GridBagConstraints.CENTER, 0.5, 0, GridBagConstraints.HORIZONTAL);
        westPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), westBorderCaption));
        addComponent(descriptionText, envParamConfigPanel, 2, 0, 1, 1, GridBagConstraints.CENTER, 0, 0, GridBagConstraints.NONE);
        descriptionText.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), textBorderCaption));
        return envParamConfigPanel;
    }

    /**
     * This method can return true, if the Connection Name variable exists 
     * already. If The connection does not exist it will return false. 
     * This is used as a measure so that there will be no double entries.
     *
     * @param childToCheck   The element variable with the connection to check 
     *                       if already exists in the LDAP directory already.
     *
     * @return   a boolean which says if a connection name exists or not in the 
     *           'pe.cfg' file.
     */
    public boolean isInPolicy(Element childToCheck) {
        NodeList nlist1 = ((Element) getParentNode()).getElementsByTagName("RoleSpec");
        for (int i = 0; i < nlist.getLength(); i++) {
            Element child = (Element) nlist1.item(i);
            if ((childToCheck.getAttribute("Name").equals(child.getAttribute("Name"))) || (childToCheck.getAttribute("OID").equals(child.getAttribute("OID")))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reads the Fields from the TextBoxes. Checks that the required entries 
     * to create a valid connection are not empty fields. Then adds the 
     * new Directory to the XML document.
     */
    public void addItem() {
        oidTextField.focusLost(new FocusEvent(oidTextField, FocusEvent.FOCUS_LOST));
        if (!oidTextField.isValidFormat) {
            JOptionPane.showMessageDialog(this, roleTypesParamsConfigError3, rbl.getString("ErrorHeader"), JOptionPane.ERROR_MESSAGE);
            oidTextField.grabFocus();
            return;
        }
        if (oidTextField.getText().intern().equals("") || typeTextField.getText().intern().equals("")) {
            JOptionPane.showMessageDialog(xmlED, roleTypesParamsConfigError1, errorHeader, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dataTypeField.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(xmlED, roleTypesParamsConfigError4, errorHeader, JOptionPane.ERROR_MESSAGE);
            return;
        }
        Element child = xmlED.DOM.createElement("RoleSpec");
        child.setAttribute("OID", oidTextField.getText());
        child.setAttribute("Name", typeTextField.getText());
        child.setAttribute("Type", (String) dataTypeField.getSelectedItem());
        if (isInPolicy(child)) {
            JOptionPane.showMessageDialog(xmlED, roleTypesParamsConfigError2, errorHeader, JOptionPane.ERROR_MESSAGE);
            return;
        }
        xmlED.addItem(child, (Element) getParentNode());
        typeTextField.grabFocus();
    }

    /**
     * Deletes the Currently Selected node from the NodeList.
     */
    public void deleteItem() {
        if (this.getSelectedNode() != null) {
            xmlED.deleteItem((Element) this.getSelectedNode(), ((Element) getParentNode()));
            clearTextFields();
        }
    }

    /**
     * Method that resets all the Textfields to empty or a predefined value.
     */
    public void clearTextFields() {
        oidTextField.setText("");
        typeTextField.setText("");
        dataTypeField.setSelectedIndex(0);
    }

    /**
     * Method that replaces a Selected Node with the contents of the textfields,
     * Provided that the required fields have a valid Variable Definition, are not
     * empty.
     */
    public void replaceItem() {
        if (this.getSelectedNode() != null) {
            if (!oidTextField.getText().intern().equals("") && !typeTextField.getText().intern().equals("") && dataTypeField.getSelectedIndex() > 0) {
                String attribs[] = { "OID", "Name", "Type" };
                String values[] = { oidTextField.getText(), typeTextField.getText(), (String) dataTypeField.getSelectedItem() };
                Element child = xmlED.DOM.createElement("RoleSpec");
                child.setAttribute("OID", oidTextField.getText());
                if (isInPolicy(child) && !((Element) this.getSelectedNode()).getAttribute("OID").equals(oidTextField.getText())) {
                    JOptionPane.showMessageDialog(xmlED, roleTypesParamsConfigError2, errorHeader, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                xmlED.setAttributeValue((Element) this.getSelectedNode(), attribs, values);
            } else {
                JOptionPane.showMessageDialog(xmlED, roleTypesParamsConfigError1, errorHeader, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    /**
     * Method That Populates the NodeList with the current Environment Variables.
     */
    public void refreshView() {
        if (xmlED == null || xmlED.DOM == null) return;
        if (getParentNode() != null) {
            NodeList nlist = ((Element) getParentNode()).getElementsByTagName("RoleSpec");
            Node n;
            String envNames[] = new String[nlist.getLength()];
            for (int i = 0; i < nlist.getLength(); i++) {
                n = nlist.item(i);
                envNames[i] = (((Element) n).getAttribute("Name"));
            }
            setNodeList(nlist, envNames);
        }
    }

    /**
     * When the NodeItemList has a selected Element, the contents of that node, 
     * will populate the textfields, so the user will see exactly what the 
     * Environment Variable has as a defined type. 
     * <p> 
     * Users might then want to modify the Selected node, and therefore the 
     * refreshed textfields, would just need to be modified.
     * <p>
     * If nothing is selected in the NodeItemList, the textfields are reset
     * using the clearTextFields() method.
     */
    public void itemSelected() {
        Element selectedNode;
        dataTypeField.removeItemListener(this);
        super.itemSelected();
        addButton.setEnabled(false);
        replaceButton.setEnabled(false);
        if (getSelectedNode() == null) {
            clearTextFields();
        } else {
            selectedNode = (Element) getSelectedNode();
            typeTextField.setText(selectedNode.getAttribute("Name"));
            oidTextField.setText(selectedNode.getAttribute("OID"));
            dataTypeField.setSelectedItem(selectedNode.getAttribute("Type"));
            oidTextField.isValidFormat = true;
        }
        dataTypeField.addItemListener(this);
    }

    /**
     * The root node of the Current Document.
     *
     * @return   a node with the root Element of the 'pe.cfg' XML file.
     */
    public Node getParentNode() {
        return xmlED.DOM.getElementsByTagName("RoleTypes").item(0);
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
     * Method That enables/disables the appropriate buttons when the Text Fields
     * are being typed into.
     */
    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        oidTextField.focusLost(new FocusEvent(oidTextField, FocusEvent.FOCUS_LOST));
        if (e.getSource() == oidTextField || e.getSource() == typeTextField) {
            if (this.listBox.getSelectedValue() != null && !oidTextField.getText().intern().equals("") && !typeTextField.getText().intern().equals("") && oidTextField.isValidFormat && dataTypeField.getSelectedIndex() > 0) {
                replaceButton.setEnabled(true);
                addButton.setEnabled(true);
            } else if (!oidTextField.getText().intern().equals("") && !typeTextField.getText().intern().equals("") && oidTextField.isValidFormat && dataTypeField.getSelectedIndex() > 0) {
                addButton.setEnabled(true);
            } else if (oidTextField.getText().intern().equals("") || typeTextField.getText().intern().equals("") || !oidTextField.isValidFormat || dataTypeField.getSelectedIndex() <= 0) {
                replaceButton.setEnabled(false);
                addButton.setEnabled(false);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().intern().equals("oid_tf") && !oidTextField.getText().intern().equals("")) {
            dataTypeField.grabFocus();
        } else if (e.getActionCommand().intern().equals("type_tf") && !typeTextField.getText().intern().equals("")) {
            oidTextField.grabFocus();
        } else super.actionPerformed(e);
    }

    public void itemStateChanged(ItemEvent e) {
        if (dataTypeField.getSelectedIndex() <= 0) {
            replaceButton.setEnabled(false);
            addButton.setEnabled(false);
            return;
        }
        if (getSelectedIndex() >= 0 && e.getStateChange() == ItemEvent.SELECTED) {
            replaceButton.setEnabled(true);
        } else if (listBox.getSelectedValue() == null && !typeTextField.getText().intern().equals("") && !oidTextField.getText().intern().equals("") && oidTextField.isValidFormat && !((String) dataTypeField.getSelectedItem()).equals("") && getSelectedIndex() < 0) addButton.setEnabled(true); else if (!typeTextField.getText().intern().equals("") && !oidTextField.getText().intern().equals("") && oidTextField.isValidFormat && !((String) dataTypeField.getSelectedItem()).equals("") && getSelectedIndex() < 0) {
            replaceButton.setEnabled(true);
            addButton.setEnabled(true);
        }
    }
}

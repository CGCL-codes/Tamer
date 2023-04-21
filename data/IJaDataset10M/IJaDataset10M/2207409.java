package exmld.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.w3c.dom.DOMException;
import exmld.dtdContent.construction.ContentListener;
import exmld.dtdContent.construction.ContentListenerImpl;
import exmld.dtdContent.construction.ContentModelParser;
import exmld.dtdContent.tree.AttributeDefinitionNode;
import exmld.dtdContent.tree.ContentDTDNode;
import exmld.dtdContent.tree.ElementDefinitionNode;
import exmld.dtdContent.tree.NodeMapImpl;

/**
 * The ElementDefinitionDialog creates a dialog to let the user modify an existing element definition.
 * ElementDefinitionDialog extends {@link javax.swing.JDialog}.
 */
public class ElementDefinitionDialog extends JDialog {

    /**The element definition*/
    ElementDefinitionNode elementDefinition;

    /**The nodemap of attributes definitions.*/
    NodeMapImpl attributeDefinitions;

    /**Semantic control.*/
    boolean modifying;

    /**GUI related field.*/
    JTextField tagField;

    /**GUI related field.*/
    JTextField contentField;

    /**GUI related field.*/
    JTextField attributeField;

    /**GUI related field.*/
    JComboBox typeSelector;

    /**GUI related field.*/
    JTextField typeField;

    /**GUI related field.*/
    JTextField valueField;

    /**The textarea for the XML..*/
    JTextArea xmlArea;

    /**Status bar.*/
    JLabel statusBar;

    /**Error message, missing element name.*/
    static final String MISSING_ELEMENT_NAME = "Error: No name was given for the element";

    /**Error message, illegal conent model.*/
    static final String ILLEGAL_CONTENT = "Error: Illegal content model";

    /**Error message, the specified element already exists.*/
    static final String ELEMENT_EXISTS = "Error: Specified element already exists";

    /**Error message, the attribute filed is empty.*/
    static final String ATTRIBUTE_EMPTY = "Error: Attribute field must not be empty";

    /**Error message, the specified attribute is unkown*/
    static final String ATTRIBUTE_UNKNOWN = "Error: The specified attribute is unknown";

    /**Error message, type field must not be empty.*/
    static final String TYPE_EMPTY = "Error: Type field must not be empty";

    /**Error message, value field must not be empty.*/
    static final String VALUE_EMPTY = "Error: Value field must not be empty";

    /**Error message, illegal attribute type.*/
    static final String ILLEGAL_TYPE = "Error: Illegal attribute type";

    /**Error message, illegal attribute value.*/
    static final String ILLEGAL_VALUE = "Error: Illegal attribute value";

    /**Attribute Validation Strings */
    static final String[] possibleTypes = { "CDATA", "ID", "IDREF", "IDREFS", "ENTITY", "ENTITIES", "NMTOKEN", "NMTOKENS", "NOTATION" };

    /**
	 * Constructs a new instance of ElementDefinitionDialog.
	 * @param owner The parent frame of the dialog.
	 * @param modal a flag indicating whether the dialog should be modal.
	 */
    public ElementDefinitionDialog(Frame owner, boolean modal) {
        this(owner, modal, null);
    }

    /**
	 * Constructs a new instance of ElementDefinitionDialog.
	 * @param parent The parent frame of the dialog.
	 * @param modal a flag indicating whether the dialog should be modal.
	 * @param ed An element definition to initialize the dialog.
	 */
    public ElementDefinitionDialog(Frame parent, boolean modal, ElementDefinitionNode ed) {
        super(parent, modal);
        this.elementDefinition = ed;
        if (ed == null || ed.attributeDefinitions == null) attributeDefinitions = new NodeMapImpl(); else attributeDefinitions = ed.attributeDefinitions;
        setTitle("Element Definition");
        setResizable(false);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                cancelAction();
            }
        });
        JPanel dialog = new JPanel();
        dialog.setLayout(new GridBagLayout());
        getContentPane().setLayout(new GridLayout(1, 1));
        getContentPane().add(dialog);
        JPanel tagPanel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        dialog.add(tagPanel, gbc);
        JPanel attributePanel = new JPanel();
        gbc.gridy = 1;
        dialog.add(attributePanel, gbc);
        JPanel xmlPanel = new JPanel();
        gbc.gridy = 2;
        gbc.weighty = 1;
        dialog.add(xmlPanel, gbc);
        JPanel buttonPanel = new JPanel();
        gbc.gridy = 3;
        gbc.weighty = 0;
        dialog.add(buttonPanel, gbc);
        gbc.gridy = 4;
        statusBar = new JLabel(" ");
        dialog.add(statusBar, gbc);
        JLabel tagLabel = new JLabel("Name: ");
        tagField = new JTextField();
        tagField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tagEntered();
            }
        });
        JLabel contentLabel = new JLabel("Content: ");
        contentField = new JTextField();
        contentField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okAction();
            }
        });
        tagPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tagPanel.add(tagLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        tagPanel.add(tagField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        tagPanel.add(contentLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        tagPanel.add(contentField, gbc);
        tagPanel.setBorder(BorderFactory.createTitledBorder("Tag"));
        JLabel attributeLabel = new JLabel("Attr: ");
        attributeLabel.setMaximumSize(attributeLabel.getMinimumSize());
        attributeField = new JTextField();
        attributeField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                typeField.requestFocus();
            }
        });
        JLabel typeLabel = new JLabel("Type: ");
        typeLabel.setMaximumSize(typeLabel.getMinimumSize());
        typeSelector = new JComboBox();
        for (int i = 0; i < possibleTypes.length; i++) typeSelector.addItem(possibleTypes[i]);
        typeSelector.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                typeSelectedAction();
            }
        });
        typeField = new JTextField();
        typeField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                valueField.requestFocus();
            }
        });
        JLabel valueLabel = new JLabel("Value: ");
        valueLabel.setMaximumSize(valueLabel.getMinimumSize());
        valueField = new JTextField();
        valueField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addAttributeAction();
            }
        });
        JButton addButton = new JButton("Add");
        addButton.setToolTipText("Add new attribute definition");
        addButton.setMaximumSize(addButton.getMinimumSize());
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addAttributeAction();
            }
        });
        JButton delButton = new JButton("Del");
        delButton.setToolTipText("Delete attribute definition");
        delButton.setMaximumSize(delButton.getMinimumSize());
        delButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                deleteAttributeAction();
            }
        });
        attributePanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        attributePanel.add(attributeLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        attributePanel.add(attributeField, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        attributePanel.add(typeSelector, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        attributePanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        attributePanel.add(typeField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        attributePanel.add(valueLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        attributePanel.add(valueField, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        attributePanel.add(addButton, gbc);
        gbc.gridy = 1;
        attributePanel.add(delButton, gbc);
        attributePanel.setBorder(BorderFactory.createTitledBorder("Attribute"));
        xmlArea = new JTextArea();
        xmlArea.setEditable(false);
        xmlArea.setBackground(Color.lightGray);
        xmlArea.setRows(4);
        xmlArea.setMinimumSize(xmlArea.getSize());
        xmlPanel.setLayout(new GridLayout(1, 1));
        xmlPanel.add(new JScrollPane(xmlArea));
        xmlPanel.setBorder(BorderFactory.createTitledBorder("XML-Code"));
        JButton okButton = new JButton("OK");
        okButton.setToolTipText("Adds a new element definition");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okAction();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setToolTipText("Cancel element definition");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelAction();
            }
        });
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        if (elementDefinition != null) {
            tagField.setText(elementDefinition.getNodeName());
            contentField.setText(elementDefinition.getContentModel());
            updateXMLRep();
        }
        pack();
        int width = parent.getWidth();
        setSize(new Dimension(width / 2, getSize().height));
        Point pos = new Point(0, 0);
        pos.x += width;
        pos.x -= getSize().width;
        pos.y += parent.getSize().height;
        pos.y -= getSize().height;
        pos.x /= 2;
        pos.y /= 2;
        pos.x += (parent.getLocation()).x;
        pos.y += (parent.getLocation()).y;
        setLocation(pos);
    }

    /**
	 * Invokes creation of a new ElementDefinitionNode from the dialogs fields 
	 * and disposes the dialog.
	 */
    void okAction() {
        if (extract()) dispose();
    }

    /**
	 * Creates a new element definition from the dialogs fields.
	 * @return true if the creation was successfull, false else.
	 */
    boolean extract() {
        String name = tagField.getText();
        if (name == null || name.trim().equals("")) {
            tagField.selectAll();
            statusBar.setText(MISSING_ELEMENT_NAME);
            return false;
        }
        name = name.trim();
        String contentModel = contentField.getText();
        if (contentModel != null && contentModel.trim().equals("")) contentModel = null;
        if (contentModel != null) contentModel = contentModel.trim();
        ContentListener listener = new ContentListenerImpl();
        ContentModelParser parser = new ContentModelParser(contentModel, listener);
        if (!parser.parse()) {
            contentField.selectAll();
            statusBar.setText(ILLEGAL_CONTENT);
            return false;
        }
        ContentDTDNode content = listener.getRoot();
        this.elementDefinition = new ElementDefinitionNode(name, contentModel, content);
        this.elementDefinition.attributeDefinitions = this.attributeDefinitions;
        return true;
    }

    /**
	 * Executed when the user pressed 'cancel' in this dialog. The element definition is set to null
	 * and the dialog is disposed.
	 */
    void cancelAction() {
        elementDefinition = null;
        dispose();
    }

    /**
	 * Creates a new attribute definition from the dialogs fields. Every value is checked for consistency with
	 * the XML-specification. If the check fails, the method returns without creating an attribute definition.
	 * If successfull the attribute definition is added to this dialogs list.
	 */
    void addAttributeAction() {
        String[] options = null;
        boolean fixed = false;
        boolean required = false;
        boolean implied = false;
        String defaultValue = null;
        String elementName = tagField.getText();
        if (elementName == null || elementName.trim().equals("")) {
            tagField.requestFocus();
            statusBar.setText(MISSING_ELEMENT_NAME);
            return;
        }
        String name = attributeField.getText();
        if (name == null || name.trim().equals("")) {
            attributeField.requestFocus();
            statusBar.setText(ATTRIBUTE_EMPTY);
            return;
        }
        name = name.trim();
        String type = typeField.getText();
        if (type == null || type.trim().equals("")) {
            typeField.requestFocus();
            statusBar.setText(TYPE_EMPTY);
            return;
        }
        type = type.trim();
        boolean legal = false;
        if (type.startsWith("(") && type.endsWith(")")) {
            StringTokenizer p = new StringTokenizer(type.substring(1, type.length() - 1), " |\n\r\t");
            options = new String[p.countTokens()];
            for (int i = 0; i < options.length; i++) options[i] = p.nextToken();
            legal = true;
        } else {
            for (int i = 0; i < possibleTypes.length; i++) {
                if (type.equals(possibleTypes[i])) {
                    legal = true;
                    break;
                }
            }
        }
        if (!legal) {
            typeField.selectAll();
            statusBar.setText(ILLEGAL_TYPE);
            return;
        }
        legal = false;
        String value = valueField.getText();
        if (value == null || value.trim().equals("")) {
            valueField.requestFocus();
            statusBar.setText(VALUE_EMPTY);
            return;
        }
        value = value.trim();
        if (value.equals("#REQUIRED")) {
            required = true;
            legal = true;
        } else if (value.equals("#IMPLIED")) legal = true; else if (value.startsWith("\"") && value.endsWith("\"")) {
            defaultValue = value.substring(1, value.length() - 1);
            legal = true;
        } else if (value.startsWith("#FIXED")) {
            fixed = true;
            defaultValue = value.substring(6).trim();
            if (defaultValue.startsWith("\"") && defaultValue.endsWith("\"")) {
                legal = true;
                defaultValue = defaultValue.substring(1, defaultValue.length() - 1);
            } else legal = false;
        }
        if (!legal) {
            valueField.selectAll();
            statusBar.setText(ILLEGAL_VALUE);
            return;
        }
        AttributeDefinitionNode attribute = new AttributeDefinitionNode(elementName, name, type, options, defaultValue, fixed, required, implied, "");
        attributeDefinitions.setNamedItem(attribute);
        updateXMLRep();
    }

    /**
	 * Deletes an attribute definition from the dialogs fields.
	 */
    void deleteAttributeAction() {
        String elementName = tagField.getText();
        if (elementName == null || elementName.trim().equals("")) {
            tagField.requestFocus();
            statusBar.setText(MISSING_ELEMENT_NAME);
            return;
        }
        String name = attributeField.getText();
        if (name == null || name.trim().equals("")) {
            attributeField.requestFocus();
            statusBar.setText(ATTRIBUTE_EMPTY);
            return;
        }
        name = name.trim();
        try {
            attributeDefinitions.removeNamedItem(name);
            updateXMLRep();
        } catch (DOMException dex) {
            statusBar.setText(ATTRIBUTE_UNKNOWN);
        }
    }

    /**
	 * Returns the element definition created with this dialog.
	 */
    public ElementDefinitionNode getElementDefinition() {
        return elementDefinition;
    }

    /**
	 * Executed when the tagField has the cursor and 'return' is pressed.
	 */
    void tagEntered() {
        updateXMLRep();
        contentField.requestFocus();
    }

    /**
	 * Executed upon a selection in the typeselector-listbox
	 */
    void typeSelectedAction() {
        String x = (String) typeSelector.getSelectedItem();
        typeField.setText(x);
        typeField.selectAll();
    }

    /**
	 * Creates an XML-Code representation from the current element and attribute definitions and
	 * displays it on the dialog.
	 */
    void updateXMLRep() {
        String toShow = "<!ELEMENT: " + tagField.getText() + " " + contentField.getText() + ">\n";
        if (attributeDefinitions != null) {
            toShow += "<!ATTRIBUTE " + tagField.getText() + "\n";
            for (int i = 0; i < attributeDefinitions.getLength(); i++) {
                AttributeDefinitionNode att = (AttributeDefinitionNode) attributeDefinitions.item(i);
                toShow += "\t" + att.attributeName;
                toShow += " " + att.attributeType;
                if (att.isFixed) toShow += " #FIXED " + att.defaultValue; else if (att.defaultValue != null) toShow += " \"" + att.defaultValue + "\""; else if (att.isRequired) toShow += " #REQUIRED"; else toShow += " #IMPLIED";
                toShow += "\n";
            }
            toShow += ">";
        }
        xmlArea.setText(toShow);
    }
}

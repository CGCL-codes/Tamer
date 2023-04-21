package org.eml.MMAX2.annotation.scheme;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.eml.MMAX2.annotation.markables.MarkableHelper;
import org.eml.MMAX2.annotation.markables.MarkableRelation;
import org.eml.MMAX2.api.AttributeAPI;
import org.eml.MMAX2.core.MMAX2;
import org.eml.MMAX2.utils.MMAX2Utils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MMAX2Attribute extends JPanel implements java.awt.event.ActionListener, javax.swing.event.DocumentListener, org.eml.MMAX2.api.AttributeAPI {

    private String ID;

    /** Name of the Attribute this MMAX2Attribute controls, as supplied in the annotation scheme (for display purposes) */
    private String displayAttributeName;

    /** Name of the Attribute this MMAX2Attribute controls, set to lower (for matching purposes & writing to XML) */
    private String lowerCasedAttributeName;

    /** Whether this Attribute is currently frozen. */
    private boolean frozen = false;

    /** Number of options for this Attribute (for exclusive nominal attributes only). */
    private int size;

    /** Reference to the AnnotationScheme object that this Attribute is defined in / part of */
    private MMAX2AnnotationScheme annotationscheme;

    /** Type of this Attribute: AttributeAPI.NOMINAL_BUTTON, AttributeAPI.NOMINAL_LIST, AttributeAPI.FREETEXT, AttributeAPI.MARKABLE_SET, AttributeAPI.MARKABLE_POINTER, ... */
    private int type;

    private String toShowInFlag = "";

    private int lineWidth;

    private Color lineColor;

    private int lineStyle;

    private int maxSize;

    private boolean dashed;

    private String add_to_markableset_instruction;

    private String remove_from_markableset_instruction;

    private String adopt_into_markableset_instruction;

    private String merge_into_markableset_instruction;

    private String point_to_markable_instruction;

    private String remove_pointer_to_markable_instruction;

    private MarkableRelation markableRelation;

    private String targetDomain;

    private UIMATypeMapping uimaTypeMapping;

    JRadioButton invisibleButton;

    private ArrayList buttonIndicesToLowerCasedValueStrings;

    private ArrayList buttons;

    private ArrayList nextAttributes;

    private Hashtable lowerCasedValueStringsToButtonIndices;

    ButtonGroup group = null;

    private JComboBox listSelector = null;

    JTextArea freetextArea;

    JScrollPane scrollPane;

    JLabel idLabel;

    JLabel attributeLabel;

    public boolean isBranching = false;

    public boolean readOnly = false;

    String tooltiptext;

    public String oldValue = "";

    int currentIndex = -1;

    private String noneAvailableForValue = "<no hint available for this value>";

    private ArrayList dependsOn = new ArrayList();

    private ArrayList orderedValues = new ArrayList();

    public MMAX2Attribute(String id, String attributeName, int _type, NodeList allChildren, MMAX2AnnotationScheme currentScheme, int width, String tiptext, String hintText, int _lineWidth, Color _color, int _lineStyle, int _maxSize, String _targetDomain, String _add_instruction, String _remove_instruction, String _adopt_instruction, String _merge_instruction, String _point_to_markable_instruction, String _remove_pointer_to_markable_instruction, float fontSize, boolean _dashed, String _toShowInFlag, UIMATypeMapping _uimaTypeMapping) {
        super();
        setAlignmentX(JPanel.LEFT_ALIGNMENT);
        toShowInFlag = _toShowInFlag;
        dashed = _dashed;
        type = _type;
        tooltiptext = tiptext;
        final String tempName = attributeName;
        final String tip = tooltiptext;
        final String tempHintText = hintText;
        ID = id;
        displayAttributeName = attributeName;
        lowerCasedAttributeName = attributeName.toLowerCase();
        size = 0;
        annotationscheme = currentScheme;
        lineWidth = _lineWidth;
        lineColor = _color;
        lineStyle = _lineStyle;
        maxSize = _maxSize;
        targetDomain = _targetDomain;
        add_to_markableset_instruction = _add_instruction;
        remove_from_markableset_instruction = _remove_instruction;
        adopt_into_markableset_instruction = _adopt_instruction;
        merge_into_markableset_instruction = _merge_instruction;
        point_to_markable_instruction = _point_to_markable_instruction;
        remove_pointer_to_markable_instruction = _remove_pointer_to_markable_instruction;
        nextAttributes = new ArrayList();
        uimaTypeMapping = _uimaTypeMapping;
        String filler = "";
        for (int q = 0; q < width + 3; q++) {
            filler = filler + " ";
        }
        attributeLabel = new JLabel(displayAttributeName);
        attributeLabel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        if (MMAX2.getStandardFont() != null) {
            attributeLabel.setFont(MMAX2.getStandardFont().deriveFont((float) fontSize));
        }
        attributeLabel.setForeground(Color.darkGray);
        if (tooltiptext.equals("") == false) {
            attributeLabel.setToolTipText(tooltiptext);
        }
        final MMAX2AnnotationScheme schemeCopy = currentScheme;
        if (tempHintText.equals("") == false) {
            attributeLabel.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseEntered(java.awt.event.MouseEvent me) {
                    schemeCopy.showAnnotationHint(tempHintText, false, tempName);
                }

                public void mouseExited(java.awt.event.MouseEvent me) {
                    schemeCopy.hideAnnotationHint();
                }

                public void mouseClicked(java.awt.event.MouseEvent me) {
                    if (me.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                        schemeCopy.showAnnotationHint(tempHintText, true, tempName);
                        return;
                    }
                }
            });
        }
        setAlignmentX(JComponent.LEFT_ALIGNMENT);
        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        Box innerBox = Box.createHorizontalBox();
        Box labelBox = Box.createVerticalBox();
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        JPanel buttonBox = new JPanel();
        attributeLabel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        attributeLabel.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent me) {
                if (schemeCopy.getAttributePanel().getContainer().isHintToFront()) {
                    schemeCopy.annotationHintToFront();
                }
            }

            public void mouseExited(java.awt.event.MouseEvent me) {
            }
        });
        labelPanel.add(attributeLabel);
        labelBox.add(Box.createHorizontalStrut(120));
        labelBox.add(labelPanel);
        innerBox.add(labelBox);
        Node currentNode = null;
        String nextValue = "";
        String currentValue = "";
        String tempText = "";
        JRadioButton currentButton = null;
        invisibleButton = new JRadioButton();
        group = new ButtonGroup();
        group.add(invisibleButton);
        buttonIndicesToLowerCasedValueStrings = new ArrayList();
        buttons = new ArrayList();
        lowerCasedValueStringsToButtonIndices = new Hashtable();
        for (int z = 0; z < allChildren.getLength(); z++) {
            currentNode = allChildren.item(z);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                try {
                    tempText = currentNode.getAttributes().getNamedItem("text").getNodeValue();
                } catch (java.lang.NullPointerException ex) {
                    tempText = "";
                }
                String descFileName = "";
                try {
                    descFileName = currentNode.getAttributes().getNamedItem("description").getNodeValue();
                } catch (java.lang.NullPointerException ex) {
                }
                if (descFileName.equals("") == false) {
                    String schemeFileName = annotationscheme.getSchemeFileName();
                    tempText = MMAX2AnnotationScheme.readHTMLFromFile(schemeFileName.substring(0, schemeFileName.lastIndexOf(File.separator) + 1) + descFileName);
                }
                if (tempText.equals("")) {
                    NodeList valueChildren = currentNode.getChildNodes();
                    if (valueChildren != null) {
                        for (int q = 0; q < valueChildren.getLength(); q++) {
                            Node valueChild = (Node) valueChildren.item(q);
                            if (valueChild.getNodeName().equalsIgnoreCase("longtext")) {
                                try {
                                    tempText = "<html>" + valueChild.getFirstChild().getNodeValue() + "</html>";
                                } catch (java.lang.NumberFormatException ex) {
                                    tempText = "";
                                }
                                break;
                            }
                        }
                    }
                }
                if (tempText.equals("")) {
                    try {
                        tempText = currentNode.getAttributes().getNamedItem("id").getNodeValue();
                    } catch (java.lang.NullPointerException ex) {
                    }
                }
                if (tempText.equals("")) {
                } else {
                    tempText = tempText.replaceAll("\\{", "<");
                    tempText = tempText.replaceAll("\\}", ">");
                }
                final String currentText = tempText;
                try {
                    currentValue = currentNode.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
                } catch (java.lang.NullPointerException ex) {
                    System.out.println("Error: No 'name' attribute for <value> " + currentNode);
                }
                orderedValues.add(currentValue);
                final String valueName = currentValue;
                if (type == AttributeAPI.NOMINAL_BUTTON) {
                    currentButton = null;
                    currentButton = new JRadioButton(currentValue);
                    if (MMAX2.getStandardFont() != null) {
                        currentButton.setFont(MMAX2.getStandardFont().deriveFont((float) fontSize));
                    }
                    final String currentAtt = displayAttributeName + ":" + currentValue;
                    if (currentText.equals(noneAvailableForValue) == false) {
                        currentButton.addMouseListener(new java.awt.event.MouseAdapter() {

                            public void mouseEntered(java.awt.event.MouseEvent me) {
                                schemeCopy.showAnnotationHint(currentText, false, currentAtt);
                            }

                            public void mouseExited(java.awt.event.MouseEvent me) {
                                schemeCopy.hideAnnotationHint();
                            }

                            public void mouseClicked(java.awt.event.MouseEvent me) {
                                if (me.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                                    schemeCopy.showAnnotationHint(currentText, true, currentAtt);
                                    return;
                                }
                            }
                        });
                    } else {
                        currentButton.setToolTipText(currentText);
                    }
                    currentButton.addActionListener(this);
                    buttonIndicesToLowerCasedValueStrings.add(currentValue);
                    lowerCasedValueStringsToButtonIndices.put(new String(currentValue), new Integer(size));
                    currentButton.setActionCommand(new String(size + ""));
                    buttons.add(currentButton);
                    try {
                        nextValue = currentNode.getAttributes().getNamedItem("next").getNodeValue();
                    } catch (java.lang.NullPointerException ex) {
                        nextValue = "";
                    }
                    if (nextValue.equals("") == false) {
                        isBranching = true;
                    }
                    group.add(currentButton);
                    buttonBox.add(currentButton);
                    nextAttributes.add(nextValue);
                    size++;
                } else if (type == AttributeAPI.NOMINAL_LIST) {
                    if (listSelector == null) {
                        listSelector = new JComboBox();
                        if (MMAX2.getStandardFont() != null) {
                            listSelector.setFont(MMAX2.getStandardFont().deriveFont((float) fontSize));
                        }
                    }
                    buttonIndicesToLowerCasedValueStrings.add(currentValue);
                    lowerCasedValueStringsToButtonIndices.put(new String(currentValue), new Integer(size));
                    listSelector.addItem(currentValue);
                    try {
                        nextValue = currentNode.getAttributes().getNamedItem("next").getNodeValue();
                    } catch (java.lang.NullPointerException ex) {
                        nextValue = "";
                    }
                    if (nextValue.equals("") == false) {
                        isBranching = true;
                    }
                    if (listSelector.getItemCount() == 1) {
                        buttonBox.add(listSelector);
                    }
                    nextAttributes.add(nextValue);
                    size++;
                } else if (this.type == AttributeAPI.FREETEXT) {
                    freetextArea = new JTextArea(1, 10);
                    freetextArea.getDocument().addDocumentListener(this);
                    freetextArea.setLineWrap(false);
                    freetextArea.setWrapStyleWord(true);
                    if (MMAX2.getStandardFont() != null) {
                        freetextArea.setFont(MMAX2.getStandardFont().deriveFont((float) fontSize));
                    }
                    scrollPane = new JScrollPane(freetextArea);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    buttonBox.add(scrollPane);
                    freetextArea.setVisible(true);
                } else if (type == AttributeAPI.MARKABLE_POINTER) {
                    if (idLabel == null) {
                        idLabel = new JLabel(MMAX2.defaultRelationValue);
                        if (MMAX2.getStandardFont() != null) {
                            idLabel.setFont(MMAX2.getStandardFont().deriveFont((float) fontSize));
                        }
                        idLabel.setEnabled(false);
                        buttonBox.add(idLabel);
                    }
                    try {
                        nextValue = currentNode.getAttributes().getNamedItem("next").getNodeValue();
                    } catch (java.lang.NullPointerException ex) {
                        nextValue = "";
                    }
                    if (nextValue.equals("") == false) {
                        isBranching = true;
                    }
                    nextAttributes.add(nextValue);
                    size++;
                } else if (type == AttributeAPI.MARKABLE_SET) {
                    idLabel = new JLabel(MMAX2.defaultRelationValue);
                    if (MMAX2.getStandardFont() != null) {
                        idLabel.setFont(MMAX2.getStandardFont().deriveFont((float) fontSize));
                    }
                    idLabel.setEnabled(false);
                    buttonBox.add(idLabel);
                }
            }
        }
        if (listSelector != null) listSelector.addActionListener(this);
        if (isBranching) {
            attributeLabel.setText("< > " + attributeLabel.getText());
        }
        innerBox.add(buttonBox);
        this.add(innerBox);
    }

    public final String getDisplayAttributeName() {
        return displayAttributeName;
    }

    public final ArrayList getOrderedValues() {
        return orderedValues;
    }

    public final MMAX2Attribute[] getDirectlyDependentAttributes() {
        MMAX2Attribute[] result = null;
        ArrayList temp = new ArrayList();
        if (type == AttributeAPI.NOMINAL_BUTTON || type == AttributeAPI.NOMINAL_LIST || type == AttributeAPI.MARKABLE_POINTER) {
            for (int z = 0; z < nextAttributes.size(); z++) {
                String nextVal = (String) nextAttributes.get(z);
                if (nextVal.equals("") == false) {
                    ArrayList tempresult = MarkableHelper.parseCompleteSpan(nextVal);
                    for (int p = 0; p < tempresult.size(); p++) {
                        MMAX2Attribute currentAttrib = (MMAX2Attribute) annotationscheme.getAttributeByID((String) tempresult.get(p));
                        if (currentAttrib != null) {
                            if (temp.contains(currentAttrib) == false) {
                                temp.add(currentAttrib);
                            }
                        } else {
                            System.err.println("Attribute " + (String) tempresult.get(p) + " not found!");
                        }
                    }
                }
            }
        }
        return (MMAX2Attribute[]) temp.toArray(new MMAX2Attribute[0]);
    }

    public final String getAttributeNameToShowInMarkablePointerFlag() {
        return toShowInFlag;
    }

    public final void destroy() {
        markableRelation = null;
        annotationscheme = null;
    }

    public final boolean inDomain(String domain) {
        boolean result = false;
        if (targetDomain.equals("")) {
            result = true;
        } else {
            if (targetDomain.equals(domain) || targetDomain.startsWith(domain + ",") || targetDomain.endsWith("," + domain) || targetDomain.indexOf("," + domain + ",") != -1) {
                result = true;
            }
        }
        return result;
    }

    public final UIMATypeMapping getUIMATypeMapping() {
        return uimaTypeMapping;
    }

    public final String getAddToMarkablesetInstruction() {
        return this.add_to_markableset_instruction;
    }

    public final String getRemoveFromMarkablesetInstruction() {
        return this.remove_from_markableset_instruction;
    }

    public final String getAdoptIntoMarkablesetInstruction() {
        return this.adopt_into_markableset_instruction;
    }

    public final String getMergeIntoMarkablesetInstruction() {
        return this.merge_into_markableset_instruction;
    }

    public final String getPointToMarkableInstruction() {
        return this.point_to_markable_instruction;
    }

    public final String getRemovePointerToMarkableInstruction() {
        return this.remove_pointer_to_markable_instruction;
    }

    public final void setMarkableRelation(MarkableRelation mrelation) {
        markableRelation = mrelation;
    }

    public final MarkableRelation getMarkableRelation() {
        return markableRelation;
    }

    /** This method returns the (lower cased) default value for this attribute */
    public String getDefaultValue() {
        String result = "";
        if (type == AttributeAPI.NOMINAL_BUTTON) {
            result = (String) buttonIndicesToLowerCasedValueStrings.get(0);
        } else if (type == AttributeAPI.NOMINAL_LIST) {
            result = (String) listSelector.getItemAt(0);
        } else if (type == AttributeAPI.FREETEXT) {
            result = "";
        } else if (type == AttributeAPI.MARKABLE_SET || type == AttributeAPI.MARKABLE_POINTER) {
            result = MMAX2.defaultRelationValue;
        }
        return result;
    }

    public final boolean getIsDashed() {
        return dashed;
    }

    public final int getLineWidth() {
        return lineWidth;
    }

    public final Color getLineColor() {
        return lineColor;
    }

    public final int getLineStyle() {
        return lineStyle;
    }

    public final int getMaxSize() {
        return maxSize;
    }

    /** This handler is called upon the selection of a button or a box menu item on this Attribute. */
    public void actionPerformed(java.awt.event.ActionEvent p1) {
        if (annotationscheme.ignoreClick) {
            return;
        }
        if (type == AttributeAPI.NOMINAL_BUTTON) {
            int position = new Integer(p1.getActionCommand()).intValue();
            if (position == currentIndex) {
                return;
            }
            currentIndex = position;
            setIsFrozen(false, "");
            annotationscheme.valueChanged(this, this, null, position, new ArrayList());
        } else if (type == AttributeAPI.NOMINAL_LIST) {
            int position = listSelector.getSelectedIndex();
            if (position == currentIndex) {
                return;
            }
            currentIndex = position;
            setIsFrozen(false, "");
            annotationscheme.valueChanged(this, this, null, position, new ArrayList());
        }
    }

    /** This method returns an array of the MMAX2Attributes that *the current value* of this points to as 'next', or empty Array. */
    public MMAX2Attribute[] getNextAttributes(boolean toDefault) {
        MMAX2Attribute[] result = new MMAX2Attribute[0];
        if (frozen) return result;
        ArrayList tempresult = new ArrayList();
        int selIndex = -1;
        if (getType() == AttributeAPI.NOMINAL_BUTTON || getType() == AttributeAPI.NOMINAL_LIST) {
            selIndex = getSelectedIndex();
        } else if (getType() == AttributeAPI.MARKABLE_POINTER) {
            String currentValue = getSelectedValue();
            if (currentValue.equalsIgnoreCase(MMAX2.defaultRelationValue)) {
                selIndex = 0;
            } else {
                selIndex = 1;
            }
        } else if (getType() == AttributeAPI.MARKABLE_SET || getType() == AttributeAPI.FREETEXT) {
            return result;
        } else {
            System.out.println("Error: Unknown attribute type! " + getLowerCasedAttributeName());
            return result;
        }
        String nextString = (String) nextAttributes.get(selIndex);
        if (nextString.equals("") == false) {
            tempresult = MarkableHelper.parseCompleteSpan(nextString);
            result = new MMAX2Attribute[tempresult.size()];
            for (int p = 0; p < tempresult.size(); p++) {
                result[p] = (MMAX2Attribute) annotationscheme.getAttributeByID((String) tempresult.get(p));
                if (result[p] != null) {
                    if (toDefault) {
                        ((MMAX2Attribute) result[p]).toDefault();
                    }
                } else {
                    System.err.println("No Attribute with ID " + (String) tempresult.get(p) + "!");
                }
            }
        }
        return result;
    }

    public int getSelectedIndex() {
        int index = -1;
        if (type == AttributeAPI.NOMINAL_BUTTON) {
            JRadioButton currentButton = null;
            for (int p = 0; p < this.size; p++) {
                currentButton = (JRadioButton) buttons.get(p);
                if (currentButton.isSelected()) {
                    index = p;
                    break;
                }
            }
        } else if (type == AttributeAPI.NOMINAL_LIST) {
            index = this.listSelector.getSelectedIndex();
        } else {
            System.err.println("getSelectedIndex not legal for attribute " + this.attributeLabel.getText());
        }
        return index;
    }

    public String getSelectedValue() {
        if (frozen) {
            return "";
        }
        String value = "";
        JRadioButton currentButton = null;
        if (this.type == AttributeAPI.NOMINAL_BUTTON) {
            for (int p = 0; p < this.size; p++) {
                currentButton = (JRadioButton) buttons.get(p);
                if (currentButton.isSelected()) {
                    value = ((String) buttonIndicesToLowerCasedValueStrings.get(p));
                    break;
                }
            }
        } else if (this.type == AttributeAPI.NOMINAL_LIST) {
            value = (String) listSelector.getSelectedItem();
        } else if (this.type == AttributeAPI.FREETEXT) {
            try {
                value = freetextArea.getText();
            } catch (java.lang.NullPointerException ex) {
                value = "";
            }
            value.trim();
            String tempresult = "";
            String currentChar = "";
            for (int z = 0; z < value.length(); z++) {
                currentChar = value.substring(z, z + 1);
                if (currentChar.equals("\"")) currentChar = "'"; else if (currentChar.equals("�")) currentChar = "ae"; else if (currentChar.equals("�")) currentChar = "ue"; else if (currentChar.equals("�")) currentChar = "oe"; else if (currentChar.equals("�")) currentChar = "AE"; else if (currentChar.equals("�")) currentChar = "UE"; else if (currentChar.equals("�")) currentChar = "OE"; else if (currentChar.equals("<")) currentChar = ""; else if (currentChar.equals(">")) currentChar = ""; else if (currentChar.equals("�")) currentChar = "ss"; else if (currentChar.equals("\n")) currentChar = " ";
                tempresult = tempresult + currentChar;
            }
            value = tempresult.trim();
        } else if (type == AttributeAPI.MARKABLE_SET) {
            value = idLabel.getText();
        } else if (type == AttributeAPI.MARKABLE_POINTER) {
            value = MMAX2Utils.expandTargetSpan(idLabel.getText());
        }
        return value;
    }

    public boolean setSelectedValue(String desiredValue, boolean ignore) {
        boolean result = false;
        int buttonPosition = 0;
        int itemPosition = -1;
        if (type == AttributeAPI.NOMINAL_BUTTON) {
            if (desiredValue == null || desiredValue.equals("")) {
                setSelectedIndex(0);
                result = true;
            } else {
                try {
                    buttonPosition = ((Integer) lowerCasedValueStringsToButtonIndices.get(desiredValue)).intValue();
                } catch (java.lang.NullPointerException ex) {
                    buttonPosition = -1;
                    result = false;
                }
                if (buttonPosition != -1) {
                    if (ignore) {
                        setSelectedIndex(buttonPosition);
                    } else {
                        ((JRadioButton) buttons.get(buttonPosition)).doClick();
                    }
                    result = true;
                } else {
                    System.err.println("Error: Value " + desiredValue + " not found on attribute " + displayAttributeName + "!");
                }
            }
        } else if (type == AttributeAPI.NOMINAL_LIST) {
            if (desiredValue == null || desiredValue.equals("")) {
                listSelector.setSelectedIndex(0);
                result = true;
            } else {
                try {
                    for (int z = 0; z < listSelector.getItemCount(); z++) {
                        if (desiredValue.equalsIgnoreCase((String) listSelector.getItemAt(z))) {
                            itemPosition = z;
                            break;
                        }
                    }
                } catch (java.lang.NullPointerException ex) {
                    itemPosition = -1;
                    result = false;
                }
                if (itemPosition != -1) {
                    if (ignore) {
                        annotationscheme.ignoreClick = true;
                    }
                    listSelector.setSelectedIndex(itemPosition);
                    annotationscheme.ignoreClick = false;
                    currentIndex = itemPosition;
                    result = true;
                } else {
                    System.err.println("Error: Value " + desiredValue + " not found on attribute " + this.displayAttributeName + "!");
                }
            }
        } else if (type == AttributeAPI.FREETEXT) {
            if (desiredValue != null) {
                freetextArea.setText(desiredValue);
            } else {
                freetextArea.setText("");
            }
            annotationscheme.ignoreClick = false;
            result = true;
        } else if (type == AttributeAPI.MARKABLE_SET) {
            if (desiredValue != null && desiredValue.equals("") == false && desiredValue.equals(MMAX2.defaultRelationValue) == false) {
                idLabel.setText(desiredValue);
            } else {
                idLabel.setText(MMAX2.defaultRelationValue);
            }
            result = true;
        } else if (type == AttributeAPI.MARKABLE_POINTER) {
            if (desiredValue != null && desiredValue.equals("") == false && desiredValue.equals(MMAX2.defaultRelationValue) == false) {
                idLabel.setText(MMAX2Utils.condenseTargetSpan(desiredValue));
            } else {
                idLabel.setText(MMAX2.defaultRelationValue);
            }
            result = true;
        }
        return result;
    }

    public final void addDependsOn(MMAX2Attribute attrib) {
        dependsOn.add(attrib);
    }

    public final boolean isIndependent() {
        return (dependsOn.size() == 0);
    }

    public final boolean dependsOn(MMAX2Attribute superiorAttribute) {
        boolean result = false;
        if (dependsOn.contains(superiorAttribute)) {
            result = true;
        } else {
            MMAX2Attribute currentAttribute = null;
            for (int z = 0; z < dependsOn.size(); z++) {
                currentAttribute = (MMAX2Attribute) dependsOn.get(z);
                if (currentAttribute.dependsOn(superiorAttribute)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public void setSelectedIndex(int num) {
        if (type == AttributeAPI.NOMINAL_BUTTON) {
            annotationscheme.ignoreClick = true;
            ((JRadioButton) buttons.get(num)).setSelected(true);
            currentIndex = num;
            annotationscheme.ignoreClick = false;
        } else if (type == AttributeAPI.NOMINAL_LIST) {
            annotationscheme.ignoreClick = true;
            listSelector.setSelectedIndex(num);
            currentIndex = num;
            annotationscheme.ignoreClick = false;
        }
    }

    public void setEnabled(boolean status) {
        if (this.type == AttributeAPI.NOMINAL_BUTTON) {
            for (int o = 0; o < this.size; o++) {
                ((JRadioButton) buttons.get(o)).setEnabled(status);
                if (this.readOnly) ((JRadioButton) buttons.get(o)).setEnabled(false);
            }
        } else if (this.type == AttributeAPI.NOMINAL_LIST) {
            listSelector.setEnabled(status);
        } else if (this.type == AttributeAPI.FREETEXT) {
            this.freetextArea.setEnabled(status);
            this.freetextArea.setEditable(status);
        }
    }

    /** This method resets this Schemelevel to default */
    public void toDefault() {
        if (type == AttributeAPI.NOMINAL_BUTTON) {
            annotationscheme.ignoreClick = true;
            ((JRadioButton) buttons.get(0)).setSelected(true);
            annotationscheme.ignoreClick = false;
            currentIndex = 0;
        }
        if (type == AttributeAPI.NOMINAL_LIST) {
            annotationscheme.ignoreClick = true;
            listSelector.setSelectedIndex(0);
            annotationscheme.ignoreClick = false;
            currentIndex = 0;
        } else if (type == AttributeAPI.FREETEXT) {
            annotationscheme.ignoreClick = true;
            freetextArea.setText("");
            annotationscheme.ignoreClick = false;
        } else if (type == AttributeAPI.MARKABLE_SET || type == AttributeAPI.MARKABLE_POINTER) {
            idLabel.setText(MMAX2.defaultRelationValue);
        }
    }

    public void removeUpdate(javax.swing.event.DocumentEvent p1) {
        if (annotationscheme.ignoreClick) return;
        if (annotationscheme.getCurrentAttributePanel().hasUncommittedChanges == false) annotationscheme.getCurrentAttributePanel().setHasUncommittedChanges(true);
        annotationscheme.getCurrentAttributePanel().setApplyEnabled(true);
        annotationscheme.getCurrentAttributePanel().setUndoEnabled(true);
    }

    public void changedUpdate(javax.swing.event.DocumentEvent p1) {
        if (annotationscheme.ignoreClick) return;
        if (annotationscheme.getCurrentAttributePanel().hasUncommittedChanges == false) annotationscheme.getCurrentAttributePanel().setHasUncommittedChanges(true);
        annotationscheme.getCurrentAttributePanel().setApplyEnabled(true);
        annotationscheme.getCurrentAttributePanel().setUndoEnabled(true);
    }

    public void insertUpdate(javax.swing.event.DocumentEvent p1) {
        if (annotationscheme.ignoreClick) return;
        if (annotationscheme.getCurrentAttributePanel().hasUncommittedChanges == false) annotationscheme.getCurrentAttributePanel().setHasUncommittedChanges(true);
        annotationscheme.getCurrentAttributePanel().setApplyEnabled(true);
        annotationscheme.getCurrentAttributePanel().setUndoEnabled(true);
    }

    public int getType() {
        return type;
    }

    public final String decodeAttributeType() {
        String result = "unknown type !";
        if (type == AttributeAPI.NOMINAL_BUTTON || type == AttributeAPI.NOMINAL_LIST) {
            result = "(NOMINAL";
        } else if (type == AttributeAPI.MARKABLE_SET) {
            result = "(MARKABLE_SET";
        } else if (type == AttributeAPI.MARKABLE_POINTER) {
            result = "(MARKABLE_POINTER";
        } else if (type == AttributeAPI.FREETEXT) {
            result = "(FREETEXT";
        }
        if (this.isBranching) {
            result = result + ", branching)";
        } else {
            result = result + ")";
        }
        return result;
    }

    public boolean isDefined(String value) {
        if (type == AttributeAPI.NOMINAL_BUTTON || type == AttributeAPI.NOMINAL_LIST) {
            return buttonIndicesToLowerCasedValueStrings.contains(value);
        } else if (type == AttributeAPI.FREETEXT) {
            return true;
        } else if (type == AttributeAPI.MARKABLE_SET) {
            if (value.equalsIgnoreCase("empty") == true) {
                return true;
            } else if (value.equalsIgnoreCase("initial") == true) {
                return true;
            } else if (value.equalsIgnoreCase("final") == true) {
                return true;
            } else {
                return false;
            }
        } else if (type == AttributeAPI.MARKABLE_POINTER) {
            if (value.equalsIgnoreCase("empty") == true) {
                return true;
            } else if (value.equalsIgnoreCase("target") == true) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public final String getLowerCasedAttributeName() {
        return this.lowerCasedAttributeName;
    }

    public final String getID() {
        return ID;
    }

    public final boolean getIsBranching() {
        return this.isBranching;
    }

    public final boolean getIsFrozen() {
        return this.frozen;
    }

    public final boolean getIsReadOnly() {
        return this.readOnly;
    }

    public void setIsFrozen(boolean status, String illegalValue) {
        if (status == true) {
            if (type == AttributeAPI.NOMINAL_BUTTON) {
                String warning = "Illegal attribute value: '" + illegalValue + "'";
                invisibleButton.setSelected(true);
                attributeLabel.setForeground(Color.red);
                attributeLabel.setToolTipText(warning);
                frozen = true;
            } else if (type == AttributeAPI.NOMINAL_LIST) {
                String warning = "Illegal attribute value: '" + illegalValue + "'";
                attributeLabel.setForeground(Color.red);
                attributeLabel.setToolTipText(warning);
                frozen = true;
            }
        } else if (frozen == true) {
            attributeLabel.setForeground(Color.darkGray);
            attributeLabel.setToolTipText(tooltiptext);
            toDefault();
            frozen = false;
        }
    }
}

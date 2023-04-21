package issrg.editor2;

import issrg.editor2.ifcondition.ConditionOnComboBox;
import issrg.utils.xml.XMLEditor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class will display the appropriate panel for creating the If conditions,
 * be the advanced or the simple panel, and will be displayed in the
 * TargetAccessPolicy tabs when the user clicks the Conditions button.
 *
 * @author Christian Azzopardi
 */
public class IFConstraintsPanelSimple extends JPanel {

    /**
     * The Reference to the XML Editor.
     */
    XMLEditor xmlED;

    /**
     *  The index of the tab that is being displayed.
     */
    int index;

    /**
     * Grid Bag Constraints, to be able to place the components on the panel,
     * with the required constraints.
     */
    GridBagConstraints constraints;

    GridBagConstraints tmpconstraints;

    JPanel simplePanel;

    JFrame owner;

    JRadioButton andSelected;

    JRadioButton orSelected;

    ConditionsTable condTable;

    public static final int SIMPLE_PANEL = 0;

    public static final int ADVANCED_PANEL = 1;

    /**
     * Loads the String Resources from the '.properties' file.
     */
    ResourceBundle rbl = ResourceBundle.getBundle("issrg/editor2/PEComponent_i18n");

    String dialogTitle = rbl.getString("IfCondition_Dialog_Title");

    String simpleBorderCaption = rbl.getString("IfCondition_SimplePanel_Border");

    String sourceBorderCaption = rbl.getString("IfCondition_Source_Border");

    String varNameBorderCaption = rbl.getString("IfCondition_VarName_Border");

    String varTypeBorderCaption = rbl.getString("IfCondition_VarType_Border");

    String operatorBorderCaption = rbl.getString("IfCondition_Operator_Border");

    String constantBorderCaption = rbl.getString("IfCondition_Constant_Border");

    String andRadioButtonCaption = rbl.getString("IfCondition_RadioButton_AND");

    String andRadioButtonCaptionTtip = rbl.getString("IfCondition_RadioButton_AND_Ttip");

    String orRadioButtonCaption = rbl.getString("IfCondition_RadioButton_OR");

    String orRadioButtonCaptionTtip = rbl.getString("IfCondition_RadioButton_OR_Ttip");

    String andOrSelectionBorder = rbl.getString("IfCondition_Border_AndOr");

    /**
     * Creates a new instance of the panel that needs to be displayed,
     * according to the parrameters that are passed.
     *
     * @param xmlED         The XML Editor
     * @param index         The index of the tab that is being displayed.
     * @oaram typeOfPanel   the type of panel to display, be it the Simple or
     *                      Advanced Panel. These could be reffered to by the
     *                      static integeres declared above.
     */
    public IFConstraintsPanelSimple(XMLEditor xmlED, JFrame owner, int index) {
        condTable = new ConditionsTable(xmlED, owner, index);
        this.xmlED = xmlED;
        this.index = index;
        this.owner = owner;
        JPanel displayPanel = new JPanel(new GridBagLayout());
        constraints = new GridBagConstraints();
        this.add(getSimplePanel());
        refresh();
    }

    public JPanel getSimplePanel() {
        simplePanel = new JPanel(new GridBagLayout());
        JPanel oldPanel = condTable.getContentPanel();
        orSelected = new JRadioButton(orRadioButtonCaption);
        orSelected.setToolTipText(orRadioButtonCaptionTtip);
        orSelected.setMnemonic(KeyEvent.VK_N);
        andSelected = new JRadioButton(andRadioButtonCaption);
        andSelected.setToolTipText(andRadioButtonCaptionTtip);
        andSelected.setMnemonic(KeyEvent.VK_L);
        ButtonGroup group = new ButtonGroup();
        group.add(orSelected);
        group.add(andSelected);
        andSelected.setSelected(true);
        JPanel andOrPanel = new JPanel(new GridBagLayout());
        andOrPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), andOrSelectionBorder));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 0, 0);
        andOrPanel.add(andSelected, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        andOrPanel.add(orSelected, constraints);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        simplePanel.add(andOrPanel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        simplePanel.add(oldPanel, constraints);
        simplePanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), simpleBorderCaption));
        return simplePanel;
    }

    public void writeToPolicy() {
        if (xmlED.DOM == null) return;
        NodeList nodesInPolicy = xmlED.DOM.getElementsByTagName("TargetAccessPolicy");
        Element root = ((Element) nodesInPolicy.item(0));
        Node nodeToModify = root.getElementsByTagName("IF").item(this.index);
        Element child = null;
        if (orSelected.isSelected()) {
            child = createElement("OR");
        } else if (andSelected.isSelected()) {
            child = createElement("AND");
        }
        NodeList existAlready = DomainPolicyEditor.getChildElements(nodeToModify);
        if (condTable.column1Store.size() == 0) {
            if (existAlready.getLength() == 0) return;
            xmlED.deleteItem((Element) existAlready.item(0), ((Element) nodeToModify));
        } else if (existAlready.getLength() == 0) {
            xmlED.addItem(child, ((Element) nodeToModify));
        } else {
            xmlED.replaceNode(((Element) nodeToModify), ((Element) existAlready.item(0)), child);
        }
    }

    public Element createElement(String type) {
        Element createdElement = xmlED.DOM.createElement(type);
        for (int i = 0; i < condTable.table.getRowCount(); i++) {
            String operator = ((String) ((JComboBox) condTable.column2Store.get(i)).getSelectedItem());
            String tmpType = ((ConditionOnComboBox) condTable.column1Store.get(i)).types[((JComboBox) condTable.column1Store.get(i)).getSelectedIndex()];
            operator = getXMLOperator(operator, tmpType);
            Element newOperator = xmlED.DOM.createElement(operator);
            createdElement.appendChild((Node) newOperator);
            Element newArg = null;
            String srcString = ((ConditionOnComboBox) condTable.column1Store.get(i)).variable[((JComboBox) condTable.column1Store.get(i)).getSelectedIndex()];
            if (srcString.intern().equals(rbl.getString("ConditionsTable_DropDown_Source1"))) {
                newArg = xmlED.DOM.createElement("Environment");
            } else if (srcString.intern().equals(rbl.getString("ConditionsTable_DropDown_Source2"))) {
                newArg = xmlED.DOM.createElement("Arg");
            }
            newOperator.appendChild((Node) newArg);
            Element newConstant = xmlED.DOM.createElement("Constant");
            newOperator.appendChild((Node) newConstant);
            newArg.setAttribute("Name", ((String) ((JComboBox) condTable.column1Store.get(i)).getSelectedItem()));
            newArg.setAttribute("Type", tmpType);
            newConstant.setAttribute("Type", tmpType);
            newConstant.setAttribute("Value", ((String) ((JComboBox) condTable.column3Store.get(i)).getSelectedItem()));
        }
        return createdElement;
    }

    /**
     * This method, changes the descriptive part of the operator, that is
     * seen in the table, and returns it into a Tag that can be
     * inserted in the XML.
     *
     * @param row       the row the descriptive operator is on.
     * @param operator  the descriptive value as obtained in String form.
     * @return the relevant XML tag, that describes the operation done.
     */
    public String getXMLOperator(String operator, String type) {
        Vector tmpItems = (Vector) condTable.varTypeLinks.get(type);
        for (int y = 0; y < tmpItems.size(); y++) {
            String tmpString[] = ((String[]) tmpItems.get(y));
            if (tmpString[0].intern().equals(operator)) {
                operator = tmpString[1];
            }
        }
        return operator;
    }

    /**
     * This method, changes the XML Tag of the operator, in a more readable
     * human form. The Value is obtained from the hash table that is
     * populater in Conditions Table.
     *
     * @param operator  the descriptive value as obtained in String form.
     * @param type      the Variable Type (String/Integer/Time/Boolean/Real)
     * @return the relevant XML tag, that describes the operation done.
     */
    public String getOperatorDescription(String operator, String type) {
        Vector tmpItems = (Vector) condTable.varTypeLinks.get(type);
        for (int y = 0; y < tmpItems.size(); y++) {
            String tmpString[] = ((String[]) tmpItems.get(y));
            if (tmpString[1].intern().equals(operator)) {
                operator = tmpString[0];
            }
        }
        return operator;
    }

    public void refresh() {
        if (xmlED.DOM == null) return;
        NodeList nodesInPolicy = xmlED.DOM.getElementsByTagName("TargetAccessPolicy");
        Element root = ((Element) nodesInPolicy.item(0));
        NodeList targetAccess = DomainPolicyEditor.getChildElements(root);
        NodeList section = DomainPolicyEditor.getChildElements(targetAccess.item(index));
        NodeList types = DomainPolicyEditor.getChildElements((Element) section.item(2));
        if (types.getLength() == 0) {
            condTable.addRow(0);
            return;
        }
        Element typeAndOr = ((Element) types.item(0));
        if (typeAndOr.getTagName() == "OR") orSelected.setSelected(true); else if (typeAndOr.getTagName() == "AND") andSelected.setSelected(true);
        NodeList operators = DomainPolicyEditor.getChildElements(typeAndOr);
        for (int i = 0; i < operators.getLength(); i++) {
            String operator = ((Element) operators.item(i)).getTagName();
            NodeList argsConstant = DomainPolicyEditor.getChildElements((Element) operators.item(i));
            String sourceType = ((Element) argsConstant.item(0)).getTagName();
            String argName = ((Element) argsConstant.item(0)).getAttribute("Name");
            String argType = ((Element) argsConstant.item(0)).getAttribute("Type");
            operator = getOperatorDescription(operator, argType);
            String constantType = ((Element) argsConstant.item(1)).getAttribute("Type");
            String constantValue = ((Element) argsConstant.item(1)).getAttribute("Value");
            condTable.addRow(i);
            ((JComboBox) condTable.column1Store.get(i)).setSelectedItem(argName);
            ((JComboBox) condTable.column2Store.get(i)).setSelectedItem(operator);
            ((JComboBox) condTable.column3Store.get(i)).setSelectedItem(constantValue);
        }
    }
}

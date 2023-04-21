package issrg.utils.gui.ifcondition;

import issrg.utils.gui.xml.NodeVector;
import issrg.utils.gui.xml.XMLEditor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
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
public abstract class IFConstraintsPanelSimple extends JPanel implements ActionListener {

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

    JPanel simplePanel;

    JRadioButton andSelected;

    JRadioButton orSelected;

    JButton addConditionsButton;

    JFrame owner;

    ConditionsTable condTable;

    public static final int SIMPLE_PANEL = 0;

    public static final int ADVANCED_PANEL = 1;

    /**
     * Loads the String Resources from the '.properties' file.
     */
    ResourceBundle rb;

    String errorHeader;

    String dialogTitle;

    String sourceBorderCaption;

    String varNameBorderCaption;

    String varTypeBorderCaption;

    String operatorBorderCaption;

    String constantBorderCaption;

    String andRadioButtonCaption;

    String andRadioButtonCaptionTtip;

    String orRadioButtonCaption;

    String orRadioButtonCaptionTtip;

    String addRowCaption;

    String addButtonTtipCaption;

    String selectDateCaption;

    String selectTimeCaption;

    String errorMSG1;

    /**
     * Creates a new instance of the panel that needs to be displayed,
     * according to the parrameters that are passed.
     *
     * @param xmlED         The XML Editor
     * @param index         The index of the tab that is being displayed.
     * @param typeOfPanel   the type of panel to display, be it the Simple or
     *                      Advanced Panel. These could be reffered to by the
     *                      static integeres declared above.
     */
    public IFConstraintsPanelSimple(XMLEditor xmlED, int index, JFrame owner, ResourceBundle rb, ConditionOnComboBoxFactory conditionOnCBFactory) {
        condTable = new ConditionsTable(xmlED, owner, rb, conditionOnCBFactory, index);
        this.loadProperties(rb);
        this.xmlED = xmlED;
        this.index = index;
        this.owner = owner;
        JPanel displayPanel = new JPanel(new GridBagLayout());
        constraints = new GridBagConstraints();
        this.setLayout(new BorderLayout());
        this.add(getSimplePanel(), BorderLayout.CENTER);
    }

    private void loadProperties(ResourceBundle rb) {
        if (rb == null) {
            throw new IllegalArgumentException("Resource Bundle cannot be null.");
        }
        this.rb = rb;
        this.errorHeader = this.rb.getString("ErrorHeader");
        this.dialogTitle = this.rb.getString("IfCondition_Dialog_Title");
        this.sourceBorderCaption = this.rb.getString("IfCondition_Source_Border");
        this.varNameBorderCaption = this.rb.getString("IfCondition_VarName_Border");
        this.varTypeBorderCaption = this.rb.getString("IfCondition_VarType_Border");
        this.operatorBorderCaption = this.rb.getString("IfCondition_Operator_Border");
        this.constantBorderCaption = this.rb.getString("IfCondition_Constant_Border");
        this.andRadioButtonCaption = this.rb.getString("IfCondition_RadioButton_AND");
        this.andRadioButtonCaptionTtip = this.rb.getString("IfCondition_RadioButton_AND_Ttip");
        this.orRadioButtonCaption = this.rb.getString("IfCondition_RadioButton_OR");
        this.orRadioButtonCaptionTtip = this.rb.getString("IfCondition_RadioButton_OR_Ttip");
        this.addRowCaption = this.rb.getString("IfCondition_Button_Add_Caption");
        this.addButtonTtipCaption = this.rb.getString("IfCondition_Button_Add_Ttip");
        this.selectDateCaption = this.rb.getString("TimeDatePanel_DateDialog_Name");
        this.selectTimeCaption = this.rb.getString("TimePanel_Dialog_Name");
        this.errorMSG1 = this.rb.getString("IfCondition_Error_Empty_Field");
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
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 0, 0, 0);
        simplePanel.add(andOrPanel, constraints);
        constraints.insets = new Insets(0, 0, 0, 0);
        addConditionsButton = new JButton(addRowCaption);
        addConditionsButton.addActionListener(this);
        addConditionsButton.setActionCommand("ADD_ROW");
        addConditionsButton.setToolTipText(addButtonTtipCaption);
        JPanel addPanel = new JPanel(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        addPanel.add(addConditionsButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(10, 0, 0, 0);
        simplePanel.add(addPanel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        oldPanel.setPreferredSize(new Dimension(520, 200));
        simplePanel.add(oldPanel, constraints);
        return simplePanel;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().intern().equals("ADD_ROW")) {
            condTable.addRow(condTable.column1Store.size());
        }
    }

    public String getAbsoluteTime(String s) {
        if (s.indexOf(":") == -1) {
            return s + "T*:*:*";
        } else if (s.indexOf("-") == -1) {
            return "*-*-*T" + s;
        } else return s;
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
            if (tmpString[0].intern().equalsIgnoreCase(operator)) {
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
            if (tmpString[1].intern().equalsIgnoreCase(operator)) {
                operator = tmpString[0];
            }
        }
        return operator;
    }

    public abstract void refresh();

    public void setXmlED(XMLEditor xmlED) {
        this.xmlED = xmlED;
    }

    public XMLEditor getXmlED() {
        return xmlED;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setAndSelected(JRadioButton andSelected) {
        this.andSelected = andSelected;
    }

    public JRadioButton getAndSelected() {
        return andSelected;
    }

    public void setOrSelected(JRadioButton orSelected) {
        this.orSelected = orSelected;
    }

    public JRadioButton getOrSelected() {
        return orSelected;
    }

    public void setErrorMSG1(String errorMSG1) {
        this.errorMSG1 = errorMSG1;
    }

    public String getErrorMSG1() {
        return errorMSG1;
    }

    public void setErrorHeader(String errorHeader) {
        this.errorHeader = errorHeader;
    }

    public String getErrorHeader() {
        return errorHeader;
    }

    public void setCondTable(ConditionsTable condTable) {
        this.condTable = condTable;
    }

    public ConditionsTable getCondTable() {
        return condTable;
    }
}

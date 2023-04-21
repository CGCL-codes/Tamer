package issrg.utils.gui.ifcondition;

import issrg.utils.gui.xml.NodeVector;
import issrg.utils.gui.xml.XMLEditor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Christian Azzopardi
 */
public abstract class IFConstraintsPanelAdvanced extends JPanel implements ActionListener, ItemListener, ChangeListener {

    JPanel advancedPanel;

    JTabbedPane conditionsTabbedPane;

    XMLEditor xmlED;

    int index;

    GridBagConstraints constraints;

    JRadioButton andSelected;

    JRadioButton orSelected;

    JButton addConditionsBlocks;

    JButton deleteConditionsBlocks;

    JFrame owner;

    Vector conditionTables;

    ResourceBundle rb;

    String errorHeader;

    String andRadioButtonCaption;

    String andRadioButtonCaptionTtip;

    String orRadioButtonCaption;

    String orRadioButtonCaptionTtip;

    String addTabCaption;

    String deleteTabCaption;

    String addButtonTtipCaption;

    String deleteButtonTtipCaption;

    String allOfTheFollowingCaption;

    String oneOfTheFollowingCaption;

    String allConditionsListCaption;

    String oneConditionsListCaption;

    String selectDateCaption;

    String selectTimeCaption;

    String errorMSG1;

    private ConditionOnComboBoxFactory conditionOnCBFactory;

    /** Creates a new instance of IFConstraintsPanelAdvanced */
    public IFConstraintsPanelAdvanced(XMLEditor xmlED, int index, JFrame owner, ResourceBundle rb, ConditionOnComboBoxFactory conditionOnCBFactory) {
        this.setLayout(new BorderLayout());
        this.loadProperties(rb);
        this.setConditionOnCB(conditionOnCBFactory);
        this.xmlED = xmlED;
        this.index = index;
        this.conditionTables = new Vector();
        this.owner = owner;
        conditionTables = new Vector();
        this.add(getAdvancedPanel(), BorderLayout.CENTER);
    }

    private void loadProperties(ResourceBundle rb) {
        if (rb == null) {
            throw new IllegalArgumentException("Resource Bundle cannot be null.");
        }
        this.rb = rb;
        this.errorHeader = this.rb.getString("ErrorHeader");
        this.andRadioButtonCaption = this.rb.getString("IfCondition_Advanced_RadioButton_AND");
        this.andRadioButtonCaptionTtip = this.rb.getString("IfCondition_RadioButton_AND_Ttip");
        this.orRadioButtonCaption = this.rb.getString("IfCondition_Advanced_RadioButton_OR");
        this.orRadioButtonCaptionTtip = this.rb.getString("IfCondition_RadioButton_OR_Ttip");
        this.addTabCaption = this.rb.getString("IfCondition_AdvancedPanel_Button_Add_Caption");
        this.deleteTabCaption = this.rb.getString("IfCondition_AdvancedPanel_Button_Remove_Caption");
        this.addButtonTtipCaption = this.rb.getString("IfCondition_AdvancedPanel_Add_Ttip");
        this.deleteButtonTtipCaption = this.rb.getString("IfCondition_AdvancedPanel_Delete_Ttip");
        this.allOfTheFollowingCaption = this.rb.getString("IfCondition_AdvancedPanel_TabName_AND");
        this.oneOfTheFollowingCaption = this.rb.getString("IfCondition_AdvancedPanel_TabName_OR");
        this.allConditionsListCaption = this.rb.getString("IfCondition_AdvancedPanel_ConditionsList2");
        this.oneConditionsListCaption = this.rb.getString("IfCondition_AdvancedPanel_ConditionsList1");
        this.selectDateCaption = this.rb.getString("TimeDatePanel_DateDialog_Name");
        this.selectTimeCaption = this.rb.getString("TimePanel_Dialog_Name");
        this.errorMSG1 = this.rb.getString("IfCondition_Error_Empty_Field");
    }

    public JPanel getAdvancedPanel() {
        advancedPanel = new JPanel(new GridBagLayout());
        constraints = new GridBagConstraints();
        orSelected = new JRadioButton(orRadioButtonCaption);
        orSelected.setToolTipText(orRadioButtonCaptionTtip);
        orSelected.setMnemonic(KeyEvent.VK_N);
        orSelected.addItemListener(this);
        andSelected = new JRadioButton(andRadioButtonCaption);
        andSelected.setToolTipText(andRadioButtonCaptionTtip);
        andSelected.setMnemonic(KeyEvent.VK_L);
        andSelected.addItemListener(this);
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
        advancedPanel.add(andOrPanel, constraints);
        addConditionsBlocks = new JButton(addTabCaption);
        addConditionsBlocks.addActionListener(this);
        addConditionsBlocks.setActionCommand("ADD_BLOCK");
        addConditionsBlocks.setToolTipText(addButtonTtipCaption);
        deleteConditionsBlocks = new JButton(deleteTabCaption);
        deleteConditionsBlocks.addActionListener(this);
        deleteConditionsBlocks.setActionCommand("DELETE_BLOCK");
        deleteConditionsBlocks.setToolTipText(deleteButtonTtipCaption);
        deleteConditionsBlocks.setEnabled(false);
        JPanel addPanel = new JPanel(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 0, 0);
        addPanel.add(addConditionsBlocks, constraints);
        constraints.insets = new Insets(0, 3, 0, 0);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        addPanel.add(deleteConditionsBlocks, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(10, 0, 0, 0);
        advancedPanel.add(addPanel, constraints);
        conditionsTabbedPane = new JTabbedPane();
        conditionsTabbedPane.addChangeListener(this);
        conditionsTabbedPane.setPreferredSize(new Dimension(520, 200));
        JPanel tabbedPanel = new JPanel(new BorderLayout());
        tabbedPanel.add(conditionsTabbedPane, BorderLayout.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        advancedPanel.add(tabbedPanel, constraints);
        return advancedPanel;
    }

    public String getAbsoluteTime(String s) {
        if (s.indexOf(":") == -1) {
            return s + "T*:*:*";
        } else if (s.indexOf("-") == -1) {
            return "*-*-*T" + s;
        } else return s;
    }

    public void getTabNames() {
        String baseName, borderName;
        if (andSelected.isSelected()) {
            borderName = oneConditionsListCaption;
            baseName = oneOfTheFollowingCaption;
        } else {
            borderName = allConditionsListCaption;
            baseName = allOfTheFollowingCaption;
        }
        if (conditionsTabbedPane == null) return;
        for (int i = 0; i < conditionsTabbedPane.getTabCount(); i++) {
            conditionsTabbedPane.setTitleAt(i, baseName + " " + (i + 1));
            ((AdvancedConditionsTable) conditionTables.get(i)).tableView.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), borderName));
        }
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
    public String getXMLOperator(AdvancedConditionsTable act, String operator, String type) {
        Vector tmpItems = (Vector) act.varTypeLinks.get(type);
        for (int y = 0; y < tmpItems.size(); y++) {
            String tmpString[] = ((String[]) tmpItems.get(y));
            if (tmpString[0].intern().equalsIgnoreCase(operator)) {
                operator = tmpString[1];
            }
        }
        return operator;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().intern().equals("ADD_BLOCK")) {
            AdvancedConditionsTable newTable = new AdvancedConditionsTable(this.xmlED, this.owner, this.index, this.rb, this.conditionOnCBFactory);
            this.conditionTables.add(newTable);
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.fill = GridBagConstraints.BOTH;
            this.conditionsTabbedPane.add(((AdvancedConditionsTable) conditionTables.get(conditionTables.size() - 1)).getContentPanel());
            newTable.addRow(0);
            conditionsTabbedPane.setSelectedIndex(conditionTables.size() - 1);
            deleteConditionsBlocks.setEnabled(true);
        } else if (e.getActionCommand().intern().equals("DELETE_BLOCK")) {
            this.conditionTables.remove(conditionsTabbedPane.getSelectedIndex());
            conditionsTabbedPane.remove(conditionsTabbedPane.getSelectedIndex());
            if (conditionsTabbedPane.getTabCount() == 0) deleteConditionsBlocks.setEnabled(false);
            refreshTabColours(conditionsTabbedPane);
        }
        getTabNames();
    }

    public void stateChanged(ChangeEvent e) {
        refreshTabColours((JTabbedPane) e.getSource());
    }

    public void refreshTabColours(JTabbedPane jPane) {
        for (int i = 0; i < jPane.getTabCount(); i++) {
            jPane.setForegroundAt(i, Color.BLACK);
        }
        if (jPane.getSelectedIndex() > -1) jPane.setForegroundAt(jPane.getSelectedIndex(), Color.BLUE);
    }

    public void itemStateChanged(ItemEvent e) {
        getTabNames();
    }

    public abstract void refresh();

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
        Vector tmpItems = (Vector) ((AdvancedConditionsTable) conditionTables.get(0)).varTypeLinks.get(type);
        for (int y = 0; y < tmpItems.size(); y++) {
            String tmpString[] = ((String[]) tmpItems.get(y));
            if (tmpString[1].intern().equalsIgnoreCase(operator)) {
                operator = tmpString[0];
            }
        }
        return operator;
    }

    public void setConditionOnCB(ConditionOnComboBoxFactory conditionOnCBFactory) {
        if (conditionOnCBFactory != null) {
            this.conditionOnCBFactory = conditionOnCBFactory;
        } else {
            throw new IllegalArgumentException("Condition ComboBox cannot be null.");
        }
    }

    public ConditionOnComboBoxFactory getConditionOnCBFactory() {
        return conditionOnCBFactory;
    }

    public void setConditionsTabbedPane(JTabbedPane conditionsTabbedPane) {
        this.conditionsTabbedPane = conditionsTabbedPane;
    }

    public JTabbedPane getConditionsTabbedPane() {
        return conditionsTabbedPane;
    }

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

    public void setConditionTables(Vector conditionTables) {
        this.conditionTables = conditionTables;
    }

    public Vector getConditionTables() {
        return conditionTables;
    }

    public void setErrorHeader(String errorHeader) {
        this.errorHeader = errorHeader;
    }

    public String getErrorHeader() {
        return errorHeader;
    }

    public void setErrorMSG1(String errorMSG1) {
        this.errorMSG1 = errorMSG1;
    }

    public String getErrorMSG1() {
        return errorMSG1;
    }

    public void setConditionOnCBFactory(ConditionOnComboBoxFactory conditionOnCBFactory) {
        this.conditionOnCBFactory = conditionOnCBFactory;
    }
}

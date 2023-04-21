package issrg.editor2;

import issrg.pba.rbac.xmlpolicy.XMLTags;
import issrg.utils.gui.xml.XMLEditor;
import issrg.utils.xml.OldPolicyFormatCompat;
import issrg.utils.xml.XMLUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class implements the editor for MSoD policy. 
 */
public class MSoDPolicyEditor extends JPanel implements ActionListener, ChangeListener, ListSelectionListener {

    private static Logger logger = Logger.getLogger(MSoDPolicyEditor.class);

    String msodID = "";

    private JPanel scopePanel, scopeSubPanel1, scopeSubPanel2, bcPanel, mmerPanel, mmerSubPanel1, mmerSubPanel2, mmepPanel, mmepSubPanel1, mmepSubPanel2, editPanel;

    private JTextField scopeType;

    private TargetsComboBox scopeValueSpec;

    private JRadioButton scopeValueOption1;

    private String scopeValueOption1Text = "!";

    private JRadioButton scopeValueOption2;

    private String scopeValueOption2Text = "*";

    private JRadioButton scopeValueOption3;

    private ButtonGroup radioButtonGroup;

    private JButton addScope, editScope, delScope, addRoles, delRoles, addMMER, delMMER, addPrivs, delPrivs, addMMEP, delMMEP;

    private JList scopeList, roleList, roleSelectedList, mmerList, privList, privSelectedList, mmepList;

    private JLabel scopeLabel1, scopeLabel2, bcLabel1, bcLabel2, mmerLabel2, mmepLabel2;

    private JComboBox taskList1, taskList2, mmerCarList, mmepCarList;

    private JScrollPane scopeScrollPane, roleScrollPane, roleSelectedScrollPane, mmerScrollPane, privScrollPane, privSelectedScrollPane, mmepScrollPane;

    private XMLEditor xmlED;

    private ResourceBundle rbl = ResourceBundle.getBundle("issrg/editor2/PEComponent_i18n");

    private String errorHeader = rbl.getString("ErrorHeader");

    private String MSoDPolicyError1 = rbl.getString("MSoDPolicyError1");

    private String MSoDPolicyError2 = rbl.getString("MSoDPolicyError2");

    private String MSoDPolicyError3 = rbl.getString("MSoDPolicyError3");

    private String MSoDPolicyError4 = rbl.getString("MSoDPolicyError4");

    private String MSoDPolicyError5 = rbl.getString("MSoDPolicyError5");

    private String MSoDPolicyError6 = rbl.getString("MSoDPolicyError6");

    private static final String TARGET_ACCESS_ELE = XMLTags.TARGET_ACCESS_NODE;

    private static final String TARGET_ELE = XMLTags.TARGET_NODE;

    private static final String TARGET_ACTIONS_DELIMITER = ",";

    private static final String PRIVILEGE_TOKENIZER_FOR_DISPLAY = "::";

    private static final String ROLE_NODE = XMLTags.ROLE_NODE;

    private static final String ROLE_TYPE_ATTR = XMLTags.TYPE_ATTRIBUTE;

    private static final String ROLE_VALUE_ATTR = XMLTags.VALUE_ATTRIBUTE;

    private static final String ROLE_SPEC_ELE = XMLTags.ROLE_SPEC_NODE;

    private static final String SUPROLE_ELE = XMLTags.SUP_ROLE_NODE;

    private static final String BUSINESS_CONTEXT_TOKENIZER = ", ";

    private Hashtable<String, RoleAttribute> roleAttributeHash = new Hashtable<String, RoleAttribute>();

    private Hashtable<String, TargetAccess> targetAccessHash = new Hashtable<String, TargetAccess>();

    private Hashtable<String, MutualExclusiveRoles> mmerHash = new Hashtable<String, MutualExclusiveRoles>();

    private Hashtable<String, MutualExclusivePrivileges> mmepHash = new Hashtable<String, MutualExclusivePrivileges>();

    /** Creates a new instance of MSoDPolicyEditor */
    public MSoDPolicyEditor(XMLEditor pec) {
        super();
        this.xmlED = pec;
        this.setLayout(new BorderLayout());
        this.add(getContentPanel(), BorderLayout.CENTER);
        this.refreshContentPanel();
    }

    public MSoDPolicyEditor(XMLEditor pec, Element msodPolicy) {
        this(pec);
        String bc = msodPolicy.getAttribute(XMLTags.CONTEXT_NAME);
        this.msodID = msodPolicy.getAttribute(XMLTags.ID_ATTRIBUTE);
        if (bc != null && (!bc.equals(""))) {
            Vector<String> scopeListItems = new Vector<String>();
            StringTokenizer st = new StringTokenizer(bc, BUSINESS_CONTEXT_TOKENIZER);
            while (st.hasMoreTokens()) {
                scopeListItems.add(st.nextToken());
            }
            scopeList.setListData(scopeListItems);
            NodeList nl = msodPolicy.getElementsByTagName(XMLTags.MSoD_POLICY_FIRSTSTEP);
            if (nl.getLength() > 0) {
                Element firstStepElement = (Element) nl.item(0);
                String[] actionId = XMLUtils.getAttributeValuesOnChildren(firstStepElement, XMLTags.ALLOWED_ACTION_NODE, XMLTags.ID_ATTRIBUTE);
                String actionName = OldPolicyFormatCompat.getNameFromId(xmlED.getElement(XMLTags.ACTION_POLICY_NODE), actionId[0]);
                String[] domainId = XMLUtils.getAttributeValuesOnChildren(firstStepElement, XMLTags.TARGET_DOMAIN_NODE, XMLTags.ID_ATTRIBUTE);
                taskList1.setSelectedItem(actionName + PRIVILEGE_TOKENIZER_FOR_DISPLAY + domainId[0]);
            }
            nl = msodPolicy.getElementsByTagName(XMLTags.MSoD_POLICY_LASTSTEP);
            if (nl.getLength() > 0) {
                Element lastStepElement = (Element) nl.item(0);
                String[] actionId = XMLUtils.getAttributeValuesOnChildren(lastStepElement, XMLTags.ALLOWED_ACTION_NODE, XMLTags.ID_ATTRIBUTE);
                String actionName = OldPolicyFormatCompat.getNameFromId(xmlED.getElement(XMLTags.ACTION_POLICY_NODE), actionId[0]);
                String[] domainId = XMLUtils.getAttributeValuesOnChildren(lastStepElement, XMLTags.TARGET_DOMAIN_NODE, XMLTags.ID_ATTRIBUTE);
                taskList2.setSelectedItem(actionName + PRIVILEGE_TOKENIZER_FOR_DISPLAY + domainId[0]);
            }
            nl = msodPolicy.getElementsByTagName(XMLTags.MSoD_MMER_NODE);
            Vector mmerRoleV = new Vector();
            for (int i = 0; i < nl.getLength(); i++) {
                MutualExclusiveRoles mer = new MutualExclusiveRoles((Element) nl.item(i));
                mmerHash.put(mer.displayString, mer);
                mmerRoleV.add(mer.displayString);
            }
            mmerList.setListData(mmerRoleV);
            Vector mmepRoleV = new Vector();
            nl = msodPolicy.getElementsByTagName(XMLTags.MSoD_MMEP_NODE);
            for (int i = 0; i < nl.getLength(); i++) {
                MutualExclusivePrivileges mep = new MutualExclusivePrivileges((Element) nl.item(i));
                mmepHash.put(mep.getDisplayString(), mep);
                if (logger.isTraceEnabled()) {
                    logger.trace("Following has been added to mmepHash");
                    logger.trace(mep.toString());
                }
                mmepRoleV.add(mep.getDisplayString());
            }
            mmepList.setListData(mmepRoleV);
        }
    }

    private JPanel getContentPanel() {
        editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());
        scopePanel = new JPanel(new GridBagLayout());
        scopePanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Scope of business context (e.g. organisation=Uiversity of Kent, department=<Any single department>, process=exams)"));
        scopeSubPanel1 = new JPanel(new GridBagLayout());
        scopeSubPanel1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), ""));
        scopeSubPanel2 = new JPanel(new GridBagLayout());
        scopeSubPanel2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), ""));
        scopeLabel1 = new JLabel("Business context component: ");
        scopeType = new JTextField(20);
        scopeLabel2 = new JLabel("Scope of the component: ");
        scopeValueOption1 = new JRadioButton("Any single instance", true);
        scopeValueOption2 = new JRadioButton("Across all instances");
        scopeValueOption3 = new JRadioButton("This instance only: ");
        scopeValueOption3.addChangeListener(this);
        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(scopeValueOption1);
        radioButtonGroup.add(scopeValueOption2);
        radioButtonGroup.add(scopeValueOption3);
        scopeValueSpec = new TargetsComboBox(this.xmlED, true, PEApplication.configComponent);
        scopeValueSpec.setEnabled(false);
        addComponent(scopeLabel1, scopeSubPanel1, 0, 0, 1, 1, GridBagConstraints.WEST, 0.4, 0.25, GridBagConstraints.NONE);
        addComponent(scopeType, scopeSubPanel1, 0, 1, 2, 1, GridBagConstraints.WEST, 0.5, 0.25, GridBagConstraints.NONE);
        addComponent(scopeLabel2, scopeSubPanel1, 1, 0, 1, 3, GridBagConstraints.WEST, 0.4, 0.75, GridBagConstraints.NONE);
        addComponent(scopeValueOption1, scopeSubPanel1, 1, 1, 2, 1, GridBagConstraints.WEST, 0.5, 0.25, GridBagConstraints.NONE);
        addComponent(scopeValueOption2, scopeSubPanel1, 2, 1, 2, 1, GridBagConstraints.WEST, 0.5, 0.25, GridBagConstraints.NONE);
        addComponent(scopeValueOption3, scopeSubPanel1, 3, 1, 1, 1, GridBagConstraints.WEST, 0.1, 0.25, GridBagConstraints.NONE);
        addComponent(scopeValueSpec, scopeSubPanel1, 4, 1, 1, 1, GridBagConstraints.WEST, 0.4, 0.25, GridBagConstraints.NONE);
        addScope = new JButton("Add");
        editScope = new JButton("Replace");
        delScope = new JButton("Remove");
        addScope.addActionListener(this);
        editScope.addActionListener(this);
        delScope.addActionListener(this);
        scopeList = new JList();
        scopeList.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), ""));
        scopeList.setVisibleRowCount(3);
        scopeList.addListSelectionListener(this);
        scopeScrollPane = new JScrollPane(scopeList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scopeScrollPane.setPreferredSize(new Dimension(200, 100));
        addComponent(addScope, scopeSubPanel2, 0, 0, 1, 1, GridBagConstraints.WEST, 0.1, 0.3, GridBagConstraints.NONE);
        addComponent(editScope, scopeSubPanel2, 1, 0, 1, 1, GridBagConstraints.WEST, 0.1, 0.3, GridBagConstraints.NONE);
        addComponent(delScope, scopeSubPanel2, 2, 0, 1, 1, GridBagConstraints.WEST, 0.1, 0.3, GridBagConstraints.NONE);
        addComponent(scopeScrollPane, scopeSubPanel2, 0, 1, 3, 3, GridBagConstraints.WEST, 0.8, 0.9, GridBagConstraints.BOTH);
        addComponent(scopeSubPanel1, scopePanel, 0, 0, 1, 1, GridBagConstraints.WEST, 0.2, 1, GridBagConstraints.BOTH);
        addComponent(scopeSubPanel2, scopePanel, 0, 1, 1, 1, GridBagConstraints.WEST, 0.5, 1, GridBagConstraints.BOTH);
        bcPanel = new JPanel(new GridBagLayout());
        bcPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Boundary of the business context (Optional)"));
        bcLabel1 = new JLabel("The first Privilege:");
        bcLabel2 = new JLabel("The last Privilege:");
        taskList1 = new JComboBox();
        taskList2 = new JComboBox();
        taskList1.setPrototypeDisplayValue("01234567890123456789");
        taskList2.setPrototypeDisplayValue("01234567890123456789");
        addComponent(bcLabel1, bcPanel, 0, 0, 1, 1, GridBagConstraints.WEST, 0.2, 1, GridBagConstraints.NONE);
        addComponent(taskList1, bcPanel, 0, 1, 1, 1, GridBagConstraints.WEST, 0.7, 1, GridBagConstraints.NONE);
        addComponent(bcLabel2, bcPanel, 1, 0, 1, 1, GridBagConstraints.WEST, 0.2, 1, GridBagConstraints.NONE);
        addComponent(taskList2, bcPanel, 1, 1, 1, 1, GridBagConstraints.WEST, 0.7, 1, GridBagConstraints.NONE);
        mmerPanel = new JPanel(new GridBagLayout());
        mmerPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Mutually exclusive roles (MER)"));
        mmerLabel2 = new JLabel("Forbidden cardinality: ");
        roleList = new JList();
        roleList.setVisibleRowCount(6);
        roleList.setAutoscrolls(true);
        roleScrollPane = new JScrollPane(roleList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        roleScrollPane.setPreferredSize(new Dimension(150, 120));
        mmerSubPanel1 = new JPanel(new GridBagLayout());
        mmerSubPanel2 = new JPanel(new GridBagLayout());
        addRoles = new JButton("Add role(s)");
        delRoles = new JButton("Remove role(s)");
        addRoles.addActionListener(this);
        delRoles.addActionListener(this);
        roleSelectedList = new JList();
        roleSelectedList.setVisibleRowCount(4);
        roleSelectedList.setAutoscrolls(true);
        roleSelectedScrollPane = new JScrollPane(roleSelectedList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        roleSelectedScrollPane.setPreferredSize(new Dimension(150, 100));
        mmerCarList = new JComboBox();
        mmerCarList.setPrototypeDisplayValue("01");
        addComponent(addRoles, mmerSubPanel1, 0, 0, 1, 1, GridBagConstraints.WEST, 0.1, 0.3, GridBagConstraints.NONE);
        addComponent(delRoles, mmerSubPanel1, 1, 0, 1, 1, GridBagConstraints.WEST, 0.1, 0.3, GridBagConstraints.NONE);
        addComponent(roleSelectedScrollPane, mmerSubPanel1, 0, 1, 2, 2, GridBagConstraints.WEST, 0.2, 0.6, GridBagConstraints.BOTH);
        addComponent(mmerLabel2, mmerSubPanel2, 0, 0, 1, 1, GridBagConstraints.WEST, 0.2, 0.3, GridBagConstraints.BOTH);
        addComponent(mmerCarList, mmerSubPanel2, 0, 1, 1, 1, GridBagConstraints.WEST, 0.1, 0.3, GridBagConstraints.BOTH);
        addMMER = new JButton("Add a MER constraint");
        delMMER = new JButton("Remove an MER constraint");
        addMMER.addActionListener(this);
        delMMER.addActionListener(this);
        mmerList = new JList();
        mmerScrollPane = new JScrollPane(mmerList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mmerScrollPane.setPreferredSize(new Dimension(250, 120));
        addComponent(roleScrollPane, mmerPanel, 0, 0, 1, 6, GridBagConstraints.WEST, 0.2, 0.9, GridBagConstraints.BOTH);
        addComponent(mmerSubPanel1, mmerPanel, 0, 1, 1, 4, GridBagConstraints.WEST, 0.2, 0.6, GridBagConstraints.BOTH);
        addComponent(mmerSubPanel2, mmerPanel, 4, 1, 1, 2, GridBagConstraints.WEST, 0.2, 0.3, GridBagConstraints.BOTH);
        addComponent(addMMER, mmerPanel, 1, 2, 1, 1, GridBagConstraints.WEST, 0.2, 0.45, GridBagConstraints.NONE);
        addComponent(delMMER, mmerPanel, 3, 2, 1, 1, GridBagConstraints.WEST, 0.2, 0.45, GridBagConstraints.NONE);
        addComponent(mmerScrollPane, mmerPanel, 0, 3, 1, 6, GridBagConstraints.WEST, 0.2, 0.9, GridBagConstraints.BOTH);
        mmepPanel = new JPanel(new GridBagLayout());
        mmepPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Mutually exclusive privileges (MEP)"));
        mmepLabel2 = new JLabel("Forbidden cardinality: ");
        privList = new JList();
        privList.setVisibleRowCount(6);
        privScrollPane = new JScrollPane(privList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        privScrollPane.setPreferredSize(new Dimension(150, 120));
        mmepSubPanel1 = new JPanel(new GridBagLayout());
        mmepSubPanel2 = new JPanel(new GridBagLayout());
        addPrivs = new JButton("Add Privileges");
        delPrivs = new JButton("Remove Privileges");
        addPrivs.addActionListener(this);
        delPrivs.addActionListener(this);
        privSelectedList = new JList();
        privSelectedList.setVisibleRowCount(4);
        privSelectedScrollPane = new JScrollPane(privSelectedList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        privSelectedScrollPane.setPreferredSize(new Dimension(150, 100));
        mmepCarList = new JComboBox();
        mmepCarList.setPrototypeDisplayValue("01");
        addComponent(addPrivs, mmepSubPanel1, 0, 0, 1, 1, GridBagConstraints.WEST, 0.1, 0.3, GridBagConstraints.NONE);
        addComponent(delPrivs, mmepSubPanel1, 1, 0, 1, 1, GridBagConstraints.WEST, 0.1, 0.3, GridBagConstraints.NONE);
        addComponent(privSelectedScrollPane, mmepSubPanel1, 0, 1, 2, 2, GridBagConstraints.WEST, 0.2, 0.6, GridBagConstraints.BOTH);
        addComponent(mmepLabel2, mmepSubPanel2, 0, 0, 1, 1, GridBagConstraints.WEST, 0.2, 0.3, GridBagConstraints.BOTH);
        addComponent(mmepCarList, mmepSubPanel2, 0, 1, 1, 1, GridBagConstraints.WEST, 0.1, 0.3, GridBagConstraints.BOTH);
        addMMEP = new JButton("Add an MEP constraint");
        delMMEP = new JButton("Remove an MEP constraint");
        addMMEP.addActionListener(this);
        delMMEP.addActionListener(this);
        mmepList = new JList();
        mmepScrollPane = new JScrollPane(mmepList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mmepScrollPane.setPreferredSize(new Dimension(250, 120));
        addComponent(privScrollPane, mmepPanel, 0, 0, 1, 6, GridBagConstraints.WEST, 0.2, 0.9, GridBagConstraints.BOTH);
        addComponent(mmepSubPanel1, mmepPanel, 0, 1, 1, 4, GridBagConstraints.WEST, 0.2, 0.6, GridBagConstraints.BOTH);
        addComponent(mmepSubPanel2, mmepPanel, 4, 1, 1, 2, GridBagConstraints.WEST, 0.2, 0.3, GridBagConstraints.BOTH);
        addComponent(addMMEP, mmepPanel, 1, 2, 1, 1, GridBagConstraints.WEST, 0.2, 0.45, GridBagConstraints.NONE);
        addComponent(delMMEP, mmepPanel, 3, 2, 1, 1, GridBagConstraints.WEST, 0.2, 0.45, GridBagConstraints.NONE);
        addComponent(mmepScrollPane, mmepPanel, 0, 3, 1, 6, GridBagConstraints.WEST, 0.2, 0.9, GridBagConstraints.BOTH);
        addComponent(scopePanel, editPanel, 0, 0, 2, 1, GridBagConstraints.WEST, 1, 0.25, GridBagConstraints.BOTH);
        addComponent(bcPanel, editPanel, 0, 2, 1, 1, GridBagConstraints.EAST, 1, 0.25, GridBagConstraints.BOTH);
        addComponent(mmerPanel, editPanel, 1, 0, 3, 1, GridBagConstraints.WEST, 1, 0.25, GridBagConstraints.BOTH);
        addComponent(mmepPanel, editPanel, 2, 0, 3, 1, GridBagConstraints.WEST, 1, 0.25, GridBagConstraints.BOTH);
        return editPanel;
    }

    public void refreshContentPanel() {
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("Start of refreshContentPanel");
            }
            NodeList targetAccessPolicy = xmlED.DOM.getElementsByTagName(TARGET_ACCESS_ELE);
            NodeList roles = xmlED.getElement(XMLTags.ROLE_ASSIGNMENT_POLICY_NODE).getElementsByTagName(ROLE_NODE);
            taskList1.removeAllItems();
            taskList2.removeAllItems();
            if (logger.isTraceEnabled()) {
                logger.trace("Before loop that runs over TargetAccess elements");
            }
            if (targetAccessPolicy.getLength() > 0) {
                taskList1.addItem("");
                taskList2.addItem("");
                for (int i = 0; i < targetAccessPolicy.getLength(); i++) {
                    NodeList target = ((Element) targetAccessPolicy.item(i)).getElementsByTagName(TARGET_ELE);
                    for (int j = 0; j < target.getLength(); j++) {
                        NodeList children = target.item(j).getChildNodes();
                        int k = 0;
                        while (k < children.getLength() && children.item(k).getNodeType() != Node.ELEMENT_NODE) {
                            k++;
                        }
                        String targetSpec = children.item(k).getAttributes().item(0).getNodeValue();
                        if (logger.isTraceEnabled()) {
                            logger.trace("targetSpec = " + targetSpec);
                        }
                        String actions = OldPolicyFormatCompat.getAllowedActionsOldFormat((Element) target.item(j), xmlED.getElement(XMLTags.ACTION_POLICY_NODE));
                        if (logger.isTraceEnabled()) {
                            logger.trace("actions in old format:" + actions);
                        }
                        StringTokenizer st = new StringTokenizer(actions, TARGET_ACTIONS_DELIMITER);
                        while (st.hasMoreTokens()) {
                            String action = (String) st.nextElement();
                            TargetAccess ta = new TargetAccess(action, targetSpec);
                            targetAccessHash.put(ta.displayString, ta);
                        }
                    }
                }
            }
            if (logger.isTraceEnabled()) {
                logger.trace("After loop that runs over TargetAccess elements");
            }
            Vector<String> mmepListItems = new Vector<String>();
            Enumeration<String> keys = targetAccessHash.keys();
            while (keys.hasMoreElements()) {
                String displayString = targetAccessHash.get(keys.nextElement()).displayString;
                mmepListItems.add(displayString);
                taskList1.addItem(displayString);
                taskList2.addItem(displayString);
            }
            taskList1.setSelectedItem("");
            taskList2.setSelectedItem("");
            privList.setListData(mmepListItems);
            roleAttributeHash.clear();
            if (roles.getLength() > 0) {
                for (int i = 0; i < roles.getLength(); i++) {
                    String roleType = ((Element) roles.item(i)).getAttribute(ROLE_TYPE_ATTR);
                    String roleValue = ((Element) roles.item(i)).getAttribute(ROLE_VALUE_ATTR);
                    if (roleValue == null || roleValue.equals("")) {
                        NodeList roleSpecs = this.xmlED.DOM.getElementsByTagName(ROLE_SPEC_ELE);
                        for (int j = 0; j < roleSpecs.getLength(); j++) {
                            if (roleType.equals(((Element) roleSpecs.item(j)).getAttribute(ROLE_TYPE_ATTR))) {
                                NodeList supRoles = ((Element) roleSpecs.item(j)).getElementsByTagName(SUPROLE_ELE);
                                for (int k = 0; k < supRoles.getLength(); k++) {
                                    roleValue = ((Element) supRoles.item(k)).getAttribute(ROLE_VALUE_ATTR);
                                    RoleAttribute ra = new RoleAttribute(roleType, roleValue);
                                    roleAttributeHash.put(ra.displayString, ra);
                                }
                            }
                        }
                    } else {
                        RoleAttribute ra = new RoleAttribute(roleType, roleValue);
                        roleAttributeHash.put(ra.displayString, ra);
                    }
                }
            }
            Vector<String> mmerListItems = new Vector<String>();
            keys = roleAttributeHash.keys();
            while (keys.hasMoreElements()) {
                mmerListItems.add(roleAttributeHash.get(keys.nextElement()).displayString);
            }
            roleList.setListData(mmerListItems);
        } catch (NullPointerException e) {
            logger.trace("NullPointerException: " + e.getMessage());
        }
    }

    private void addComponent(Component component, JPanel pane, int row, int column, int width, int height, int anchor, double weightx, double weighty, int fill) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.anchor = anchor;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.fill = fill;
        pane.add(component, constraints);
    }

    private void addScope() {
        ListModel scopeListModel = scopeList.getModel();
        Vector scopeListItems = new Vector();
        for (int i = 0; i < scopeListModel.getSize(); i++) {
            scopeListItems.add(scopeListModel.getElementAt(i));
        }
        if (!scopeType.getText().trim().equals("")) {
            String scopeSpec = scopeType.getText() + "=";
            if (scopeValueOption1.isSelected()) {
                scopeListItems.add(scopeSpec + scopeValueOption1Text);
            } else if (scopeValueOption2.isSelected()) {
                scopeListItems.add(scopeSpec + scopeValueOption2Text);
            } else {
                if (scopeValueOption3.isSelected() && (!((String) scopeValueSpec.getSelectedItem()).equals(""))) {
                    scopeListItems.add(scopeSpec + scopeValueSpec.getSelectedItem());
                    boolean itemExist = false;
                    for (int i = 0; i < scopeValueSpec.getItemCount(); i++) {
                        if (((String) scopeValueSpec.getItemAt(i)).equals((String) scopeValueSpec.getSelectedItem())) {
                            itemExist = true;
                        }
                    }
                    if (!itemExist) scopeValueSpec.addItem((String) scopeValueSpec.getSelectedItem());
                } else {
                    JOptionPane.showMessageDialog(xmlED, MSoDPolicyError2, errorHeader, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(xmlED, MSoDPolicyError1, errorHeader, JOptionPane.ERROR_MESSAGE);
            return;
        }
        scopeList.setListData(scopeListItems);
        scopeType.setText("");
        scopeValueSpec.setSelectedItem("");
    }

    private void replaceScope() {
        int itemToBeReplaced = scopeList.getSelectedIndex();
        if (itemToBeReplaced == -1) {
            return;
        }
        ListModel scopeListModel = scopeList.getModel();
        Vector scopeListItems = new Vector();
        for (int i = 0; i < scopeListModel.getSize(); i++) {
            if (i != itemToBeReplaced) {
                scopeListItems.add(scopeListModel.getElementAt(i));
            } else {
                if (!scopeType.getText().trim().equals("")) {
                    String scopeSpec = scopeType.getText() + "=";
                    if (scopeValueOption1.isSelected()) {
                        scopeListItems.add(scopeSpec + scopeValueOption1Text);
                    } else if (scopeValueOption2.isSelected()) {
                        scopeListItems.add(scopeSpec + scopeValueOption2Text);
                    } else {
                        if (scopeValueOption3.isSelected() && (!((String) scopeValueSpec.getSelectedItem()).equals(""))) {
                            scopeListItems.add(scopeSpec + scopeValueSpec.getSelectedItem());
                            boolean itemExist = false;
                            for (int j = 0; j < scopeValueSpec.getItemCount(); j++) {
                                if (((String) scopeValueSpec.getItemAt(j)).equals((String) scopeValueSpec.getSelectedItem())) {
                                    itemExist = true;
                                }
                            }
                            if (!itemExist) scopeValueSpec.addItem((String) scopeValueSpec.getSelectedItem());
                        } else {
                            JOptionPane.showMessageDialog(xmlED, MSoDPolicyError2, errorHeader, JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(xmlED, MSoDPolicyError1, errorHeader, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        scopeList.setListData(scopeListItems);
        scopeType.setText("");
        scopeValueSpec.setSelectedItem("");
    }

    private void deleteScope() {
        int selectedIndex = scopeList.getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        ListModel scopeListModel = scopeList.getModel();
        Vector scopeListItems = new Vector();
        for (int i = 0; i < scopeListModel.getSize(); i++) {
            if (i != selectedIndex) scopeListItems.add(scopeListModel.getElementAt(i));
        }
        scopeList.setListData(scopeListItems);
        scopeType.setText("");
        scopeValueSpec.setSelectedItem("");
    }

    private void addRoles() {
        int[] selectedIndices = roleList.getSelectedIndices();
        if (selectedIndices.length > 0) {
            ListModel listModel = roleList.getModel();
            ListModel selectedListModel = roleSelectedList.getModel();
            Vector itemsV = new Vector();
            Vector selectedItemsV = new Vector();
            for (int i = 0; i < listModel.getSize(); i++) {
                itemsV.add(listModel.getElementAt(i));
            }
            for (int i = 0; i < selectedListModel.getSize(); i++) {
                selectedItemsV.add(selectedListModel.getElementAt(i));
            }
            for (int j = 0; j < selectedIndices.length; j++) {
                itemsV.remove((String) listModel.getElementAt(selectedIndices[j]));
                selectedItemsV.add((String) listModel.getElementAt(selectedIndices[j]));
            }
            roleList.setListData(itemsV);
            roleSelectedList.setListData(selectedItemsV);
            setMmerCardinality(selectedItemsV.size());
            roleList.clearSelection();
        }
    }

    private void removeRoles() {
        int[] selectedIndices = roleSelectedList.getSelectedIndices();
        if (selectedIndices.length > 0) {
            ListModel listModel = roleList.getModel();
            ListModel selectedListModel = roleSelectedList.getModel();
            Vector itemsV = new Vector();
            Vector selectedItemsV = new Vector();
            for (int i = 0; i < listModel.getSize(); i++) {
                itemsV.add(listModel.getElementAt(i));
            }
            for (int i = 0; i < selectedListModel.getSize(); i++) {
                selectedItemsV.add(selectedListModel.getElementAt(i));
            }
            for (int j = 0; j < selectedIndices.length; j++) {
                itemsV.add((String) selectedListModel.getElementAt(selectedIndices[j]));
                selectedItemsV.remove((String) selectedListModel.getElementAt(selectedIndices[j]));
            }
            roleList.setListData(itemsV);
            roleSelectedList.setListData(selectedItemsV);
            setMmerCardinality(selectedItemsV.size());
            roleSelectedList.clearSelection();
        }
    }

    private void addMMER() {
        ListModel selectedListModel = roleSelectedList.getModel();
        if (selectedListModel.getSize() <= 1 || mmerCarList.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(xmlED, MSoDPolicyError3, errorHeader, JOptionPane.ERROR_MESSAGE);
            return;
        }
        ListModel roleListModel = roleList.getModel();
        Vector<String> roleItemV = new Vector<String>();
        for (int i = 0; i < roleListModel.getSize(); i++) {
            roleItemV.add((String) roleListModel.getElementAt(i));
        }
        Vector<RoleAttribute> selectedRoleItemV = new Vector<RoleAttribute>();
        for (int i = 0; i < selectedListModel.getSize(); i++) {
            roleItemV.add((String) selectedListModel.getElementAt(i));
            selectedRoleItemV.add(roleAttributeHash.get((String) selectedListModel.getElementAt(i)));
        }
        MutualExclusiveRoles mer = new MutualExclusiveRoles(selectedRoleItemV, Integer.parseInt((String) mmerCarList.getSelectedItem()));
        mmerHash.put(mer.displayString, mer);
        ListModel mmerListModel = mmerList.getModel();
        Vector<String> mmerItemsV = new Vector<String>();
        for (int i = 0; i < mmerListModel.getSize(); i++) {
            mmerItemsV.add((String) mmerListModel.getElementAt(i));
        }
        mmerItemsV.add(mer.displayString);
        mmerList.setListData(mmerItemsV);
        roleList.setListData(roleItemV);
        roleSelectedList.setListData(new Vector());
        setMmerCardinality(0);
    }

    private void delMMER() {
        int selectedIndex = mmerList.getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        ListModel mmerListModel = mmerList.getModel();
        Vector<String> mmerItemsV = new Vector<String>();
        for (int i = 0; i < mmerListModel.getSize(); i++) {
            if (i == selectedIndex) {
                mmerHash.remove((String) mmerListModel.getElementAt(i));
            } else {
                mmerItemsV.add((String) mmerListModel.getElementAt(i));
            }
        }
        mmerList.setListData(mmerItemsV);
    }

    private void addPrivs() {
        int[] selectedIndices = privList.getSelectedIndices();
        if (selectedIndices.length > 0) {
            ListModel listModel = privList.getModel();
            ListModel selectedListModel = privSelectedList.getModel();
            Vector itemsV = new Vector();
            Vector selectedItemsV = new Vector();
            for (int i = 0; i < listModel.getSize(); i++) {
                itemsV.add(listModel.getElementAt(i));
            }
            for (int i = 0; i < selectedListModel.getSize(); i++) {
                selectedItemsV.add(selectedListModel.getElementAt(i));
            }
            for (int j = 0; j < selectedIndices.length; j++) {
                itemsV.remove((String) listModel.getElementAt(selectedIndices[j]));
                selectedItemsV.add((String) listModel.getElementAt(selectedIndices[j]));
            }
            privList.setListData(itemsV);
            privSelectedList.setListData(selectedItemsV);
            setMmepCardinality(selectedItemsV.size());
            privList.clearSelection();
        }
    }

    private void removePrivs() {
        int[] selectedIndices = privSelectedList.getSelectedIndices();
        if (selectedIndices.length > 0) {
            ListModel listModel = privList.getModel();
            ListModel selectedListModel = privSelectedList.getModel();
            Vector itemsV = new Vector();
            Vector selectedItemsV = new Vector();
            for (int i = 0; i < listModel.getSize(); i++) {
                itemsV.add(listModel.getElementAt(i));
            }
            for (int i = 0; i < selectedListModel.getSize(); i++) {
                selectedItemsV.add(selectedListModel.getElementAt(i));
            }
            for (int j = 0; j < selectedIndices.length; j++) {
                itemsV.add((String) selectedListModel.getElementAt(selectedIndices[j]));
                selectedItemsV.remove((String) selectedListModel.getElementAt(selectedIndices[j]));
            }
            privList.setListData(itemsV);
            privSelectedList.setListData(selectedItemsV);
            setMmepCardinality(selectedItemsV.size());
            privSelectedList.clearSelection();
        }
    }

    private void setMmepCardinality(int maxCardinality) {
        setCardinality(mmepCarList, maxCardinality);
    }

    private void setMmerCardinality(int maxCardinality) {
        setCardinality(mmerCarList, maxCardinality);
    }

    /**
     * Removes all items from the given JComboBox and adds the 
     * integers between 2 and maxCardinality to it. If maxCardinality
     * is smaller than 2 this has simply the effect of clearing the JComboBox.
     * 
     * @param comboBox the JComboBox to work on
     * @param maxCardinality the given maximum cardinality
     */
    private static void setCardinality(JComboBox comboBox, int maxCardinality) {
        comboBox.removeAllItems();
        for (int i = 2; i <= maxCardinality; i++) {
            comboBox.addItem(String.valueOf(i));
        }
    }

    private void addMMEP() {
        ListModel selectedListModel = privSelectedList.getModel();
        if (logger.isTraceEnabled()) {
            logger.trace("addMMEP. privSelectedList model is:");
            for (int i = 0; i < selectedListModel.getSize(); i++) {
                logger.trace(selectedListModel.getElementAt(i));
            }
        }
        if (selectedListModel.getSize() <= 1 || mmepCarList.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(xmlED, MSoDPolicyError4, errorHeader, JOptionPane.ERROR_MESSAGE);
            return;
        }
        ListModel privListModel = privList.getModel();
        Vector<String> privItemV = new Vector<String>();
        for (int i = 0; i < privListModel.getSize(); i++) {
            privItemV.add((String) privListModel.getElementAt(i));
        }
        if (logger.isTraceEnabled()) {
            logger.trace("The elements in privList are:");
            for (String s : privItemV) {
                logger.trace(s);
            }
        }
        Vector<TargetAccess> selectedPrivItemV = new Vector<TargetAccess>();
        for (int i = 0; i < selectedListModel.getSize(); i++) {
            privItemV.add((String) selectedListModel.getElementAt(i));
            selectedPrivItemV.add(targetAccessHash.get((String) selectedListModel.getElementAt(i)));
        }
        MutualExclusivePrivileges mep = new MutualExclusivePrivileges(selectedPrivItemV, Integer.parseInt((String) mmepCarList.getSelectedItem()));
        mmepHash.put(mep.getDisplayString(), mep);
        if (logger.isTraceEnabled()) {
            logger.trace("The following has been added as an object in mmepHash");
            logger.trace(mep.toString());
        }
        ListModel mmepListModel = mmepList.getModel();
        Vector<String> mmepItemsV = new Vector<String>();
        for (int i = 0; i < mmepListModel.getSize(); i++) {
            mmepItemsV.add((String) mmepListModel.getElementAt(i));
        }
        mmepItemsV.add(mep.getDisplayString());
        mmepList.setListData(mmepItemsV);
        privList.setListData(privItemV);
        privSelectedList.setListData(new Vector());
        setMmepCardinality(0);
    }

    private void delMMEP() {
        int selectedIndex = mmepList.getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        ListModel mmepListModel = mmepList.getModel();
        Vector mmepItemsV = new Vector();
        for (int i = 0; i < mmepListModel.getSize(); i++) {
            if (i == selectedIndex) {
                mmepHash.remove((String) mmepListModel.getElementAt(i));
            } else {
                mmepItemsV.add(mmepListModel.getElementAt(i));
            }
        }
        mmepList.setListData(mmepItemsV);
    }

    private boolean newMSoD(boolean isNew, Element oldPolicyEle, String newID) {
        if (logger.isTraceEnabled()) {
            logger.trace("Starting newMSoD method. Arguments are:");
            logger.trace("isNew = " + isNew);
            logger.trace("oldPolicyEle = " + oldPolicyEle);
            logger.trace("newID = " + newID);
        }
        ListModel scopeListModel = scopeList.getModel();
        ListModel mmerListModel = mmerList.getModel();
        ListModel mmepListModel = mmepList.getModel();
        if (scopeListModel.getSize() == 0 || mmerListModel.getSize() + mmepListModel.getSize() == 0) {
            JOptionPane.showMessageDialog(xmlED, MSoDPolicyError5, errorHeader, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Element policyEle = createMSoDPolicyElement(newID, getBusinessContext());
        if (taskList1.getSelectedIndex() != -1 && !taskList1.getSelectedItem().equals("")) {
            String firstTaskDescription = (String) taskList1.getSelectedItem();
            policyEle.appendChild(createPrivilegeElement(XMLTags.MSoD_POLICY_FIRSTSTEP, firstTaskDescription));
        }
        if (taskList2.getSelectedIndex() != -1 && !taskList2.getSelectedItem().equals("")) {
            String lastTaskDescription = (String) taskList2.getSelectedItem();
            policyEle.appendChild(createPrivilegeElement(XMLTags.MSoD_POLICY_LASTSTEP, lastTaskDescription));
        }
        if (mmerListModel.getSize() > 0) {
            if (logger.isTraceEnabled()) {
                logger.trace("Size of mmerListModel = " + mmerListModel.getSize());
            }
            for (int j = 0; j < mmerListModel.getSize(); j++) {
                Element mmerEle = xmlED.DOM.createElement(XMLTags.MSoD_MMER_NODE);
                String mmerDesc = (String) mmerListModel.getElementAt(j);
                MutualExclusiveRoles mer = mmerHash.get(mmerDesc);
                for (RoleAttribute ra : mer.roles) {
                    Element mmerRoleEle = xmlED.DOM.createElement(XMLTags.ROLE_NODE);
                    mmerRoleEle.setAttribute(XMLTags.TYPE_ATTRIBUTE, ra.roleType);
                    if (ra.roleValue != null && (!ra.roleValue.equals(""))) {
                        mmerRoleEle.setAttribute(XMLTags.VALUE_ATTRIBUTE, ra.roleValue);
                    }
                    mmerEle.appendChild(mmerRoleEle);
                }
                mmerEle.setAttribute(XMLTags.MSoD_MMER_FORBIDDEN_ATT, String.valueOf(mer.cardinality));
                policyEle.appendChild(mmerEle);
            }
        }
        if (mmepListModel.getSize() > 0) {
            for (int j = 0; j < mmepListModel.getSize(); j++) {
                Element mmepEle = xmlED.DOM.createElement(XMLTags.MSoD_MMEP_NODE);
                String mmepDesc = (String) mmepListModel.getElementAt(j);
                MutualExclusivePrivileges mep = mmepHash.get(mmepDesc);
                for (TargetAccess ta : mep.targetAccess) {
                    Element mmepPrivEle = createPrivilegeElement(XMLTags.MSoD_PRIVILEGE_NODE, ta.target, getIDFromActionName(ta.action));
                    mmepEle.appendChild(mmepPrivEle);
                }
                mmepEle.setAttribute(XMLTags.MSoD_MMEP_FORBIDDEN_ATT, String.valueOf(mep.getCardinality()));
                policyEle.appendChild(mmepEle);
            }
        }
        if (isNew) {
            if (logger.isTraceEnabled()) {
                logger.trace("Adding a new MSoDPolicy element to the document");
            }
            xmlED.addItem(policyEle, xmlED.getElement(XMLTags.MSoD_POLICY_SET_NODE));
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("Replacing a MSoDPolicy element in the document");
            }
            xmlED.replaceNode(xmlED.getElement(XMLTags.MSoD_POLICY_SET_NODE), oldPolicyEle, policyEle);
        }
        return true;
    }

    /**
     * Gets the action name given the action id. The mapping is determined by the
     * ActionPolicy element in the document that is part of the XML editor.
     * 
     * @param actionName the name of the action
     * @return the id of the action for which the name is given
     */
    private String getIDFromActionName(String actionName) {
        Element actionPolicy = xmlED.getElement(XMLTags.ACTION_POLICY_NODE);
        return OldPolicyFormatCompat.getIdFromName(actionPolicy, actionName);
    }

    /**
     * Creates an MSoDPolicy element with the given ID and context name.
     * The element belongs to the document controlled by the XMLEditor reference
     * held in this object, but has not yet been added. 
     * 
     * @param id the value of the ID attribute for the element
     * @param contextName the value of the ContextName attribute for the element
     * @return the MSoDPolicy element with the given id and context name
     */
    private Element createMSoDPolicyElement(String id, String contextName) {
        Element msodPolicyElement = xmlED.getDocument().createElement(XMLTags.MSoD_POLICY_NODE);
        msodPolicyElement.setAttribute(XMLTags.ID_ATTRIBUTE, id);
        msodPolicyElement.setAttribute(XMLTags.CONTEXT_NAME, contextName);
        return msodPolicyElement;
    }

    /**
     * Creates an element conforming to the Privilege type. Its tag name 
     * is given by the tagName parameter, and its contents is given by the 
     * description in the string. More in particular this string consists
     * of two parts, the part before the tokenizer string represents the action
     * name, the part after the tokenizer string is the ID of the target domain.
     * The element will belong to the document controlled by the XML editor.
     * 
     * @param tagName the tag name of the to be created element 
     * @param privDescription a description from which the attributes of the child
     * elements can be derived
     * @return an element with the given tag name and with children whose ID attribute 
     * values are determined by the description string.
     * @see MSoDPolicyEditor#PRIVILEGE_TOKENIZER_FOR_DISPLAY
     */
    private Element createPrivilegeElement(String tagName, String privDescription) {
        String[] actionNameAndDomainID = privDescription.split(PRIVILEGE_TOKENIZER_FOR_DISPLAY);
        Element actionPolicy = xmlED.getElement(XMLTags.ACTION_POLICY_NODE);
        String actionID = OldPolicyFormatCompat.getIdFromName(actionPolicy, actionNameAndDomainID[0]);
        return createPrivilegeElement(tagName, actionNameAndDomainID[1], actionID);
    }

    /**
     * Creates an element that conforms to the PrivilegeType definition in the schema,
     * with the exception that only TargetDomain elements are used. Thus, an element
     * is created with the given tag name. This element has two child elements, namely
     * a TargetDomain element and an AllowedAction element. The element itself
     * is created by the document under control of the xml editor.
     * 
     * @param tagName the name of the element
     * @param targetDomainID the ID of the target domain element 
     * @param allowedActionID the ID of the allowed action
     * @return an element conforming to the given specifications
     */
    private Element createPrivilegeElement(String tagName, String targetDomainID, String allowedActionID) {
        if (logger.isTraceEnabled()) {
            logger.trace("Creating privilege element:");
            logger.trace("tag name = " + tagName);
            logger.trace("target domain = " + targetDomainID);
            logger.trace("allowed action id = " + allowedActionID);
        }
        Element privElement = xmlED.getDocument().createElement(tagName);
        privElement.appendChild(createTargetDomainElement(targetDomainID));
        privElement.appendChild(createAllowedActionElement(allowedActionID));
        return privElement;
    }

    /**
     * Creates a TargetDomain element given the ID attribute of 
     * a target domain. 
     * 
     * @param id the value of the ID attribute of the resulting element
     * @return a TargetDomain element with the given ID attribute value
     */
    private Element createTargetDomainElement(String id) {
        return createElementWithIDAttribute(XMLTags.TARGET_DOMAIN_NODE, id);
    }

    /**
     * Creates an AllowedAction element given the ID attribute of 
     * an action. 
     * 
     * @param id the value of the ID attribute of the resulting element
     * @return an AllowedAction element with the given ID attribute value
     */
    private Element createAllowedActionElement(String id) {
        return createElementWithIDAttribute(XMLTags.ALLOWED_ACTION_NODE, id);
    }

    /**
     * Creates an element with an ID attribute. The tagname and the value
     * of the ID attribute are passed in as parameters. The element is 
     * created by the document under control of the XML editor.
     * 
     * @param tagName the tag name of the element to be created
     * @param id the value of the ID attribute 
     * @return an element with the given tag name and ID attribute value
     */
    private Element createElementWithIDAttribute(String tagName, String id) {
        Element element = xmlED.getDocument().createElement(tagName);
        element.setAttribute(XMLTags.ID_ATTRIBUTE, id);
        return element;
    }

    public boolean confirmMSoD(boolean isNewMSoD, String oldID, String newID) {
        NodeList policySet = xmlED.DOM.getElementsByTagName(XMLTags.MSoD_POLICY_SET_NODE);
        int policyNum = policySet.getLength();
        if (policySet.getLength() == 0) {
            try {
                Element child = xmlED.DOM.createElement(XMLTags.MSoD_POLICY_SET_NODE);
                xmlED.addItem(child, xmlED.DOM.getDocumentElement());
                return newMSoD(true, null, newID);
            } catch (DOMException e) {
                JOptionPane.showMessageDialog(xmlED, MSoDPolicyError6, errorHeader, JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        NodeList policyEleList = xmlED.DOM.getElementsByTagName(XMLTags.MSoD_POLICY_NODE);
        if (policyEleList.getLength() == 0) {
            return newMSoD(true, null, newID);
        } else {
            ListModel scopeListModel = scopeList.getModel();
            ListModel mmerListModel = mmerList.getModel();
            ListModel mmepListModel = mmepList.getModel();
            if (scopeListModel.getSize() == 0 || mmerListModel.getSize() + mmepListModel.getSize() == 0) {
                JOptionPane.showMessageDialog(xmlED, MSoDPolicyError5, errorHeader, JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (isNewMSoD) {
                return newMSoD(isNewMSoD, null, newID);
            } else {
                int i = 0;
                for (; i < policyEleList.getLength(); i++) {
                    if (((Element) policyEleList.item(i)).getAttribute(XMLTags.ID_ATTRIBUTE).equals(oldID)) {
                        break;
                    }
                }
                return newMSoD(isNewMSoD, (Element) policyEleList.item(i), newID);
            }
        }
    }

    public boolean deleteMSoD() {
        ListModel scopeListModel = scopeList.getModel();
        if (scopeListModel.getSize() == 0) {
            return false;
        }
        NodeList policyEleList = xmlED.DOM.getElementsByTagName(XMLTags.MSoD_POLICY_NODE);
        if (policyEleList.getLength() == 0) {
            return true;
        }
        String bc = "";
        for (int i = 0; i < scopeListModel.getSize(); i++) {
            bc += (String) scopeListModel.getElementAt(i) + BUSINESS_CONTEXT_TOKENIZER;
        }
        bc = bc.substring(0, bc.length() - BUSINESS_CONTEXT_TOKENIZER.length());
        boolean isPolicyExist = false;
        for (int i = 0; i < policyEleList.getLength(); i++) {
            if (((Element) policyEleList.item(i)).getAttribute(XMLTags.CONTEXT_NAME).equals(bc)) {
                xmlED.deleteItem((Element) policyEleList.item(i), (Element) policyEleList.item(i).getParentNode());
                isPolicyExist = true;
                break;
            }
        }
        return isPolicyExist;
    }

    public void setMSoDID(String msodID) {
        this.msodID = msodID;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addScope) {
            addScope();
        }
        if (e.getSource() == editScope) {
            replaceScope();
        }
        if (e.getSource() == delScope) {
            deleteScope();
        }
        if (e.getSource() == scopeValueSpec) {
            scopeValueOption1.setSelected(false);
            scopeValueOption2.setSelected(false);
            scopeValueOption3.setSelected(true);
        }
        if (e.getSource() == addRoles) {
            addRoles();
        }
        if (e.getSource() == delRoles) {
            removeRoles();
        }
        if (e.getSource() == addMMER) {
            addMMER();
        }
        if (e.getSource() == delMMER) {
            delMMER();
        }
        if (e.getSource() == addPrivs) {
            addPrivs();
        }
        if (e.getSource() == delPrivs) {
            removePrivs();
        }
        if (e.getSource() == addMMEP) {
            addMMEP();
        }
        if (e.getSource() == delMMEP) {
            delMMEP();
        }
    }

    public void stateChanged(ChangeEvent e) {
        scopeValueSpec.setEnabled(scopeValueOption3.isSelected());
    }

    private String getBusinessContext() {
        ListModel scopeListModel = scopeList.getModel();
        String bc = "";
        for (int i = 0; i < scopeListModel.getSize(); i++) {
            bc += (String) scopeListModel.getElementAt(i) + BUSINESS_CONTEXT_TOKENIZER;
        }
        bc = bc.substring(0, bc.length() - BUSINESS_CONTEXT_TOKENIZER.length());
        return bc;
    }

    public void valueChanged(ListSelectionEvent e) {
        String selectedValue;
        if (e.getSource() == scopeList) {
            selectedValue = (String) scopeList.getSelectedValue();
            if (selectedValue != null) {
                String selectedScopeType = selectedValue.substring(0, selectedValue.indexOf("="));
                String selectedScopeValue = selectedValue.substring(selectedValue.indexOf("=") + 1);
                scopeType.setText(selectedScopeType);
                if (selectedScopeValue.equals(scopeValueOption1Text)) {
                    scopeValueOption1.setSelected(true);
                    scopeValueSpec.setSelectedItem("");
                } else if (selectedScopeValue.equals(scopeValueOption2Text)) {
                    scopeValueOption2.setSelected(true);
                    scopeValueSpec.setSelectedItem("");
                } else {
                    scopeValueOption3.setSelected(true);
                    scopeValueSpec.setSelectedItem(selectedScopeValue);
                }
            }
        }
    }
}

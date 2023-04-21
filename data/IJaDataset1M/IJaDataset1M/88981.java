package issrg.editor2;

import issrg.utils.gui.xml.NodeItemList;
import issrg.utils.gui.xml.NodeSelectionEvent;
import issrg.utils.gui.xml.ReadablePERMISXML;
import issrg.utils.gui.xml.XMLEditor;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import org.w3c.dom.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classname:    SubRoles
 *
 * Description:  This class contains the SubRoles component used for the
 *               RoleHierarchyPolicy.
 *
 * Date:         26th November 2005
 *
 * @author       Christian Azzopardi
 *
 */
public class SubRoles extends NodeItemList implements ActionListener {

    JLabel lblSelectedValue;

    public JLabel supLbl, availLbl;

    RoleSelectionList availRolesList;

    public JPanel tb;

    JPanel oldDisplay, availableRolesPanel;

    JPanel tmpPanel1, tmpPanel2, tmpPanel3;

    JPanel panelInPanel1;

    public JPanel txtSupPanel;

    ResourceBundle rbl = ResourceBundle.getBundle("issrg/editor2/PEComponent_i18n");

    String addDelListClearPopup = rbl.getString("AddDelListClearPopup");

    String subRoleButton1 = rbl.getString("SubRoleButton1");

    String subRoleButton2 = rbl.getString("SubRoleButton2");

    String subRoleLabel1 = rbl.getString("SubRoleLabel1");

    String subRolePanel1 = rbl.getString("SubRolePanel1");

    String subRolePanel2 = rbl.getString("SubRolePanel2");

    String subRoleError1 = rbl.getString("SubRoleError1");

    String subRoleError2 = rbl.getString("SubRoleError2");

    /**
     * Creates an instance of the SubRoles component. 
     * <p> 
     * Constructs the GUI. 
     * <p>
     * Adds listeners to listen to XMLChangeEvents and NodeSelectionChangeEvents.
     *
     * @param that   the XMLEditor that the SubRoles ccommponent will refer to.
     */
    public SubRoles(XMLEditor that) {
        super(that);
        availRolesList = new RoleSelectionList(xmlED);
        availableRolesPanel = availRolesList.getContentPanel();
        panelInPanel1.setLayout(new GridBagLayout());
        txtSupPanel = new JPanel();
        txtSupPanel.setLayout(new GridBagLayout());
        addComponent(supLbl, txtSupPanel, 0, 0, 1, 1, GridBagConstraints.FIRST_LINE_END, 0, 0, GridBagConstraints.NONE);
        constraints.insets = new Insets(0, 0, 0, 0);
        tmpPanel1 = removeAddButton();
        tmpPanel2 = removeDeleteButton();
        tmpPanel3 = removeReplaceButton();
        addComponent(tmpPanel1, panelInPanel1, 1, 1, 1, 1, GridBagConstraints.LINE_START, 1, 0, GridBagConstraints.HORIZONTAL);
        addComponent(tmpPanel2, panelInPanel1, 2, 1, 1, 1, GridBagConstraints.LINE_START, 1, 0, GridBagConstraints.HORIZONTAL);
        addComponent(panelInPanel1, tb, 2, 2, 1, 1, GridBagConstraints.LINE_START, 0.1, 0.05, GridBagConstraints.BOTH);
        addComponent(txtSupPanel, tb, 0, 1, 3, 1, GridBagConstraints.LINE_START, 0, 0, GridBagConstraints.BOTH);
        addComponent(oldDisplay, tb, 1, 1, 1, 5, GridBagConstraints.LINE_START, 1, 0.5, GridBagConstraints.BOTH);
        addComponent(availableRolesPanel, tb, 1, 3, 1, 5, GridBagConstraints.LINE_START, 1, 0.5, GridBagConstraints.BOTH);
        this.setLayout(new BorderLayout());
        ReadablePERMISXML viewXML = new ReadablePERMISXML((XMLEditor) that, "RoleHierarchyPolicy", "text/html");
        viewXML.setPreferredSize(new Dimension(80, 100));
        tb.setMinimumSize(new Dimension(290, 100));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tb, viewXML);
        splitPane.setResizeWeight(0);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        add(splitPane, BorderLayout.CENTER);
        setCaption("ADD_BUTTON", subRoleButton1);
        setCaption("DELETE_BUTTON", subRoleButton2);
        setCaption("SUPERIOR_LABEL", subRoleLabel1);
        setCaption("SUBROLE_SUBORDINATES_PANEL", subRolePanel1);
        setCaption("SUBROLE_AVAILABLE_PANEL", subRolePanel2);
        setCaption("CLEAR_POP_MENU", addDelListClearPopup);
        availRolesList.addNodeChangeListener(this);
        addButton.setEnabled(false);
        xmlED.addXMLChangeListener(this);
        this.setEnabled(false);
    }

    public JPanel getContentPanel() {
        oldDisplay = super.getContentPanel();
        lblSelectedValue = new JLabel();
        availLbl = new JLabel();
        supLbl = new JLabel();
        tb = new JPanel();
        tmpPanel1 = new JPanel();
        tmpPanel2 = new JPanel();
        tmpPanel3 = new JPanel();
        panelInPanel1 = new JPanel();
        tb.setLayout(new GridBagLayout());
        return tb;
    }

    /**
     * Invoked when a user clicks on the '<' button. The '<' Button is enabled
     * when users select an available role value that can be a subrole to the 
     * selected superior role.
     *
     * This will add a role to the subroles list. 
     */
    public void addItem() {
        RoleHierarchyPolicy.roleValuesCB.removeActionListener(this);
        if (getParentNode() != null && availRolesList.getSelectedNode() != null) {
            Element supRole = (Element) getParentNode();
            Element subRole = xmlED.DOM.createElement("SubRole");
            subRole.setAttribute("Value", ((Element) availRolesList.getSelectedNode()).getAttribute("Value"));
            String selItem = (String) RoleHierarchyPolicy.roleValuesCB.getSelectedItem();
            xmlED.addItem(subRole, supRole);
            RoleHierarchyPolicy.roleValuesCB.setSelectedItem(selItem);
        }
        RoleHierarchyPolicy.roleValuesCB.addActionListener(this);
    }

    /**
     * Invoked when a user clicks on the '>' button. The '>' Button is enabled
     * when users select a sub role value.
     * <p>
     * This will delete the role value from the list of subroles of a particular
     * selected superior Role Value. 
     */
    public void deleteItem() {
        RoleHierarchyPolicy.roleValuesCB.removeActionListener(this);
        if (getParentNode() != null && this.getSelectedNode() != null) {
            String selItem = (String) RoleHierarchyPolicy.roleValuesCB.getSelectedItem();
            xmlED.deleteItem((Element) getSelectedNode(), (Element) getParentNode());
            RoleHierarchyPolicy.roleValuesCB.setSelectedItem(selItem);
        }
        RoleHierarchyPolicy.roleValuesCB.addActionListener(this);
    }

    public void replaceItem() {
        if (getParentNode() != null && this.getSelectedNode() != null && availRolesList.getSelectedNode() != null) {
            String attrib[] = { "Value" };
            String value[] = { ((Element) availRolesList.getSelectedNode()).getAttribute("Value") };
            xmlED.setAttributeValue((Element) getSelectedNode(), attrib, value);
        }
    }

    /**
     * Refreshes the GUI of this component. Normally called when an 
     * XMLChangeEvent has been fired.
     */
    public void refreshView() {
        if (getParentNode() == null) {
            lblSelectedValue.setText("");
            setNodeList(null, null);
        } else {
            lblSelectedValue.setText("<html><b> " + ((Element) getParentNode()).getAttribute("Value") + " </b></html>");
            NodeList nlist = ((Element) getParentNode()).getElementsByTagName("SubRole");
            Node n;
            String values[] = new String[nlist.getLength()];
            for (int i = 0; i < nlist.getLength(); i++) {
                n = nlist.item(i);
                values[i] = ((Element) n).getAttribute("Value");
            }
            setNodeList(nlist, values);
        }
        availRolesList.refreshView();
    }

    /**
    *  Sets the text the component will show.  
    *
    *  @param internalName         The internal name used in the program.     
    *  @param internationalName    The international name to set the text to.
    */
    public void setCaption(String internalName, String internationalName) {
        if (internalName.equals("SELECTEDVALUE_LABEL")) {
            lblSelectedValue.setText(internationalName);
        } else if (internalName.equals("AVAILABLE_LABEL")) {
            availLbl.setText(internationalName);
        } else if (internalName.equals("SUPERIOR_LABEL")) {
            supLbl.setText(internationalName);
        } else if (internalName.equals("SUBROLE_SUBORDINATES_PANEL")) {
            oldDisplay.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), internationalName));
        } else if (internalName.equals("SUBROLE_AVAILABLE_PANEL")) {
            availableRolesPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), internationalName));
        } else super.setCaption(internalName, internationalName);
    }

    /**
     * When a node selection has been made, from the role values drop down list, 
     * this component will refresh the available roles list to display the roles
     * that can be subroles to the selected node.
     */
    public void NodeSelectionChanged(NodeSelectionEvent ev) {
        if (ev.getSource() == availRolesList) {
            if (ev.getSelectedNode() != null) {
                addButton.setEnabled(true);
            } else {
                addButton.setEnabled(false);
            }
        } else {
            if (ev.getSelectedNode() == null) {
                this.setEnabled(false);
            } else {
                this.setEnabled(true);
            }
            setParentNode(ev.getSelectedNode());
        }
    }

    public void setParentNode(Node n) {
        availRolesList.setParentNode(n);
        super.setParentNode(n);
    }

    /**
     * Sets the component to be enabled or not. Will Grey all the components 
     * that make it up when disabed.
     *
     * @param enabled  boolean value to set.
     */
    public void setEnabled(boolean enabled) {
        this.listBox.setEnabled(enabled);
        this.availRolesList.listBox.setEnabled(enabled);
        if (enabled) {
            this.listBox.setBackground(Color.WHITE);
            this.availRolesList.listBox.setBackground(Color.WHITE);
        } else {
            this.listBox.setBackground(new Color(236, 233, 216));
            this.availRolesList.listBox.setBackground(new Color(236, 233, 216));
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof RoleValuesComboBox) {
            if (((RoleValuesComboBox) e.getSource()).getSelectedIndex() != -1) RoleHierarchyPolicy.roleValues.setSelectedNode(RoleHierarchyPolicy.roleValuesCB.correspondingNodes[((RoleValuesComboBox) e.getSource()).getSelectedIndex()]);
        } else super.actionPerformed(e);
    }
}

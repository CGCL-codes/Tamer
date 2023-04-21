package issrg.policywizard;

import issrg.editor2.RoleValuesComboBox;
import issrg.utils.gui.xml.NodeSelectionEvent;
import issrg.utils.gui.xml.NodeSelectionListener;
import issrg.utils.gui.xml.ReadablePERMISXML;
import issrg.utils.gui.xml.ReadableXML;
import issrg.utils.gui.xml.XMLEditor;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.w3c.dom.Element;

/**
 * Classname:    RoleHierarchyPolicy
 *
 * Description:  This class contains the Role Hierarchy Policy components to be placed
 *               in the Wizard. This class loads the new RoleTypes and RoleValues classes
 *               defined in the issrg.policywizard package
 * 
 * Date:         26th November 2005
 *
 * @author       Christian Azzopardi
 *
 */
public class RoleHierarchyPolicy extends JPanel implements NodeSelectionListener {

    JPanel roleTypeEditPanel;

    JPanel roleValueEditPanel;

    public static JPanel roleHierarchyEditPanel;

    JPanel readableRoleHierarchy;

    JPanel viewDisplay;

    public JPanel tb;

    public JPanel top;

    private GridBagConstraints constraints;

    ReadableXML viewXML;

    public static RoleTypes roleTypes;

    public static RoleValues roleValues;

    public static SubRoles subRoles;

    public static RoleValuesComboBox roleValuesCB;

    ResourceBundle rbl = ResourceBundle.getBundle("issrg/editor2/PEComponent_i18n");

    String roleHierarchyPanel1 = rbl.getString("RoleHierarchyPanel1");

    String roleHierarchyPanel2 = rbl.getString("RoleHierarchyPanel2");

    String roleHierarchyPanel3 = rbl.getString("RoleHierarchyPanel3");

    String roleHierarchyPanel4 = rbl.getString("RoleHierarchyPanel4");

    public RoleHierarchyPolicy(XMLEditor that) {
        roleTypes = new RoleTypes((XMLEditor) that);
        roleValues = new RoleValues((XMLEditor) that);
        subRoles = new SubRoles((XMLEditor) that);
        roleValuesCB = new RoleValuesComboBox((XMLEditor) that);
        setLayout(new BorderLayout());
        roleTypeEditPanel = new JPanel(new BorderLayout());
        roleValueEditPanel = new JPanel(new BorderLayout());
        roleHierarchyEditPanel = new JPanel(new BorderLayout());
        readableRoleHierarchy = new JPanel(new BorderLayout());
        viewDisplay = new JPanel(new BorderLayout());
        top = new JPanel(new GridBagLayout());
        JPanel bottom = new JPanel(new BorderLayout());
        tb = new JPanel(new BorderLayout());
        constraints = new GridBagConstraints();
        roleTypes.addNodeChangeListener(roleValues);
        roleTypes.addItemAddedListener(roleValues);
        roleValues.addNodeChangeListener(subRoles);
        roleValues.addNodeChangeListener(this);
        roleValuesCB.addActionListener(subRoles);
        roleTypes.addNodeChangeListener(roleValuesCB);
        subRoles.constraints.insets = new Insets(0, 5, 0, 60);
        subRoles.addComponent(roleValuesCB, subRoles.txtSupPanel, 0, 1, 1, 1, GridBagConstraints.FIRST_LINE_START, 0.4, 0, GridBagConstraints.HORIZONTAL);
        roleTypeEditPanel.add(roleTypes.getContentPanel(), BorderLayout.CENTER);
        roleValueEditPanel.add(roleValues.getContentPanel(), BorderLayout.CENTER);
        roleHierarchyEditPanel.add(subRoles, BorderLayout.CENTER);
        roleTypeEditPanel.setPreferredSize(new Dimension(200, 160));
        roleValueEditPanel.setPreferredSize(new Dimension(200, 160));
        addComponent(roleTypeEditPanel, top, 0, 0, 1, 1, GridBagConstraints.FIRST_LINE_START, 1, 1, GridBagConstraints.BOTH);
        addComponent(roleValueEditPanel, top, 0, 1, 1, 1, GridBagConstraints.FIRST_LINE_END, 1, 1, GridBagConstraints.BOTH);
        addComponent(roleHierarchyEditPanel, top, 1, 0, 2, 1, GridBagConstraints.PAGE_START, 1, 1, GridBagConstraints.BOTH);
        viewXML = new ReadablePERMISXML((XMLEditor) that, "RoleHierarchyPolicy", "text/html");
        viewXML.setPreferredSize(new java.awt.Dimension(580, 130));
        readableRoleHierarchy.add(viewXML, BorderLayout.CENTER);
        bottom.add(readableRoleHierarchy, BorderLayout.CENTER);
        top.setMinimumSize(new Dimension(100, 200));
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        tb.add(splitPane, BorderLayout.CENTER);
        add(tb);
        viewXML.setXMLEditor(that);
        setCaption("ROLETYPE_PANEL", roleHierarchyPanel1);
        setCaption("ROLEVALUE_PANEL", roleHierarchyPanel2);
        setCaption("SUBROLE_PANEL", roleHierarchyPanel3);
        setCaption("READABLEXML_PANEL", roleHierarchyPanel4);
    }

    public void setCaption(String internalName, String internationalName) {
        if (internalName.equals("ROLETYPE_PANEL")) {
            roleTypeEditPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), roleHierarchyPanel1));
        } else if (internalName.equals("ROLEVALUE_PANEL")) {
            roleValueEditPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), roleHierarchyPanel2));
        } else if (internalName.equals("SUBROLE_PANEL")) {
            roleHierarchyEditPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), roleHierarchyPanel3));
        } else if (internalName.equals("READABLEXML_PANEL")) {
            readableRoleHierarchy.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), roleHierarchyPanel4));
        }
    }

    public void addComponent(Component component, JPanel pane, int row, int column, int width, int height, int anchor, double weightx, double weighty, int fill) {
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

    public void NodeSelectionChanged(NodeSelectionEvent ev) {
        if (ev.getSource() == roleValues) {
            if (roleValues.getSelectedNode() == null) {
                return;
            } else {
                roleValuesCB.setSelectedItem(((Element) roleValues.getSelectedNode()).getAttribute("Value"));
            }
        }
    }
}

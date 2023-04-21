package org.mitre.rt.client.ui.applications;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import org.mitre.rt.client.core.MetaManager;
import org.mitre.rt.client.ui.groups.GroupsTablePanel;
import org.mitre.rt.client.xml.ApplicationHelper;
import org.mitre.rt.client.xml.VersionedItemTypeHelper;
import org.mitre.rt.rtclient.ApplicationType;
import org.mitre.rt.rtclient.GroupType;
import org.mitre.rt.rtclient.UserType;

/**
 *
 * @author  BWORRELL
 */
public class GroupsTab extends javax.swing.JPanel {

    private static Logger logger = Logger.getLogger(GroupsTab.class.getPackage().getName());

    private ApplicationType parent = null;

    private GroupsTabHeaderPanel headerPanel = null;

    private GroupsTablePanel table = null;

    /** Creates new form GroupsTab */
    public GroupsTab() {
        initComponents();
        initTable();
        initHeaderPanel();
    }

    public GroupsTab(ApplicationType app) {
        initComponents();
        setParent(app);
        initTable(app);
        initHeaderPanel();
    }

    public void setParent(ApplicationType app) {
        parent = app;
    }

    private void initHeaderPanel() {
        headerPanel = new GroupsTabHeaderPanel(table);
        headerPanel.addPropertyChangeListener("table_data_changed", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                totalCntLabel.setText(Integer.toString(table.getRowCnt()));
            }
        });
        panelNorth.add(headerPanel);
    }

    private void initTable(ApplicationType app) {
        table = new GroupsTablePanel(app);
        panelCenter.add(table);
        setSelectionListener();
        totalCntLabel.setText(Integer.toString(table.getRowCnt()));
    }

    private void initTable() {
        table = new GroupsTablePanel();
        panelCenter.add(table);
        setSelectionListener();
    }

    private void setSelectionListener() {
        ListSelectionListener lsl = new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                logger.debug("Selection Change: " + e.getFirstIndex());
                if (e.getValueIsAdjusting() == false) {
                    updateButtons();
                }
            }
        };
        table.addSelectionListener(lsl);
    }

    private void updateButtons() {
        logger.debug("Updating Buttons");
        UserType user = MetaManager.getAuthenticatedUser();
        GroupType selectedRec = table.getSelectedGroup();
        if (selectedRec != null) {
            boolean canEditRec = VersionedItemTypeHelper.userCanEdit(selectedRec, parent, user);
            boolean canEditApp = ApplicationHelper.canEdit(parent, user);
            boolean hasEditorRole = ApplicationHelper.isEditor(parent, user);
            headerPanel.setAddEnabled(canEditApp || hasEditorRole);
            headerPanel.setDeleteEnabled(canEditRec || canEditApp);
            headerPanel.setEditEnabled(canEditRec || canEditApp);
        } else {
            headerPanel.setDeleteEnabled(false);
            headerPanel.setEditEnabled(false);
        }
    }

    public void updateTable(ApplicationType app) {
        setParent(app);
        table.updateTable(app);
        headerPanel.initAddButton(table);
        totalCntLabel.setText(Integer.toString(table.getRowCnt()));
    }

    public GroupsTabHeaderPanel getHeaderPanel() {
        return headerPanel;
    }

    private void initComponents() {
        panelNorth = new javax.swing.JPanel();
        panelCenter = new javax.swing.JPanel();
        panelSouth = new javax.swing.JPanel();
        totalLabel = new javax.swing.JLabel();
        totalCntLabel = new javax.swing.JLabel();
        setLayout(new java.awt.BorderLayout());
        panelNorth.setMaximumSize(new java.awt.Dimension(32767, 27));
        panelNorth.setMinimumSize(new java.awt.Dimension(50, 27));
        panelNorth.setPreferredSize(new java.awt.Dimension(100, 27));
        panelNorth.setLayout(new java.awt.BorderLayout());
        add(panelNorth, java.awt.BorderLayout.NORTH);
        panelCenter.setLayout(new java.awt.BorderLayout());
        add(panelCenter, java.awt.BorderLayout.CENTER);
        panelSouth.setMaximumSize(new java.awt.Dimension(32767, 20));
        panelSouth.setMinimumSize(new java.awt.Dimension(0, 20));
        panelSouth.setPreferredSize(new java.awt.Dimension(0, 20));
        panelSouth.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        totalLabel.setText("Total:");
        totalLabel.setMaximumSize(new java.awt.Dimension(28, 14));
        totalLabel.setMinimumSize(new java.awt.Dimension(28, 14));
        totalLabel.setPreferredSize(new java.awt.Dimension(28, 14));
        panelSouth.add(totalLabel);
        totalCntLabel.setText("0");
        panelSouth.add(totalCntLabel);
        add(panelSouth, java.awt.BorderLayout.SOUTH);
    }

    private javax.swing.JPanel panelCenter;

    private javax.swing.JPanel panelNorth;

    private javax.swing.JPanel panelSouth;

    private javax.swing.JLabel totalCntLabel;

    private javax.swing.JLabel totalLabel;
}

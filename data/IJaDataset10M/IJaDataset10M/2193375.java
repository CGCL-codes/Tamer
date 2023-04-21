package org.mitre.rt.client.ui.applications;

import org.mitre.rt.client.core.MetaManager;
import org.mitre.rt.client.ui.profile.ProfilesTablePanel;
import org.mitre.rt.client.xml.ApplicationHelper;
import org.mitre.rt.rtclient.ApplicationType;
import org.mitre.rt.rtclient.UserType;

/**
 *
 * @author  BAKERJ
 */
public class ProfilesTabHeaderPanel extends javax.swing.JPanel {

    private ProfilesTablePanel profilesTable = null;

    /** Creates new form ProfilesTabHeaderPanel */
    public ProfilesTabHeaderPanel(ProfilesTablePanel profileTable) {
        setProfileTable(profileTable);
        initComponents();
        initAddButton(profileTable);
    }

    public ProfilesTabHeaderPanel() {
        initComponents();
        addjButton.setEnabled(false);
    }

    public void initAddButton(ProfilesTablePanel recTab) {
        ApplicationType app = recTab.getApplication();
        UserType user = MetaManager.getAuthenticatedUser();
        boolean canEditApp = ApplicationHelper.canEdit(app, user);
        boolean isAppEditor = ApplicationHelper.isEditor(app, user);
        addjButton.setEnabled(canEditApp || isAppEditor);
    }

    private void initComponents() {
        buttonsjPanel = new javax.swing.JPanel();
        addjButton = new javax.swing.JButton();
        editjButton = new javax.swing.JButton();
        deletejButton = new javax.swing.JButton();
        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        setMaximumSize(new java.awt.Dimension(2147483647, 27));
        setMinimumSize(new java.awt.Dimension(4, 27));
        setPreferredSize(new java.awt.Dimension(4, 27));
        setLayout(new java.awt.BorderLayout());
        buttonsjPanel.setBackground(new java.awt.Color(255, 255, 255));
        buttonsjPanel.setPreferredSize(new java.awt.Dimension(252, 23));
        buttonsjPanel.setLayout(new javax.swing.BoxLayout(buttonsjPanel, javax.swing.BoxLayout.X_AXIS));
        addjButton.setBackground(new java.awt.Color(255, 255, 255));
        addjButton.setText("Add");
        addjButton.setEnabled(false);
        addjButton.setMaximumSize(new java.awt.Dimension(63, 23));
        addjButton.setMinimumSize(new java.awt.Dimension(63, 23));
        addjButton.setPreferredSize(new java.awt.Dimension(63, 23));
        addjButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addjButtonActionPerformed(evt);
            }
        });
        buttonsjPanel.add(addjButton);
        editjButton.setBackground(new java.awt.Color(255, 255, 255));
        editjButton.setText("Edit");
        editjButton.setEnabled(false);
        editjButton.setMaximumSize(new java.awt.Dimension(63, 23));
        editjButton.setMinimumSize(new java.awt.Dimension(63, 23));
        editjButton.setOpaque(false);
        editjButton.setPreferredSize(new java.awt.Dimension(63, 23));
        editjButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editjButtonActionPerformed(evt);
            }
        });
        buttonsjPanel.add(editjButton);
        deletejButton.setBackground(new java.awt.Color(255, 255, 255));
        deletejButton.setText("Delete");
        deletejButton.setEnabled(false);
        deletejButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletejButtonActionPerformed(evt);
            }
        });
        buttonsjPanel.add(deletejButton);
        add(buttonsjPanel, java.awt.BorderLayout.EAST);
    }

    private void addjButtonActionPerformed(java.awt.event.ActionEvent evt) {
        profilesTable.doCreateItem();
    }

    private void editjButtonActionPerformed(java.awt.event.ActionEvent evt) {
        profilesTable.doEditItem();
    }

    private void deletejButtonActionPerformed(java.awt.event.ActionEvent evt) {
        profilesTable.doDeleteItem();
    }

    public void setAddEnabled(boolean enabled) {
        addjButton.setEnabled(enabled);
    }

    public void setEditEnabled(boolean enabled) {
        editjButton.setEnabled(enabled);
    }

    public void setDeleteEnabled(boolean enabled) {
        deletejButton.setEnabled(enabled);
    }

    public void enableAllButtons(boolean enable) {
        setAddEnabled(enable);
        setDeleteEnabled(enable);
        setEditEnabled(enable);
    }

    private javax.swing.JButton addjButton;

    private javax.swing.JPanel buttonsjPanel;

    private javax.swing.JButton deletejButton;

    private javax.swing.JButton editjButton;

    public void setProfileTable(ProfilesTablePanel profileTable) {
        profilesTable = profileTable;
    }
}

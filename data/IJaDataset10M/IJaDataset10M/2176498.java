package org.verus.ngl.client.administration.technicalprocessing;

import java.util.Vector;
import org.verus.ngl.client.components.NGLUtilities;
import org.verus.ngl.utilities.NGLXMLUtility;

/**
 *
 * @author  PIXEL1
 */
public class WorksheetGroupsDialog extends javax.swing.JDialog {

    private javax.swing.table.DefaultTableModel dtmodelTableGroupNames = null;

    /** Creates new form WorksheetGroupsDialog */
    public WorksheetGroupsDialog(java.awt.Frame frame) {
        super(frame);
        initComponents();
        inits();
    }

    public WorksheetGroupsDialog(javax.swing.JDialog dialog) {
        super(dialog);
        initComponents();
        inits();
    }

    public void inits() {
        this.dtmodelTableGroupNames = new javax.swing.table.DefaultTableModel(new Object[] { org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Group"), org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Groupdescription") }, 0) {

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        };
        tableGroupNames.setModel(dtmodelTableGroupNames);
        tableGroupNames.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableGroupNames.getTableHeader().setReorderingAllowed(false);
        tableGroupNames.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableGroupNames.getColumnModel().getColumn(1).setPreferredWidth(400);
        getGroupNames();
    }

    private void initComponents() {
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableGroupNames = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        bnOk = new javax.swing.JButton();
        bnCancel = new javax.swing.JButton();
        setTitle(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Groupnames"));
        setModal(true);
        jPanel3.setLayout(new java.awt.BorderLayout());
        jScrollPane4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tableGroupNames.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane4.setViewportView(tableGroupNames);
        jPanel3.add(jScrollPane4, java.awt.BorderLayout.CENTER);
        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bnOk.setMnemonic('o');
        bnOk.setText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Ok"));
        bnOk.setToolTipText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Ok"));
        bnOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnOkActionPerformed(evt);
            }
        });
        jPanel4.add(bnOk);
        bnCancel.setMnemonic('c');
        bnCancel.setText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Cancel"));
        bnCancel.setToolTipText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Cancel"));
        bnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCancelActionPerformed(evt);
            }
        });
        jPanel4.add(bnCancel);
        getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);
    }

    private void bnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void bnOkActionPerformed(java.awt.event.ActionEvent evt) {
        this.OK_Click = 1;
        this.groupName = dtmodelTableGroupNames.getValueAt(tableGroupNames.getSelectedRow(), 0).toString();
        this.groupDesc = dtmodelTableGroupNames.getValueAt(tableGroupNames.getSelectedRow(), 1).toString();
        this.dispose();
    }

    private void getGroupNames() {
        try {
            org.jdom.Element root = null;
            org.jdom.Element libraryID = new org.jdom.Element("LibraryID");
            root = org.verus.ngl.utilities.NGLXMLUtility.getInstance().getRequestDataRoot("MARCWorksheetHandler", "6");
            libraryID.setText(NGLUtilities.getInstance().getLibraryId());
            root.addContent(libraryID);
            String xml = NGLXMLUtility.getInstance().generateXML(root);
            String xmlResp = org.verus.ngl.client.components.ServletConnector.getInstance().sendRequest(xml);
            System.out.println("=======xmlResp==========" + xmlResp);
            if (xmlResp != null || !xmlResp.equals("")) {
                dtmodelTableGroupNames.setRowCount(0);
                org.jdom.Element response = NGLXMLUtility.getInstance().getRootElementFromXML(xmlResp);
                java.util.List listGroup = response.getChildren("Sheet");
                Vector groupInfo = new Vector();
                for (int i = 0; i < listGroup.size(); i++) {
                    org.jdom.Element group = (org.jdom.Element) listGroup.get(i);
                    String groupName = group.getChildText("Group");
                    if (!groupInfo.contains(groupName)) {
                        groupInfo.addElement(groupName);
                        Vector addRow = new Vector();
                        addRow.addElement(groupName);
                        addRow.addElement(group.getChildText("GroupDescription"));
                        dtmodelTableGroupNames.addRow(addRow);
                    }
                }
                tableGroupNames.setRowSelectionInterval(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private javax.swing.JButton bnCancel;

    private javax.swing.JButton bnOk;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JScrollPane jScrollPane4;

    private javax.swing.JTable tableGroupNames;

    public int OK_Click = -1;

    public String groupName = "";

    public String groupDesc = "";
}

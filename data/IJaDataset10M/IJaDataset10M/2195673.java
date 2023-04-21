package guineu.modules.dataanalysis.standardVariation;

import guineu.data.Dataset;
import guineu.modules.dataanalysis.Ttest.*;
import guineu.main.GuineuCore;
import guineu.util.dialogs.ExitCode;
import java.util.logging.Logger;
import javax.swing.JDialog;

/**
 *
 * @author  SCSANDRA
 */
public class StandardVariationDataDialog extends JDialog {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private Dataset dataset;

    private TtestDataModel group1, group2, from;

    private ExitCode exit = ExitCode.UNKNOWN;

    /** Creates new form TtestDataDialog */
    public StandardVariationDataDialog(Dataset dataset) {
        super(GuineuCore.getDesktop().getMainFrame(), "Please select a experiment groups to do the t-test...", true);
        logger.finest("Displaying experiment open dialog");
        this.dataset = dataset;
        initComponents();
        try {
            this.from = new TtestDataModel("Experiment Names");
            this.group1 = new TtestDataModel("Batch1 - Experiment Names");
            this.group2 = new TtestDataModel("Batch2 - Experiment Names");
            this.jTablefrom.setModel(from);
            this.jTablefrom.createToolTip();
            this.jTablegroup1.setModel(group1);
            this.jTablegroup2.setModel(group2);
            this.setValuesTable();
        } catch (Exception exception) {
        }
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablefrom = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButtonizq2 = new javax.swing.JButton();
        jButtonder2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablegroup1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTablegroup2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jButtonizq1 = new javax.swing.JButton();
        jButtonder1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jTablefrom.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Experiment Names" }) {

            Class[] types = new Class[] { java.lang.String.class };

            boolean[] canEdit = new boolean[] { false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTablefrom.setShowHorizontalLines(false);
        jScrollPane1.setViewportView(jTablefrom);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.PAGE_AXIS));
        jButtonizq2.setText(">>");
        jButtonizq2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonizq2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonizq2);
        jButtonder2.setText("<<");
        jButtonder2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonder2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonder2);
        jTablegroup1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Group 1 - Experiment Name" }) {

            Class[] types = new Class[] { java.lang.String.class };

            boolean[] canEdit = new boolean[] { false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTablegroup1.setShowHorizontalLines(false);
        jScrollPane2.setViewportView(jTablegroup1);
        jTablegroup2.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "WT - Experiment Name" }) {

            Class[] types = new Class[] { java.lang.String.class };

            boolean[] canEdit = new boolean[] { false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTablegroup2.setShowHorizontalLines(false);
        jScrollPane3.setViewportView(jTablegroup2);
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.PAGE_AXIS));
        jButtonizq1.setText(">>");
        jButtonizq1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonizq1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonizq1);
        jButtonder1.setText("<<");
        jButtonder1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonder1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonder1);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jScrollPane3, 0, 0, Short.MAX_VALUE).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addGroup(jPanel1Layout.createSequentialGroup().addGap(57, 57, 57).addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(238, 238, 238).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(47, Short.MAX_VALUE)));
        jLabel1.setText("Select the groups of experiments");
        jButtonOK.setText("Ok");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonOK);
        jButtonClose.setText("Cancel");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonClose);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(18, 18, 18).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {
        exit = ExitCode.OK;
        dispose();
    }

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {
        exit = ExitCode.CANCEL;
        dispose();
    }

    private void jButtonizq2ActionPerformed(java.awt.event.ActionEvent evt) {
        int[] selRows = this.jTablefrom.getSelectedRows();
        for (int i = 0; i < selRows.length; i++) {
            if (!this.from.getValueAt(selRows[i], 0).isEmpty()) {
                this.group2.addRows((String) this.from.getValueAt(selRows[i], 0));
                this.from.removeRow(selRows[i]);
            }
        }
        this.from.reconstruct();
        this.jTablefrom.revalidate();
        this.jTablegroup2.revalidate();
    }

    private void jButtonder2ActionPerformed(java.awt.event.ActionEvent evt) {
        int[] selRows = this.jTablegroup2.getSelectedRows();
        for (int i = 0; i < selRows.length; i++) {
            if (!this.group2.getValueAt(selRows[i], 0).isEmpty()) {
                this.from.addRows((String) this.group2.getValueAt(selRows[i], 0));
                this.group2.removeRow(selRows[i]);
            }
        }
        this.group2.reconstruct();
        this.jTablefrom.revalidate();
        this.jTablegroup2.revalidate();
    }

    private void jButtonizq1ActionPerformed(java.awt.event.ActionEvent evt) {
        int[] selRows = this.jTablefrom.getSelectedRows();
        for (int i = 0; i < selRows.length; i++) {
            if (!this.from.getValueAt(selRows[i], 0).isEmpty()) {
                this.group1.addRows((String) this.from.getValueAt(selRows[i], 0));
                this.from.removeRow(selRows[i]);
            }
        }
        this.from.reconstruct();
        this.jTablefrom.revalidate();
        this.jTablegroup1.revalidate();
    }

    private void jButtonder1ActionPerformed(java.awt.event.ActionEvent evt) {
        int[] selRows = this.jTablegroup1.getSelectedRows();
        for (int i = 0; i < selRows.length; i++) {
            if (!this.group1.getValueAt(selRows[i], 0).isEmpty()) {
                this.from.addRows((String) this.group1.getValueAt(selRows[i], 0));
                this.group1.removeRow(selRows[i]);
            }
        }
        this.group1.reconstruct();
        this.jTablefrom.revalidate();
        this.jTablegroup1.revalidate();
    }

    public ExitCode getExitCode() {
        return exit;
    }

    public String[] getGroup1() {
        String[] sgroup1 = new String[this.jTablegroup1.getRowCount()];
        for (int i = 0; i < this.jTablegroup1.getRowCount(); i++) {
            sgroup1[i] = this.jTablegroup1.getValueAt(i, 0).toString();
        }
        return sgroup1;
    }

    public String[] getGroup2() {
        String[] sgroup2 = new String[this.jTablegroup2.getRowCount()];
        for (int i = 0; i < this.jTablegroup2.getRowCount(); i++) {
            sgroup2[i] = this.jTablegroup2.getValueAt(i, 0).toString();
        }
        return sgroup2;
    }

    private javax.swing.JButton jButtonClose;

    private javax.swing.JButton jButtonOK;

    private javax.swing.JButton jButtonder1;

    private javax.swing.JButton jButtonder2;

    private javax.swing.JButton jButtonizq1;

    private javax.swing.JButton jButtonizq2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JTable jTablefrom;

    private javax.swing.JTable jTablegroup1;

    private javax.swing.JTable jTablegroup2;

    private void setValuesTable() {
        for (String experimentName : dataset.getAllColumnNames()) {
            this.from.addRows(experimentName);
        }
    }
}

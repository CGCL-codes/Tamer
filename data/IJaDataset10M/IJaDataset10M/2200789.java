package jy2.crm.szotar;

import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JTextArea;
import jy2.common.db.DataBinding;
import jy2.common.db.FieldBinding;
import jy2.common.ini.DBConf;

/**
 *
 * @author  yehuitragasitscs
 */
public class IntezkedesTipusDetail extends javax.swing.JDialog {

    private static String FORMNAME = "IntezkedesTipusDetail";

    private static String FORMHEADER = "Int�zked�st�pus karbantart�sa";

    private static int USERPOS = 3;

    private static int TIMESTAMPPOS = 5;

    private IntezkedesTipusDataSet ds;

    private char status;

    private int row;

    private DBConf dbConf;

    private DataBinding db;

    /**
     * Creates new form IntezkedesTipusDetail
     */
    public IntezkedesTipusDetail(IntezkedesTipusDataSet ds, int row, char status, DBConf dbConf) {
        initComponents();
        setLocationRelativeTo(null);
        this.ds = ds;
        this.row = row;
        this.status = status;
        this.dbConf = dbConf;
        this.setName(FORMNAME);
        this.setTitle(FORMHEADER);
        this.dbConf.getIni().readFormPos(this);
        db = new DataBinding(ds, row, dbConf);
        db.add(new FieldBinding(jTextField1, 0));
        db.add(new FieldBinding(jTextField2, 1));
        db.add(new FieldBinding(jTextArea1, 2));
        db.add(new FieldBinding(jTextField3, 3));
        db.add(new FieldBinding(jTextField4, 4));
        db.add(new FieldBinding(jTextField5, 5));
        if (status == 'I') db.clear(); else db.load();
    }

    private void initComponents() {
        jOptionPane1 = new javax.swing.JOptionPane();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField4 = new javax.swing.JTextField();
        setTitle("Megye tétel karbantartása");
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jy2/common/icons/cancel.gif")));
        jButton1.setText("cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jy2/common/icons/save.gif")));
        jButton2.setText("OK");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("ID:");
        jTextField1.setEditable(false);
        jTextField1.setText("jTextField1");
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Típus");
        jLabel3.setText("Leírás:");
        jLabel4.setText("Létrehozta:");
        jTextField3.setEditable(false);
        jLabel5.setText("Létrehozva:");
        jTextField5.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        jTextField4.setEditable(false);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel2).add(jLabel1).add(jLabel3)).add(10, 10, 10).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jTextField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, jTextField2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE))).add(jLabel5).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jButton2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton1)).add(layout.createSequentialGroup().add(jLabel4).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(10, 10, 10).add(jTextField5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jTextField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 265, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))).addContainerGap()));
        layout.linkSize(new java.awt.Component[] { jLabel1, jLabel2, jLabel3, jLabel4, jLabel5 }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        layout.linkSize(new java.awt.Component[] { jButton1, jButton2 }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel3).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(jTextField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(jTextField5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButton1).add(jButton2)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jOptionPane1.showConfirmDialog(null, "K�v�nja menteni?", "R�k�rdez�s", jOptionPane1.YES_NO_OPTION) == jOptionPane1.YES_OPTION) {
            if (status == 'I') {
                try {
                    Vector newRow = db.insert();
                    newRow.set(USERPOS, dbConf.getUserID());
                    newRow.set(TIMESTAMPPOS, new java.sql.Timestamp((new java.util.Date()).getTime()));
                    ds.insertData(ds.insertRow(newRow));
                    dbConf.getIni().writeFormPos(this);
                    this.dispose();
                } catch (SQLException ex) {
                    jOptionPane1.showMessageDialog(null, ex.getMessage());
                    ds.deleteRow(ds.getRowCount() - 1);
                }
            } else {
                try {
                    db.save();
                    ds.setValueAt(dbConf.getUserID(), row, USERPOS);
                    ds.setValueAt((new java.sql.Timestamp((new java.util.Date()).getTime())), row, TIMESTAMPPOS);
                    ds.updateData(row);
                    dbConf.getIni().writeFormPos(this);
                    this.dispose();
                } catch (SQLException ex) {
                    jOptionPane1.showMessageDialog(null, ex.getMessage());
                }
            }
            ds.fireTableDataChanged();
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (this.status == 'U') db.loadOld();
        dbConf.getIni().writeFormPos(this);
        this.dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JOptionPane jOptionPane1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea jTextArea1;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JTextField jTextField3;

    private javax.swing.JTextField jTextField4;

    private javax.swing.JTextField jTextField5;
}

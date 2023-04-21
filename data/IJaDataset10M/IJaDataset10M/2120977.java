package jy2.cikk;

import java.awt.FontMetrics;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.sql.SQLException;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import jy2.common.db.ColumnModel;
import jy2.common.ini.DBConf;

/**
 *
 * @author  yehuitragasitscs
 */
public class CikkCsopKedvFrame extends javax.swing.JFrame {

    private static String FORMNAME = "CikkCsopKedvFrame";

    private static String FORMHEADER = "Kedvezm�nyek karbantart�sa";

    private static String FORMREPORT = "cikkcsopkedv";

    private CikkCsopKedvDataSet ds;

    FontMetrics fm;

    ColumnModel cm;

    private DBConf dbConf;

    private JDialog caller;

    private String cikkCsopKedvlID;

    /** Creates new form grid */
    public CikkCsopKedvFrame(DBConf dbConf, JDialog caller, String cikkCsopKedvlID, String cikkCsopKedvlNev) {
        initComponents();
        setLocationRelativeTo(null);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setName(FORMNAME);
        this.setTitle(FORMHEADER);
        this.dbConf = dbConf;
        this.dbConf.getIni().readFormPos(this);
        this.caller = caller;
        this.cikkCsopKedvlID = cikkCsopKedvlID;
        try {
            ds = new CikkCsopKedvDataSet(dbConf.getDatabase(), cikkCsopKedvlID);
        } catch (ClassNotFoundException ex) {
            jOptionPane1.showMessageDialog(null, ex.getMessage());
        } catch (SQLException ex) {
            jOptionPane1.showMessageDialog(null, ex.getMessage());
        }
        setStatus("", null);
        setNavButton(false);
        ds = (CikkCsopKedvDataSet) dbConf.getIni().readTableColsFromIni(this, ds);
        setTable();
        jTextField1.setText(new Integer(cikkCsopKedvlID).toString());
        jTextField2.setText(cikkCsopKedvlNev);
        for (int i = 0; i < ds.getColumnCount(); i++) {
            JCheckBoxMenuItem mi = new JCheckBoxMenuItem(ds.getField(i).getHeaderName());
            mi.setSelected(ds.getField(i).isVisible());
            mi.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jCheckBoxMenuItemActionPerformed(evt);
                }
            });
            jPopupMenu1.add(mi);
        }
    }

    private void jCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        for (int i = 0; i < ds.getColumnCount(); i++) {
            JCheckBoxMenuItem mi = (JCheckBoxMenuItem) jPopupMenu1.getComponent(i + 2);
            ds.getField(i).setVisible(mi.isSelected());
        }
        setTable();
    }

    private void setNavButton(boolean b) {
        boolean j = false;
        try {
            j = dbConf.isFormJog(this.FORMNAME);
        } catch (SQLException ex) {
            jOptionPane1.showMessageDialog(null, ex.getMessage());
        }
        jButton1.setEnabled(j);
        jButton2.setEnabled(b & j);
        jButton3.setEnabled(b & j);
        jButton4.setEnabled(b);
        jButton5.setEnabled(b);
        jButton6.setEnabled(b);
    }

    public void setTable() {
        jTable1.setModel(ds);
        fm = jTable1.getFontMetrics(jTable1.getFont());
        cm = new ColumnModel(fm, ds);
        jTable1.setColumnModel(cm);
        for (int i = 0; i < ds.getFieldsLength(); i++) {
            if (!ds.getField(i).isVisible()) {
                jTable1.getColumnModel().getColumn(i).setMaxWidth(0);
                jTable1.getColumnModel().getColumn(i).setMinWidth(0);
                jTable1.getTableHeader().getColumnModel().getColumn(i).setMaxWidth(0);
                jTable1.getTableHeader().getColumnModel().getColumn(i).setMinWidth(0);
            }
        }
    }

    private void initComponents() {
        jOptionPane1 = new javax.swing.JOptionPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jButton7 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 100, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 100, Short.MAX_VALUE));
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 100, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 100, Short.MAX_VALUE));
        jMenuItem1.setText("Oszlopok");
        jPopupMenu1.add(jMenuItem1);
        jPopupMenu1.add(jSeparator1);
        jMenuItem2.setText("Oszlopok");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Csoport jogosultságok karbantartása");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        jScrollPane1.setComponentPopupMenu(jPopupMenu1);
        jTable1.setComponentPopupMenu(jPopupMenu1);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jToolBar1.setFloatable(false);
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jy2/common/icons/close.gif")));
        jButton7.setMnemonic('B');
        jButton7.setText("Bezár");
        jButton7.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jy2/common/icons/insert.gif")));
        jButton1.setMnemonic('Ú');
        jButton1.setText("Új");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jy2/common/icons/update.gif")));
        jButton2.setMnemonic('M');
        jButton2.setText("Módosít");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jy2/common/icons/delete.gif")));
        jButton3.setMnemonic('T');
        jButton3.setText("Töröl");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jy2/common/icons/up.gif")));
        jButton4.setMnemonic('F');
        jButton4.setText("Fel");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jy2/common/icons/down.gif")));
        jButton5.setMnemonic('L');
        jButton5.setText("Le");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jy2/common/icons/locate.gif")));
        jButton6.setMnemonic('K');
        jButton6.setText("Keres");
        jButton6.setEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);
        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jy2/common/icons/filter.gif")));
        jToggleButton1.setMnemonic('S');
        jToggleButton1.setText("Szűrés");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("jLabel2");
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel2.setText("Cikkcsoport:");
        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jTextField1.setText("jTextField1");
        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Tahoma", 1, 14));
        jTextField2.setText("jTextField2");
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE).add(layout.createSequentialGroup().addContainerGap().add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jTextField2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE).addContainerGap()).add(layout.createSequentialGroup().add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE).addContainerGap()).add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel1)));
        pack();
    }

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (!jToggleButton1.isSelected()) {
            try {
                ds.filterData(0, "", false);
                this.setTable();
                this.setStatus("", null);
            } catch (SQLException ex) {
                jOptionPane1.showMessageDialog(null, ex.getMessage());
            }
        } else {
            int i = jTable1.getSelectedColumn();
            int j = 0;
            String s = "";
            if (i == -1) {
                jOptionPane1.showMessageDialog(null, "Nincsen oszlop kiv�lasztva!");
                jToggleButton1.setSelected(false);
            } else {
                try {
                    s = jOptionPane1.showInputDialog("Sz�r�si �rt�k (" + ds.getField(i).getHeaderName() + ")", "");
                    if (s != null) {
                        j = ds.filterData(i, s, true);
                        this.setTable();
                        jTable1.clearSelection();
                        if (j == 0) {
                            jOptionPane1.showMessageDialog(null, "A sz�r�snek nincs eredm�nye: " + s);
                        } else {
                            jTable1.setRowSelectionInterval(0, 0);
                            Rectangle rect = jTable1.getCellRect(j, 0, true);
                            jTable1.scrollRectToVisible(rect);
                            setStatus(s, i);
                        }
                    } else {
                        jToggleButton1.setSelected(false);
                    }
                } catch (SQLException ex) {
                    jOptionPane1.showMessageDialog(null, ex.getMessage());
                }
            }
        }
    }

    private void setStatus(String filter, Integer filterCol) {
        String s = "";
        if (!filter.equals("")) {
            s = "Filter: " + ds.getField(filterCol).getHeaderName() + "(" + filter + ") ";
        }
        s += "Let�ltve: " + Integer.toString(ds.getRowCount()) + "/" + Integer.toString(dbConf.getMaxRowCount()) + "(MAX)";
        jLabel1.setText(s);
    }

    private void formClose() {
        dbConf.getIni().writeFormPos(this);
        dbConf.getIni().writeTableColsToIni(this, ds);
        if (caller != null) {
            if (jTable1.getSelectedRow() != -1) {
                if (caller.getName().equals("KapcsolatDetail")) {
                } else if (caller.getName().equals("RaktarDetail")) {
                }
            }
        }
    }

    public CikkCsopKedvDataSet getDs() {
        return ds;
    }

    public javax.swing.JTable getJTable1() {
        return jTable1;
    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {
        formClose();
        this.dispose();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        formClose();
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        int i = jTable1.getSelectedColumn();
        String s = "";
        if (i == -1) {
            jOptionPane1.showMessageDialog(null, "Nincsen oszlop kiv�lasztva!");
        } else {
            try {
                s = jOptionPane1.showInputDialog("Keres�si �rt�k (" + ds.getField(i).getHeaderName() + ")", "");
                if (s != null) {
                    i = ds.locateData(i, s);
                    this.setTable();
                    jTable1.clearSelection();
                    if (i == -1) {
                        jOptionPane1.showMessageDialog(null, "A keres�s nem tal�lt �rt�ket: " + s);
                    } else {
                        jTable1.setRowSelectionInterval(i, i);
                        Rectangle rect = jTable1.getCellRect(i, 0, true);
                        jTable1.scrollRectToVisible(rect);
                    }
                }
            } catch (SQLException ex) {
                jOptionPane1.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    public void locate(final String s, int i) throws SQLException, HeadlessException {
        if (!s.equals(null)) {
            i = ds.locateData(i, s);
            this.setTable();
            jTable1.clearSelection();
            if (i == -1) {
                jOptionPane1.showMessageDialog(null, "A keres�s nem tal�lt �rt�ket: " + s);
            } else {
                jTable1.setRowSelectionInterval(i, i);
                Rectangle rect = jTable1.getCellRect(i, 0, true);
                jTable1.scrollRectToVisible(rect);
            }
        }
    }

    private void jTable1FocusGained(java.awt.event.FocusEvent evt) {
        setNavButton(true);
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jTable1.getSelectedColumn() != -1) {
            try {
                ds.selectOrderBy(jTable1.getSelectedColumn(), false);
                this.setTable();
                setNavButton(false);
            } catch (SQLException ex) {
                jOptionPane1.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jTable1.getSelectedColumn() != -1) {
            try {
                ds.selectOrderBy(jTable1.getSelectedColumn(), true);
                this.setTable();
                setNavButton(false);
            } catch (SQLException ex) {
                jOptionPane1.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jTable1.getSelectedRow() != -1) {
            if (jOptionPane1.showConfirmDialog(null, "K�v�nja t�r�lni?", "R�k�rdez�s", jOptionPane1.YES_NO_OPTION) == jOptionPane1.YES_OPTION) {
                try {
                    ds.deleteData(jTable1.getSelectedRow());
                } catch (SQLException ex) {
                    jOptionPane1.showMessageDialog(null, ex.getMessage());
                }
                setNavButton(false);
                jTable1.repaint();
            }
        }
    }

    /**
     * 
     * @param evt 
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jTable1.getSelectedRow() != -1) {
            CikkCsopKedvDetail md = new CikkCsopKedvDetail(ds, jTable1.getSelectedRow(), 'U', dbConf, cikkCsopKedvlID);
            md.pack();
            md.setVisible(true);
            jTable1.repaint();
        }
    }

    /**
     * 
     * @param evt 
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        CikkCsopKedvDetail md = new CikkCsopKedvDetail(ds, jTable1.getSelectedRow(), 'I', dbConf, cikkCsopKedvlID);
        md.pack();
        md.setVisible(true);
        jTable1.repaint();
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JButton jButton4;

    private javax.swing.JButton jButton5;

    private javax.swing.JButton jButton6;

    private javax.swing.JButton jButton7;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem2;

    private javax.swing.JOptionPane jOptionPane1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPopupMenu jPopupMenu1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JTable jTable1;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JToggleButton jToggleButton1;

    private javax.swing.JToolBar jToolBar1;
}

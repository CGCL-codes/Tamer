package busticketvendingsystem;

import javax.swing.*;
import java.text.*;

/**
 *
 * @author  Administrator
 */
public class InsertStoppage extends javax.swing.JDialog {

    ConnectDatabase cd = new ConnectDatabase();

    /** Creates new form InsertStoppage */
    public InsertStoppage(JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        prestoppage = new javax.swing.JTextField();
        nextstoppage = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        stoppagename = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        distance = new javax.swing.JTextField();
        inbetween = new javax.swing.JRadioButton();
        atend = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        insert = new javax.swing.JButton();
        finish = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Enter data"));
        prestoppage.setEnabled(false);
        nextstoppage.setEnabled(false);
        jLabel4.setText("Previous stoppage :");
        jLabel5.setText("Next stoppage :");
        jLabel6.setText("Stoppage name :");
        stoppagename.setEnabled(false);
        jLabel7.setText("Distance from Garia :");
        distance.setEnabled(false);
        distance.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distanceActionPerformed(evt);
            }
        });
        buttonGroup1.add(inbetween);
        inbetween.setText("in between");
        inbetween.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        inbetween.setMargin(new java.awt.Insets(0, 0, 0, 0));
        inbetween.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                inbetweenItemStateChanged(evt);
            }
        });
        buttonGroup1.add(atend);
        atend.setText("at end");
        atend.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        atend.setMargin(new java.awt.Insets(0, 0, 0, 0));
        atend.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                atendItemStateChanged(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel7).add(jLabel6).add(jLabel5).add(jPanel1Layout.createSequentialGroup().add(inbetween).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(atend)).add(jLabel4)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 32, Short.MAX_VALUE).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(nextstoppage).add(stoppagename).add(distance, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE).add(prestoppage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)).addContainerGap(41, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(inbetween).add(atend)).add(20, 20, 20).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(prestoppage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(nextstoppage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel6).add(stoppagename, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel7).add(distance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/busticketvendingsystem/resources/images/ins_stop.gif")));
        jLabel2.setText("Insert a stoppage");
        insert.setText("Insert");
        insert.setEnabled(false);
        insert.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertActionPerformed(evt);
            }
        });
        finish.setText("Finish");
        finish.setEnabled(false);
        finish.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishActionPerformed(evt);
            }
        });
        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/busticketvendingsystem/resources/images/help_icon_small.gif")));
        jButton1.setText("Help");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jLabel2).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(insert).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(finish).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cancel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton1))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cancel).add(finish).add(insert).add(jButton1)).addContainerGap()));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new HelpWindow(InsertStoppage.this, true, "ins_stoppage.htm").setVisible(true);
    }

    private void distanceActionPerformed(java.awt.event.ActionEvent evt) {
        Insert();
    }

    private void finishActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void insertActionPerformed(java.awt.event.ActionEvent evt) {
        Insert();
    }

    private void atendItemStateChanged(java.awt.event.ItemEvent evt) {
        stoppagename.setEnabled(true);
        distance.setEnabled(true);
        insert.setEnabled(true);
        prestoppage.setEnabled(false);
        nextstoppage.setEnabled(false);
    }

    private void inbetweenItemStateChanged(java.awt.event.ItemEvent evt) {
        stoppagename.setEnabled(true);
        distance.setEnabled(true);
        prestoppage.setEnabled(true);
        nextstoppage.setEnabled(true);
        insert.setEnabled(true);
    }

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void Insert() {
        String sname = "";
        float dis;
        String prestp = "";
        String nextstp = "";
        boolean type = false;
        boolean type1 = false;
        boolean type2 = false;
        try {
            if (inbetween.isSelected()) {
                sname = stoppagename.getText();
                String dist = distance.getText();
                dis = Float.parseFloat(dist);
                prestp = prestoppage.getText();
                nextstp = nextstoppage.getText();
                if (cd.isStoppage(prestp) && cd.isStoppage(nextstp) && !cd.isStoppage(sname) && dis > cd.getDistance(prestp) && dis < cd.getDistance(nextstp)) {
                    type = true;
                } else {
                    type = false;
                }
            } else {
                sname = stoppagename.getText();
                String dist = distance.getText();
                dis = Float.parseFloat(dist);
                String stoppages[] = cd.getStoppage_name();
                String lasts = stoppages[stoppages.length - 1];
                if (!cd.isStoppage(sname) && (dis > cd.getDistance(lasts))) {
                    type = true;
                } else {
                    type = false;
                }
            }
            if (type) {
                DecimalFormat threeDigits = new DecimalFormat("000");
                String id = "S" + threeDigits.format(cd.getNumber_of_rows("Stoppage") + 1);
                type1 = cd.insertStoppage(id, sname, dis);
                type2 = cd.sortStoppage();
            } else {
                JOptionPane.showMessageDialog(null, "Wrong Input!\nCheck your entry carefully", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Wrong Input!\nCheck your entry carefully", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        if (type1 && type2) {
            JOptionPane.showMessageDialog(null, "New stoppage have been inserted");
            finish.setEnabled(true);
            insert.setEnabled(false);
            stoppagename.setText("");
            distance.setText("");
            prestoppage.setText("");
            nextstoppage.setText("");
            stoppagename.setEnabled(false);
            distance.setEnabled(false);
            prestoppage.setEnabled(false);
            nextstoppage.setEnabled(false);
            inbetween.setSelected(false);
            atend.setSelected(false);
        } else if (!type1 && !type2 && type) {
            JOptionPane.showMessageDialog(null, "Wrong Input!\nCheck your entry carefully", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new InsertStoppage(null, true).setVisible(true);
            }
        });
    }

    private javax.swing.JRadioButton atend;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JButton cancel;

    private javax.swing.JTextField distance;

    private javax.swing.JButton finish;

    private javax.swing.JRadioButton inbetween;

    private javax.swing.JButton insert;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JTextField nextstoppage;

    private javax.swing.JTextField prestoppage;

    private javax.swing.JTextField stoppagename;
}

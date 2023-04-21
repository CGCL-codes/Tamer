package busticketvendingsystem;

import javax.swing.*;

/**
 *
 * @author  Administrator
 */
public class RemoveBus extends javax.swing.JDialog {

    ConnectDatabase cd = new ConnectDatabase();

    String bus[] = new String[4];

    String bnumber = "";

    /** Creates new form RemoveBus */
    public RemoveBus(JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        busnumber1 = new javax.swing.JTextField();
        find = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        downtime = new javax.swing.JLabel();
        uptime = new javax.swing.JLabel();
        busnumber = new javax.swing.JLabel();
        seats = new javax.swing.JLabel();
        delete = new javax.swing.JButton();
        finish = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Remove a bus");
        setResizable(false);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Enter data"));
        jLabel2.setText("Bus number :");
        busnumber1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                busnumber1ActionPerformed(evt);
            }
        });
        find.setText("Find");
        find.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(busnumber1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(find).addContainerGap(116, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(busnumber1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(find)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/busticketvendingsystem/resources/images/del_bus.gif")));
        jLabel1.setText("Remove a bus");
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Information"));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Bus number :");
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("Up schedule time :");
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setText("Down schedule time :");
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setText("No. of seats :");
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel5).add(jLabel4).add(jLabel3).add(jLabel6)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(seats).add(busnumber).add(uptime).add(downtime)).addContainerGap(229, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(busnumber)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(uptime)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(downtime)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel6).add(seats)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        delete.setText("Delete");
        delete.setEnabled(false);
        delete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
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
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(delete).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(finish).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cancel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton1)).add(jLabel1).add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cancel).add(finish).add(delete).add(jButton1)).addContainerGap()));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new HelpWindow(RemoveBus.this, true, "del_bus.htm").setVisible(true);
    }

    private void busnumber1ActionPerformed(java.awt.event.ActionEvent evt) {
        Find();
    }

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {
        boolean type = true;
        type = cd.deleteBus(bus[0]);
        if (!type) {
            JOptionPane.showMessageDialog(null, "Wrong Input.\n Please check your entry carefully", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else {
            finish.setEnabled(true);
            delete.setEnabled(false);
        }
    }

    private void finishActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void findActionPerformed(java.awt.event.ActionEvent evt) {
        Find();
    }

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void Find() {
        boolean type = true;
        bnumber = busnumber1.getText();
        bus = cd.getBus(bnumber);
        String allbus[] = cd.getBus_number_byUpSchedule();
        for (int i = 0; i < allbus.length; i++) {
            if (allbus[i].equals(bnumber)) {
                busnumber.setText(bus[0]);
                uptime.setText(bus[1]);
                downtime.setText(bus[2]);
                seats.setText(bus[3]);
                delete.setEnabled(true);
                type = true;
                break;
            } else {
                type = false;
            }
        }
        if (!type) {
            JOptionPane.showMessageDialog(null, "Wrong Input.\n Please check your entry carefully", "ERROR", JOptionPane.ERROR_MESSAGE);
            busnumber1.setText("");
        } else {
            delete.setEnabled(true);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new RemoveBus(null, true).setVisible(true);
            }
        });
    }

    private javax.swing.JLabel busnumber;

    private javax.swing.JTextField busnumber1;

    private javax.swing.JButton cancel;

    private javax.swing.JButton delete;

    private javax.swing.JLabel downtime;

    private javax.swing.JButton find;

    private javax.swing.JButton finish;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JLabel seats;

    private javax.swing.JLabel uptime;
}

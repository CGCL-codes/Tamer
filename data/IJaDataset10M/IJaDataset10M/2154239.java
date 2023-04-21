package busticketvendingsystem;

import javax.swing.*;

/**
 *
 * @author  Administrator
 */
public class UpdateStoppage extends javax.swing.JDialog {

    ConnectDatabase cd = new ConnectDatabase();

    String sname = "";

    String stp[] = new String[3];

    /** Creates new form UpdateStoppage */
    public UpdateStoppage(JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        stoppagename1 = new javax.swing.JTextField();
        find = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        stoppagename = new javax.swing.JTextField();
        distance = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cancel = new javax.swing.JButton();
        finish = new javax.swing.JButton();
        update = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update a stoppage");
        setResizable(false);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Enter Data"));
        jLabel1.setText("Stoppage name :");
        stoppagename1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stoppagename1ActionPerformed(evt);
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
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(stoppagename1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 135, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(find).addContainerGap(74, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(stoppagename1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(find)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Modify"));
        jLabel2.setText("Stoppage name :");
        jLabel3.setText("Distance :");
        distance.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distanceActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel2).add(jLabel3)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(distance).add(stoppagename, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(135, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(stoppagename, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(distance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/busticketvendingsystem/resources/images/update_stop.gif")));
        jLabel4.setText("Update a stoppage");
        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        finish.setText("Finish");
        finish.setEnabled(false);
        finish.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishActionPerformed(evt);
            }
        });
        update.setText("Update");
        update.setEnabled(false);
        update.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
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
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(update).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(finish).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cancel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton1)).add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jLabel4)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(jLabel4).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cancel).add(finish).add(update).add(jButton1)).addContainerGap()));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new HelpWindow(UpdateStoppage.this, true, "update_stoppage.htm").setVisible(true);
    }

    private void distanceActionPerformed(java.awt.event.ActionEvent evt) {
        Update();
    }

    private void stoppagename1ActionPerformed(java.awt.event.ActionEvent evt) {
        Find();
    }

    private void finishActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {
        Update();
    }

    private void findActionPerformed(java.awt.event.ActionEvent evt) {
        Find();
    }

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void Find() {
        boolean type = true;
        sname = stoppagename1.getText();
        String stp1[] = cd.getStoppage_name();
        for (int i = 0; i < stp1.length; i++) {
            if (stp1[i].equals(sname)) {
                if (sname.equals("Garia")) {
                    JOptionPane.showMessageDialog(null, "You can't change any information on 'Garia' stoppage", "ERROR", JOptionPane.ERROR_MESSAGE);
                    stoppagename1.setText("");
                } else {
                    stp = cd.getStoppage(sname);
                    stoppagename.setText(stp[1]);
                    distance.setText(stp[2]);
                    update.setEnabled(true);
                }
                type = true;
                break;
            } else {
                type = false;
            }
        }
        if (!type) {
            stoppagename1.setText("");
            JOptionPane.showMessageDialog(null, "Wrong Input.\n Please check your entry carefully", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Update() {
        boolean type = true, type1 = false;
        try {
            String upsname = stoppagename.getText();
            String dis = distance.getText();
            Float updis = Float.parseFloat(dis);
            String stp1[] = cd.getStoppage_name();
            String sname1 = "";
            String sname2 = "";
            for (int i = 1; i < stp1.length; i++) {
                if (stp1[i].equals(sname)) {
                    sname1 = stp1[i + 1];
                    sname2 = stp1[i - 1];
                    type = true;
                    break;
                } else {
                    type = false;
                }
            }
            for (int i = 1; i < stp1.length; i++) {
                if (stp1[i].equals(upsname)) {
                    type = false;
                    break;
                }
            }
            float dis1 = cd.getDistance(sname1);
            float dis2 = cd.getDistance(sname2);
            if (type) {
                if (dis1 > updis && dis2 < updis) {
                    type1 = cd.updateStoppage(stp[0], upsname, updis);
                } else JOptionPane.showMessageDialog(null, "Wrong Input of distance", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else JOptionPane.showMessageDialog(null, "Wrong Input.\n Please check your entry carefully", "ERROR", JOptionPane.ERROR_MESSAGE);
            if (!type1 && type) {
                JOptionPane.showMessageDialog(null, "Wrong Input.\n Please check your entry carefully", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else if (type1 && type) {
                finish.setEnabled(true);
                update.setEnabled(false);
                JOptionPane.showMessageDialog(null, "Stoppage updation has been done.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Wrong Input.\n Please check your entry carefully", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new UpdateStoppage(null, true).setVisible(true);
            }
        });
    }

    private javax.swing.JButton cancel;

    private javax.swing.JTextField distance;

    private javax.swing.JButton find;

    private javax.swing.JButton finish;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JTextField stoppagename;

    private javax.swing.JTextField stoppagename1;

    private javax.swing.JButton update;
}

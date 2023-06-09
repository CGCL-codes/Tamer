package main;

import io.load;
import prompts.*;

/**
 *
 * @author amgupt01
 */
public class mainMenu extends javax.swing.JFrame {

    /** Creates new form mainMenu */
    public mainMenu() {
        initComponents();
        setLocationRelativeTo(null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 15));
        jButton1.setText("Start resolving a conflict!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 15));
        jButton2.setText("Load a saved conflict!");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 15));
        jButton3.setText("View examples! (Coming Soon)");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 15));
        jLabel2.setText("<html>Please select an action to start using this program:</html>");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE).addComponent(jLabel2).addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE).addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel2).addGap(18, 18, 18).addComponent(jButton1).addGap(18, 18, 18).addComponent(jButton2).addGap(18, 18, 18).addComponent(jButton3).addContainerGap(73, Short.MAX_VALUE)));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new promptScenario().setVisible(true);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        new load().setVisible(true);
        setVisible(false);
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        new examples.example().setVisible(true);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new mainMenu().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel2;
}

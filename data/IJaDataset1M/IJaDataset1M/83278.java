package org.paja.group.capa.visual.alfa;

/**
 *
 * @author  Claver Isaac
 */
public class Splash extends javax.swing.JFrame {

    /** Creates new form Splash */
    public Splash() {
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/splash.jpg")));
        getContentPane().add(jLabel1);
        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Splash().setVisible(true);
            }
        });
    }

    private javax.swing.JLabel jLabel1;
}

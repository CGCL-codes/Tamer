package picto.test;

import javax.swing.JOptionPane;
import picto.draw.DrawCanvas;

/**
 *
 * @author  Matt
 */
public class TestTablet extends javax.swing.JFrame {

    /** Creates new form TestTablet */
    public TestTablet() {
        initComponents();
        DrawCanvas canvas = new DrawCanvas();
    }

    private void initComponents() {
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setText("jButton2");
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap(183, Short.MAX_VALUE).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jButton1).add(144, 144, 144)).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jButton2).add(135, 135, 135)))));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(83, 83, 83).add(jButton1).add(36, 36, 36).add(jButton2).addContainerGap(135, Short.MAX_VALUE)));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "ha");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new TestTablet().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;
}

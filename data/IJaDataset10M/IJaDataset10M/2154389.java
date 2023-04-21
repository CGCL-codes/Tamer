package issrg.test;

import java.awt.Dimension;

/**
 *
 * @author  Bassem Nasser
 */
public class SATJDialog extends javax.swing.JDialog {

    int satlevel;

    /** Creates new form NewJDialog */
    public SATJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        arrayy = new java.awt.Label[] { label1, label2, label3, label4, label5 };
        label1.setEnabled(false);
        jLabel1.setText("Logging: " + label5.getText() + ": " + label4.getText() + ": " + label3.getText() + ": " + label2.getText());
        this.setTitle("Choose SAT Level");
        this.setModal(true);
        this.setLocation(0, 0);
        this.setBounds(0, 0, 500, 400);
        this.setEnabled(true);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(true);
        this.toFront();
        this.setVisible(true);
    }

    private void initComponents() {
        jSlider1 = new javax.swing.JSlider();
        label4 = new java.awt.Label();
        label3 = new java.awt.Label();
        label5 = new java.awt.Label();
        label2 = new java.awt.Label();
        label1 = new java.awt.Label();
        jLabel1 = new javax.swing.JLabel();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SATLevel");
        setModal(true);
        jSlider1.setMajorTickSpacing(1);
        jSlider1.setMaximum(4);
        jSlider1.setOrientation(javax.swing.JSlider.VERTICAL);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setSnapToTicks(true);
        jSlider1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jSlider1MouseReleased(evt);
            }
        });
        getContentPane().add(jSlider1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));
        label4.setText("Acess Deny");
        getContentPane().add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, -1, 30));
        label3.setText("Policy changes");
        getContentPane().add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, -1, -1));
        label5.setText("Acess Grants");
        getContentPane().add(label5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, -1, -1));
        label2.setText("ShutDown/StartUp");
        getContentPane().add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, -1, 30));
        label1.setText("No Info");
        getContentPane().add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, -1, -1));
        jLabel1.setText(" ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));
        jToggleButton3.setText("Cancel");
        jToggleButton3.setMaximumSize(new java.awt.Dimension(53, 23));
        jToggleButton3.setMinimumSize(new java.awt.Dimension(53, 23));
        jToggleButton3.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jToggleButton3MouseClicked(evt);
            }
        });
        getContentPane().add(jToggleButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 120, -1, -1));
        jToggleButton2.setText("OK");
        jToggleButton2.setMaximumSize(new java.awt.Dimension(53, 23));
        jToggleButton2.setMinimumSize(new java.awt.Dimension(53, 23));
        jToggleButton2.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jToggleButton2MouseClicked(evt);
            }
        });
        getContentPane().add(jToggleButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, 70, -1));
        pack();
    }

    private void jToggleButton2MouseClicked(java.awt.event.MouseEvent evt) {
        satlevel = jSlider1.getValue();
        this.setVisible(false);
    }

    private void jToggleButton3MouseClicked(java.awt.event.MouseEvent evt) {
        this.dispose();
        System.exit(-1);
    }

    private void jSlider1MouseReleased(java.awt.event.MouseEvent evt) {
        if (jSlider1.getValue() == 0) {
            label1.setEnabled(true);
            label2.setEnabled(false);
            label3.setEnabled(false);
            label4.setEnabled(false);
            label5.setEnabled(false);
        } else {
            label1.setEnabled(false);
            turnOffAbove(jSlider1.getValue(), 4);
            turnOnBelow(jSlider1.getValue(), 0);
        }
        showOnNames();
    }

    void turnOffAbove(int x, int maximum) {
        for (int i = x; i <= maximum; i++) arrayy[i].setEnabled(false);
    }

    void turnOnBelow(int x, int minimum) {
        for (int i = x; i > minimum; i--) {
            arrayy[i].setEnabled(true);
        }
    }

    void showOnNames() {
        jLabel1.setText("Logging: ");
        for (int i = 0; i < 5; i++) if (arrayy[i].isEnabled()) jLabel1.setText(jLabel1.getText() + ":  " + arrayy[i].getText());
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JSlider jSlider1;

    private javax.swing.JToggleButton jToggleButton2;

    private javax.swing.JToggleButton jToggleButton3;

    private java.awt.Label label1;

    private java.awt.Label label2;

    private java.awt.Label label3;

    private java.awt.Label label4;

    private java.awt.Label label5;

    private java.awt.Label[] arrayy;

    public int getSatlevel() {
        return this.satlevel;
    }
}

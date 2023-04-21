package edu.ucsd.ncmir.jinx.segmentation.manual.gui;

import edu.ucsd.ncmir.jinx.events.JxErrorEvent;
import edu.ucsd.ncmir.spl.gui.DialogDestroyer;
import java.awt.Frame;
import javax.swing.JDialog;

/**
 *
 * @author  spl
 */
public class JxIntervalEditor extends JDialog {

    private static final long serialVersionUID = 42L;

    /** Creates new form JxIntervalEditor */
    public JxIntervalEditor(Frame parent) {
        super(parent, true);
        this.initComponents();
        this.setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        number_field = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(5, 5));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Interval Editor");
        getContentPane().add(jLabel1, java.awt.BorderLayout.NORTH);
        number_field.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        number_field.setAutoscrolls(false);
        number_field.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                number_fieldActionPerformed(evt);
            }
        });
        getContentPane().add(number_field, java.awt.BorderLayout.CENTER);
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jPanel2, java.awt.BorderLayout.EAST);
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jButton1.setText("Accept");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);
        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);
        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);
        getContentPane().add(jPanel4, java.awt.BorderLayout.LINE_END);
        getContentPane().add(jPanel5, java.awt.BorderLayout.LINE_START);
        pack();
    }

    private void number_fieldActionPerformed(java.awt.event.ActionEvent evt) {
        String text = this.number_field.getText();
        try {
            this._value = Double.parseDouble(text);
        } catch (NumberFormatException nfe) {
            new JxErrorEvent().send(nfe);
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String text = this.number_field.getText();
        try {
            this._value = Double.parseDouble(text);
            new DialogDestroyer(this);
        } catch (NumberFormatException nfe) {
            new JxErrorEvent().send(nfe);
        }
        new DialogDestroyer(this);
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        new DialogDestroyer(this);
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JTextField number_field;

    private double _value = -1;

    double getValue() {
        return this._value;
    }
}
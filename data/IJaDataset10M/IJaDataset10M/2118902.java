package test;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

/**
 * VisualMarginTest.
 *
 * @author  Werner Randelshofer
 * @version 1.0  07 April 2005  Created.
 */
public class VisualMarginTest extends javax.swing.JPanel {

    /** Creates new form. */
    public VisualMarginTest() {
        initComponents();
        jPanel1.putClientProperty("Quaqua.Component.visualMargin", new Insets(3, 3, 3, 3));
        jPanel2.putClientProperty("Quaqua.Component.visualMargin", new Insets(2, 2, 2, 2));
        jPanel3.putClientProperty("Quaqua.Component.visualMargin", new Insets(1, 1, 1, 1));
        jPanel4.putClientProperty("Quaqua.Component.visualMargin", new Insets(0, 0, 0, 0));
        jLabel1.putClientProperty("Quaqua.Component.visualMargin", new Insets(3, 3, 3, 3));
        jLabel2.putClientProperty("Quaqua.Component.visualMargin", new Insets(2, 2, 2, 2));
        jLabel3.putClientProperty("Quaqua.Component.visualMargin", new Insets(1, 1, 1, 1));
        jLabel4.putClientProperty("Quaqua.Component.visualMargin", new Insets(0, 0, 0, 0));
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        classGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        FormListener formListener = new FormListener();
        setLayout(new java.awt.GridBagLayout());
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));
        jTextField1.setText("Ångström H");
        jPanel1.add(jTextField1);
        jTextField2.setText("Ångström H");
        jPanel1.add(jTextField2);
        jTextField3.setText("Ångström H");
        jPanel1.add(jTextField3);
        add(jPanel1, new java.awt.GridBagConstraints());
        jLabel1.setText("Visual Margin 3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jLabel1, gridBagConstraints);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));
        jTextField4.setText("Ångström H");
        jPanel2.add(jTextField4);
        jTextField5.setText("Ångström H");
        jPanel2.add(jTextField5);
        jTextField6.setText("Ångström H");
        jPanel2.add(jTextField6);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        add(jPanel2, gridBagConstraints);
        jLabel2.setText("Visual Margin 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jLabel2, gridBagConstraints);
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));
        jTextField7.setText("Ångström H");
        jPanel3.add(jTextField7);
        jTextField8.setText("Ångström H");
        jPanel3.add(jTextField8);
        jTextField9.setText("Ångström H");
        jPanel3.add(jTextField9);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        add(jPanel3, gridBagConstraints);
        jLabel3.setText("Visual Margin 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jLabel3, gridBagConstraints);
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));
        jTextField10.setText("Ångström H");
        jPanel4.add(jTextField10);
        jTextField11.setText("Ångström H");
        jPanel4.add(jTextField11);
        jTextField12.setText("Ångström H");
        jPanel4.add(jTextField12);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        add(jPanel4, gridBagConstraints);
        jLabel4.setText("Visual Margin 0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        add(jSeparator1, gridBagConstraints);
        classGroup.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Text Fields");
        jRadioButton1.setActionCommand("javax.swing.JTextField");
        jRadioButton1.addItemListener(formListener);
        jPanel5.add(jRadioButton1);
        classGroup.add(jRadioButton2);
        jRadioButton2.setText("Buttons");
        jRadioButton2.setActionCommand("javax.swing.JButton");
        jRadioButton2.addItemListener(formListener);
        jPanel5.add(jRadioButton2);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        add(jPanel5, gridBagConstraints);
    }

    private class FormListener implements java.awt.event.ItemListener {

        FormListener() {
        }

        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            if (evt.getSource() == jRadioButton1) {
                VisualMarginTest.this.classItemChanged(evt);
            } else if (evt.getSource() == jRadioButton2) {
                VisualMarginTest.this.classItemChanged(evt);
            }
        }
    }

    private void classItemChanged(java.awt.event.ItemEvent evt) {
        try {
            Class clazz = Class.forName(classGroup.getSelection().getActionCommand());
            for (JPanel p : new JPanel[] { jPanel1, jPanel2, jPanel3, jPanel4 }) {
                Insets vm = (Insets) p.getClientProperty("Quaqua.Component.visualMargin");
                p.removeAll();
                for (int i = 0; i < 3; i++) {
                    JComponent c = (JComponent) clazz.newInstance();
                    if (c instanceof JTextComponent) {
                        ((JTextComponent) c).setText("Ångström H");
                    } else if (c instanceof AbstractButton) {
                        ((AbstractButton) c).setText("Ångström H");
                    }
                    c.putClientProperty("Quaqua.Component.visualMargin", vm);
                    p.add(c);
                }
            }
            revalidate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private javax.swing.ButtonGroup classGroup;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JRadioButton jRadioButton1;

    private javax.swing.JRadioButton jRadioButton2;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField10;

    private javax.swing.JTextField jTextField11;

    private javax.swing.JTextField jTextField12;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JTextField jTextField3;

    private javax.swing.JTextField jTextField4;

    private javax.swing.JTextField jTextField5;

    private javax.swing.JTextField jTextField6;

    private javax.swing.JTextField jTextField7;

    private javax.swing.JTextField jTextField8;

    private javax.swing.JTextField jTextField9;
}

package test;

import ch.randelshofer.quaqua.*;
import java.awt.*;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.border.*;

/**
 * ToggleButtonTest.
 *
 * @author  Werner Randelshofer
 * @version $Id: ToggleButtonTest.java 418 2011-08-04 07:59:43Z wrandelshofer $
 */
public class ToggleButtonTest extends javax.swing.JPanel {

    /** Creates new form. */
    public ToggleButtonTest() {
        initComponents();
        texturedRadio.setVisible(false);
        capsuleRadio.setVisible(false);
        LinkedList<JComponent> todo = new LinkedList<JComponent>();
        todo.add(this);
        while (!todo.isEmpty()) {
            JComponent jc = todo.removeFirst();
            if (jc instanceof JPanel) {
                for (Component c : jc.getComponents()) {
                    todo.add((JComponent) c);
                }
            } else if (jc instanceof JToggleButton) {
                JToggleButton jb = (JToggleButton) jc;
                jb.putClientProperty("JButton.buttonType", "segmented");
                if (jb.getText().equals("West")) {
                    jb.putClientProperty("JButton.segmentPosition", "first");
                } else if (jb.getText().equals("Center")) {
                    jb.putClientProperty("JButton.segmentPosition", "middle");
                } else if (jb.getText().equals("East")) {
                    jb.putClientProperty("JButton.segmentPosition", "last");
                } else {
                    jb.putClientProperty("JButton.segmentPosition", "only");
                }
            }
        }
        for (JComponent jc : new JComponent[] { smallToggle1, smallToggle2, smallToggle3, smallToggle4, smallToggle5, smallLabel }) {
            jc.putClientProperty("JComponent.sizeVariant", "small");
        }
        for (JComponent jc : new JComponent[] { miniToggle1, miniToggle2, miniToggle3, miniToggle4, miniToggle5, miniLabel }) {
            jc.putClientProperty("JComponent.sizeVariant", "mini");
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(QuaquaManager.getLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame f = new JFrame("Quaqua Toggle Button Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new ToggleButtonTest());
        ((JComponent) f.getContentPane()).setBorder(new EmptyBorder(9, 17, 17, 17));
        f.pack();
        f.setVisible(true);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        typeGroup = new javax.swing.ButtonGroup();
        toggle1 = new javax.swing.JToggleButton();
        toggle2 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        regularPanel = new javax.swing.JPanel();
        toggle3 = new javax.swing.JToggleButton();
        toggle4 = new javax.swing.JToggleButton();
        toggle5 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        smallToggle1 = new javax.swing.JToggleButton();
        smallToggle2 = new javax.swing.JToggleButton();
        smallLabel = new javax.swing.JLabel();
        smallPanel = new javax.swing.JPanel();
        smallToggle3 = new javax.swing.JToggleButton();
        smallToggle4 = new javax.swing.JToggleButton();
        smallToggle5 = new javax.swing.JToggleButton();
        jSeparator2 = new javax.swing.JSeparator();
        miniToggle1 = new javax.swing.JToggleButton();
        miniToggle2 = new javax.swing.JToggleButton();
        miniLabel = new javax.swing.JLabel();
        miniPanel = new javax.swing.JPanel();
        miniToggle3 = new javax.swing.JToggleButton();
        miniToggle4 = new javax.swing.JToggleButton();
        miniToggle5 = new javax.swing.JToggleButton();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        segmentedRadio = new javax.swing.JRadioButton();
        texturedRadio = new javax.swing.JRadioButton();
        squareButton = new javax.swing.JRadioButton();
        roundRectRadio = new javax.swing.JRadioButton();
        capsuleRadio = new javax.swing.JRadioButton();
        gradientButton = new javax.swing.JRadioButton();
        stretcherPanel = new javax.swing.JPanel();
        FormListener formListener = new FormListener();
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 17, 17, 17));
        setLayout(new java.awt.GridBagLayout());
        toggle1.setSelected(true);
        toggle1.setText("Ångström H");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        add(toggle1, gridBagConstraints);
        toggle2.setText("Ångström H");
        toggle2.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        add(toggle2, gridBagConstraints);
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        add(jPanel1, gridBagConstraints);
        regularPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        toggle3.setText("West");
        regularPanel.add(toggle3);
        toggle4.setText("Center");
        regularPanel.add(toggle4);
        toggle5.setText("East");
        regularPanel.add(toggle5);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        add(regularPanel, gridBagConstraints);
        jLabel1.setText("Enabled");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jLabel1, gridBagConstraints);
        jLabel2.setText("Disabled");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jLabel2, gridBagConstraints);
        jLabel3.setText("Segmented");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        add(jSeparator1, gridBagConstraints);
        smallToggle1.setText("Ångström H");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        add(smallToggle1, gridBagConstraints);
        smallToggle2.setText("Ångström H");
        smallToggle2.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        add(smallToggle2, gridBagConstraints);
        smallLabel.setText("Small");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(smallLabel, gridBagConstraints);
        smallPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        smallToggle3.setText("West");
        smallPanel.add(smallToggle3);
        smallToggle4.setText("Center");
        smallPanel.add(smallToggle4);
        smallToggle5.setText("East");
        smallPanel.add(smallToggle5);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(smallPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        add(jSeparator2, gridBagConstraints);
        miniToggle1.setText("Ångström H");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        add(miniToggle1, gridBagConstraints);
        miniToggle2.setText("Ångström H");
        miniToggle2.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        add(miniToggle2, gridBagConstraints);
        miniLabel.setText("Mini");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(miniLabel, gridBagConstraints);
        miniPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        miniToggle3.setText("West");
        miniPanel.add(miniToggle3);
        miniToggle4.setText("Center");
        miniPanel.add(miniToggle4);
        miniToggle5.setText("East");
        miniPanel.add(miniToggle5);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(miniPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        add(jSeparator3, gridBagConstraints);
        jPanel3.setLayout(new java.awt.GridBagLayout());
        typeGroup.add(segmentedRadio);
        segmentedRadio.setSelected(true);
        segmentedRadio.setText("Segmented");
        segmentedRadio.setActionCommand("segmented");
        segmentedRadio.addActionListener(formListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(segmentedRadio, gridBagConstraints);
        typeGroup.add(texturedRadio);
        texturedRadio.setText("Textured");
        texturedRadio.setActionCommand("segmentedTextured");
        texturedRadio.addActionListener(formListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(texturedRadio, gridBagConstraints);
        typeGroup.add(squareButton);
        squareButton.setText("Square");
        squareButton.setActionCommand("square");
        squareButton.addActionListener(formListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(squareButton, gridBagConstraints);
        typeGroup.add(roundRectRadio);
        roundRectRadio.setText("Round Rect");
        roundRectRadio.setActionCommand("segmentedRoundRect");
        roundRectRadio.addActionListener(formListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(roundRectRadio, gridBagConstraints);
        typeGroup.add(capsuleRadio);
        capsuleRadio.setText("Capsule");
        capsuleRadio.setActionCommand("segmentedCapsule");
        capsuleRadio.addActionListener(formListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(capsuleRadio, gridBagConstraints);
        typeGroup.add(gradientButton);
        gradientButton.setText("Gradient");
        gradientButton.setActionCommand("gradient");
        gradientButton.addActionListener(formListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(gradientButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        add(jPanel3, gridBagConstraints);
        stretcherPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 99;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.weighty = 1.0;
        add(stretcherPanel, gridBagConstraints);
    }

    private class FormListener implements java.awt.event.ActionListener {

        FormListener() {
        }

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if (evt.getSource() == segmentedRadio) {
                ToggleButtonTest.this.typeRadioPerformed(evt);
            } else if (evt.getSource() == texturedRadio) {
                ToggleButtonTest.this.typeRadioPerformed(evt);
            } else if (evt.getSource() == squareButton) {
                ToggleButtonTest.this.typeRadioPerformed(evt);
            } else if (evt.getSource() == roundRectRadio) {
                ToggleButtonTest.this.typeRadioPerformed(evt);
            } else if (evt.getSource() == capsuleRadio) {
                ToggleButtonTest.this.typeRadioPerformed(evt);
            } else if (evt.getSource() == gradientButton) {
                ToggleButtonTest.this.typeRadioPerformed(evt);
            }
        }
    }

    private void typeRadioPerformed(java.awt.event.ActionEvent evt) {
        String type = typeGroup.getSelection().getActionCommand();
        LinkedList<JComponent> todo = new LinkedList<JComponent>();
        todo.add(this);
        while (!todo.isEmpty()) {
            JComponent jc = todo.removeFirst();
            if (jc instanceof JPanel) {
                for (Component c : jc.getComponents()) {
                    todo.add((JComponent) c);
                }
            } else if (jc instanceof JToggleButton) {
                JToggleButton jb = (JToggleButton) jc;
                jb.putClientProperty("JButton.buttonType", type);
            }
        }
        regularPanel.revalidate();
        smallPanel.revalidate();
        miniPanel.revalidate();
    }

    private javax.swing.JRadioButton capsuleRadio;

    private javax.swing.JRadioButton gradientButton;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JSeparator jSeparator3;

    private javax.swing.JLabel miniLabel;

    private javax.swing.JPanel miniPanel;

    private javax.swing.JToggleButton miniToggle1;

    private javax.swing.JToggleButton miniToggle2;

    private javax.swing.JToggleButton miniToggle3;

    private javax.swing.JToggleButton miniToggle4;

    private javax.swing.JToggleButton miniToggle5;

    private javax.swing.JPanel regularPanel;

    private javax.swing.JRadioButton roundRectRadio;

    private javax.swing.JRadioButton segmentedRadio;

    private javax.swing.JLabel smallLabel;

    private javax.swing.JPanel smallPanel;

    private javax.swing.JToggleButton smallToggle1;

    private javax.swing.JToggleButton smallToggle2;

    private javax.swing.JToggleButton smallToggle3;

    private javax.swing.JToggleButton smallToggle4;

    private javax.swing.JToggleButton smallToggle5;

    private javax.swing.JRadioButton squareButton;

    private javax.swing.JPanel stretcherPanel;

    private javax.swing.JRadioButton texturedRadio;

    private javax.swing.JToggleButton toggle1;

    private javax.swing.JToggleButton toggle2;

    private javax.swing.JToggleButton toggle3;

    private javax.swing.JToggleButton toggle4;

    private javax.swing.JToggleButton toggle5;

    private javax.swing.ButtonGroup typeGroup;
}

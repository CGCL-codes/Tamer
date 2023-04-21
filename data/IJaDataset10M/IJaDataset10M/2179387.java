package sts.gui.prefs;

import sts.gui.*;
import sts.gui.fontsize.*;

/**
 *
 * @author ken
 */
public class FontSizeEditor extends javax.swing.JPanel {

    int size = 0;

    /** Creates new form FontSizeEditor */
    public FontSizeEditor() {
        initComponents();
    }

    public void setFontSizeValue(int size) {
        this.size = size;
        if (size == 0) size = 12;
        fontSizeTextField.setText(Integer.toString(size));
    }

    public int getFontSizeValue() {
        return Integer.parseInt(fontSizeTextField.getText());
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        fontSizeTextField = new javax.swing.JTextField();
        applyButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        setLayout(new java.awt.GridBagLayout());
        fontSizeTextField.setDocument(new kellinwood.meshi.form.UnsignedIntegerTextDocument());
        fontSizeTextField.setText("12");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(fontSizeTextField, gridBagConstraints);
        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(applyButton, gridBagConstraints);
        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(resetButton, gridBagConstraints);
    }

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            setFontSizeValue(0);
            FontSizeManager.setFontSize(0, sts.gui.Main.onlyInstance());
        } catch (Exception x) {
            ErrorDialog.handle(x);
        }
    }

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            size = Integer.parseInt(fontSizeTextField.getText());
            FontSizeManager.setFontSize(size, sts.gui.Main.onlyInstance());
        } catch (Exception x) {
            sts.gui.ErrorDialog.handle(x);
        }
    }

    private javax.swing.JButton applyButton;

    private javax.swing.JTextField fontSizeTextField;

    private javax.swing.JButton resetButton;
}

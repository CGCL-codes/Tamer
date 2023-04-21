package net.sourceforge.sctmf.view.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author  rafael2009_00
 */
public class LetterNumber extends javax.swing.JPanel {

    public static final int JCB_NUMBER = 1;

    public static final int JCB_LETTER = 2;

    /** Creates new form LetterNumber */
    public LetterNumber() {
        initComponents();
        buttonGroup.add(rbLetter);
        buttonGroup.add(rbNumber);
        jcb.setVisible(false);
    }

    public void setCustomTextInBorderTitle(String customText) {
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), customText, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 51, 51)));
    }

    public void hideRadioButtons() {
        rbLetter.setVisible(false);
        rbNumber.setVisible(false);
    }

    public void checkLetraMauscula() {
        jcb.setSelected(true);
    }

    public void showCheck() {
        jcb.setVisible(true);
    }

    private void initComponents() {
        buttonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        cbLetter = new javax.swing.JComboBox();
        cbNumber = new javax.swing.JComboBox();
        jcb = new javax.swing.JCheckBox();
        rbLetter = new javax.swing.JRadioButton();
        rbNumber = new javax.swing.JRadioButton();
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Símbolos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 51, 51)));
        jPanel1.setMaximumSize(new java.awt.Dimension(150, 80));
        jPanel1.setMinimumSize(new java.awt.Dimension(150, 80));
        jPanel1.setPreferredSize(new java.awt.Dimension(150, 80));
        cbLetter.setFont(new java.awt.Font("Lucida Sans Typewriter", 1, 18));
        cbLetter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" }));
        cbLetter.setPreferredSize(new java.awt.Dimension(50, 25));
        jPanel1.add(cbLetter);
        cbNumber.setFont(new java.awt.Font("Lucida Sans Typewriter", 1, 18));
        cbNumber.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" }));
        cbNumber.setPreferredSize(new java.awt.Dimension(50, 25));
        jPanel1.add(cbNumber);
        jcb.setToolTipText("Letra Minúscula");
        jcb.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jcb.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel1.add(jcb);
        rbLetter.setSelected(true);
        rbLetter.setText("A-Z");
        rbLetter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbLetter.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel1.add(rbLetter);
        rbNumber.setText("0-9");
        rbNumber.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbNumber.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel1.add(rbNumber);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE));
    }

    public void enableCbNumber(boolean arg) {
        this.cbNumber.setEnabled(arg);
        this.rbNumber.setEnabled(arg);
    }

    public void enableCbLetter(boolean arg) {
        this.cbLetter.setEnabled(arg);
        this.rbLetter.setEnabled(arg);
    }

    public void enableRbNumber(boolean arg) {
        this.rbNumber.setEnabled(arg);
    }

    public void enableRbLetter(boolean arg) {
        this.rbLetter.setEnabled(arg);
    }

    private Character getLetter() {
        char c = this.cbLetter.getSelectedItem().toString().charAt(0);
        if (this.jcb.isSelected()) return Character.toLowerCase(c); else return c;
    }

    private Character getNumber() {
        return this.cbNumber.getSelectedItem().toString().charAt(0);
    }

    public Character getValue() {
        if (rbLetter.isSelected()) return this.getLetter(); else return this.getNumber();
    }

    public String getLetterAndNumber() {
        return "" + getLetter() + getNumber();
    }

    public void addItem(String s) {
        List<String> l = new ArrayList<String>();
        l.add(s);
        int size = cbLetter.getModel().getSize();
        for (int i = 0; i < size; i++) l.add(cbLetter.getItemAt(i).toString());
        this.cbLetter.removeAllItems();
        Collections.sort(l);
        for (String str : l) this.cbLetter.addItem(str);
    }

    public void removeItem(Object o) {
        this.cbLetter.removeItem(o.toString());
    }

    /**
     * Forca as letras serem todas min�sculas
     */
    public void forceAllLowerCase(boolean status) {
        if (status) {
            this.jcb.setSelected(true);
            this.jcb.setEnabled(false);
        } else {
            this.jcb.setSelected(false);
            this.jcb.setEnabled(true);
        }
    }

    private javax.swing.ButtonGroup buttonGroup;

    private javax.swing.JComboBox cbLetter;

    private javax.swing.JComboBox cbNumber;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JCheckBox jcb;

    private javax.swing.JRadioButton rbLetter;

    private javax.swing.JRadioButton rbNumber;
}

package jrackattack.gui;

import java.awt.Dimension;
import jonkoshare.util.VersionInformation;

/**
 *
 * @author  methke01
 */
@VersionInformation(lastChanged = "$LastChangedDate: 2009-07-25 05:59:33 -0400 (Sat, 25 Jul 2009) $", authors = { "Alexander Methke" }, revision = "$LastChangedRevision: 11 $", lastEditor = "$LastChangedBy: onkobu $", id = "$Id")
public class ImportSoundDialog extends javax.swing.JDialog implements Controller {

    /** Creates new form ImportSoundDialog */
    public ImportSoundDialog(java.awt.Frame parent) {
        this(parent, true);
        buttonPanel.setController(this);
    }

    public ImportSoundDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("Import Sound(s)");
    }

    public void okAction() {
        setVisible(false);
    }

    public void cancelAction() {
        jCheckBox1.setSelected(false);
        jCheckBox2.setSelected(false);
        jCheckBox3.setSelected(false);
        jCheckBox4.setSelected(false);
        jCheckBox5.setSelected(false);
        jCheckBox6.setSelected(false);
        jCheckBox7.setSelected(false);
        jCheckBox8.setSelected(false);
        jCheckBox9.setSelected(false);
        jCheckBox10.setSelected(false);
        jCheckBox11.setSelected(false);
        jCheckBox12.setSelected(false);
        jCheckBox13.setSelected(false);
        jCheckBox14.setSelected(false);
        jCheckBox15.setSelected(false);
        jCheckBox16.setSelected(false);
        jCheckBox17.setSelected(false);
        jCheckBox18.setSelected(false);
        jCheckBox19.setSelected(false);
        jCheckBox20.setSelected(false);
        jCheckBox21.setSelected(false);
        jCheckBox22.setSelected(false);
        jCheckBox23.setSelected(false);
        jCheckBox24.setSelected(false);
        jCheckBox25.setSelected(false);
        jCheckBox27.setSelected(false);
        setVisible(false);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        jCheckBox13 = new javax.swing.JCheckBox();
        jCheckBox14 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jCheckBox16 = new javax.swing.JCheckBox();
        jCheckBox17 = new javax.swing.JCheckBox();
        jCheckBox18 = new javax.swing.JCheckBox();
        jCheckBox19 = new javax.swing.JCheckBox();
        jCheckBox20 = new javax.swing.JCheckBox();
        jCheckBox21 = new javax.swing.JCheckBox();
        jCheckBox22 = new javax.swing.JCheckBox();
        jCheckBox23 = new javax.swing.JCheckBox();
        jCheckBox24 = new javax.swing.JCheckBox();
        jCheckBox25 = new javax.swing.JCheckBox();
        jCheckBox26 = new javax.swing.JCheckBox();
        jCheckBox27 = new javax.swing.JCheckBox();
        buttonPanel = new jrackattack.gui.ButtonPanel();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(350, 250));
        setModal(true);
        jTextArea1.setColumns(40);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jrackattack/gui");
        jTextArea1.setText(bundle.getString("text.import_sound"));
        jScrollPane1.setViewportView(jTextArea1);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jScrollPane1, gridBagConstraints);
        jCheckBox1.setText("1");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox1, gridBagConstraints);
        jCheckBox2.setText("2");
        jCheckBox2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox2, gridBagConstraints);
        jCheckBox3.setText("3");
        jCheckBox3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox3, gridBagConstraints);
        jCheckBox4.setText("4");
        jCheckBox4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox4, gridBagConstraints);
        jCheckBox5.setText("5");
        jCheckBox5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox5, gridBagConstraints);
        jCheckBox6.setText("6");
        jCheckBox6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox6.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox6, gridBagConstraints);
        jCheckBox7.setText("7");
        jCheckBox7.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox7.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox7, gridBagConstraints);
        jCheckBox8.setText("8");
        jCheckBox8.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox8.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox8, gridBagConstraints);
        jCheckBox9.setText("9");
        jCheckBox9.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox9.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox9, gridBagConstraints);
        jCheckBox10.setText("10");
        jCheckBox10.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox10.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox10, gridBagConstraints);
        jCheckBox11.setText("11");
        jCheckBox11.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox11.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox11, gridBagConstraints);
        jCheckBox12.setText("12");
        jCheckBox12.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox12.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox12, gridBagConstraints);
        jCheckBox13.setText("13");
        jCheckBox13.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox13.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox13, gridBagConstraints);
        jCheckBox14.setText("14");
        jCheckBox14.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox14.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox14, gridBagConstraints);
        jCheckBox15.setText("15");
        jCheckBox15.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox15.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox15, gridBagConstraints);
        jCheckBox16.setText("16");
        jCheckBox16.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox16.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox16, gridBagConstraints);
        jCheckBox17.setText("17");
        jCheckBox17.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox17.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox17, gridBagConstraints);
        jCheckBox18.setText("18");
        jCheckBox18.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox18.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox18, gridBagConstraints);
        jCheckBox19.setText("19");
        jCheckBox19.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox19.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox19, gridBagConstraints);
        jCheckBox20.setText("20");
        jCheckBox20.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox20.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox20, gridBagConstraints);
        jCheckBox21.setText("21");
        jCheckBox21.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox21.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox21, gridBagConstraints);
        jCheckBox22.setText("22");
        jCheckBox22.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox22.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox22, gridBagConstraints);
        jCheckBox23.setText("23");
        jCheckBox23.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox23.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox23, gridBagConstraints);
        jCheckBox24.setText("24");
        jCheckBox24.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox24.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox24, gridBagConstraints);
        jCheckBox25.setText(bundle.getString("checkbox.all"));
        jCheckBox25.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox25.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox25.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox25ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox25, gridBagConstraints);
        jCheckBox26.setText(bundle.getString("checkbox.none"));
        jCheckBox26.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox26.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox26.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox26ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox26, gridBagConstraints);
        jCheckBox27.setText(bundle.getString("checkbox.invert"));
        jCheckBox27.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox27.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox27.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox27ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(jCheckBox27, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(buttonPanel, gridBagConstraints);
        pack();
    }

    private void jCheckBox27ItemStateChanged(java.awt.event.ItemEvent evt) {
        if (jCheckBox27.isSelected()) {
            jCheckBox1.setSelected(!jCheckBox1.isSelected());
            jCheckBox2.setSelected(!jCheckBox2.isSelected());
            jCheckBox3.setSelected(!jCheckBox3.isSelected());
            jCheckBox4.setSelected(!jCheckBox4.isSelected());
            jCheckBox5.setSelected(!jCheckBox5.isSelected());
            jCheckBox6.setSelected(!jCheckBox6.isSelected());
            jCheckBox7.setSelected(!jCheckBox7.isSelected());
            jCheckBox8.setSelected(!jCheckBox8.isSelected());
            jCheckBox9.setSelected(!jCheckBox9.isSelected());
            jCheckBox10.setSelected(!jCheckBox10.isSelected());
            jCheckBox11.setSelected(!jCheckBox11.isSelected());
            jCheckBox12.setSelected(!jCheckBox12.isSelected());
            jCheckBox13.setSelected(!jCheckBox13.isSelected());
            jCheckBox14.setSelected(!jCheckBox14.isSelected());
            jCheckBox15.setSelected(!jCheckBox15.isSelected());
            jCheckBox16.setSelected(!jCheckBox16.isSelected());
            jCheckBox17.setSelected(!jCheckBox17.isSelected());
            jCheckBox18.setSelected(!jCheckBox18.isSelected());
            jCheckBox19.setSelected(!jCheckBox19.isSelected());
            jCheckBox20.setSelected(!jCheckBox20.isSelected());
            jCheckBox21.setSelected(!jCheckBox21.isSelected());
            jCheckBox22.setSelected(!jCheckBox22.isSelected());
            jCheckBox23.setSelected(!jCheckBox23.isSelected());
            jCheckBox24.setSelected(!jCheckBox24.isSelected());
            jCheckBox25.setSelected(false);
            jCheckBox26.setSelected(false);
        }
    }

    private void jCheckBox26ItemStateChanged(java.awt.event.ItemEvent evt) {
        if (jCheckBox26.isSelected()) {
            jCheckBox1.setSelected(false);
            jCheckBox2.setSelected(false);
            jCheckBox3.setSelected(false);
            jCheckBox4.setSelected(false);
            jCheckBox5.setSelected(false);
            jCheckBox6.setSelected(false);
            jCheckBox7.setSelected(false);
            jCheckBox8.setSelected(false);
            jCheckBox9.setSelected(false);
            jCheckBox10.setSelected(false);
            jCheckBox11.setSelected(false);
            jCheckBox12.setSelected(false);
            jCheckBox13.setSelected(false);
            jCheckBox14.setSelected(false);
            jCheckBox15.setSelected(false);
            jCheckBox16.setSelected(false);
            jCheckBox17.setSelected(false);
            jCheckBox18.setSelected(false);
            jCheckBox19.setSelected(false);
            jCheckBox20.setSelected(false);
            jCheckBox21.setSelected(false);
            jCheckBox22.setSelected(false);
            jCheckBox23.setSelected(false);
            jCheckBox24.setSelected(false);
            jCheckBox25.setSelected(false);
            jCheckBox27.setSelected(false);
        }
    }

    private void jCheckBox25ItemStateChanged(java.awt.event.ItemEvent evt) {
        if (jCheckBox25.isSelected()) {
            jCheckBox1.setSelected(true);
            jCheckBox2.setSelected(true);
            jCheckBox3.setSelected(true);
            jCheckBox4.setSelected(true);
            jCheckBox5.setSelected(true);
            jCheckBox6.setSelected(true);
            jCheckBox7.setSelected(true);
            jCheckBox8.setSelected(true);
            jCheckBox9.setSelected(true);
            jCheckBox10.setSelected(true);
            jCheckBox11.setSelected(true);
            jCheckBox12.setSelected(true);
            jCheckBox13.setSelected(true);
            jCheckBox14.setSelected(true);
            jCheckBox15.setSelected(true);
            jCheckBox16.setSelected(true);
            jCheckBox17.setSelected(true);
            jCheckBox18.setSelected(true);
            jCheckBox19.setSelected(true);
            jCheckBox20.setSelected(true);
            jCheckBox21.setSelected(true);
            jCheckBox22.setSelected(true);
            jCheckBox23.setSelected(true);
            jCheckBox24.setSelected(true);
            jCheckBox26.setSelected(false);
            jCheckBox27.setSelected(false);
        }
    }

    public boolean[] getParameterList() {
        setResizable(true);
        setPreferredSize(new Dimension(300, 250));
        setMinimumSize(getPreferredSize());
        setVisible(true);
        boolean[] res = new boolean[24];
        if (jCheckBox1.isSelected()) {
            res[0] = true;
        }
        if (jCheckBox2.isSelected()) {
            res[1] = true;
        }
        if (jCheckBox3.isSelected()) {
            res[2] = true;
        }
        if (jCheckBox4.isSelected()) {
            res[3] = true;
        }
        if (jCheckBox5.isSelected()) {
            res[4] = true;
        }
        if (jCheckBox6.isSelected()) {
            res[5] = true;
        }
        if (jCheckBox7.isSelected()) {
            res[6] = true;
        }
        if (jCheckBox8.isSelected()) {
            res[7] = true;
        }
        if (jCheckBox9.isSelected()) {
            res[8] = true;
        }
        if (jCheckBox10.isSelected()) {
            res[9] = true;
        }
        if (jCheckBox11.isSelected()) {
            res[10] = true;
        }
        if (jCheckBox12.isSelected()) {
            res[11] = true;
        }
        if (jCheckBox13.isSelected()) {
            res[12] = true;
        }
        if (jCheckBox14.isSelected()) {
            res[13] = true;
        }
        if (jCheckBox15.isSelected()) {
            res[14] = true;
        }
        if (jCheckBox16.isSelected()) {
            res[15] = true;
        }
        if (jCheckBox17.isSelected()) {
            res[16] = true;
        }
        if (jCheckBox18.isSelected()) {
            res[17] = true;
        }
        if (jCheckBox19.isSelected()) {
            res[18] = true;
        }
        if (jCheckBox20.isSelected()) {
            res[19] = true;
        }
        if (jCheckBox21.isSelected()) {
            res[20] = true;
        }
        if (jCheckBox22.isSelected()) {
            res[21] = true;
        }
        if (jCheckBox23.isSelected()) {
            res[22] = true;
        }
        if (jCheckBox24.isSelected()) {
            res[23] = true;
        }
        return res;
    }

    /**
	 * @param args the command line arguments
	 */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ImportSoundDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    private jrackattack.gui.ButtonPanel buttonPanel;

    private javax.swing.JCheckBox jCheckBox1;

    private javax.swing.JCheckBox jCheckBox10;

    private javax.swing.JCheckBox jCheckBox11;

    private javax.swing.JCheckBox jCheckBox12;

    private javax.swing.JCheckBox jCheckBox13;

    private javax.swing.JCheckBox jCheckBox14;

    private javax.swing.JCheckBox jCheckBox15;

    private javax.swing.JCheckBox jCheckBox16;

    private javax.swing.JCheckBox jCheckBox17;

    private javax.swing.JCheckBox jCheckBox18;

    private javax.swing.JCheckBox jCheckBox19;

    private javax.swing.JCheckBox jCheckBox2;

    private javax.swing.JCheckBox jCheckBox20;

    private javax.swing.JCheckBox jCheckBox21;

    private javax.swing.JCheckBox jCheckBox22;

    private javax.swing.JCheckBox jCheckBox23;

    private javax.swing.JCheckBox jCheckBox24;

    private javax.swing.JCheckBox jCheckBox25;

    private javax.swing.JCheckBox jCheckBox26;

    private javax.swing.JCheckBox jCheckBox27;

    private javax.swing.JCheckBox jCheckBox3;

    private javax.swing.JCheckBox jCheckBox4;

    private javax.swing.JCheckBox jCheckBox5;

    private javax.swing.JCheckBox jCheckBox6;

    private javax.swing.JCheckBox jCheckBox7;

    private javax.swing.JCheckBox jCheckBox8;

    private javax.swing.JCheckBox jCheckBox9;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea jTextArea1;
}

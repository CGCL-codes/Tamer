package medisnap.gui;

import javax.swing.*;
import javax.swing.text.*;
import medisnap.*;
import java.awt.event.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.util.*;

public class panelEditor extends javax.swing.JPanel implements medisnap.gui.isvisible {

    public static final long serialVersionUID = 1;

    private static final int comboFontSize = 12;

    private Integer fontsize;

    /** Creates new form panelEditor */
    public panelEditor() {
        initComponents();
        this.jcFontSize.addActionListener(new FontSizeListener(jcFontSize));
        jbFett.addActionListener(new StyledEditorKit.BoldAction());
        jbKursiv.addActionListener(new StyledEditorKit.ItalicAction());
        jbUnterstrichen.addActionListener(new StyledEditorKit.UnderlineAction());
        jbCut.addActionListener(new DefaultEditorKit.CutAction());
        jbPaste.addActionListener(new DefaultEditorKit.PasteAction());
        jbCopy.addActionListener(new DefaultEditorKit.CopyAction());
        jbSize6.addActionListener(new StyledEditorKit.FontSizeAction("xxx", 6));
        jbSize8.addActionListener(new StyledEditorKit.FontSizeAction("xxx", 8));
        jbSize10.addActionListener(new StyledEditorKit.FontSizeAction("xxx", 10));
        jbSize12.addActionListener(new StyledEditorKit.FontSizeAction("xxx", 12));
        jbSize16.addActionListener(new StyledEditorKit.FontSizeAction("xxx", 16));
        String[] x = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel(x);
        jcFont.setModel(dcbm);
        jcFont.setFont(new Font(x[0], 0, comboFontSize));
        jcFont.setRenderer(new DefaultListCellRenderer() {

            public static final long serialVersionUID = 1;

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Font font = new Font((String) value, 0, comboFontSize);
                JLabel label = (JLabel) super.getListCellRendererComponent(list, (String) value, index, isSelected, cellHasFocus);
                label.setFont(font);
                return label;
            }
        });
        jcFont.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                javax.swing.text.StyledEditorKit.FontFamilyAction action = new javax.swing.text.StyledEditorKit.FontFamilyAction("xxx", (String) jcFont.getSelectedItem());
                jcFont.setFont(new Font((String) jcFont.getSelectedItem(), 0, comboFontSize));
                action.actionPerformed(new ActionEvent(jcFont, ActionEvent.ACTION_PERFORMED, "xxx"));
            }
        });
        Integer[] fontsizes = new Integer[20];
        for (int i = 0; i < 20; i++) {
            fontsizes[i] = new Integer(i + 4);
        }
        DefaultComboBoxModel dcbmfs = new DefaultComboBoxModel(fontsizes);
        jcFontSize.setModel(dcbmfs);
    }

    public void isNowVisible() {
    }

    private void initComponents() {
        jToolBar1 = new javax.swing.JToolBar();
        jcFont = new javax.swing.JComboBox();
        jcFontSize = new javax.swing.JComboBox();
        jbCopy = new javax.swing.JButton();
        jbCut = new javax.swing.JButton();
        jbPaste = new javax.swing.JButton();
        jbFett = new javax.swing.JButton();
        jbKursiv = new javax.swing.JButton();
        jbUnterstrichen = new javax.swing.JButton();
        jbSize6 = new javax.swing.JButton();
        jbSize8 = new javax.swing.JButton();
        jbSize10 = new javax.swing.JButton();
        jbSize12 = new javax.swing.JButton();
        jbSize16 = new javax.swing.JButton();
        setOpaque(false);
        setLayout(new java.awt.BorderLayout());
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setFocusable(false);
        jToolBar1.setOpaque(false);
        jcFont.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jcFont.setFocusable(false);
        jcFont.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcFontActionPerformed(evt);
            }
        });
        jToolBar1.add(jcFont);
        jcFontSize.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jcFontSize.setFocusable(false);
        jToolBar1.add(jcFontSize);
        jbCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/Copy24.gif")));
        jbCopy.setActionCommand("Kopieren");
        jbCopy.setFocusable(false);
        jToolBar1.add(jbCopy);
        jbCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/Cut24.gif")));
        jbCut.setActionCommand("Ausschneiden");
        jbCut.setFocusable(false);
        jToolBar1.add(jbCut);
        jbPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/Paste24.gif")));
        jbPaste.setActionCommand("Einfuegen");
        jbPaste.setFocusable(false);
        jToolBar1.add(jbPaste);
        jbFett.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/Bold24.gif")));
        jbFett.setFocusable(false);
        jToolBar1.add(jbFett);
        jbKursiv.setFont(new java.awt.Font("Dialog", 2, 12));
        jbKursiv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/Italic24.gif")));
        jbKursiv.setFocusable(false);
        jToolBar1.add(jbKursiv);
        jbUnterstrichen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medisnap/gui/ressources/Underline24.gif")));
        jbUnterstrichen.setFocusable(false);
        jToolBar1.add(jbUnterstrichen);
        jbSize6.setFont(new java.awt.Font("Dialog", 0, 10));
        jbSize6.setText("6");
        jToolBar1.add(jbSize6);
        jbSize8.setFont(new java.awt.Font("Dialog", 0, 10));
        jbSize8.setText("8");
        jToolBar1.add(jbSize8);
        jbSize10.setFont(new java.awt.Font("Dialog", 0, 10));
        jbSize10.setText("10");
        jToolBar1.add(jbSize10);
        jbSize12.setFont(new java.awt.Font("Dialog", 0, 12));
        jbSize12.setText("12");
        jToolBar1.add(jbSize12);
        jbSize16.setFont(new java.awt.Font("Dialog", 0, 16));
        jbSize16.setText("16");
        jToolBar1.add(jbSize16);
        add(jToolBar1, java.awt.BorderLayout.NORTH);
    }

    private void jcFontActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JToolBar jToolBar1;

    private javax.swing.JButton jbCopy;

    private javax.swing.JButton jbCut;

    private javax.swing.JButton jbFett;

    private javax.swing.JButton jbKursiv;

    private javax.swing.JButton jbPaste;

    private javax.swing.JButton jbSize10;

    private javax.swing.JButton jbSize12;

    private javax.swing.JButton jbSize16;

    private javax.swing.JButton jbSize6;

    private javax.swing.JButton jbSize8;

    private javax.swing.JButton jbUnterstrichen;

    private javax.swing.JComboBox jcFont;

    private javax.swing.JComboBox jcFontSize;
}

class FontSizeListener implements ActionListener {

    Integer i = 0;

    JComboBox jc;

    public FontSizeListener(JComboBox jc) {
        this.jc = jc;
        jc.addActionListener(new StyledEditorKit.FontSizeAction("xxx", i));
    }

    public void actionPerformed(ActionEvent e) {
        if (jc.getSelectedItem() != null) {
            i = (Integer) jc.getSelectedItem();
        }
    }
}

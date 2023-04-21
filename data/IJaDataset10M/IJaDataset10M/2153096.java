package com.ek.mitapp.ui.panel;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

/**
 * The introduction panel.
 * <p>
 * Id: $Id: $
 *
 * @author Dave Irwin (dirwin@ekmail.com)
 */
public class IntroPanel extends javax.swing.JPanel {

    private JLabel coh_icon_l1;

    private JLabel intro_text_l1;

    private JLabel welcome_l;

    private JLabel dataEntry_l;

    /** Creates new form IntroPanel */
    public IntroPanel() {
        initComponents();
    }

    private void initComponents() {
        setBackground(new java.awt.Color(255, 255, 255));
        welcome_l = new JLabel();
        welcome_l.setFont(new java.awt.Font("Tahoma", 1, 12));
        welcome_l.setText("Welcome to the City of Houston");
        intro_text_l1 = new JLabel();
        intro_text_l1.setIcon(new javax.swing.ImageIcon(getClass().getResource("resources/introText.png")));
        dataEntry_l = new JLabel();
        dataEntry_l.setFont(new java.awt.Font("Tahoma", 1, 12));
        dataEntry_l.setText("A data entry tool");
        coh_icon_l1 = new JLabel();
        ImageIcon coh_icon_i = new ImageIcon(getClass().getResource("resources/coh_header.png"));
        coh_icon_l1.setIcon(coh_icon_i);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(intro_text_l1).add(dataEntry_l).add(welcome_l)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(coh_icon_l1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(coh_icon_l1).add(layout.createSequentialGroup().add(welcome_l).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(intro_text_l1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(dataEntry_l))).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }
}

package org.perfectday.main.laboratocGUI.model;

import java.awt.Graphics;

/**
 *
 * @author  Miguel Angel Lopez Montellano ( alakat@gmail.com )
 */
public class PerfectDayMessage extends javax.swing.JPanel {

    /** Creates new form PerfectDayMessage */
    public PerfectDayMessage() {
        initComponents();
    }

    private void initComponents() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
    }

    @Override
    public void paint(Graphics g) {
    }
}

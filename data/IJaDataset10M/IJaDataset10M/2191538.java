package com.ebixio.virtmus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import org.openide.util.ImageUtilities;

/**
 *
 * @author  gburca
 */
public class About extends javax.swing.JPanel {

    Image splash;

    /** Creates new form NewJPanel */
    public About() {
        initComponents();
        splash = ImageUtilities.loadImage("org/netbeans/core/startup/splash_virtmus.gif");
        int w = splash.getWidth(null);
        int h = splash.getHeight(null);
        this.setPreferredSize(new Dimension(w, h));
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(splash, 0, 0, null);
        g.setColor(Color.WHITE);
        g.drawString("VirtMus version " + MainApp.VERSION + "             For more information visit: http://virtmus.com/", 43, 30);
    }

    private void initComponents() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
    }
}

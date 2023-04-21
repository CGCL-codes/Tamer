package com.ebixio.virtmus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 *
 * @author  gburca
 */
public class Thumbnail extends javax.swing.JPanel {

    private boolean selected = false;

    private MusicPage page;

    private BufferedImage img = null;

    private Color defaultLabelColor;

    private Border defaultBorder;

    /** Creates new form Thumbnail */
    public Thumbnail() {
        initComponents();
        defaultLabelColor = this.label.getBackground();
        defaultBorder = this.getBorder();
        this.setBackground(new Color(255, 255, 255));
    }

    public Thumbnail(int w, int h) {
        this();
        this.setSize(w, h);
        Border b = getBorder();
        h = label.getPreferredSize().height;
        if (b != null) {
            Insets insets = b.getBorderInsets(this);
            label.setSize(w - insets.left - insets.right, h);
        } else {
            label.setSize(w, h);
        }
    }

    public Thumbnail(int w, int h, String description) {
        this(w, h);
        setName(description);
    }

    private void initComponents() {
        label = new javax.swing.JLabel();
        canvas = new Thumb();
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setMinimumSize(new java.awt.Dimension(20, 20));
        label.setFont(new java.awt.Font("Arial", 0, 13));
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label.setText("jLabel1");
        label.setMinimumSize(new java.awt.Dimension(20, 14));
        label.setOpaque(true);
        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 131, Short.MAX_VALUE));
        canvasLayout.setVerticalGroup(canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 132, Short.MAX_VALUE));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE).addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(0, 0, 0).addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
    }

    private javax.swing.JPanel canvas;

    private javax.swing.JLabel label;

    @Override
    public void setName(String name) {
        String origName = name;
        FontMetrics fm = label.getFontMetrics(label.getFont());
        int chars = 0;
        do {
            name = Utils.shortenString(origName, chars++);
        } while (fm.stringWidth(name) > label.getSize().getWidth() && chars < origName.length());
        label.setText(name);
        this.setToolTipText(origName);
    }

    @Override
    public String getName() {
        return (label != null) ? label.getText() : super.getName();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            this.setBorder(BorderFactory.createEtchedBorder(new Color(255, 200, 200), new Color(100, 100, 100)));
            this.label.setBackground(new Color(255, 102, 102));
        } else {
            this.setBorder(defaultBorder);
            this.label.setBackground(defaultLabelColor);
        }
    }

    public MusicPage getPage() {
        return page;
    }

    public void setPage(MusicPage page) {
        this.page = page;
    }

    private class Thumb extends javax.swing.JPanel implements MusicPage.JobRequester {

        boolean imgRequested = false, imgReturned = false;

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (img == null) {
                if (page != null) {
                    if (imgRequested) {
                        if (imgReturned) {
                            paintMsg(g, "No image.");
                        } else {
                            paintMsg(g, "Loading...");
                        }
                    } else {
                        MusicPage.JobRequest req = new MusicPage.JobRequest(this, 0, 10, this.getSize());
                        req.fillSize = true;
                        page.requestRendering(req);
                        imgRequested = true;
                        paintMsg(g, "Loading...");
                    }
                } else {
                    paintMsg(g, "No image.");
                }
            } else {
                g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), this);
            }
        }

        private void paintMsg(Graphics g, String msg) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            int msgW = g.getFontMetrics().stringWidth(msg);
            g.drawString(msg, getWidth() / 2 - msgW / 2, getHeight() / 2);
        }

        @Override
        public void renderingComplete(MusicPage mp, MusicPage.JobRequest request) {
            img = mp.getRenderedImage(this);
            imgReturned = true;
            if (img != null && this.isShowing()) this.repaint();
        }
    }
}

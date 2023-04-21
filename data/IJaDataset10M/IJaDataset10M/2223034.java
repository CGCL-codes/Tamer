package eu.irreality.age.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import com.kitfox.svg.app.beans.SVGIcon;
import eu.irreality.age.ImageConstants;

public class FancyJTextPane extends JTextPane implements ImageConstants {

    private ImageIcon rasterBackgroundImage;

    private SVGIcon vectorBackgroundImage;

    private BufferedImage upperSubImage;

    private BufferedImage lowerSubImage;

    private boolean marginsOnViewableArea = false;

    public void setMarginsOnViewableArea(boolean value) {
        marginsOnViewableArea = value;
    }

    public ImageIcon getRasterBackgroundImage() {
        return rasterBackgroundImage;
    }

    public SVGIcon getVectorBackgroundImage() {
        return vectorBackgroundImage;
    }

    private void refreshUpperSubImage() {
        Rectangle rect = getVisibleRect();
        upperSubImage = new BufferedImage(rect.width, getMargin().top, BufferedImage.TYPE_INT_ARGB);
        Graphics tempG = upperSubImage.createGraphics();
        tempG.drawImage(rasterBackgroundImage.getImage(), 0, 0, rect.width, rect.height, this);
        tempG.dispose();
    }

    private void refreshLowerSubImage() {
        Rectangle rect = getVisibleRect();
        lowerSubImage = new BufferedImage(rect.width, getMargin().bottom, BufferedImage.TYPE_INT_ARGB);
        Graphics tempG = lowerSubImage.createGraphics();
        tempG.drawImage(rasterBackgroundImage.getImage(), 0, -rect.height + getMargin().bottom, rect.width, rect.height, this);
        tempG.dispose();
    }

    public void setRasterBackgroundImage(ImageIcon i) {
        this.rasterBackgroundImage = i;
        if (rasterBackgroundImage != null || vectorBackgroundImage != null) setOpaque(false); else setOpaque(true);
        if (rasterBackgroundImage != null) {
            refreshUpperSubImage();
            refreshLowerSubImage();
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                repaint();
            }
        });
    }

    public void setVectorBackgroundImage(Icon i) {
        this.vectorBackgroundImage = (SVGIcon) i;
        if (vectorBackgroundImage != null || rasterBackgroundImage != null) setOpaque(false); else setOpaque(true);
        vectorBackgroundImage.setAntiAlias(true);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                repaint();
            }
        });
    }

    public void setBackgroundImage(Icon ic) {
        if (ic == null) {
            if (vectorBackgroundImage != null) setVectorBackgroundImage(null);
            if (rasterBackgroundImage != null) setRasterBackgroundImage(null);
            return;
        }
        if (!(ic instanceof ImageIcon) && !(ic instanceof SVGIcon)) throw new UnsupportedOperationException("setBackgroundImage only supports ImageIcon or SVGIcon"); else if (ic instanceof ImageIcon) {
            vectorBackgroundImage = null;
            setRasterBackgroundImage((ImageIcon) ic);
        } else if (ic instanceof SVGIcon) {
            rasterBackgroundImage = null;
            setVectorBackgroundImage((SVGIcon) ic);
        }
    }

    public FancyJTextPane() {
        super();
        LookAndFeel laf = UIManager.getLookAndFeel();
        if (laf.getID().equals("Nimbus")) {
            setUI(new javax.swing.plaf.basic.BasicEditorPaneUI());
        }
        setDocument(new FancyStyledDocument());
        setBackground(new Color(0, 0, 0, 0));
    }

    private int lastWidth = -1;

    private int lastHeight = -1;

    public void paintComponent(Graphics g) {
        Rectangle rect = null;
        rect = getVisibleRect();
        if (rasterBackgroundImage != null) {
            g.drawImage(rasterBackgroundImage.getImage(), rect.x, rect.y, rect.width, rect.height, this);
        }
        if (vectorBackgroundImage != null) {
            vectorBackgroundImage.setPreferredSize(new Dimension(rect.width, rect.height));
            vectorBackgroundImage.setScaleToFit(true);
            vectorBackgroundImage.paintIcon(this, g, rect.x, rect.y);
        }
        if (rasterBackgroundImage == null && vectorBackgroundImage == null && marginsOnViewableArea && (getMargin().top > 0 || getMargin().bottom > 0)) {
            setOpaque(false);
            Color oldColor = g.getColor();
            g.setColor(getBackground());
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
            g.setColor(oldColor);
        }
        super.paintComponent(g);
        if (marginsOnViewableArea && (getMargin().top > 0 || getMargin().bottom > 0)) {
            if (rasterBackgroundImage != null) {
                if (lastWidth != rect.width || lastHeight != rect.height) {
                    if (getMargin().top > 0) refreshUpperSubImage();
                    if (getMargin().bottom > 0) refreshLowerSubImage();
                }
                lastWidth = rect.width;
                lastHeight = rect.height;
                if (getMargin().top > 0) g.drawImage(upperSubImage, rect.x, rect.y, rect.width, getMargin().top, this);
                if (getMargin().bottom > 0) g.drawImage(lowerSubImage, rect.x, rect.y + rect.height - getMargin().bottom, rect.width, getMargin().bottom, this);
            }
            if (vectorBackgroundImage != null) {
                if (getMargin().top > 0) {
                    g.setClip(rect.x, rect.y, rect.width, getMargin().top);
                    vectorBackgroundImage.setPreferredSize(new Dimension(rect.width, rect.height));
                    vectorBackgroundImage.paintIcon(this, g, rect.x, rect.y);
                }
                if (getMargin().bottom > 0) {
                    g.setClip(rect.x, rect.y + rect.height - getMargin().bottom, rect.width, getMargin().bottom);
                    vectorBackgroundImage.setPreferredSize(new Dimension(rect.width, rect.height));
                    vectorBackgroundImage.paintIcon(this, g, rect.x, rect.y);
                }
            }
            if (rasterBackgroundImage == null && vectorBackgroundImage == null) {
                Color oldColor = g.getColor();
                g.setColor(getBackground());
                g.fillRect(rect.x, rect.y, rect.width, getMargin().top);
                g.fillRect(rect.x, rect.y + rect.height - getMargin().bottom, rect.width, getMargin().bottom);
                g.setColor(oldColor);
            }
        }
    }
}

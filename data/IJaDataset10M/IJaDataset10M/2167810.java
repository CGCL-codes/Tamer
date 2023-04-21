package org.freehep.graphics2d;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterGraphics;
import javax.swing.JPanel;

/**
 * This class extends JPanel by adding double buffering. This is intended to be
 * used in situations in which redrawing the contents of the panel is expensive.
 * 
 * @author Mark Donszelmann
 * @version $Id: BufferedPanel.java 8584 2006-08-10 23:06:37Z duns $
 */
public class BufferedPanel extends JPanel implements java.io.Serializable {

    private VectorGraphics offScreenGraphics;

    private Image offScreenImage;

    private Dimension oldDimension = new Dimension();

    private Dimension dim = new Dimension();

    private boolean printing = false;

    private boolean exporting = false;

    private boolean repaint = false;

    public BufferedPanel() {
        this(true);
    }

    /**
     * Creates a new BufferedPanel with a width and height set to zero.
     * 
     * @param opaque transparent panel
     */
    public BufferedPanel(boolean opaque) {
        super(false);
        setOpaque(opaque);
    }

    /**
     * Triggers a full "user" repaint. If the "system" wants to repaint it will
     * call the paint(Graphics) method directly, rather than scheduling a
     * paint(Graphics) through a repaint().
     */
    public void repaint() {
        super.repaint();
        repaint = true;
    }

    /**
     * Triggers a full repaint, since the component is not valid anymore (size
     * change, iconized, ...)
     */
    public void invalidate() {
        super.invalidate();
        repaint = true;
    }

    /**
     * Returns if true if paintComponent(VectorGraphics) should be called (was
     * triggered by a repaint() or invalidate(), and resets the trigger.
     */
    private synchronized boolean shouldRepaint() {
        boolean result = repaint;
        repaint = false;
        return result;
    }

    /**
     * Paint this panel by calling paintComponent(VectorGraphics) if necessary
     * and flushing the buffered image to the screen. This method also handles
     * printing and exporting separately.
     * 
     * @param g Graphics object
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if ((g == null) || (offScreenImage == null)) return;
        if (g instanceof PrinterGraphics) printing = true;
        if (g instanceof VectorGraphics) exporting = true;
        if (!isDisplaying()) {
            paintComponent(VectorGraphics.create(g));
            return;
        }
        if (shouldRepaint()) {
            paintComponent(offScreenGraphics);
        }
        g.drawImage(offScreenImage, 0, 0, this);
    }

    /**
     * Returns a pointer to the graphics (VectorGraphics) context of the buffer.
     * The user is NOT allowed to call dispose() on this graphics object.
     * <P>
     * NOTE: this method used to be called getGraphics, however, since the JVM
     * paint thread may call getGraphics from paintImmediately and fails to work
     * with our VectorGraphics context (the gc is not longer attached to the
     * image), we decided to rename the method.
     */
    public Graphics getBufferedGraphics() {
        return offScreenGraphics;
    }

    /**
     * Allows for custom graphics to be painted. Subclasses should implement
     * real drawing here. They can ask isPrinting(), isExporting() or
     * isDisplaying() to see where the output goes. If painting is done to a
     * display it is done to a buffer which is kept and copied afterwards.
     * 
     * Note that the parameter here is of class VectorGraphics rather than
     * Graphics.
     */
    public void paintComponent(VectorGraphics vg) {
    }

    /**
     * Resize and move a component.
     */
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        makeImage();
    }

    /**
     * Returns true if the drawing is made for a PrinterGraphics context.
     */
    public boolean isPrinting() {
        return printing;
    }

    /**
     * Returns true if the drawing is made for a VectorGraphics context.
     */
    public boolean isExporting() {
        return exporting;
    }

    /**
     * Returns true if the drawing is made for a PixelGraphics context, the
     * display. True if not Printing and not Exporting.
     */
    public boolean isDisplaying() {
        return ((!isExporting()) && (!isPrinting()));
    }

    /**
     * Make the buffered image for this panel. Check to see if the size has
     * changed before doing anything.
     */
    private void makeImage() {
        dim = getSize(dim);
        if (dim.width > 0 && dim.height > 0) {
            if (!oldDimension.equals(dim)) {
                if (isOpaque()) {
                    offScreenImage = super.createImage(dim.width, dim.height);
                } else {
                    offScreenImage = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
                }
                offScreenGraphics = VectorGraphics.create(offScreenImage.getGraphics());
                oldDimension.setSize(dim);
            }
        } else {
            offScreenImage = null;
            offScreenGraphics = null;
        }
    }
}

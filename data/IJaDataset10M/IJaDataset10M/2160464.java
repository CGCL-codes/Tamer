package org.pushingpixels.lafwidget.contrib.blogofbug.swing.borders;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import org.pushingpixels.lafwidget.contrib.blogofbug.utility.ImageUtilities;

/**
 *
 * @author nigel
 */
public class AbstractImageBorder {

    /**
   * Creates a new ImageBofder loading the image from the supplied URL
   * @param imageURL The location of the image to use
   * @param imageInsets The insets around the edge of the image that allow the cookie-cut-and-stretch of the image 
   * around the edge of the border
   */
    public AbstractImageBorder(URL imageURL, Insets imageInsets) {
        this.imageInsets = imageInsets;
        borderImage = ImageUtilities.loadCompatibleImage(imageURL.toString());
    }

    /** 
   * Creates a new ImageBorder using the supplied image and the insets
   * 
   * @param borderImage The image to be used as the border
   * @param imageInsets The insets around the edge of the image that allow the cookie-cut-and-stretch of the image
   * around the edge of the border
   */
    public AbstractImageBorder(BufferedImage borderImage, Insets imageInsets) {
        this.borderImage = borderImage;
        this.imageInsets = imageInsets;
    }

    protected BufferedImage borderImage;

    protected Insets imageInsets;

    /** 
     * Paints the border around the specified component
     * 
     * @param c The component to paint the border on 
     * @param g The graphics context
     * @param x The x offset
     * @param y The y offset
     * @param width The width
     * @param height The height
     */
    public void paintBorder(int compWidth, int compHeight, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        int imageWidth = borderImage.getWidth();
        int imageHeight = borderImage.getHeight();
        drawSlice(g2, 0, 0, imageInsets.left, imageInsets.top, 0, 0);
        drawSlice(g2, imageWidth - imageInsets.right, 0, imageInsets.right, imageInsets.bottom, compWidth - imageInsets.right, 0);
        drawSlice(g2, 0, imageHeight - imageInsets.bottom, imageInsets.left, imageInsets.bottom, 0, compHeight - imageInsets.bottom);
        drawSlice(g2, imageWidth - imageInsets.right, imageHeight - imageInsets.bottom, imageInsets.left, imageInsets.bottom, compWidth - imageInsets.right, compHeight - imageInsets.bottom);
        g2.drawImage(borderImage, 0, imageInsets.top, imageInsets.left, compHeight - imageInsets.bottom, 0, imageInsets.top, imageInsets.left, imageHeight - imageInsets.bottom, null);
        g2.drawImage(borderImage, compWidth - imageInsets.right, imageInsets.top + 6, compWidth, compHeight - imageInsets.bottom, imageWidth - imageInsets.right, imageInsets.top, imageWidth, imageHeight - imageInsets.bottom, null);
        g2.drawImage(borderImage, imageInsets.left, 0, compWidth - imageInsets.left, imageInsets.top, imageInsets.left, 0, imageWidth - imageInsets.right, imageInsets.top, null);
        g2.drawImage(borderImage, imageInsets.left, compHeight - imageInsets.bottom, compWidth - imageInsets.left, compHeight, imageInsets.left, imageHeight - imageInsets.bottom, imageWidth - imageInsets.right, imageHeight, null);
    }

    /** 
     * Sets the insets around the edge of the image to be used to cookie cut the image into a border
     * 
     * @param insets The edges of the image
     */
    public void setInsets(Insets insets) {
        this.imageInsets = insets;
    }

    public Insets getImageInsets() {
        return (Insets) imageInsets.clone();
    }

    /**
   * Paints a stretched version of the center of the image (as the border is drawn
   * first, then the component paints itself) so that the component can use it in 
   * its own paint if the border lends itself to having a centre area over-painted
   *
   * @param g2 The graphics context
   * @param c The component
   */
    public void paintCenter(Graphics2D g2, int compWidth, int compHeight) {
        int imageWidth = borderImage.getWidth();
        int imageHeight = borderImage.getHeight();
        g2.drawImage(borderImage, imageInsets.left, imageInsets.top, compWidth - imageInsets.right, compHeight - imageInsets.bottom, imageInsets.left, imageInsets.top, imageWidth - imageInsets.right, imageHeight - imageInsets.bottom, null);
    }

    /**
   * Draws a slicde from the specified image onto the graphics area
   * 
   * @param g2 The graphics context to draw into
   * @param sliceX The x-cordinate of the slice
   * @param sliceY The y-cordinate of the slice
   * @param sliceWidth The width of the slice
   * @param sliceHeight The height of the slice
   * @param destX The target location of the drawn slice
   * @param destY The target location of the drawn slice
   */
    private void drawSlice(Graphics2D g2, int sliceX, int sliceY, int sliceWidth, int sliceHeight, int destX, int destY) {
        g2.drawImage(borderImage, destX, destY, destX + sliceWidth, destY + sliceHeight, sliceX, sliceY, sliceX + sliceWidth, sliceY + sliceHeight, null);
    }
}

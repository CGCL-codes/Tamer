package org.joone.samples.editor.som;

import java.awt.event.*;

/**
 *
 * @author  Julien Norman
 */
public class ImagePainter extends ImageDrawer {

    private java.awt.Color drawColor = new java.awt.Color(0);

    private java.awt.image.BufferedImage ImageToEdit = null;

    /** Creates a new instance of ImageDrawer */
    public ImagePainter() {
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                paintPixel(e.getX(), e.getY());
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {
                paintPixel(e.getX(), e.getY());
            }
        });
    }

    public void setDrawColor(java.awt.Color newColor) {
        drawColor = newColor;
    }

    public java.awt.Color getDrawColor() {
        return (drawColor);
    }

    public void paintPixel(int x, int y) {
        if (x < getImageToDraw().getWidth(this)) {
            if (x >= 0) {
                if (y < getImageToDraw().getHeight(this)) {
                    if (y >= 0) {
                        getImageToEdit().setRGB(x, y, getDrawColor().getRGB());
                        repaint(0, 0, getImageToDraw().getWidth(this), getImageToDraw().getHeight(this));
                    }
                }
            }
        }
    }

    /** Getter for property ImageToEdit.
     * @return Value of property ImageToEdit.
     *
     */
    public java.awt.image.BufferedImage getImageToEdit() {
        return ImageToEdit;
    }

    /** Setter for property ImageToEdit.
     * @param ImageToEdit New value of property ImageToEdit.
     *
     */
    public void setImageToEdit(java.awt.image.BufferedImage ImageToEdit) {
        this.ImageToEdit = ImageToEdit;
        setImageToDraw(ImageToEdit);
    }
}

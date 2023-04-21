package es.eucm.eadventure.editor.gui.otherpanels.imagepanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.AssetsController;

/**
 * This panel holds an image inside, painted with its own aspect ratio.
 * 
 * @author Bruno Torijano Bueno
 */
public class ImagePanel extends JPanel {

    /**
     * Required.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Margin for the image.
     */
    protected static final int MARGIN = 20;

    /**
     * Image to show.
     */
    protected Image image;

    /**
     * X position of the image in the panel.
     */
    protected int x;

    /**
     * Y position of the image in the panel.
     */
    protected int y;

    /**
     * Width of the image.
     */
    protected int width;

    /**
     * Height of the image.
     */
    protected int height;

    /**
     * Size ratio of the image.
     */
    protected double sizeRatio;

    /**
     * Constructor.
     */
    public ImagePanel() {
        super();
        image = null;
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                calculateSize();
            }
        });
        setLayout(new GridBagLayout());
        add(new JLabel(TC.get("ImagePanel.ImageNotAvalaible")));
    }

    /**
     * Constructor.
     * 
     * @param imagePath
     *            Path of the image
     */
    public ImagePanel(String imagePath) {
        this();
        loadImage(imagePath);
    }

    /**
     * Removes the current image from the panel.
     */
    public void removeImage() {
        if (image != null) image.flush();
        image = null;
        removeAll();
        add(new JLabel(TC.get("ImagePanel.ImageNotAvalaible")));
        revalidate();
        repaint();
    }

    /**
     * Loads the given image in the panel.
     * 
     * @param imagePath
     *            Path of the image
     */
    public void loadImage(String imagePath) {
        if (image != null) image.flush();
        if (imagePath != null && imagePath.length() > 0) image = AssetsController.getImage(imagePath); else image = null;
        calculateSize();
        removeAll();
        if (!isImageLoaded()) {
            add(new JLabel(TC.get("ImagePanel.ImageNotAvalaible")));
            revalidate();
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (isImageLoaded()) {
            g.drawImage(image, x, y, width, height, this);
        }
    }

    /**
     * Returns whether the image is loaded or not.
     * 
     * @return True if the image was loaded, false otherwise
     */
    public boolean isImageLoaded() {
        return image != null;
    }

    /**
     * Returns the relative position in the image.
     * 
     * @param x
     *            X position of the panel
     * @return X position of the image
     */
    public int getRelativeX(int x) {
        int relativeX = 0;
        if (isImageLoaded()) relativeX = (int) ((x - this.x) / sizeRatio);
        return relativeX;
    }

    /**
     * Returns the relative position in the image.
     * 
     * @param y
     *            Y position of the panel
     * @return Y position of the image
     */
    public int getRelativeY(int y) {
        int relativeY = 0;
        if (isImageLoaded()) relativeY = (int) ((y - this.y) / sizeRatio);
        return relativeY;
    }

    /**
     * Returns the absolute position in the image of the given relative
     * position.
     * 
     * @param x
     *            X position of the image
     * @return X position of the panel
     */
    public int getAbsoluteX(int x) {
        int absoluteX = 0;
        if (isImageLoaded()) absoluteX = (int) ((x * sizeRatio) + this.x);
        return absoluteX;
    }

    /**
     * Returns the absolute position in the image of the given relative
     * position.
     * 
     * @param y
     *            Y position of the image
     * @return Y position of the panel
     */
    public int getAbsoluteY(int y) {
        int absoluteY = 0;
        if (isImageLoaded()) absoluteY = (int) ((y * sizeRatio) + this.y);
        return absoluteY;
    }

    /**
     * Returns the absolute width in the image of the given relative width.
     * 
     * @param width
     *            Width of the image
     * @return Width in the panel
     */
    protected int getAbsoluteWidth(int width) {
        int absoluteWidth = 0;
        if (isImageLoaded()) absoluteWidth = (int) (width * sizeRatio);
        return absoluteWidth;
    }

    /**
     * Returns the absolute height in the image of the given relative height.
     * 
     * @param height
     *            Height of the image
     * @return Height in the panel
     */
    protected int getAbsoluteHeight(int height) {
        int absoluteHeight = 0;
        if (isImageLoaded()) absoluteHeight = (int) (height * sizeRatio);
        return absoluteHeight;
    }

    /**
     * Paints an rescaled image in the given graphics.
     * 
     * @param g
     *            Graphics to paint
     * @param image
     *            Image to be painted
     * @param x
     *            Absolute X position of the center of the image
     * @param y
     *            Absolute Y position of the bottom of the image
     * @param highlighted
     *            True if the image must be painted with a border
     */
    protected void paintRelativeImage(Graphics g, Image image, int x, int y, float scale, boolean highlighted) {
        if (isImageLoaded()) {
            int width = (int) (image.getWidth(null) * sizeRatio * scale);
            int height = (int) (image.getHeight(null) * sizeRatio * scale);
            int posX = getAbsoluteX((int) (x - (image.getWidth(null) * scale / 2)));
            int posY = getAbsoluteY((int) (y - image.getHeight(null) * scale));
            g.drawImage(image, posX, posY, width, height, null);
            if (highlighted) {
                g.setColor(Color.BLACK);
                g.drawRect(posX - 1, posY - 1, width + 2, height + 2);
                g.drawRect(posX - 3, posY - 3, width + 6, height + 6);
                g.setColor(Color.RED);
                g.drawRect(posX - 2, posY - 2, width + 4, height + 4);
            }
        }
    }

    /**
     * Calculates and stores the size of the current image.
     */
    private synchronized void calculateSize() {
        if (isImageLoaded() && getWidth() > 0 && getHeight() > 0) {
            double panelRatio = (double) getWidth() / (double) getHeight();
            double imageRatio = (double) image.getWidth(null) / (double) image.getHeight(null);
            if (panelRatio <= imageRatio) {
                int panelWidth = getWidth() - MARGIN * 2;
                width = panelWidth;
                height = (int) (panelWidth / imageRatio);
            } else {
                int panelHeight = getHeight() - MARGIN * 2;
                width = (int) (panelHeight * imageRatio);
                height = panelHeight;
            }
            x = ((getWidth() - width) / 2);
            y = ((getHeight() - height) / 2);
            sizeRatio = (double) width / (double) image.getWidth(null);
            repaint();
        }
    }

    public Dimension getImageSize() {
        return new Dimension(image.getWidth(null), image.getHeight(null));
    }
}

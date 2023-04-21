package cdox.gui;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * This class reprents a pane (preview, original) to draw a bufferedImage on. This can be
 *used for all purposes as an ImageDialog a FileChooser or ... I don't know think of
 *something. Just construct it with a BufferedImage or null, and set the
 *setPaintImage(BufferedImage bi) method to update the image. The rendering is done in
 *speed. If now image is given (paintImage) a black X is drawn on the preview pane.
 * @author <a href="mailto:cdox@gmx.net">Rutger Bezema, Andreas Schmitz</a>
 * @version May 14th 2002 
 * @see ImageDialog
 */
public class ImagePreview extends JPanel {

    private BufferedImage paintImage, origImage;

    private Dimension size;

    private float scaleFactor;

    private Graphics2D g2d = null;

    private AffineTransform scaled;

    private AffineTransformOp scaledTransform;

    private boolean scaledWidth = false;

    /**
     *Constructs a new ImagePane. If bi!= null the image is automatically resized to fit
     *in a rectangle, smaller than 150*150. With it "original" scaled height and width
     *dimension. bi == null the size of this pane will be 100*100, bi== null if the
     *ImagePreview is used in the FileChooser class. 
     *@param bi the BufferedImage to be drawn on the pane
     */
    public ImagePreview(BufferedImage bi) {
        if (bi != null) {
            origImage = bi;
            scaleFactor = (((float) 150) / ((float) origImage.getWidth()) + ((float) 150) / ((float) origImage.getHeight())) / 2f;
            scaled = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
            scaledTransform = new AffineTransformOp(scaled, new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED));
            resize(origImage);
            size = new Dimension(paintImage.getWidth(), paintImage.getHeight());
        } else {
            setBorder(BorderFactory.createLoweredBevelBorder());
            size = new Dimension(100, 100);
        }
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    /**
     *Sets the image to be painted with the current scaledTransform. Use this method to update
     *a filtered picture etc. bi can be null
     *@param bi a filtered copy of the Original Image.
     */
    public void setPaintImage(BufferedImage bi) {
        if (bi != null) resize(bi);
    }

    /**
     *Sets the new preview image. This can be used to display a range of files which have
     *different sizes, but must be displayed in a 100 x 100 panel.
     *@param bi the new bufferedImage.
     */
    public void setNewPreviewImage(BufferedImage bi) {
        if (bi != null) {
            setScaleFactor(bi);
            resize(bi);
        } else {
            paintImage = null;
        }
    }

    /**
     *Just calculates the AffineTransformOp for the given BufferedImage, so that it is
     *quadratic.
     *@param bi the BufferedImage to be scaled.
     */
    private void setScaleFactor(BufferedImage bi) {
        origImage = bi;
        if (bi.getWidth() > bi.getHeight()) {
            scaleFactor = ((float) getSize().height) / ((float) bi.getWidth());
            scaledWidth = true;
        } else {
            scaleFactor = ((float) getSize().width) / ((float) bi.getHeight());
            scaledWidth = false;
        }
        scaled = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
        scaledTransform = new AffineTransformOp(scaled, new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED));
    }

    /**
     *Returns the current paint image (scaled).
     *@return The scaled paintImage.
     */
    public BufferedImage getCurrentPaintImage() {
        return paintImage;
    }

    /**
     *Returns the current orig image (not scaled).
     *@return The not scaled origImage.
     */
    public BufferedImage getOriginalImage() {
        return origImage;
    }

    /**
     *Scales the given BufferedImage in the current paintImage with the current
     *AffineTransformOp.
     *@param bi the Image to be scaled, and drawn.
     */
    private void resize(BufferedImage bi) {
        paintImage = scaledTransform.filter(bi, null);
    }

    /**
     * Sets the image to null, it is possible to do some garbage collection.
     * @see MyFileChooser
     */
    public void resetImages() {
        origImage = null;
        paintImage = null;
    }

    /**
     *Draws the buffered image on the pane. If no image is selected (see the fileChooser
     *class) a black cross is drawn on the pane.
     *@param g the graphics object to draw on.
     */
    public void paint(Graphics g) {
        super.paint(g);
        g2d = (Graphics2D) g;
        g2d.setPaint(getBackground());
        g2d.fillRect(0, 0, size.width, size.height);
        if (paintImage != null) {
            if (scaledWidth) {
                int y = Math.round(((float) getSize().height - (float) paintImage.getHeight()) / 2f);
                g2d.drawImage(paintImage, 0, y, this);
            } else {
                int x = Math.round(((float) getSize().width - (float) paintImage.getWidth()) / 2f);
                g2d.drawImage(paintImage, x, 0, this);
            }
        } else {
            g2d.setPaint(Color.BLACK);
            g2d.drawLine(0, 0, size.width, size.height);
            g2d.drawLine(size.width, 0, 0, size.height);
        }
        paintBorder(g2d);
    }
}

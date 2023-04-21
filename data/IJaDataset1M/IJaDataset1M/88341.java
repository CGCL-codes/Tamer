package openvend.image;

import java.awt.image.BufferedImage;
import openvend.main.OvLog;
import org.apache.commons.logging.Log;

/**
 * Image action to scale an image to a max. width and height.<p/>
 * 
 * @author Thomas Weckert
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public class OvScaleToFitImageAction extends A_OvImageAction {

    private static Log log = OvLog.getLog(OvScaleToFitImageAction.class);

    private int height;

    private int width;

    public OvScaleToFitImageAction(int width, int height) {
        super();
        this.width = width;
        this.height = height;
    }

    /**
     * @see openvend.image.I_OvImageAction#process(java.awt.image.BufferedImage)
     */
    public BufferedImage process(BufferedImage image) {
        if (log.isDebugEnabled()) {
            log.debug("Fitting image to max. height of " + height + " pixels and max. width of " + width + " pixels");
        }
        return scaleToFit(image, width, height);
    }

    protected BufferedImage scaleToFit(BufferedImage image, int maxWidth, int maxHeight) {
        double scaleX = (double) maxWidth / (double) image.getWidth();
        double scaleY = (double) maxHeight / (double) image.getHeight();
        double scaleFactor = Math.min(scaleX, scaleY);
        int scaledWidth = (int) (scaleFactor * image.getWidth());
        int scaledHeight = (int) (scaleFactor * image.getHeight());
        if (scaleFactor == 1) {
            return image;
        }
        return scaleToSize(image, scaledWidth, scaledHeight);
    }
}

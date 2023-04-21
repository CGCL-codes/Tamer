package tjger.lib;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Some utilties for Gui operations.
 * 
 * @author hagru
 */
public class ImageUtil {

    private ImageUtil() {
        super();
    }

    /**
     * Compares two images because of their file name.
     * 
     * @param img1 First image.
     * @param img2 Second image.
     * @return True if it's the same image.
     */
    public static boolean isEqualImage(ImageIcon img1, ImageIcon img2) {
        if (img1 != null && img2 != null) {
            return img1.getDescription().equals(img2.getDescription());
        }
        return false;
    }

    /**
     * @param imageList A list with images.
     * @return The height of the highest image.
     */
    public static int getMaxImageHeight(ImageIcon[] imageList) {
        int max = 0;
        for (int i = 0; i < imageList.length; i++) {
            if (imageList[i] != null && imageList[i].getIconHeight() > max) {
                max = imageList[i].getIconHeight();
            }
        }
        return max;
    }

    /**
     * @param imageList A list with images.
     * @return The width of the widest image.
     */
    public static int getMaxImageWidth(ImageIcon[] imageList) {
        int max = 0;
        for (int i = 0; i < imageList.length; i++) {
            if (imageList[i] != null && imageList[i].getIconWidth() > max) {
                max = imageList[i].getIconWidth();
            }
        }
        return max;
    }

    /**
     * Puts a image on the given label and scales it if neccessary.
     * If the image is too big it's scaled to the width/height of the label.
     * 
     * @param label The label.
     * @param image The image to display, can be null.
     */
    public static void setImageOnLabel(JLabel label, ImageIcon image) {
        if (label != null) {
            setImageOnLabel(label, image, label.getWidth(), label.getHeight());
        }
    }

    /**
     * Puts a image on the given label and scales it if neccessary.
     * If the image is too big it's scaled to the given width/height.
     * Use this, if the size of the label is unknown when setting the image on it or 
     * if you want to place a text beside the image.
     * 
     * @param label The label.
     * @param image The image to display, can be null.
     * @param width The maximal width of the result image.
     * @param height The maximal height of the result image.
     */
    public static void setImageOnLabel(JLabel label, ImageIcon image, int width, int height) {
        if (label != null) {
            if (image == null) label.setIcon(image); else {
                double lw = width * 1.0;
                double lh = height * 1.0;
                double iw = image.getIconWidth() * 1.0;
                double ih = image.getIconHeight() * 1.0;
                if ((lw > 0 && lw < iw) || (lh > 0 && lh < ih)) {
                    double factorW = lw / iw;
                    double factorH = lh / ih;
                    double f = (factorW < factorH) ? factorW : factorH;
                    image = new ImageIcon(image.getImage().getScaledInstance((int) (iw * f), (int) (ih * f), Image.SCALE_SMOOTH));
                }
                label.setIcon(image);
            }
        }
    }
}

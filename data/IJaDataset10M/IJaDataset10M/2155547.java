package desperateDoctors;

import java.awt.*;
import java.awt.image.*;

/**
 * Classe permettant de rendre transparent la couleur 0xFF00FF d'une Image
 * Inspir� de http://www.rgagnon.com/javadetails/java-0265.html
 * @author Nad�ge Barrage
 * @author Jean-Fran�ois Geyelin
 * @author Francis Lim
 * @version 1.0
 * @since 1.0
 */
public class TraitementImage {

    /**
     * renvoit une image o� les pixels qui �taient de la couleur 0xFF00FF sont transparent.
     */
    public static Image rendreTransparent(Image image) {
        ImageFilter filter = new RGBImageFilter() {

            public final int filterRGB(int x, int y, int rgb) {
                if (rgb == 0xFFFF00FF) {
                    return 0x00FFFFFF;
                } else {
                    return rgb;
                }
            }
        };
        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
}

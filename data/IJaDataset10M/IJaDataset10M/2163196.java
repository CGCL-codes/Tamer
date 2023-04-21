package jscorch;

import java.awt.*;
import java.awt.image.*;
import java.net.*;

/**
 * A utility class providing methods for some heavily used functions
 * such as sin and cos using angles in degrees and methods shared
 * between many classes
 */
public class JScorchUtils {

    private static double[] sinTable = new double[91];

    private static double[] cosTable = new double[91];

    static {
        for (int i = 0; i <= 90; i++) {
            sinTable[i] = Math.sin(i * Math.PI / 180.0);
            cosTable[i] = Math.cos(i * Math.PI / 180.0);
        }
    }

    /**
	 * Makes a new color out of randomly picked RBG values between 0 and 255.
	 * @return a randomly chosen Color object
	 */
    public static Color RandomColor() {
        return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    /**
	 * Reads sin(a) out of a precreated array of values.
	 * @param a an integer angle in degrees.
	 * @return sin(a) in double precision
	 */
    public static double sin(int a) {
        if (a <= 90) {
            return sinTable[a];
        } else {
            return sinTable[180 - a];
        }
    }

    /**
	 * Reads cos(a) out of a precreated array of values.
	 * @param a an integer angle in degrees.
	 * @return cos(a) in double precision
	 */
    public static double cos(int a) {
        if (a <= 90) {
            return cosTable[a];
        } else {
            return -cosTable[180 - a];
        }
    }

    public static BufferedImage getBufImage(Component o, String path) {
        Image img = getImage(o, path);
        BufferedImage bim = new BufferedImage(img.getWidth(o), img.getHeight(o), BufferedImage.TYPE_INT_ARGB);
        int c = (new Color(1f, 1f, 1f, 0f)).getRGB();
        for (int i = 0; i < bim.getWidth(); i++) {
            for (int j = 0; j < bim.getHeight(); j++) {
                bim.setRGB(i, j, c);
            }
        }
        Graphics g = bim.createGraphics();
        g.drawImage(img, 0, 0, o);
        return bim;
    }

    public static Image getImage(Component o, String path) {
        MediaTracker tracker = new MediaTracker(o);
        URL imageURL = jscorch.GameWindow.class.getResource(path);
        Image img = Toolkit.getDefaultToolkit().getImage(imageURL);
        tracker.addImage(img, 0);
        try {
            tracker.waitForID(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }
}

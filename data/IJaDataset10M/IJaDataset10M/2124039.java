package gnu.java.awt.peer.gtk;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.peer.RobotPeer;

/**
 * Implements the RobotPeer interface using the XTest extension.
 *
 * @author Thomas Fitzsimmons
 */
public class GdkRobotPeer implements RobotPeer {

    static final ColorModel cm = new DirectColorModel(32, 0xff000000, 0x00ff0000, 0x0000ff00, 0x000000ff);

    public GdkRobotPeer(GraphicsDevice screen) throws AWTException {
        if (!initXTest()) throw new AWTException("XTest extension not supported");
    }

    native boolean initXTest();

    public native void mouseMove(int x, int y);

    public native void mousePress(int buttons);

    public native void mouseRelease(int buttons);

    public native void mouseWheel(int wheelAmt);

    public native void keyPress(int keycode);

    public native void keyRelease(int keycode);

    native int[] nativeGetRGBPixels(int x, int y, int width, int height);

    public int getRGBPixel(int x, int y) {
        return cm.getRGB(nativeGetRGBPixels(x, y, 1, 1)[0]);
    }

    public int[] getRGBPixels(Rectangle r) {
        int[] gdk_pixels = nativeGetRGBPixels(r.x, r.y, r.width, r.height);
        int[] pixels = new int[r.width * r.height];
        for (int i = 0; i < r.width * r.height; i++) pixels[i] = cm.getRGB(gdk_pixels[i]);
        return pixels;
    }
}

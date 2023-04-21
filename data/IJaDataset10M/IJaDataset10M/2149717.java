package gnu.java.awt.peer.gtk;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Locale;

public class GdkGraphicsEnvironment extends GraphicsEnvironment {

    public GdkGraphicsEnvironment() {
    }

    public GraphicsDevice[] getScreenDevices() {
        return new GraphicsDevice[] { new GdkScreenGraphicsDevice(this) };
    }

    public GraphicsDevice getDefaultScreenDevice() {
        if (GraphicsEnvironment.isHeadless()) throw new HeadlessException();
        return new GdkScreenGraphicsDevice(this);
    }

    public Graphics2D createGraphics(BufferedImage image) {
        return new GdkGraphics2D(image);
    }

    private native int nativeGetNumFontFamilies();

    private native void nativeGetFontFamilies(String[] family_names);

    public Font[] getAllFonts() {
        throw new java.lang.UnsupportedOperationException();
    }

    public String[] getAvailableFontFamilyNames() {
        String[] family_names;
        int array_size;
        array_size = nativeGetNumFontFamilies();
        family_names = new String[array_size];
        nativeGetFontFamilies(family_names);
        return family_names;
    }

    public String[] getAvailableFontFamilyNames(Locale l) {
        throw new java.lang.UnsupportedOperationException();
    }
}

package android.util;

import org.xmlvm.iphone.CGRect;
import org.xmlvm.iphone.UIScreen;
import android.content.res.Configuration;
import android.internal.ConfigurationFactory;
import android.os.*;

/**
 * A structure describing general information about a display, such as its
 * size, density, and font scaling.
 * <p>To access the DisplayMetrics members, initialize an object like this:</p>
 * <pre> DisplayMetrics metrics = new DisplayMetrics();
 * getWindowManager().getDefaultDisplay().getMetrics(metrics);</pre>
 */
public class DisplayMetrics {

    /**
     * Standard quantized DPI for low-density screens.
     */
    public static final int DENSITY_LOW = 120;

    /**
     * Standard quantized DPI for medium-density screens.
     */
    public static final int DENSITY_MEDIUM = 160;

    /**
     * Standard quantized DPI for high-density screens.
     */
    public static final int DENSITY_HIGH = 240;

    /**
     * The reference density used throughout the system.
     */
    public static final int DENSITY_DEFAULT = DENSITY_MEDIUM;

    /**
     * The device's density.
     * @hide becase eventually this should be able to change while
     * running, so shouldn't be a constant.
     */
    public static final int DENSITY_DEVICE = getDeviceDensity();

    /**
     * The absolute width of the display in pixels.
     */
    public int widthPixels;

    /**
     * The absolute height of the display in pixels.
     */
    public int heightPixels;

    /**
     * The logical density of the display.  This is a scaling factor for the
     * Density Independent Pixel unit, where one DIP is one pixel on an
     * approximately 160 dpi screen (for example a 240x320, 1.5"x2" screen), 
     * providing the baseline of the system's display. Thus on a 160dpi screen 
     * this density value will be 1; on a 106 dpi screen it would be .75; etc.
     *  
     * <p>This value does not exactly follow the real screen size (as given by 
     * {@link #xdpi} and {@link #ydpi}, but rather is used to scale the size of
     * the overall UI in steps based on gross changes in the display dpi.  For 
     * example, a 240x320 screen will have a density of 1 even if its width is 
     * 1.8", 1.3", etc. However, if the screen resolution is increased to 
     * 320x480 but the screen size remained 1.5"x2" then the density would be 
     * increased (probably to 1.5).
     *
     * @see #DENSITY_DEFAULT
     */
    public float density;

    /**
     * The screen density expressed as dots-per-inch.  May be either
     * {@link #DENSITY_LOW}, {@link #DENSITY_MEDIUM}, or {@link #DENSITY_HIGH}.
     */
    public int densityDpi;

    /**
     * A scaling factor for fonts displayed on the display.  This is the same
     * as {@link #density}, except that it may be adjusted in smaller
     * increments at runtime based on a user preference for the font size.
     */
    public float scaledDensity;

    /**
     * The exact physical pixels per inch of the screen in the X dimension.
     */
    public float xdpi;

    /**
     * The exact physical pixels per inch of the screen in the Y dimension.
     */
    public float ydpi;

    public DisplayMetrics() {
    }

    public void setTo(DisplayMetrics o) {
        widthPixels = o.widthPixels;
        heightPixels = o.heightPixels;
        density = o.density;
        densityDpi = o.densityDpi;
        scaledDensity = o.scaledDensity;
        xdpi = o.xdpi;
        ydpi = o.ydpi;
    }

    public void setToDefaults() {
        widthPixels = 0;
        heightPixels = 0;
        density = DENSITY_DEVICE / (float) DENSITY_DEFAULT;
        densityDpi = DENSITY_DEVICE;
        scaledDensity = density;
        xdpi = DENSITY_DEVICE;
        ydpi = DENSITY_DEVICE;
    }

    @Override
    public String toString() {
        return "DisplayMetrics{density=" + density + ", width=" + widthPixels + ", height=" + heightPixels + ", scaledDensity=" + scaledDensity + ", xdpi=" + xdpi + ", ydpi=" + ydpi + "}";
    }

    private static int getDeviceDensity() {
        int device = ConfigurationFactory.detectDevice();
        switch(device) {
            case ConfigurationFactory.DEVICE_IPHONE:
                return 160;
            case ConfigurationFactory.DEVICE_IPHONE4:
                return 326;
            case ConfigurationFactory.DEVICE_IPAD:
                return 132;
        }
        return DENSITY_DEFAULT;
    }
}

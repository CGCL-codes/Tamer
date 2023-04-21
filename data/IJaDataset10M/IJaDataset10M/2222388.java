package net.sf.doolin.util;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

public final class ScreenUtils {

    /**
	 * Default screen resolution to be used in a headless environment.
	 */
    public static final int DEFAULT_SCREEN_RESOLUTION = 72;

    private ScreenUtils() {
    }

    public static int getScreenResolution() {
        if (isHeadless()) {
            return DEFAULT_SCREEN_RESOLUTION;
        } else {
            return Toolkit.getDefaultToolkit().getScreenResolution();
        }
    }

    public static boolean isHeadless() {
        return GraphicsEnvironment.isHeadless();
    }
}

package net.sourceforge.mazix.components.constants;

import static java.awt.Font.PLAIN;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import static net.sourceforge.mazix.components.utils.screen.ScreenResolutionUtils.getMaximumHeight;
import static net.sourceforge.mazix.components.utils.screen.ScreenResolutionUtils.getMaximumWidth;
import static net.sourceforge.mazix.components.utils.screen.ScreenResolutionUtils.getMinimumHeight;
import static net.sourceforge.mazix.components.utils.screen.ScreenResolutionUtils.getMinimumWidth;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;

/**
 * The class which stores all useful graphical constants.
 *
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * @since 0.7
 * @version 0.7
 */
public final class GraphicConstants {

    public static final String DEFAULT_FONT_NAME = "Augustus";

    /** The 1 size font. */
    public static final int FONT_SIZE_1 = 1;

    /** The 12 size font. */
    public static final int FONT_SIZE_12 = 12;

    /** The 20 size font. */
    public static final int FONT_SIZE_20 = 20;

    /** The 30 size font. */
    public static final int FONT_SIZE_30 = 30;

    /** The 40 size font. */
    public static final int FONT_SIZE_40 = 40;

    /** The default style font. */
    public static final int DEFAULT_FONT_STYLE = PLAIN;

    /** The default size font. */
    public static final int DEFAULT_FONT_SIZE = FONT_SIZE_1;

    /** The default x coordinate where 3D objects are placed in the 3D scene. */
    public static final float X_DEFAULT = 0f;

    /** The default y coordinate where 3D objects are placed in the 3D scene. */
    public static final float Y_DEFAULT = 0f;

    /** The default z coordinate where 3D objects are placed in the 3D scene. */
    public static final float Z_DEFAULT = 0f;

    /**
     * The list of all screen resolutions supported by the game. This constant should be never used
     * as is, consider using the utility class
     * {@link net.sourceforge.mazix.components.utils.screen.ScreenResolutionUtils} if you can
     * because it dynamically computes the supported screen resolutions.
     */
    public static final Set<Dimension> SUPPORTED_SCREEN_RESOLUTION_SET = unmodifiableSet(new HashSet<Dimension>(asList(new Dimension[] { new Dimension(640, 480), new Dimension(800, 600), new Dimension(1024, 768), new Dimension(1280, 960), new Dimension(1600, 1200) })));

    /** The minimum screen width. */
    public static final int MIN_WIDTH = (int) getMinimumWidth(SUPPORTED_SCREEN_RESOLUTION_SET);

    /** The minimum screen height. */
    public static final int MIN_HEIGHT = (int) getMinimumHeight(SUPPORTED_SCREEN_RESOLUTION_SET);

    /** The maximum screen width. */
    public static final int MAX_WIDTH = (int) getMaximumWidth(SUPPORTED_SCREEN_RESOLUTION_SET);

    /** The maximum screen height. */
    public static final int MAX_HEIGHT = (int) getMaximumHeight(SUPPORTED_SCREEN_RESOLUTION_SET);

    /**
     * Private constructor to prevent from instantiation.
     *
     * @since 0.7
     */
    private GraphicConstants() {
        super();
    }
}

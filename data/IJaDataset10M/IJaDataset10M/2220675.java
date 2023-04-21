package uk.ac.cam.asnc.image.core;

import uk.ac.cam.asnc.image.gui.GUIUtils;

/**
 * A utility class for image manipulation, consisting mostly of system constants.
 * @author Peter Stokes (pas53@cam.ac.uk)
 *
 */
public class ImgUtils {

    /**
	 * True if verbose logging is required.
	 * If true then the log is displayed on screen as well as to file; otherwise just to file.
	 */
    private static final boolean VERBOSE = true;

    /**
	 * Assumed colour of ink after thresholding: 0 for black ink, 1 for white ink.
	 */
    public static final int INK_COLOUR = 0;

    /**
	 * Assumed that ink pixels will be no lower than this value: 0 for black ink.
	 */
    public static final int INK_COLOUR_MIN = 0;

    /**
	 * Assumed that ink pixels will be no higher than this value: 0 for black ink.
	 * Currently the image is thresholded to 0 or 1 so this has no meaningful function.
	 */
    public static final int INK_COLOUR_MAX = 0;

    /**
	 * Assumed colour of background (parchment) after thresholding: 1 for white parchment, 0 for inverted image.
	 */
    public static final int BG_COLOUR = 1;

    /**
	 * Assumed that ink pixels will be no lower than this value: 1 for white parchment.
	 * Currently the image is thresholded to 0 or 1 so this has no meaningful function.
	 */
    public static final int BG_COLOUR_MIN = 1;

    /**
	 * Assumed that ink pixels will be no lower than this value: 1 for white parchment.
	 */
    public static final int BG_COLOUR_MAX = 1;

    /**
	 * JAI band for simple B&W images
	 */
    public static final int BW_BAND = 0;

    /**
	 *  Values for the matrix to convert RGB to greyscale.
	 */
    public static final double RGBtoGREY_RED = 0.114D;

    public static final double RGBtoGREY_GREEN = 0.587D;

    public static final double RGBtoGREY_BLUE = 0.299D;

    public static final int RGBtoGREY_BRIGHT = 0;

    /**
	 * Matrix to convert RGB to greyscale.
	 */
    public static final double[][] RGBtoGREY = { { RGBtoGREY_RED, RGBtoGREY_GREEN, RGBtoGREY_BLUE, RGBtoGREY_BRIGHT } };

    /**
	 * Values for the vectors to extract RGB colour channels.
	 */
    public static final double[] RGB_RED = { 1.0D, 0.0D, 0.0D, 0.0D };

    public static final double[] RGB_GREEN = { 0.0D, 1.0D, 0.0D, 0.0D };

    public static final double[] RGB_BLUE = { 0.0D, 0.0D, 1.0D, 0.0D };

    /**
	 * Convolution kernel for linear blur.
	 */
    public static final double[] LINEARBLUR33 = { 1 / 9D, 1 / 9D, 1 / 9D, 1 / 9D, 1 / 9D, 1 / 9D, 1 / 9D, 1 / 9D, 1 / 9D };

    /**
	 * Convolution kernel for Gaussian blur.
	 */
    public static final double[] GAUSSBLUR5 = { 0.0625D, 0.25D, 0.375D, 0.25D, 0.0625D };

    public static void log(String s) {
        if (VERBOSE) System.out.println(s);
        GUIUtils.printlnConsole(s);
    }

    /**
	 * Helper method to write words to log (i.e. no newline inserted).
	 * Writes to log file, and also to screen if VERBOSE flag is set. Does not insert newline after each log call.
	 * @param s The string to be logged.
	 */
    public static void logWords(String s) {
        if (VERBOSE) System.out.print(s);
        GUIUtils.printConsole(s);
    }
}

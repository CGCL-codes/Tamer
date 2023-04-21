package com.fddtool.pd.report;

import java.awt.Color;
import com.fddtool.pd.fddproject.IProgressInfo;

/**
 * This class helps to determine the color to use when displaying progress
 * for a piece of work. It can also convert some of the named colors (like pink,
 * lightblue etc) into <code>Color</code> object.
 *
 * @author Serguei Khramtchenko
 */
public class ProgressColor {

    /**
     * The black color.
     */
    public static final Color BLACK = new Color(0, 0, 0);

    /**
     * The white color.
     */
    public static final Color WHITE = new Color(255, 255, 255);

    /**
     * The pink color.
     */
    public static final Color PINK = new Color(Integer.parseInt("FFC0CB", 16));

    /**
     * The light blue color.
     */
    public static final Color LIGHT_BLUE = new Color(Integer.parseInt("BFEFFF", 16));

    /**
     * The light green color.
     */
    public static final Color LIGHT_GREEN = new Color(Integer.parseInt("B0FFB0", 16));

    /**
     * The light grey color.
     */
    public static final Color LIGHT_GREY = new Color(Integer.parseInt("C3C3C3", 16));

    /**
     * Default color for a piece of work that is late.
     */
    public static final String DEFAULT_LATE_COLOR = "pink";

    /**
     * Default color for a piece of work that is in progress.
     */
    public static final String DEFAULT_STARTED_COLOR = "lightblue";

    /**
     * Default color for a piece of work that is completed.
     */
    public static final String DEFAULT_COMPLETED_COLOR = "lightgreen";

    /**
     * Default color for a piece of work that is canceled.
     */
    public static final String DEFAULT_CANCELLED_COLOR = "lightgrey";

    /**
     * Default color for a piece of work that is not yet started.
     */
    public static final String DEFAULT_NOT_STARTED_COLOR = "white";

    /**
     * Determines a color that should be used to display entity's progress.
     *
     * @param info IProgressInfo reporting information about entity's progress.
     *
     * @return String containing name of the color to use.
     */
    public static String determineColor(IProgressInfo info) {
        if (info.isCancelled()) {
            return DEFAULT_CANCELLED_COLOR;
        } else if (info.isLate()) {
            return DEFAULT_LATE_COLOR;
        } else if (info.isCompleted()) {
            return DEFAULT_COMPLETED_COLOR;
        } else if (info.isStarted()) {
            return DEFAULT_STARTED_COLOR;
        } else {
            return DEFAULT_NOT_STARTED_COLOR;
        }
    }

    /**
     * Converts the named color, or color represented in hexadecimal
     * notation into an object.
     *
     * @param color String containing either name of the color, or its
     *     hexadecimal representation. The examples of valid parameter are
     *     "lightblue" or "#FFC0CB"
     *
     * @return Color object or <code>null</code> if the parameter represents
     *     unknown color.
     */
    public static Color getColor(String color) {
        color = color.toLowerCase();
        if (color == null) {
            return BLACK;
        } else if (color.equals("white")) {
            return WHITE;
        } else if (color.equals("pink")) {
            return PINK;
        } else if (color.equals("lightgreen")) {
            return LIGHT_GREEN;
        } else if (color.equals("lightblue")) {
            return LIGHT_BLUE;
        } else if (color.equals("lightgrey")) {
            return LIGHT_GREY;
        } else {
            if (color.startsWith("#")) {
                color = color.substring(1);
            }
            try {
                int rgb = Integer.parseInt(color, 16);
                return new Color(rgb);
            } catch (NumberFormatException ex) {
            }
        }
        return null;
    }
}

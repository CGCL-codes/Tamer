package org.mapsplit.core;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.PatternLayout;

/**
 * @author vrentea
 *
 */
public class Constants {

    public static final double A4_WIDTH = 21;

    public static final double A4_HEIGHT = 29.7;

    public static final double A4_ASPECT_RATIO = A4_HEIGHT / A4_WIDTH;

    public static final double CM_TO_INCH = 2.54d;

    public enum Orientation {

        PORTRAIT, LANDSCAPE
    }

    ;

    public static final float OUTPUT_QUALITY = 0.95f;

    public static final PatternLayout logPattern = new PatternLayout("%d{HH:mm:ss} %c{2} %M - %m%n");
}

package org.geogurus.gas.utils;

import java.io.Serializable;
import java.util.Properties;
import java.util.StringTokenizer;
import org.geogurus.mapserver.objects.RGB;

/**
 * A class to generate a new MapServer RGB color based on a config file: colortable.properties
 * Maintains the current color index.
 * This object can be stored in session, to allow each GAS user to generate a new color scheme for its
 * geodata
 * @author  nri
 */
public class ColorGenerator implements Serializable {

    private int currentColorIndex;

    private int maxColorIndex;

    private Properties colorTable;

    /** the color in case of failure in props reading */
    private RGB dummy;

    /** Creates a new instance of ColorGenerator: attempt to load the colortable.properties file,
     * if failed, generates its own colortable
     */
    public ColorGenerator() {
        dummy = new RGB(255, 0, 0);
        currentColorIndex = 0;
        colorTable = new Properties();
        int step = 51;
        int r = 255;
        int g = 255;
        int b = 255;
        int i = 0;
        while (r >= 0) {
            while (g >= 0) {
                while (b >= 0) {
                    colorTable.setProperty(String.valueOf(i), r + "," + g + "," + b);
                    b = b - step;
                    i++;
                }
                g = g - step;
                b = 255;
            }
            r = r - step;
            g = 255;
        }
        maxColorIndex = i;
    }

    public RGB getNextColor() {
        RGB col = null;
        if (currentColorIndex == maxColorIndex) {
            currentColorIndex = 0;
        }
        String idx = "" + currentColorIndex;
        try {
            StringTokenizer tok = new StringTokenizer(colorTable.getProperty(idx), ",");
            int r = new Integer(tok.nextToken()).intValue();
            int g = new Integer(tok.nextToken()).intValue();
            int b = new Integer(tok.nextToken()).intValue();
            col = new RGB(r, g, b);
        } catch (Exception e) {
            col = dummy;
            e.printStackTrace();
        }
        currentColorIndex++;
        return col;
    }

    public static RGB[] getRampColors(RGB fromColor, RGB toColor, int numClasses) {
        RGB[] cols = new RGB[numClasses];
        int fromRed = fromColor.getRed();
        int fromGreen = fromColor.getGreen();
        int fromBlue = fromColor.getBlue();
        int toRed = toColor.getRed();
        int toGreen = toColor.getGreen();
        int toBlue = toColor.getBlue();
        int stepRed = Math.round((fromRed - toRed) / numClasses);
        int stepGreen = Math.round((fromGreen - toGreen) / numClasses);
        int stepBlue = Math.round((fromBlue - toBlue) / numClasses);
        int i = 0;
        while (i < numClasses) {
            cols[i] = new RGB(fromRed, fromGreen, fromBlue);
            fromRed -= stepRed;
            fromGreen -= stepGreen;
            fromBlue -= stepBlue;
            i++;
        }
        return cols;
    }

    public static RGB hexToRgb(String hexa) throws NumberFormatException {
        RGB col = null;
        String red = "0x" + hexa.substring(0, 2);
        String green = "0x" + hexa.substring(2, 4);
        String blue = "0x" + hexa.substring(4, 6);
        col = new RGB(Integer.decode(red), Integer.decode(green), Integer.decode(blue));
        return col;
    }
}

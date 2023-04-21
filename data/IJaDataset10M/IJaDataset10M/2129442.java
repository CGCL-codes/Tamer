package de.binfalse.martin.tools;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Abstract color model class.
 * 
 * @author Martin Scharm
 *
 */
public abstract class ColorModel {

    /**
	 * The max value of the map.
	 */
    protected double max;

    /**
	 * Set the maximum value.
	 * 
	 * @param max the maximum of the map
	 */
    public void setMax(double max) {
        this.max = max;
    }

    /**
	 * Set the background color.
	 * 
	 * @param g the background color
	 */
    public abstract void setBackground(Graphics g);

    /**
	 * Set the color of a particular graphics device.
	 * 
	 * @param g the graphics device
	 * @param v the value in the map
	 */
    public abstract void setColor(Graphics g, double v);

    /**
	 * Create a legend for the color map.
	 * 
	 * @param font the font to use for the text
	 * @param fm the font metrics to estimate text sizes
	 * @param height the height of the legend
	 * @param s the scaler instance used for the map
	 * @return the legend
	 */
    public abstract BufferedImage createLegend(Font font, FontMetrics fm, int height, Scaler s);

    /**
	 * Get a label for the legend (using suffixes like K, M, G etc).
	 * 
	 * @param value the value to print
	 * @return the text to print
	 */
    protected String getLabel(double value) {
        int ext = 0;
        while (value > 1000) {
            value = Math.ceil(value / 1000.);
            ext++;
        }
        String text = ((int) value) + "";
        switch(ext) {
            case 0:
                break;
            case 1:
                text += " K";
                break;
            case 2:
                text += " M";
                break;
            case 3:
                text += " G";
                break;
            case 4:
                text += " T";
                break;
            case 5:
                text += " P";
                break;
            case 6:
                text += " E";
                break;
            case 7:
                text += " Z";
                break;
            case 8:
                text += " Y";
                break;
            default:
                text = "unbelievable";
        }
        return text;
    }
}

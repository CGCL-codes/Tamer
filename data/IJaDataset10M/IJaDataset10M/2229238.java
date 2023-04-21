package cat.uvic.calculs;

import android.graphics.Bitmap;

/**
 * Represents a linear line as detected by the hough transform. This line is
 * represented by an angle theta and a radius from the centre.
 * 
 * @author Olly Oechsle, University of Essex, Date: 13-Mar-2008
 * @version 1.0
 */
public class HoughLine {

    /**
	 * theta parameter (arc)
	 */
    public double theta;

    protected double r;

    /**
	 * Initialises the hough line
	 * 
	 * @param theta
	 *            arc value
	 * @param r
	 *            distance value
	 */
    public HoughLine(double theta, double r) {
        this.theta = theta;
        this.r = r;
    }

    /**
	 * Draws the line on the image of your choice with the RGB colour of your
	 * choice.
	 * 
	 * @param image
	 * @param color
	 */
    public void draw(Bitmap image, int color) {
        int height = image.getHeight();
        int width = image.getWidth();
        int houghHeight = (int) (Math.sqrt(2) * Math.max(height, width)) / 2;
        float centerX = width / 2;
        float centerY = height / 2;
        double tsin = Math.sin(theta);
        double tcos = Math.cos(theta);
        if (theta < Math.PI * 0.25 || theta > Math.PI * 0.75) {
            for (int y = 0; y < height; y++) {
                int x = (int) ((((r - houghHeight) - ((y - centerY) * tsin)) / tcos) + centerX);
                if (x < width && x >= 0) {
                    image.setPixel(x, y, color);
                }
            }
        }
    }
}

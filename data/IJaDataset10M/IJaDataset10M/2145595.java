package simulation;

/**
 * Provides utility functions for geometric calculations related to the earth
 * grid calculations.
 * 
 * @author Andrew Bernard
 *
 */
public class Util {

    /**
	 * Computes the circumference of the circle at the given latitude. The radius
	 * of the earth should be given in pixels equal to half the size of the image
	 * in the display.
	 * 
   * @param latitude
   * @param earthRadius radius of the earth in pixels (half the image size)
   * @return
   */
    public static float cLattitudeCircum(double latitude, double earthRadius) {
        double latRadius = earthRadius * Math.sin(Math.toRadians(90d - latitude));
        return (float) (2d * latRadius * Math.PI);
    }

    /**
   * Computes the area of a trapezoid.
   * 
   * @param topLength given in pixels
   * @param bottomLength given in pixels
   * @param height given in pixels
   * 
   * @return the area of the trapezoid in pixels
   */
    public static float cTrapezoidArea(double topLength, double bottomLength, double height) {
        return (float) ((.5 * height) * (topLength + bottomLength));
    }

    /**
   * Computes the length of the non-parallel sides of a trapezoid.
   * 
   * @param topLength given in pixels
   * @param bottomLength given in pixels
   * @param height given in pixels
   * 
   * @return the length of the non-parallel sides of the trapezoid in pixels
   */
    public static float cTrapezoidSideLen(double topLength, double bottomLength, double height) {
        return (float) Math.sqrt(Math.pow((Math.abs(topLength - bottomLength) / 2), 2) + Math.pow(height, 2));
    }

    /**
   * Computes the distance from a latitudal degree to the equator.
   * 
   * @param latitude
   * @param earthRadius radius of the earth in pixels (half the image size)
   * @return
   */
    public static float distToEquator(double latitude, double earthRadius) {
        return (float) (earthRadius * Math.sin(Math.toRadians(latitude)));
    }
}

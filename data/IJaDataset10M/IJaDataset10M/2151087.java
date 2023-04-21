package org.fhi.pgps;

import java.util.Vector;
import org.fhi.pgps.math.Matrix;

/**
 * The <code>KalmanFilter</code> class implements the well know Discret Kalman Filter.
 * for more information about the mathematical fundaments see 
 * <a href="http://www.cs.unc.edu/~welch/media/pdf/kalman_intro.pdf">G.Welch and G.Bishop, 
 * "An Introduction to the Kalman Filter"</a> 
 * 
 */
public class KalmanFilter implements IFilter {

    private Vector rawCoords;

    private double sigmaP;

    private double sigmaD;

    private Matrix A;

    private int iterations;

    private Matrix P;

    private Matrix x;

    /**
	 * Constructs a Kalman Filter
	 * @param rawCoordinates - the reference to the vector of coordinates
	 * @param deltaT - the time in seconds between two iterations
	 * @param sigmaP - the initial precision of the position
	 * @param sigmaD - the initial precision of the speed
	 * @param iterations - the number of iterations 
	 */
    public KalmanFilter(Vector rawCoordinates, float deltaT, double sigmaP, double sigmaD, int iterations) {
        this.sigmaP = sigmaP;
        this.sigmaD = sigmaD;
        A = Matrix.identity(4);
        A.set(0, 1, deltaT);
        A.set(2, 3, deltaT);
        rawCoords = rawCoordinates;
        this.iterations = iterations;
    }

    /**
	 * To allow the filter to be run at a lower rate than the GPS, or to be stopped 
	 * and again launch, each filtered position is calculated using a defined number of 
	 * past positions. The algorithm has two phases:
	 * first, the values of x and P are initialized with the first coordinate's values.
	 * then it iterates until the last coordinate and return the calculated coordinate.
	 * Returns the coordinate 0,0 if there is no initial value in the array.
	 * Most of the work of this function is to convert the speed in cartesian coordinates
	 * and the GPS coordinates in meters.
	 * 
	 * @return the filtered coordinate
	 */
    public CoordinateGPS getFilteredCoordinate() {
        if (rawCoords.size() > 0) {
            int i = Math.max(rawCoords.size() - iterations, 0);
            CoordinateGPS c = (CoordinateGPS) rawCoords.elementAt(i);
            double speedLatitude = c.getSpeed() * Math.cos(Math.toRadians(c.getCourse()));
            double speedLongitude = c.getSpeed() * Math.sin(Math.toRadians(c.getCourse()));
            init(c.getHorizontalAccuracy(), new Matrix(new double[][] { { c.latitudeToMeter(c.getLatitude()), speedLatitude, c.longitudeToMeter(c.getLongitude()), speedLongitude } }));
            i++;
            while (i < rawCoords.size()) {
                c = (CoordinateGPS) rawCoords.elementAt(i);
                speedLatitude = c.getSpeed() * Math.cos(Math.toRadians(c.getCourse()));
                speedLongitude = c.getSpeed() * Math.sin(Math.toRadians(c.getCourse()));
                next(c.getHorizontalAccuracy(), new Matrix(new double[][] { { c.latitudeToMeter(c.getLatitude()), speedLatitude, c.longitudeToMeter(c.getLongitude()), speedLongitude } }));
                i++;
            }
            double lat = x.get(1, 0) / c.latitudeToMeter(1);
            double lon = x.get(3, 0) / c.longitudeToMeter(1);
            double speed = Math.sqrt(lat * lat + lon * lon);
            double course = 180 - (Math.toDegrees(org.fhi.pgps.math.Math.atan2(-lat, lon)));
            return new CoordinateGPS(x.get(0, 0) / c.latitudeToMeter(1), x.get(2, 0) / c.longitudeToMeter(1), 0.0f, (float) (P.get(0, 0) / sigmaP * Math.sqrt(2)), 0.0f, speed, course, ((CoordinateGPS) rawCoords.lastElement()).getTimeStamp());
        } else {
            return new CoordinateGPS(0, 0, 0, 0, 0, 0, 0, 0);
        }
    }

    /**
	 * Sets the current mode.
	 * the values used for each mode were choosen aproximatively after being tested on
	 * a sample path. By doing more precise tests, it could be possible to improve them.
	 * These values represents the number of seconds during which the last coordinates
	 * can influence the current one. 
	 */
    public void setMode(int mode) {
        switch(mode) {
            case MODE_STANDING:
                iterations = 10;
                break;
            case MODE_WALKING:
                iterations = 15;
                break;
            case MODE_RUNNING:
                iterations = 20;
                break;
            case MODE_CYCLING:
                iterations = 30;
                break;
            default:
                iterations = 10;
        }
    }

    /**
	 * Initializes the variable of the filter
	 * @param HDOP - the precision of the current coordinate in meter
	 * @param z - the current unfiltered coordinate
	 */
    private void init(double HDOP, Matrix z) {
        double varE = (HDOP / Math.sqrt(2) * sigmaP);
        double varVE = (HDOP / Math.sqrt(2) * sigmaD);
        Matrix R = new Matrix(new double[][] { { varE, 0, 0, 0 }, { 0, varVE, 0, 0 }, { 0, 0, varE, 0 }, { 0, 0, 0, varVE } });
        P = R.clone();
        x = z.clone();
    }

    /**
	 * Calculates the next step of the algorithm.
	 * first by estimating the next position, then by correcting
	 * accordingly to the measured value.
	 * @param HDOP - the precision of the current coordinate in meter
	 * @param z - the current unfiltered coordinate
	 */
    private void next(double HDOP, Matrix z) {
        double varE = HDOP / Math.sqrt(2) * sigmaP;
        double varVE = HDOP / Math.sqrt(2) * sigmaD;
        Matrix R = new Matrix(new double[][] { { varE, 0, 0, 0 }, { 0, varVE, 0, 0 }, { 0, 0, varE, 0 }, { 0, 0, 0, varVE } });
        Matrix x1 = A.multiply(x);
        Matrix p1 = A.multiply(P).multiply(A.transpose());
        Matrix K = p1.multiply(p1.add(R).inverse());
        x = x1.add(K.multiply(z.substract(x1)));
        P = p1.substract(K.multiply(p1));
    }
}

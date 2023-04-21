package jat.measurements;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import jat.alg.integrators.LinePrinter;
import jat.matvec.data.*;
import jat.timeRef.RSW_Frame;
import jat.spacetime.*;

public class generateYaw {

    public static double yawAngle;

    public static double alpha;

    public static double beta;

    public static Matrix T;

    public static LinePrinter yawResults;

    static boolean firstTime = true;

    /**
	 * Internal function to return the Transformation matrix
	 * to rotate the statellite into a frame useful for determining
	 * the yaw angle for a GPS satellite.  This frame is an RTN frame
	 * with the N defined in the oppsite direction (negative orbit normal)
	 * @param r  Position of the GPS satellite
	 * @param v  Velocity of the GPS satellite
	 * @return
	 */
    public static Matrix YawRotationMatrix(VectorN r, VectorN v) {
        Matrix T = new Matrix(3);
        T = RSW_Frame.ECI2SWR(r, v);
        return T;
    }

    /**
	 * Return the unit sun vector
	 * @param MJD_TT
	 * @return
	 */
    public static VectorN unitSun(double MJD_TT, VectorN r) {
        VectorN SunV = new VectorN(jat.spacetime.EarthRef.sunVector(MJD_TT));
        VectorN deltasun = SunV.minus(r);
        SunV = deltasun.unitVector();
        return SunV;
    }

    /**
	 * Generate the appropirate yaw angle of a GPS satellite to 
	 * keep it in a favorable allignement for solar pannel pointing
	 * @param MJD_TT
	 * @param r  GPS satellite State in ECI
	 * @param v  GPS satellite State in ECI
	 * @return
	 */
    public static double getYawAngle(double MJD, VectorN r, VectorN v, int prn) {
        if (firstTime) {
            String out_directory = "C:\\GOESR\\output\\";
            String alphaFile = "yawResults.txt";
            yawResults = new LinePrinter(out_directory + alphaFile);
            firstTime = false;
        }
        T = new Matrix(YawRotationMatrix(r, v));
        double MJD_TT = TimeUtils.UTCtoTT(MJD);
        VectorN uSun = unitSun(MJD_TT, r);
        VectorN uSunRSW = T.times(uSun);
        beta = Math.asin(uSunRSW.get(2) * (-1.0));
        alpha = Math.atan2(((-1.0) * uSunRSW.get(0)), (-1.0) * uSunRSW.get(1));
        yawAngle = (180 / Math.PI) * Math.atan2(Math.sin(beta), Math.cos(beta) * Math.sin(alpha));
        alpha = alpha * (180 / Math.PI);
        beta = beta * (180 / Math.PI);
        VectorN results = new VectorN(7);
        results.set(0, prn);
        results.set(1, alpha);
        results.set(2, beta);
        results.set(3, (yawAngle));
        results.set(4, r.get(0));
        results.set(5, r.get(1));
        results.set(6, r.get(2));
        yawResults.println(results.toString());
        return yawAngle;
    }
}

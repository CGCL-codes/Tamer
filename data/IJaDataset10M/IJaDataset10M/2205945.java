package jat.demo.OrbitDetermination;

import java.io.*;
import java.util.*;
import jat.util.*;
import jat.alg.estimators.*;
import jat.matvec.data.*;

/**
* The ObsData.java Class provides the measurements and measurement model for a
* demonstration of orbit determination in JAT. The data
* comes from the Estimation class at UT.
*
* @author 
* @version 1.0
*/
public class ObsData implements MeasurementModel, MeasurementData {

    public double[] time = new double[2000];

    public double[] range = new double[2000];

    public double[] weight = new double[2000];

    public int[] station = new int[2000];

    public int nobs = 0;

    private static double theta_0 = 1.6331958133;

    private static double omega_e = 7.2921157746E-05;

    private VectorN htilde;

    /**
	 * default constructor, reads in data from OBSDAT2 file.
	 */
    public ObsData() {
        try {
            String b = FileUtil.getClassFilePath("jat.demo.OrbitDetermination", "ObsData");
            String filename = b + "OBSDAT2";
            FileReader fr = new FileReader(filename);
            BufferedReader in = new BufferedReader(fr);
            String s;
            this.nobs = 0;
            while ((s = in.readLine()) != null) {
                StringTokenizer t = new StringTokenizer(s, "    ");
                this.time[nobs] = Double.parseDouble(t.nextToken());
                this.range[nobs] = Double.parseDouble(t.nextToken());
                this.weight[nobs] = Double.parseDouble(t.nextToken());
                this.station[nobs] = Integer.parseInt(t.nextToken());
                this.nobs = this.nobs + 1;
            }
            in.close();
            System.out.println("Obs Data Read Successfully");
        } catch (IOException e) {
            System.out.println("Error: " + e);
            System.exit(1);
        }
    }

    /**
	 * Returns the H matrix
	 * @param xref VectorN containing the current state
	 * @return H matrix (measurement state relation)
	 */
    public VectorN H(VectorN xref) {
        return this.htilde;
    }

    /**
	 * Returns the measurement noise value
	 * @return measurement noise (sigma^2)
	 */
    public double R() {
        return 0.01;
    }

    /** Returns the measurement
	 * @param index int containing the measurement index
	 * @return double containing the measurement corresponding to the index.
	 */
    public double z(int index) {
        return range[index];
    }

    /**
	 * Returns the time of the measurement
	 * @param index int containing the measurement index
	 * @return double containing time of the measurement corresponding to the index.
	 */
    public double time(int index) {
        return time[index];
    }

    /**
	 * Returns the predicted measurement based on the current state
	 * @param index measurement index
	 * @param t time of the measurement
	 * @param xref VectorN with the current state at the measurement time
	 */
    public double zPred(int index, double t, VectorN xref) {
        double theta = theta_0 + omega_e * t;
        Matrix rot = new Matrix(3);
        double cost = Math.cos(theta);
        double sint = Math.sin(theta);
        rot.A[0][0] = cost;
        rot.A[0][1] = -sint;
        rot.A[1][0] = sint;
        rot.A[1][1] = cost;
        double xx = xref.x[0];
        double yy = xref.x[1];
        double zz = xref.x[2];
        VectorN r = new VectorN(xx, yy, zz);
        double xs = xref.x[9];
        double ys = xref.x[10];
        double zs = xref.x[11];
        VectorN rsef = new VectorN(xs, ys, zs);
        VectorN rs = new VectorN(3);
        double[] temp = new double[12];
        int staID = this.station[index];
        switch(staID) {
            case 1:
                rs = rot.times(rsef);
                break;
            case 2:
                rsef = new VectorN(-2428826.1117, -4799750.4339, 3417273.0738);
                rs = rot.times(rsef);
                break;
            case 3:
                rsef = new VectorN(-1736003.0850, -4425049.6149, 4241427.1084);
                rs = rot.times(rsef);
                break;
            default:
                System.out.println("invalid station id in range function");
                System.exit(1);
                break;
        }
        VectorN dr = r.minus(rs);
        double rsi = dr.mag();
        temp[0] = dr.x[0] / rsi;
        temp[1] = dr.x[1] / rsi;
        temp[2] = dr.x[2] / rsi;
        for (int i = 3; i < 12; i++) {
            temp[i] = 0.0;
        }
        if (staID == 1) {
            temp[9] = -1.0 * (dr.x[0] * cost + dr.x[1] * sint) / rsi;
            temp[10] = (dr.x[0] * sint - dr.x[1] * cost) / rsi;
            temp[11] = -1.0 * dr.x[2] / rsi;
        }
        this.htilde = new VectorN(temp);
        return rsi;
    }

    /**
	 * Checks for remaining measurements. 
	 * True = more measurements left to be processed.
	 * @param index measurement index.
	 */
    public boolean hasNext(int index) {
        boolean out = false;
        if (index < (this.nobs)) {
            out = true;
        }
        return out;
    }
}

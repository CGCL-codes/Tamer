package jat.cm;

import java.io.*;
import jat.matvec.data.*;

/** Implements Hills Equations
 *
 * @author 
 * @version 1.0
 */
public class HillsEquation {

    /** Creates a new instance of HillsEquation */
    public HillsEquation() {
    }

    public static void main(java.lang.String args[]) throws IOException {
        FileOutputStream outf = new FileOutputStream("hill3.txt");
        PrintWriter pw = new PrintWriter(outf);
        VectorN x = new VectorN(6);
        Constants c = new Constants();
        double x0 = 0.0;
        double y0 = 0.0;
        double z0 = 1.0;
        double vx0 = 0.0;
        double vy0 = 0.0;
        double vz0 = 0.0;
        TwoBody orbit = new TwoBody(6770.0, 0.0, 51.8, 0.0, 0.0, 0.0);
        double period = orbit.period();
        double w = orbit.meanMotion();
        double t = 0.0;
        while (t < 2.0 * period) {
            double cwt = Math.cos(w * t);
            double swt = Math.sin(w * t);
            x.x[0] = vx0 * swt / w - (3.0 * x0 + 2.0 * vy0 / w) * cwt + 4.0 * x0 + 2.0 * vy0 / w;
            x.x[1] = (6.0 * x0 + 4.0 * vy0 / w) * swt + 2.0 * vx0 * cwt / w - (6.0 * w * x0 + 3.0 * vy0) * t + y0 - 2.0 * vx0 / w;
            x.x[2] = z0 * cwt + vz0 * swt / w;
            x.x[3] = vx0 * cwt + (3.0 * w * x0 + 2.0 * vy0) * swt;
            x.x[4] = (6.0 * w * x0 + 4.0 * vy0) * cwt - 2.0 * vx0 * swt - (6.0 * w * x0 + 3.0 * vy0);
            x.x[5] = -1.0 * z0 * w * swt + vz0 * cwt;
            System.out.println(t);
            pw.print(t + "\t");
            x.print(pw);
            t = t + 1.0;
        }
        pw.close();
        outf.close();
    }
}

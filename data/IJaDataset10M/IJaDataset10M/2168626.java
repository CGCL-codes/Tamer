package edu.mit.aero.foamcut;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 *
 * @author mschafer
 */
public class Airfoil extends Shape implements Serializable {

    Point lePoint;

    /**
     * Create a new airfoil from x,y coordinates.
     * Repeated coordinate values represent slope discontinuities.
     * @param x Airfoil x coordinates.
     * @param y Airfoil y coordinates.
     */
    public Airfoil(double x[], double y[]) {
        super(x, y);
        findLeadingEdge();
    }

    public Airfoil(Airfoil cpy) {
        super(cpy);
        lePoint = new Point(cpy.lePoint);
    }

    public Point getLeadingEdge() {
        return new Point(lePoint);
    }

    public static Airfoil loadXFoilFile(File datFile) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(datFile));
        ArrayList<Double> vx = new ArrayList<Double>();
        ArrayList<Double> vy = new ArrayList<Double>();
        String line = br.readLine();
        StringTokenizer st = new StringTokenizer(line);
        String name = "";
        if (st.countTokens() == 2) {
            try {
                vx.add(Double.valueOf(st.nextToken()));
                vy.add(Double.valueOf(st.nextToken()));
            } catch (NumberFormatException nfex) {
                name = line;
            }
        } else {
            name = line;
        }
        while ((line = br.readLine()) != null) {
            st = new StringTokenizer(line);
            if (st.countTokens() == 0) {
                continue;
            } else if (st.countTokens() != 2) {
                IOException ioex = new IOException("Error reading airfoil file");
                throw (ioex);
            } else {
                vx.add(Double.valueOf(st.nextToken()));
                vy.add(Double.valueOf(st.nextToken()));
            }
        }
        double x[] = new double[vx.size()];
        double y[] = new double[vy.size()];
        for (int i = 0; i < vx.size(); i++) {
            x[i] = vx.get(i).doubleValue();
            y[i] = vy.get(i).doubleValue();
        }
        Airfoil a = new Airfoil(x, y);
        a.m_name = name;
        return a;
    }

    /**
     * Find the leading edge point in an airfoil assuming a "normal" shape.
     * The presence of spar slots or other cutouts may confuse the algorithm.
     * A new point will be added to the internal spline at the exact point found
     * and a marker will be added at the point.
     * @return The leading edge point.
     */
    private void findLeadingEdge() {
        Point pts[] = getControlPoints();
        double xte = .5 * (pts[0].x + pts[pts.length - 1].x);
        double yte = .5 * (pts[0].y + pts[pts.length - 1].y);
        double lastDot = 0.;
        int ile;
        for (ile = 2; ile < pts.length - 1; ile++) {
            double dxte = pts[ile].x - xte;
            double dyte = pts[ile].y - yte;
            double dx = pts[ile].x - pts[ile - 1].x;
            double dy = pts[ile].y - pts[ile - 1].y;
            double dot = dxte * dx + dyte * dy;
            double test = Math.signum(dot) * Math.signum(lastDot);
            if (ile > 2 && test <= 0.) break;
            lastDot = dot;
        }
        double sle = pts[ile].s;
        double stol = sle * 1.e-8;
        double dsle;
        int iter = 10;
        do {
            Point pt = evaluate(sle);
            double xchord = pt.x - xte;
            double ychord = pt.y - yte;
            double res = pt.x_s * xchord + pt.y_s * ychord;
            double res_s = pt.x_s * pt.x_s + pt.y_s * pt.y_s + pt.x_ss * xchord + pt.y_ss * ychord;
            dsle = -res / res_s;
            double chord = Math.hypot(xchord, ychord);
            double curv = (pt.x_s * pt.y_ss - pt.y_s * pt.x_ss) / Math.sqrt(Math.pow(pt.x_s * pt.x_s + pt.y_s * pt.y_s, 3));
            double dslim = 0.01 * chord;
            if (curv != 0.0) {
                dslim = Math.min(dslim, .1 / Math.abs(curv));
            }
            dsle = Math.min(dsle, dslim);
            dsle = Math.max(dsle, -dslim);
            sle += dsle;
            iter--;
            if (iter == 0) {
                IllegalStateException isx = new IllegalStateException("Find leading edge failed to converge");
                throw (isx);
            }
        } while (Math.abs(dsle) > stol);
        lePoint = evaluate(sle);
    }
}

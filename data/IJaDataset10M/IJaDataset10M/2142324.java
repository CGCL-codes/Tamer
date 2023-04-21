package jat.vr;

import jat.cm.*;
import jat.alg.integrators.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Orbit extends Shape3D implements Printable {

    public double[] coords;

    public double[] t, x, y, z;

    int j = 0;

    Color3f Color = Colors.pink;

    private double a;

    private double e;

    private double i;

    private double raan;

    private double w;

    private double ta;

    private int steps = 500;

    Constants c = new Constants();

    public Orbit(double[] coords) {
        this.coords = coords;
    }

    public Orbit() {
        draw_orbit();
    }

    /** Construct a TwoBody orbit from 6 orbit elements. Angles are input in degrees.
	 * @param a Semi-major axis in km.
	 * @param e Eccentricity.
	 * @param i Inclination in degrees.
	 * @param raan RAAN in degrees.
	 * @param w Argument of perigee in degrees.
	 * @param ta True anomaly in degrees.
	 */
    public Orbit(double a, double e, double i, double raan, double w, double ta) {
        this.a = a;
        this.e = e;
        this.i = i;
        this.raan = raan;
        this.w = w;
        this.ta = ta;
        draw_orbit();
    }

    public Orbit(double a, double e, double i, double raan, double w, double ta, Color3f Color, int steps) {
        this.a = a;
        this.e = e;
        this.i = i;
        this.raan = raan;
        this.w = w;
        this.ta = ta;
        this.Color = Color;
        this.steps = steps;
        draw_orbit();
    }

    public Orbit(KeplerElements el) {
        this.a = el.a;
        this.e = el.e;
        this.i = Math.toRadians(el.i);
        this.raan = Math.toRadians(el.raan);
        this.w = Math.toRadians(el.w);
        this.ta = Math.toRadians(el.ta);
        draw_orbit();
    }

    public Orbit(KeplerElements el, Color3f Color) {
        this.a = el.a;
        this.e = el.e;
        this.i = Math.toRadians(el.i);
        this.raan = Math.toRadians(el.raan);
        this.w = Math.toRadians(el.w);
        this.ta = Math.toRadians(el.ta);
        this.Color = Color;
        draw_orbit();
    }

    public void print(double time, double[] pos) {
        t[j] = time;
        x[j] = pos[0];
        y[j] = pos[1];
        z[j] = pos[2];
        j++;
    }

    private void draw_orbit() {
        coords = new double[3 * steps + 6];
        t = new double[steps + 2];
        x = new double[steps + 2];
        y = new double[steps + 2];
        z = new double[steps + 2];
        TwoBody sat = new TwoBody(a, e, i, raan, w, ta);
        double tf = sat.period();
        sat.propagate(0., tf, this, true, steps);
        coords = new double[steps * 3];
        for (int k = 0; k < steps; k++) {
            coords[k * 3 + 0] = x[k];
            coords[k * 3 + 1] = y[k];
            coords[k * 3 + 2] = z[k];
        }
        int num_vert = coords.length / 3;
        int[] stripLengths = { num_vert };
        LineStripArray myLines = new LineStripArray(num_vert, GeometryArray.COORDINATES | GeometryArray.COLOR_3, stripLengths);
        Color3f colors[] = new Color3f[num_vert];
        for (int i = 0; i < num_vert; i++) colors[i] = Color;
        myLines.setColors(0, colors);
        myLines.setCoordinates(0, coords);
        this.setGeometry(myLines);
    }
}

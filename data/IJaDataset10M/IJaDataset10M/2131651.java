package kursach2;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.Arrays;

/**
 * @author Vsevolod
 *  --------------------------  �� ��������  ! ! !
 */
public class PolygonBarier extends ABarier {

    private class MyPolygon extends Path2D.Double {

        double[] x_normal, y_normal;

        double[] x, y;

        int npoints;

        public MyPolygon() {
            super();
            x = new double[10];
            y = new double[10];
            x_normal = new double[10];
            y_normal = new double[10];
            npoints = 0;
        }

        public MyPolygon(double[] xpoints, double[] ypoints, int n) {
            x = xpoints;
            y = ypoints;
            npoints = n;
            x_normal = new double[x.length];
            y_normal = new double[y.length];
            for (int i = 0; i < n - 1; i++) {
                x_normal[i] = x[i + 1] - x[i];
                y_normal[i] = y[i + 1] - y[i];
            }
            x_normal[n - 1] = x[0] - x[n - 1];
            y_normal[n - 1] = y[0] - y[n - 1];
        }

        public void addPoint(double x, double y) {
            if (npoints >= this.x.length) {
                this.x = Arrays.copyOf(this.x, this.x.length + 10);
                this.y = Arrays.copyOf(this.y, this.x.length + 10);
                this.x_normal = Arrays.copyOf(this.x_normal, this.x.length + 10);
                this.y_normal = Arrays.copyOf(this.y_normal, this.x.length + 10);
            }
            this.x[npoints] = x;
            this.y[npoints] = y;
            npoints++;
            if (npoints < 1) {
                this.moveTo(x, y);
                return;
            }
            this.lineTo(x, y);
            x_normal[npoints - 2] = this.x[npoints - 1] - this.x[npoints - 2];
            y_normal[npoints - 2] = this.y[npoints - 1] - this.y[npoints - 2];
            x_normal[npoints - 1] = this.x[0] - this.x[npoints - 1];
            y_normal[npoints - 1] = this.y[0] - this.y[npoints - 1];
        }
    }

    MyPolygon plg;

    @Override
    public Shape getShape() {
        return plg;
    }

    @Override
    public double getTimeCollision(IBarier b, double dt, int n) {
        return 0;
    }

    @Override
    public boolean is_contact(double x, double y) {
        return false;
    }

    @Override
    public boolean is_contact(IBarier ih) {
        return false;
    }
}

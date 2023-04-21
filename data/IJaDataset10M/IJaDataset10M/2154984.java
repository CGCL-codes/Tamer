package jmathlib.core.graphics.axes.coreObjects;

import java.awt.*;
import jmathlib.core.graphics.*;
import jmathlib.core.graphics.properties.*;

/** implementation of a line object*/
public class Line3DObject extends LineObject {

    public Line3DObject() {
        super();
    }

    public Line3DObject(double[] _x, double[] _y, double[] _z) {
        this(_x, _y, _z, "b", "-", "none");
    }

    /** Constructor for a line */
    public Line3DObject(double[] _x, double[] _y, double[] _z, String color, String lineStyle, String marker) {
        this();
        XDataP.setArray(_x);
        YDataP.setArray(_y);
        ZDataP.setArray(_z);
        double[] x = _x;
        double[] y = _y;
        double[] z = _z;
        mat.xrot(20);
        mat.yrot(30);
        xmin = x[0];
        xmax = x[0];
        ymin = y[0];
        ymax = y[0];
        zmin = z[0];
        zmax = z[0];
        for (int i = 0; i < x.length; i++) {
            if (x[i] < xmin) xmin = x[i];
            if (x[i] > xmax) xmax = x[i];
            if (y[i] < ymin) ymin = y[i];
            if (y[i] > ymax) ymax = y[i];
            if (z[i] < zmin) zmin = z[i];
            if (z[i] > zmax) zmax = z[i];
        }
        ColorP.setColor(color);
        LineStyleP.setStyle(lineStyle);
        MarkerP.setMarker(marker);
    }

    public void paint(Graphics g) {
        double[] x = XDataP.getArray();
        double[] y = YDataP.getArray();
        double[] z = ZDataP.getArray();
        if (x == null) return;
        if (x.length == 0) return;
        double dx = xmax - xmin;
        double dy = ymax - ymin;
        int[] tx = new int[x.length];
        int[] ty = new int[x.length];
        int[] tz = new int[x.length];
        mat.transform(x, y, z, tx, ty, tz);
        int xLast = tx[0];
        int yLast = ty[0];
        g.setColor(ColorP.getColor());
        int x0, x1, y0, y1;
        for (int i = 0; i < tx.length; i++) {
            g.drawLine(xLast, yOrig - yLast, tx[i], yOrig - ty[i]);
            xLast = tx[i];
            yLast = ty[i];
        }
    }
}

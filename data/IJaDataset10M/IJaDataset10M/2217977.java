package auo.cms.hsv.value.experiment;

import java.awt.*;
import shu.cms.plot.*;
import shu.math.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class ValueAdjustmentPlotter {

    public static void main(String[] args) {
        Plot3D plot = Plot3D.getInstance();
        Plot3D plot2 = Plot3D.getInstance();
        double scale = 20;
        double[][] dgriddata1 = new double[(int) scale + 1][(int) scale + 1];
        double[][] dgriddata2 = new double[(int) scale + 1][(int) scale + 1];
        double[][] griddata0 = new double[(int) scale + 1][(int) scale + 1];
        double[][] griddata1 = new double[(int) scale + 1][(int) scale + 1];
        double[][] griddata2 = new double[(int) scale + 1][(int) scale + 1];
        double[] X_S = new double[(int) scale + 1];
        double[] X_V = new double[(int) scale + 1];
        for (int x = 0; x < (int) scale + 1; x++) {
            X_S[x] = x / scale;
            X_V[x] = (int) x / scale * 255;
        }
        int voffset = 20;
        double cratio = 1;
        double sgap = 0.15625;
        for (int s = 0; s <= scale; s++) {
            for (int v = 0; v <= scale; v++) {
                double ss = s / scale;
                int vv = (int) (v / scale * 255);
                double c = ss * vv;
                double cp = (c * cratio > 255) ? 255 : c * cratio;
                int intc = (int) c;
                int intc2 = intc >> 3;
                int newintc = intc * (intc2 * intc2);
                int M = vv;
                int m = M - intc;
                int voffset1 = (int) (voffset * intc * ((255 - vv) * (1. + sgap - ss))) >> 13;
                int voffset2 = (int) (voffset * intc * ((255 - vv) * (1. + sgap - ss))) >> 13;
                double v1 = vv + voffset1;
                double v2 = vv + voffset2;
                v1 = v1 > 255 ? 255 : v1;
                v2 = v2 > 255 ? 255 : v2;
                double deltav1 = v1 - vv;
                double deltav2 = v2 - vv;
                dgriddata1[v][s] = deltav1;
                dgriddata2[v][s] = deltav2;
                griddata0[s][v] = vv;
                griddata1[s][v] = v1;
                griddata2[s][v] = v2;
            }
        }
        System.out.println(Maths.max(dgriddata2));
        plot.addGridPlot("org", Color.red, X_S, X_V, dgriddata1);
        plot.addGridPlot("new", Color.blue, X_S, X_V, dgriddata2);
        plot.setAxeLabel(0, "s");
        plot.setAxeLabel(1, "v");
        plot.setAxeLabel(2, "dv");
        plot.setVisible();
        plot.addLegend();
        plot.rotate(-60, -50);
        plot.setFixedBounds(1, 0, 255);
        plot.setFixedBounds(2, 0, 20);
        plot.rotate(180, 0);
        plot2.addGridPlot("0", Color.black, X_S, X_V, griddata0);
        plot2.addGridPlot("org", Color.red, X_S, X_V, griddata1);
        plot2.addGridPlot("new", Color.blue, X_S, X_V, griddata2);
        plot2.setAxeLabel(0, "v");
        plot2.setAxeLabel(1, "s");
        plot2.setAxeLabel(2, "v");
        plot2.setVisible();
        plot2.addLegend();
    }
}

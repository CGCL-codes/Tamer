package coda.plot.objects;

import coda.plot.*;
import ext.jama.Matrix;
import coda.plot.CoDa2dDisplay;
import coda.stats.Compositional;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 *
 * @author marc
 */
public class Ternary2dCurveObject implements Ternary2dObject {

    private double data[][];

    private double data0[][];

    private double dataset[][];

    double center[];

    private CoDa2dDisplay display;

    private Color color = Color.BLACK;

    public Ternary2dCurveObject(CoDa2dDisplay display, double dataset[][]) {
        this.display = display;
        this.dataset = dataset;
        center = Compositional.center(new Matrix(dataset).transpose().getArray());
        int n = dataset.length;
        data0 = new double[n][];
        data = new double[n][2];
        for (int i = 0; i < n; i++) {
            data0[i] = Compositional.ternaryTransform(dataset[i][0], dataset[i][1], dataset[i][2]);
        }
    }

    @Override
    public void plotObject(Graphics2D g2) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1.0f, BasicStroke.JOIN_MITER, BasicStroke.CAP_ROUND));
        Point2D from = null, to = null;
        AffineTransform affine = display.getGeometry();
        for (int i = 1; i < data.length; i++) {
            from = affine.transform(new Point2D.Double(data[i - 1][0], data[i - 1][1]), from);
            to = affine.transform(new Point2D.Double(data[i][0], data[i][1]), to);
            g2.draw(PlotUtils.drawLine(from, to));
        }
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void transformObject(coda.plot.CoDa2dDisplay display) {
        for (int i = 0; i < data.length; i++) data[i] = display.transform(data0[i][0], data0[i][1], data[i]);
    }

    @Override
    public void perturbeObject(double[] x) {
        for (int i = 0; i < dataset.length; i++) data0[i] = Compositional.ternaryTransform(x[0] * dataset[i][0], x[1] * dataset[i][1], x[2] * dataset[i][2]);
    }

    public void powerObject(double t) {
        for (int i = 0; i < dataset.length; i++) data0[i] = Compositional.ternaryTransform(Math.pow(dataset[i][0], t), Math.pow(dataset[i][1], t), Math.pow(dataset[i][2], t));
    }

    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public LegendItem getLegendItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] getCenter() {
        return center;
    }
}

package shu.cms.plot;

import java.awt.*;
import org.math.plot.plots.*;
import quickhull3d.*;
import shu.cms.colorspace.ColorSpace;
import shu.cms.colorspace.depend.*;
import shu.util.log.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.File;
import shu.plot.PlotBase;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author cms.shu.edu.tw
 * @version 1.0
 */
public abstract class Plot3D extends shu.plot.Plot3D {

    public static void main(String[] args) {
        Plot3D plot = Plot3D.getInstance("", Plot3D.Instance.jzy3D);
        double[][] data = new double[9][2];
        for (int x = 0; x < 9; x++) {
            data[x][0] = Math.random();
            data[x][1] = Math.random();
        }
        plot.addGridPlot("", Color.red, data);
        plot.rotateToAxis(2);
        plot.setVisible();
        plot.zoom(130, 130);
    }

    protected Plot3D(String title, int width, int height) {
        super(title, width, height);
    }

    protected Plot3D(shu.plot.Plot3D plot) {
        super(plot);
    }

    public final int addColorSpace(String name, ColorSpace cs) {
        return addColorSpace(name, getNewColor(), cs);
    }

    public final int addColorSpace(String name, Color c, ColorSpace cs) {
        return addScatterPlot(name, c, new double[][] { cs.getValues() });
    }

    private void addQuickHull3D(RGB2ColorSpaceTransfer transfer, Color c, QuickHull3D hull, float alpha) {
        Point3d[] vertices = hull.getVertices();
        int[][] faceIndices = hull.getFaces();
        for (int[] index : faceIndices) {
            Point3d p0 = vertices[index[0]];
            Point3d p1 = vertices[index[1]];
            Point3d p2 = vertices[index[2]];
            PolygonPlot polygon = null;
            if (c == null) {
                Point3d p = new Point3d(p0);
                p.add(p1);
                p.add(p2);
                p.scale(1. / 3);
                RGB rgb = transfer.getRGB(new double[] { p.z, p.x, p.y });
                polygon = new PolygonPlot("", rgb.getColor(), new double[] { p0.x, p0.y, p0.z }, new double[] { p1.x, p1.y, p1.z }, new double[] { p2.x, p2.y, p2.z });
            } else {
                polygon = new PolygonPlot("", c, new double[] { p0.x, p0.y, p0.z }, new double[] { p1.x, p1.y, p1.z }, new double[] { p2.x, p2.y, p2.z });
            }
            polygon.alpha = alpha;
            this.addPlot(polygon);
        }
    }

    public void addQuickHull3D(RGB2ColorSpaceTransfer transfer, QuickHull3D hull, float alpha) {
        addQuickHull3D(transfer, null, hull, alpha);
    }

    public void addQuickHull3D(Color c, QuickHull3D hull, float alpha) {
        addQuickHull3D(null, c, hull, alpha);
    }

    public static final Plot3D getInstance() {
        return Plot3D.getInstance("Plot3D", 600, 600);
    }

    public static final Plot3D getInstance(String title, int width, int height, Instance instance) {
        shu.plot.Plot3D plot = shu.plot.Plot3D.getInstance(title, width, height, instance);
        if (instance == Instance.LiveGraphics3D) {
        }
        Plot3DWrapper wrapper = new Plot3DWrapper(plot);
        return wrapper;
    }

    public static final Plot3D getInstance(String title, int width, int height) {
        return getInstance(title, width, height, Instance.JMathPlot3D);
    }

    public static final Plot3D getInstance(String title, Instance instance) {
        return getInstance(title, 600, 600, instance);
    }

    public static final Plot3D getInstance(String title) {
        return getInstance(title, 600, 600);
    }

    public void setAxisLabels(ColorSpace cs) {
        this.setAxisLabels(cs.getBandNames());
    }

    static class Plot3DWrapper extends Plot3D implements PlotWrapperInterface {

        private shu.plot.Plot3D plot;

        public PlotBase getOriginalPlot() {
            return plot;
        }

        protected Plot3DWrapper(shu.plot.Plot3D plot) {
            super(plot);
            this.plot = plot;
        }

        public void _dispose() {
            plot._dispose();
        }

        public int addGridPlot(String name, Color c, double[] X, double[] Y, double[][] Z) {
            return plot.addGridPlot(name, c, X, Y, Z);
        }

        public int addHistogramPlot(String name, Color c, double[][] XYdX) {
            return plot.addHistogramPlot(name, c, XYdX);
        }

        public void addImage(Image img, float alpha, double[] xySW, double[] xySE, double[] xyNW) {
            plot.addImage(img, alpha, xySW, xySE, xyNW);
        }

        public void addLegend(String o) {
            plot.addLegend(o);
        }

        public void addLegend() {
            plot.addLegend();
        }

        public int addLinePlot(String name, Color c, double[]... XY) {
            return plot.addLinePlot(name, c, XY);
        }

        public int addPlanePlot(String name, Color c, double[][][] data) {
            return plot.addPlanePlot(name, c, data);
        }

        public int addPlot(Plot newPlot) {
            return plot.addPlot(newPlot);
        }

        public int addPolygonPlot(String name, Color c, double[] p0, double[] p1, double[] p2) {
            return plot.addPolygonPlot(name, c, p0, p1, p2);
        }

        public int addPolygonPlot(String name, Color c, double[] p0, double[] p1, double[] p2, double[] p3) {
            return plot.addPolygonPlot(name, c, p0, p1, p2, p3);
        }

        public int addScatterPlot(String name, Color c, double[]... XY) {
            return plot.addScatterPlot(name, c, XY);
        }

        public void addVectortoPlot(int numPlot, double[][] v) {
            plot.addVectortoPlot(numPlot, v);
        }

        public double[] getFixedBounds(int axe) {
            return plot.getFixedBounds(axe);
        }

        public JPanel getPlotPanel() {
            return plot.getPlotPanel();
        }

        public int getPlotSize() {
            return plot.getPlotSize();
        }

        public void removeAllPlots() {
            plot.removeAllPlots();
        }

        public void removePlot(int index) {
            plot.removePlot(index);
        }

        public void rotate(int vec0, int vec1) {
            plot.rotate(vec0, vec1);
        }

        public void setAxisVisible(int axe, boolean v) {
            plot.setAxisVisible(axe, v);
        }

        public void setAxeLabel(int axe, String label) {
            plot.setAxeLabel(axe, label);
        }

        public void setAxisScale(int axe, Scale scale) {
            plot.setAxisScale(axe, scale);
        }

        public void setBackground(Color bg) {
            plot.setBackground(bg);
        }

        public void setChartTitle(String title) {
            plot.setChartTitle(title);
        }

        public void setFixedBounds(int axe, double min, double max) {
            plot.setFixedBounds(axe, min, max);
        }

        public void setGridVisible(int axe, boolean v) {
            plot.setGridVisible(axe, v);
        }

        public void setPlotVisible(boolean b) {
            plot.setPlotVisible(b);
        }

        public void setPlotVisible(int num, boolean b) {
            plot.setPlotVisible(num, b);
        }

        public void setView(double theta, double phi) {
            plot.setView(theta, phi);
        }

        public void toGraphicFile(File file) {
            plot.toGraphicFile(file);
        }

        public void zoom(double wPercent, double hPercent) {
            plot.zoom(wPercent, hPercent);
        }

        public void setVisible() {
            plot.setVisible();
        }

        public void setVisible(boolean visible) {
            plot.setVisible(visible);
        }

        public void setLinePlotDrawDot(boolean drawDot) {
            plot.setLinePlotDrawDot(drawDot);
        }

        public void setLinePlotWidth(int lineWidth) {
            plot.setLinePlotWidth(lineWidth);
        }

        public void setLineType(LineType lineType) {
            plot.setLineType(lineType);
        }

        public boolean setScatterPlotPattern(int index, Pattern pattern) {
            return plot.setScatterPlotPattern(index, pattern);
        }

        public boolean setDotRadius(int index, int radius) {
            return plot.setDotRadius(index, radius);
        }

        public boolean setDotFill(int index, DotFill dotFill) {
            return plot.setDotFill(index, dotFill);
        }
    }
}

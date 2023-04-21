package shu.plot;

import java.awt.*;
import javax.swing.*;
import static org.math.plot.utils.Array.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public abstract class Plot2D extends PlotBase {

    private static Plot2D staticPlot;

    public static Plot2D getStaticInstance() {
        if (staticPlot == null) {
            staticPlot = getInstance();
        }
        return staticPlot;
    }

    public static Plot2D getInstance() {
        return Plot2D.getInstance("Plot2D", 600, 600);
    }

    public static Plot2D getInstance(String title, int width, int height) {
        Plot2D plot = new JMathPlot2D(title, width, height);
        getInstance0(plot);
        return plot;
    }

    public static Plot2D getInstance(String title) {
        return getInstance(title, 600, 600);
    }

    public abstract void setPlotOnTop(boolean onTop);

    /**
   * ��x/y�b���Y���ҬۦP,�Hx�b���D
   * @param xMin double
   * @param xMax double
   * @param yMin double
   */
    public abstract void setFixedBoundsByXAxis(double xMin, double xMax, double yMin);

    /**
   * ��x/y�b���Y���ҬۦP,�HY�b���D
   * @param xMin double
   * @param yMin double
   * @param yMax double
   */
    public abstract void setFixedBoundsByYAxis(double xMin, double yMin, double yMax);

    public void setFixedBoundsByXAxis() {
        double[] xbounds = this.getFixedBounds(0);
        double[] ybounds = this.getFixedBounds(1);
        this.setFixedBoundsByXAxis(xbounds[0], xbounds[1], ybounds[0]);
    }

    public void setFixedBoundsByYAxis() {
        double[] xbounds = this.getFixedBounds(0);
        double[] ybounds = this.getFixedBounds(1);
        this.setFixedBoundsByYAxis(xbounds[0], ybounds[0], ybounds[1]);
    }

    protected Plot2D(String title, int width, int height) {
        super(title, width, height);
    }

    protected Plot2D(Plot2D plot) {
        super(plot.frame);
    }

    public abstract int addStaircasePlot(String name, Color c, double[]... XY);

    public abstract int addBarPlot(String name, Color c, double[]... XY);

    public int addBarPlot(String name, double[]... XY) {
        return addBarPlot(name, getNewColor(), XY);
    }

    public abstract int addBoxPlot(String name, Color c, double[][] XYdxdY);

    public int addBoxPlot(String name, double[][] XYdxdY) {
        return addBoxPlot(name, getNewColor(), XYdxdY);
    }

    public final void addCacheLinePlot(String name, double start, double end, double y) {
        addCacheLinePlot(name, getNowCachePlotColor(), start, end, y);
    }

    protected class BoxCachePlot extends CachePlot {

        /**
     * BoxCachePlot
     *
     * @param name String
     * @param c Color
     */
        protected BoxCachePlot(String name, Color c) {
            super(name, c);
        }

        protected BoxCachePlot(String name, Color c, double[] values) {
            super(name, c, values);
        }

        /**
     *
     * @return int
     */
        public int draw() {
            double[][] datas = get2DArrayFromValuesList();
            return addBoxPlot(name, c, datas);
        }
    }

    protected class LineCachePlot extends CachePlot {

        public int draw() {
            double[] line = get1DArrayFromValuesList();
            return addLinePlot(name, c, start, end, line);
        }

        public LineCachePlot(String name, Color c, double start, double end) {
            super(name, c);
            this.start = start;
            this.end = end;
        }

        public LineCachePlot(String name, Color c, double start, double end, double[] values) {
            super(name, c, values);
            this.start = start;
            this.end = end;
        }

        private double start;

        private double end;
    }

    public final void addCacheLinePlot(String name, Color c, double start, double end, double y) {
        double[] values = new double[] { y };
        checkParameter(name, values);
        CachePlot cachePlot = cache.getCachePlot(LineCachePlot.class, name);
        if (cachePlot == null) {
            cachePlot = new LineCachePlot(name, c, start, end, values);
        } else {
            cachePlot.addPlotValues(values);
        }
    }

    public final void addCacheBoxPlot(String name, double x, double y, double width, double height) {
        addCacheBoxPlot(name, getNowCachePlotColor(), x, y, width, height);
    }

    public final void addCacheBoxPlot(String name, Color c, double x, double y, double width, double height) {
        double[] values = new double[] { x, y, width, height };
        checkParameter(name, values);
        CachePlot cachePlot = cache.getCachePlot(BoxCachePlot.class, name);
        if (cachePlot == null) {
            cachePlot = new BoxCachePlot(name, c, values);
        } else {
            cachePlot.addPlotValues(values);
        }
    }

    public final void addCacheScatterPlot(String name, double x, double y) {
        addCacheScatterPlot(name, getNowCachePlotColor(), x, y);
    }

    public final void addCacheScatterPlot(String name, Color c, double x, double y) {
        addCacheScatterPlot(name, c, new double[] { x, y });
    }

    public final void addCacheScatterLinePlot(String name, double x, double y) {
        addCacheScatterLinePlot(name, getNowCachePlotColor(), x, y);
    }

    public final Color getNowCachePlotColor() {
        return getNewColor(cache.getCachePlotSize());
    }

    public final void addCacheScatterLinePlot(String name, Color c, double x, double y) {
        addCacheScatterLinePlot(name, c, new double[] { x, y });
    }

    public final void addCacheScatterLinePlot(double x, double[] rgbValues) {
        if (rgbValues.length != 3) {
            throw new IllegalArgumentException("");
        }
        addCacheScatterLinePlot("R", Color.red, new double[] { x, rgbValues[0] });
        addCacheScatterLinePlot("G", Color.green, new double[] { x, rgbValues[1] });
        addCacheScatterLinePlot("B", Color.blue, new double[] { x, rgbValues[2] });
    }

    public final int addLinePlot(String name, double[]... XY) {
        return addLinePlot(name, this.getNewColor(), XY);
    }

    public final int addLinePlot(String name, double start, double end, double[] Y) {
        return addLinePlot(name, this.getNewColor(), start, end, Y);
    }

    public final int addLinePlot(String name, Color c, double x1, double y1, double x2, double y2) {
        return addLinePlot(name, c, new double[][] { { x1, x2 }, { y1, y2 } });
    }

    public final int addLinePlot(String name, double x1, double y1, double x2, double y2) {
        return addLinePlot(name, this.getNewColor(), x1, y1, x2, y2);
    }

    public final int addLinePlot(String name, Color c, double start, double end, double[] Y) {
        double[][] lineData = produceLineData(start, end, Y);
        return addLinePlot(name, c, lineData);
    }

    protected static final double[][] produceLineData(double XStart, double XEnd, double[] YData) {
        int size = YData.length;
        double interval = (XEnd - XStart) / (size - 1);
        double[][] XYZData = new double[2][size];
        for (int x = 0; x < size; x++) {
            XYZData[0][x] = XStart + x * interval;
            XYZData[1][x] = YData[x];
        }
        return XYZData;
    }

    public static void main(String[] args) {
        Plot2D h = Plot2D.getInstance();
        h.addCacheScatterPlot("s", 1, 2);
        h.addCacheScatterPlot("s", 2, 2);
        h.addCacheScatterPlot("s", 3, 2);
        h.addCacheScatterLinePlot("a", 1, 3);
        h.addCacheScatterLinePlot("a", 2, 3);
        h.addCacheScatterLinePlot("a", 3, 3);
        h.addCacheLinePlot("l", 0, 2, 2);
        h.addCacheLinePlot("l", 0, 2, 2.1);
        h.addCacheLinePlot("l", 0, 2, 2.2);
        h.setVisible();
        System.out.println("");
    }

    public final int addScatterPlot(String name, double[]... XY) {
        return addScatterPlot(name, getNewColor(), XY);
    }

    public final int addScatterPlot(String name, double x, double y) {
        return addScatterPlot(name, new double[][] { { x }, { y } });
    }

    public final int addScatterPlot(String name, Color c, double x, double y) {
        return addScatterPlot(name, c, new double[][] { { x }, { y } });
    }

    public final int addScatterPlot(String name, double[] xAxis, double[] yAxis) {
        return addScatterPlot(name, new double[][] { xAxis, yAxis });
    }

    public final int addScatterPlot(String name, Color c, double[] xAxis, double[] yAxis) {
        return addScatterPlot(name, c, new double[][] { xAxis, yAxis });
    }

    public void addImage(Image img, float alpha, double[] originalSW, double width) {
        double ratio = (double) img.getHeight(null) / img.getWidth(null);
        double height = width * ratio;
        double[] xySE = new double[] { originalSW[0] + width, originalSW[1] };
        double[] xyNW = new double[] { originalSW[0], originalSW[1] + height };
        addImage(img, alpha, originalSW, xySE, xyNW);
    }

    public abstract int addGridPlot(String name, Color c, double[][] X, double[][] Y);

    public int addGridPlot(String name, double[][] X, double[][] Y) {
        return addGridPlot(name, getNewColor(), X, Y);
    }

    public int getAxisCount() {
        return 2;
    }

    public abstract int addHistogramPlot(String name, Color color, double[] sample, int n);

    public final int addHistogramPlot(String name, double[] sample, int n) {
        return addHistogramPlot(name, getNewColor(), sample, n);
    }
}

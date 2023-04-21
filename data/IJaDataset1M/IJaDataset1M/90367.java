package org.hswgt.teachingbox.plot;

import org.apache.log4j.Logger;
import org.hswgt.teachingbox.env.State;
import org.hswgt.teachingbox.tools.RangeParser;
import org.hswgt.teachingbox.valuefunctions.ValueFunction;

/**
 * Creates a surface plot of a ValueFunction
 */
public class ValueFunctionSurfacePlotter implements Plotter {

    private static final long serialVersionUID = -2121765611837250008L;

    protected Logger log4j = Logger.getLogger(this.getClass().getSimpleName());

    protected ValueFunction V;

    protected double[] xrange;

    protected double[] yrange;

    protected String[] gnuplotcommands;

    GnuPlotExec gplot = new GnuPlotExec();

    /**
     * Creates a surface plot of a ValueFunction
     * @param V The ValueFunction to plot
     * @param xrange The range to plot in x-direction (matlab syntax {from:intervall:to})
     * @param yrange The range to plot in y-direction (matlab syntax {from:intervall:to})
     * @param gnuplotcommands A list of strings that will be executed by gnuplot before the plotting starts
     * 
     * <pre>
     * Usage:
     *  double[] xrange = {-1.2, 0.03, 0.5};
     *  double[] yrange = {-0.07, 0.003, 0.07};
     *  ValueFunctionSurfacePlotter surf = new ValueFunctionSurfacePlotter(V, xrange, yrange, "set title 'MySurfacePlot'");
     * </pre>
     */
    public ValueFunctionSurfacePlotter(ValueFunction V, double[] xrange, double[] yrange, String... gnuplotcommands) {
        this.V = V;
        this.xrange = xrange.clone();
        this.yrange = yrange.clone();
        this.gnuplotcommands = gnuplotcommands.clone();
    }

    /**
     * Creates a surface plot of a ValueFunction
     * @param V The ValueFunction to plot
     * @param xrange The range to plot in x-direction (matlab syntax {from:intervall:to})
     * @param yrange The range to plot in y-direction (matlab syntax {from:intervall:to})
     * @param gnuplotcommands A list of strings that will be executed by gnuplot before the plotting starts
     * 
     * <pre>
     * Usage:
     *  ValueFunctionSurfacePlotter surf = new ValueFunctionSurfacePlotter(V, "[-1.2:0.03:0.5]", "[-0.07:0.003:0.07]", "set title 'MySurfacePlot'");
     * </pre>
     */
    public ValueFunctionSurfacePlotter(ValueFunction V, String xrange, String yrange, String... gnuplotcommands) {
        this.V = V;
        this.xrange = RangeParser.parse(xrange);
        this.yrange = RangeParser.parse(yrange);
        this.gnuplotcommands = gnuplotcommands.clone();
    }

    /**
     * Creates a surface plot of a ValueFunction
     */
    public void plot() {
        if (xrange.length < 3) {
            xrange = new double[] { xrange[0], 1, xrange[1] };
        }
        if (yrange.length < 3) {
            yrange = new double[] { yrange[0], 1, yrange[1] };
        }
        final int xint = (int) ((xrange[2] - xrange[0]) / xrange[1]);
        final int yint = (int) ((yrange[2] - yrange[0]) / yrange[1]);
        gplot.addCommand("set xrange [" + xrange[0] + ":" + xrange[2] + "]");
        gplot.addCommand("set yrange [" + yrange[0] + ":" + yrange[2] + "]");
        gplot.addCommand("set dgrid3d " + xint + ", " + yint + ", 10");
        if (gnuplotcommands != null) {
            for (String cmd : gnuplotcommands) gplot.addCommand(cmd);
        }
        gplot.addCommand("splot '-' title 'Value' with pm3d");
        State s = new State(2);
        for (double x = xrange[0]; x < xrange[2] + xrange[1]; x += xrange[1]) {
            if (x > xrange[2]) {
                x = xrange[2];
            }
            for (double y = yrange[0]; y < yrange[2] + yrange[1]; y += yrange[1]) {
                if (y > yrange[2]) {
                    y = yrange[2];
                }
                s.set(0, x);
                s.set(1, y);
                double value = V.getValue(s);
                gplot.addCommand(x + " " + y + " " + value);
            }
        }
        gplot.addCommand("e\n");
        try {
            gplot.plot();
        } catch (Exception e) {
            log4j.error("Unable to create ValueFunction plot");
            e.printStackTrace();
        }
    }

    /**
     * Creates a surface plot of a ValueFunction
     * @param V The ValueFunction to plot
     * @param xrange The range to plot in x-direction (matlab syntax {from:intervall:to})
     * @param yrange The range to plot in y-direction (matlab syntax {from:intervall:to})
     * @param gnuplotcommands A list of strings that will be executed by gnuplot before the plotting starts
     * 
     * <pre>
     * Usage:
     *  double[] xrange = {-1.2, 0.03, 0.5};
     *  double[] yrange = {-0.07, 0.003, 0.07};
     *  ValueFunctionSurfacePlotter.plot(V, xrange, yrange, "set title 'MySurfacePlot'");
     * </pre>
     */
    public static void plot(ValueFunction V, double[] xrange, double[] yrange, String... gnuplotcommands) {
        new ValueFunctionSurfacePlotter(V, xrange, yrange, gnuplotcommands).plot();
    }

    /**
     * Creates a surface plot of a ValueFunction
     * @param V The ValueFunction to plot
     * @param xrange The range to plot in x-direction (matlab syntax {from:intervall:to})
     * @param yrange The range to plot in y-direction (matlab syntax {from:intervall:to})
     * @param gnuplotcommands A list of strings that will be executed by gnuplot before the plotting starts
     * 
     * <pre>
     * Usage:
     *  ValueFunctionSurfacePlotter.plot(V, "[-1.2:0.03:0.5]", "[-0.07:0.003:0.07]", "set title 'MySurfacePlot'");
     * </pre>
     */
    public static void plot(ValueFunction V, String xrange, String yrange, String... gnuplotcommands) {
        new ValueFunctionSurfacePlotter(V, xrange, yrange, gnuplotcommands).plot();
    }
}

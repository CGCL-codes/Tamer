package org.hswgt.teachingbox.plot;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import org.hswgt.teachingbox.plot.GnuPlotExec;

/**
 * @author tokicm
 * Interface for 2D function plotting
 *
 */
public class FunctionPlotter2D implements java.io.Serializable {

    private static final long serialVersionUID = 3969309899432144927L;

    Logger log4j = Logger.getLogger(this.getClass().getSimpleName());

    protected String title = null;

    protected String filename = null;

    protected String xlabel = null, ylabel = null;

    protected double[] xrange = null, yrange = null;

    protected double[] xtics = null, ytics = null;

    protected double[] view = null;

    protected int isosamples[] = null;

    protected String epsFilename = null;

    protected String terminal = null;

    protected GnuPlotExec gplot = new GnuPlotExec();

    /**
	 * constructor with plot title 
	 * @param title
	 */
    public FunctionPlotter2D(String title) {
        this.title = title;
    }

    /**
	 * setup the axis-labels
	 * @param xlabel
	 * @param ylabel
	 */
    public void setLabel(String xlabel, String ylabel) {
        this.xlabel = xlabel;
        this.ylabel = ylabel;
    }

    /**
	 * setup the data ranges
	 * @param xrange
	 * @param yrange
	 */
    public void setRange(double[] xrange, double[] yrange) {
        if (xrange != null) this.xrange = xrange;
        if (yrange != null) this.yrange = yrange;
    }

    /**
	 * customization of the axis tics. Example xtics = {0, 10, 100} // from 0..100 stepwise with increment 10 or xtics = null;
	 * @param xtics
	 * @param ytics
	 */
    public void setTics(double[] xtics, double[] ytics) {
        if (xtics != null) this.xtics = xtics;
        if (ytics != null) this.ytics = ytics;
    }

    /**
	 * filename for the plot-output as EPS data (if desired) 
	 * @param filename
	 */
    public void setEpsFilename(String filename) {
        this.epsFilename = filename;
    }

    /**
	 * plots the header
	 */
    private void plotHeader() {
        if (this.xlabel != null) gplot.addCommand("set xlabel '" + xlabel + "'");
        if (this.ylabel != null) gplot.addCommand("set ylabel '" + ylabel + "'");
        if (this.xrange != null) {
            gplot.addCommand("set xrange [" + xrange[0] + ":" + xrange[1] + "]");
            log4j.info("set xrange [" + xrange[0] + ":" + xrange[1] + "]");
        }
        if (this.yrange != null) {
            gplot.addCommand("set yrange [" + yrange[0] + ":" + yrange[1] + "]");
            log4j.info("set yrange [" + yrange[0] + ":" + yrange[1] + "]");
        }
        if (this.xtics != null) {
            String tics = new Double(xtics[0]).toString();
            for (int i = 1; i < xtics.length; i++) tics = tics + ", " + new Double(xtics[i]).toString();
            gplot.addCommand("set xtics " + tics);
        }
        if (this.ytics != null) {
            String tics = new Double(ytics[0]).toString();
            for (int i = 1; i < ytics.length; i++) tics = tics + ", " + new Double(ytics[i]).toString();
            gplot.addCommand("set ytics " + tics);
        }
        gplot.addCommand("set pm3d hidden3d 1 corners2color mean");
        gplot.addCommand("set grid xtics ytics ztics");
        gplot.addCommand("set title '" + this.title + "'");
        if (this.terminal != null) {
            gplot.addCommand("set terminal " + this.terminal);
        }
    }

    /**
	 * plots the footer and finally invokes gnuplot
	 */
    private void plotFooter() {
        if (this.epsFilename != null) {
            gplot.addCommand("set terminal postscript color solid");
            gplot.addCommand("set output \"" + this.epsFilename + "\"");
            gplot.addCommand("replot");
            gplot.addCommand("set terminal " + this.terminal);
            gplot.addCommand("replot");
        }
        try {
            gplot.plot(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * plots the function dataString and saves the gnuplot-code to "filename"
	 * @param dataString
	 * @param filename
	 */
    public void plotFunction(String dataString, String filename) {
        this.filename = filename;
        this.plotHeader();
        gplot.addCommand("plot " + dataString + " with lines");
        this.plotFooter();
    }

    /**
	 * plots the data[][] array, and saves the gnuplot-code to "filename"
	 * @param data
	 * @param filename
	 */
    public void plotData(double[][] data, String filename) {
        this.filename = filename;
        this.plotHeader();
        gplot.addCommand("plot '-' title '" + this.ylabel + "' with lines");
        gplot.addCommand(data);
        this.plotFooter();
    }

    public void plotMData(ArrayList<FunctionPlotter2dData> dataFusion, String filename) {
        this.filename = filename;
        this.plotHeader();
        String cmd = new String("plot ");
        FunctionPlotter2dData fd;
        FunctionPlotter2dData.Mode mode;
        for (int i = 0; i < dataFusion.size(); i++) {
            fd = dataFusion.get(i);
            mode = fd.getMode();
            if (i > 0) {
                cmd = cmd + ", ";
            }
            if (mode == FunctionPlotter2dData.Mode.DOUBLE_ARRAY) {
                cmd = cmd + "'-' using 1:2 title '" + fd.getTitle() + "' with lines";
            } else if (mode == FunctionPlotter2dData.Mode.STRING) {
                cmd = cmd + " " + fd.getCommand() + " title '" + fd.getTitle() + "' with lines";
            }
        }
        gplot.addCommand(cmd);
        for (int i = 0; i < dataFusion.size(); i++) {
            fd = dataFusion.get(i);
            mode = fd.getMode();
            if (mode == FunctionPlotter2dData.Mode.DOUBLE_ARRAY) {
                gplot.addMData(fd.getData());
            }
        }
        this.plotFooter();
    }

    /**
     * A simple plotting example
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        double[] dataX = new double[] { -5, 2, 3 };
        double[] dataA = new double[] { 1, 2, 3 };
        double[] dataB = new double[] { 2, 4, 6 };
        double[] dataC = new double[] { 3, 6, 50 };
        FunctionPlotter2D fp = new FunctionPlotter2D("Testplot");
        fp.setTics(new double[] { -10, 1, 4 }, new double[] { 0, 5, 50 });
        fp.setRange(new double[] { -10, 4 }, null);
        fp.setLabel("x-axis", "y-axis");
        fp.setEpsFilename("test-function.eps");
        ArrayList<FunctionPlotter2dData> dataFusion = new ArrayList<FunctionPlotter2dData>();
        dataFusion.add(new FunctionPlotter2dData(new double[][] { dataX, dataA }, new String("Function A")));
        dataFusion.add(new FunctionPlotter2dData(new double[][] { dataX, dataB }, new String("Function B")));
        dataFusion.add(new FunctionPlotter2dData(new double[][] { dataX, dataC }, new String("Function C")));
        dataFusion.add(new FunctionPlotter2dData(new String("sin(x)*cos(x)"), new String("sin(x)*cos(x)")));
        dataFusion.add(new FunctionPlotter2dData(new String("cos(x)"), new String("cos(x)")));
        fp.plotMData(dataFusion, "test.gnuplot");
        System.out.println("press any key...");
        System.in.read();
        System.out.println("bye");
    }
}

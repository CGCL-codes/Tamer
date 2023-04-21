package drcl.comp.tool;

import java.io.*;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.HashMap;
import ptolemy.plot.Plot;
import ptolemy.plot.plotml.*;
import drcl.comp.*;
import drcl.data.*;
import drcl.comp.contract.*;
import drcl.util.StringUtil;

public class Plotter extends drcl.comp.Extension {

    static final String SEPARATOR = "--";

    Plot[] plots = null;

    int[] firstPoint = null;

    boolean receivedNaN = false;

    boolean plotEnabled = true, outEnabled = true;

    Port outport = addPort(".output", false);

    boolean limitRedraw = true;

    float redrawProbability = (float) .25;

    HashMap hmPlotInfo = null;

    public Plotter() {
        super();
    }

    public Plotter(String id_) {
        super(id_);
    }

    public synchronized void reset() {
        super.reset();
        receivedNaN = false;
        if (firstPoint != null) for (int i = 0; i < firstPoint.length; i++) firstPoint[i] = 0;
        if (isEnabled() && plotEnabled && plots != null) {
            for (int i = 0; i < plots.length; i++) if (plots[i] != null) {
                java.awt.Component c_ = plots[i].getParent();
                while (c_ != null && !(c_ instanceof DrclPlotFrame)) c_ = c_.getParent();
                if (c_ != null) ((DrclPlotFrame) c_).dispose();
                plots[i] = null;
            }
        }
        if (hmPlotInfo != null) hmPlotInfo.clear();
    }

    public void duplicate(Object source_) {
        if (!(source_ instanceof Plotter)) return;
    }

    public String info() {
        String setting_ = "     plot-enabled = " + plotEnabled + "\n" + "   output-enabled = " + outEnabled + "\n" + "   limited-redraw = " + outEnabled + "\n" + "redraw-probability= " + redrawProbability + "\n" + "         Plot Info: " + hmPlotInfo + "\n";
        StringBuffer sb_ = new StringBuffer();
        if (plots != null) for (int i = 0; i < plots.length; i++) if (plots[i] != null) {
            int sum_ = 0;
            for (int j = 0; j < plots[i].getNumDataSets(); j++) sum_ += plots[i].getNumPoints(j);
            sb_.append(plots[i].getTitle() + ": " + sum_ + " points.\n");
        }
        if (sb_.length() == 0) return setting_ + "No plot is created.\n";
        return setting_ + sb_;
    }

    void processXYData(XYDataInterface d, Port inPort_) {
        if (hmPlotInfo == null) hmPlotInfo = new HashMap();
        PlotInfo info_ = (PlotInfo) hmPlotInfo.get(d.getID());
        if (info_ == null) {
            int figID_ = Integer.parseInt(inPort_.groupID);
            Plot plot_ = getPlot(figID_);
            int setID_ = plot_.getNumDataSets();
            info_ = new PlotInfo(figID_, setID_);
            hmPlotInfo.put(d.getID(), info_);
            setLegend(figID_, setID_, d.getID().toString());
        }
        _plot(info_.figID, info_.setID, d.getX(), d.getY(), null, null);
    }

    protected void process(Object data_, Port inPort_) {
        if (data_ instanceof XYDataInterface) {
            processXYData((XYDataInterface) data_, inPort_);
            return;
        }
        int figID_ = 0, setID_ = 0;
        try {
            figID_ = Integer.parseInt(inPort_.groupID);
            setID_ = Integer.parseInt(inPort_.id);
        } catch (Exception e_) {
        }
        if (data_ instanceof DoubleEventContract.Message) {
            DoubleEventContract.Message s_ = (DoubleEventContract.Message) data_;
            _plot(figID_, setID_, s_.getTime(), s_.getValue(), s_.getEventName(), s_.getPortPath());
        } else if (data_ instanceof EventContract.Message) {
            EventContract.Message s_ = (EventContract.Message) data_;
            Object evt_ = s_.getEvent();
            double x_ = 0.0, y_ = 0.0;
            if (evt_ instanceof Double) {
                x_ = s_.getTime();
                y_ = ((Double) evt_).doubleValue();
            } else if (evt_ instanceof DoubleObj) {
                x_ = s_.getTime();
                y_ = ((DoubleObj) evt_).value;
            } else if (evt_ instanceof double[]) {
                double[] xy_ = (double[]) evt_;
                if (xy_.length >= 2) {
                    x_ = xy_[0];
                    y_ = xy_[1];
                } else if (xy_.length == 1) {
                    x_ = s_.getTime();
                    y_ = xy_[0];
                } else {
                    error(data_, "process()", inPort_, "zero-length double array");
                    return;
                }
            } else {
                error(data_, "process()", inPort_, "unrecognized event object: " + evt_);
                return;
            }
            _plot(figID_, setID_, x_, y_, s_.getEventName(), s_.getPortPath());
        } else if (data_ instanceof String) {
            String s_ = (String) data_;
            if (s_.indexOf("\n") >= 0) {
                StringReader sr_ = new StringReader(s_);
                plotsLoad(sr_);
            } else {
                double[] xy_ = new double[2];
                _rawPlotsParseLine(s_, xy_);
                _plot(figID_, setID_, xy_[0], xy_[1], null, null);
            }
        } else if (data_ instanceof double[]) {
            double[] xy_ = (double[]) data_;
            double x_ = 0.0, y_ = 0.0;
            if (xy_.length == 0) {
                error(data_, "process()", inPort_, "zero-length double array");
                return;
            } else if (xy_.length == 1) {
                x_ = getTime();
                y_ = xy_[0];
            } else {
                x_ = xy_[0];
                y_ = xy_[1];
            }
            _plot(figID_, setID_, x_, y_, null, null);
        } else if (data_ instanceof Double || data_ instanceof DoubleObj) {
            double y_ = data_ instanceof Double ? ((Double) data_).doubleValue() : ((DoubleObj) data_).value;
            _plot(figID_, setID_, getTime(), y_, null, null);
        } else {
            error(data_, "process()", inPort_, "unrecognized data");
        }
    }

    synchronized void _plot(int figID_, int setID_, double x_, double y_, Object titleObj_, Object legendObj_) {
        if (!receivedNaN && (Double.isNaN(x_) || Double.isNaN(y_))) {
            receivedNaN = true;
            System.err.println(this + " received NaN, skipped");
            System.err.println("\tFollowing NaN's will not be reported");
            return;
        }
        boolean justCreated_ = false;
        String title_ = titleObj_ == null ? "<No Title>" : titleObj_.toString();
        String legend_ = legendObj_ == null ? "???" : legendObj_.toString();
        synchronized (this) {
            if (plotEnabled && (plots == null || plots.length <= figID_ || plots[figID_] == null)) {
                addPlot(figID_, title_);
                justCreated_ = true;
            }
            if (firstPoint == null || firstPoint.length <= figID_) {
                int[] btmp_ = new int[figID_ + 1];
                if (firstPoint != null) System.arraycopy(firstPoint, 0, btmp_, 0, firstPoint.length);
                firstPoint = btmp_;
            }
            if (firstPoint[figID_] == 0) {
                firstPoint[figID_] = -1;
                if (outEnabled) write("###   NEW PLOT" + SEPARATOR + figID_ + SEPARATOR + title_ + SEPARATOR + "'- Time -'\n");
            }
        }
        Object syncObj_ = plots != null && plots.length > figID_ && plots[figID_] != null ? (Object) plots[figID_] : (Object) this;
        synchronized (syncObj_) {
            boolean firstPoint_ = (firstPoint[figID_] & (1 << setID_)) > 0;
            Plot plot_ = plotEnabled ? plots[figID_] : null;
            if (firstPoint_) {
                if (plotEnabled && plot_ != null) {
                    if (plot_.getLegend(setID_) == null) {
                        plot_.addLegend(setID_, legend_);
                    }
                }
                if (outEnabled) {
                    write("#####  NEW SET" + SEPARATOR + figID_ + SEPARATOR + setID_ + SEPARATOR + legend_ + "\n");
                }
                firstPoint[figID_] &= ~(1 << setID_);
            }
            if (plotEnabled && plot_ != null) {
                plot_.addPoint(setID_, x_, y_, !firstPoint_);
                if (!limitRedraw || Math.random() < redrawProbability) plot_.fillPlot();
            }
            if (outEnabled) {
                write((firstPoint_ ? 'm' : 'p') + SEPARATOR + figID_ + SEPARATOR + setID_ + SEPARATOR + x_ + SEPARATOR + y_ + SEPARATOR + legend_ + "\n");
            }
        }
    }

    void write(String line_) {
        try {
            outport.doSending(line_);
        } catch (Exception e_) {
            error(line_, "write()", null, e_);
        }
    }

    private void ___SCRIPT___() {
    }

    public void drawLine(int plotID_, int setID_, double x1, double y1, double x2, double y2) {
        Plot plot_ = getPlot(plotID_);
        int np_ = plot_.getNumPoints(setID_);
        ptolemy.plot.PlotPoint p_ = np_ > 0 ? plot_.getPoint(setID_, np_ - 1) : null;
        plot_.addPoint(setID_, x1, y1, false);
        plot_.addPoint(setID_, x2, y2, true);
        if (p_ != null) plot_.addPoint(setID_, p_.x, p_.y, false);
        plot_.fillPlot();
    }

    public boolean isPlotEnabled() {
        return plotEnabled;
    }

    public void setPlotEnabled(boolean enabled_) {
        plotEnabled = enabled_;
    }

    public boolean isOutputEnabled() {
        return outEnabled;
    }

    public void setOutputEnabled(boolean enabled_) {
        outEnabled = enabled_;
    }

    /** If "limit-redraw" is enabled, plots are redrawn according to
   * redraw probability ({@link #setRedrawProbability(double)}).
   * This is by default enabled. 
   */
    public void setLimitRedrawEnabled(boolean enabled_) {
        limitRedraw = enabled_;
    }

    /** Returns true if limited-redraw is enabled. */
    public boolean isLimitRedrawEnabled() {
        return limitRedraw;
    }

    public float getRedrawProbability() {
        return redrawProbability;
    }

    public void setRedrawProbability(float prob_) {
        redrawProbability = prob_;
    }

    public int addPlot(int plot_, String title_) {
        if (plots == null) plots = new Plot[3];
        if (plot_ < 0) {
            plot_ = plots.length;
            for (int i = 0; i < plots.length; i++) if (plots[i] == null) {
                plot_ = i;
                break;
            }
        }
        if (plots.length <= plot_) {
            Plot[] tmp_ = new Plot[Math.max(plot_ + 1, plots.length + 3)];
            System.arraycopy(plots, 0, tmp_, 0, plots.length);
            plots = tmp_;
        }
        if (plots[plot_] == null) {
            Plot tmp_ = new Plot();
            tmp_.setTitle(title_ == null ? "No Title" : title_);
            tmp_.setXLabel("- Time - ");
            DrclPlotFrame f_ = new DrclPlotFrame(this + " -- Plot " + plot_, tmp_);
            f_.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent evt_) {
                    Frame frame_ = (Frame) evt_.getSource();
                    for (int i = 0; i < plots.length; i++) if (plots[i] != null && plots[i].getParent() == frame_) {
                        firstPoint[i] = 0;
                        plots[i] = null;
                        frame_.setVisible(false);
                        break;
                    }
                }
            });
            f_.setVisible(true);
            plots[plot_] = tmp_;
        }
        return plot_;
    }

    public Plot getPlot(int plot_) {
        if (plots == null || plot_ >= plots.length || plots[plot_] == null) plot_ = addPlot(plot_, "Plot " + plot_);
        return plots[plot_];
    }

    public synchronized void removePlot(int plot_) {
        firstPoint[plot_] = 0;
        if (plots[plot_] != null) {
            java.awt.Component c_ = plots[plot_].getParent();
            while (c_ != null && !(c_ instanceof DrclPlotFrame)) c_ = c_.getParent();
            if (c_ != null) ((DrclPlotFrame) c_).dispose();
            plots[plot_] = null;
        }
    }

    public synchronized javax.swing.JFrame getPlotFrame(int plot_) {
        if (plots[plot_] != null) {
            java.awt.Component c_ = plots[plot_].getParent();
            while (c_ != null && !(c_ instanceof DrclPlotFrame)) c_ = c_.getParent();
            return (javax.swing.JFrame) c_;
        }
        return null;
    }

    public void fill(int plot_) {
        getPlot(plot_).fillPlot();
    }

    public void repaint(int plot_) {
        getPlot(plot_).repaint();
    }

    /** Returns the parent component that contains the plot panel. */
    protected java.awt.Component getParent(int plot_) {
        if (plots == null || plot_ >= plots.length) return null;
        java.awt.Component c_ = plots[plot_];
        while (c_ != null && !(c_ instanceof Frame)) c_ = c_.getParent();
        return c_;
    }

    /** Hides the plot from displaying on the screen. */
    public void hide(int plot_) {
        java.awt.Component c_ = getParent(plot_);
        if (c_ != null) c_.setVisible(false);
    }

    /** Hides all the plots from displaying on the screen. */
    public void hideAll() {
        if (plots == null) return;
        for (int i = 0; i < plots.length; i++) hide(i);
    }

    /** Re-displays the plot if it is not shown on the screen. */
    public void show(int plot_) {
        java.awt.Component c_ = getParent(plot_);
        if (c_ != null) c_.setVisible(true);
    }

    /** Re-displays all the plots if they are not shown on the screen. */
    public void showAll() {
        if (plots == null) return;
        for (int i = 0; i < plots.length; i++) show(i);
    }

    public void addLegend(int plot_, int dataset_, String legend_) {
        Plot p_ = getPlot(plot_);
        p_.addLegend(dataset_, legend_);
        p_.repaint();
    }

    public void setLegend(int plot_, int dataset_, String legend_) {
        Plot p_ = getPlot(plot_);
        p_.setLegend(dataset_, legend_);
        p_.repaint();
    }

    public void exportEPS(int plot_, String fileName_) {
        if (plots == null || plot_ >= plots.length || plots[plot_] == null) return;
        try {
            java.io.FileOutputStream file_ = new java.io.FileOutputStream(fileName_);
            plots[plot_].export(file_);
            file_.close();
        } catch (Exception e_) {
            error("exportEPS()", e_);
        }
    }

    public void setFrameTitle(int plot_, String title_) {
        Plot p_ = getPlot(plot_);
        java.awt.Component c = p_.getParent();
        while (c != null && !(c instanceof DrclPlotFrame)) c = c.getParent();
        if (c != null) {
            ((DrclPlotFrame) c).setTitle(title_);
        }
    }

    public void setTitle(int plot_, String title_) {
        Plot p_ = getPlot(plot_);
        p_.setTitle(title_);
        p_.repaint();
    }

    public void setXLabel(int plot_, String label_) {
        Plot p_ = getPlot(plot_);
        p_.setXLabel(label_);
        p_.repaint();
    }

    public void setXLog(int plot_, boolean enabled_) {
        Plot p_ = getPlot(plot_);
        p_.setXLog(enabled_);
        p_.repaint();
    }

    public void setXRange(int plot_, double min_, double max_) {
        Plot p_ = getPlot(plot_);
        p_.setXRange(min_, max_);
        p_.repaint();
    }

    public void setYLabel(int plot_, String label_) {
        Plot p_ = getPlot(plot_);
        p_.setYLabel(label_);
        p_.repaint();
    }

    public void setYLog(int plot_, boolean enabled_) {
        Plot p_ = getPlot(plot_);
        p_.setYLog(enabled_);
        p_.repaint();
    }

    public void setStepwise(int plot_, boolean enabled_) {
        Plot p_ = getPlot(plot_);
        p_.setStepwise(enabled_);
        p_.repaint();
    }

    public void setYRange(int plot_, double min_, double max_) {
        Plot p_ = getPlot(plot_);
        p_.setYRange(min_, max_);
        p_.repaint();
    }

    public void plotMLOutput(int plot_, String fname_) {
        try {
            plotMLOutput(plot_, new FileWriter(fname_));
        } catch (Exception e_) {
            e_.printStackTrace();
        }
    }

    public synchronized void plotMLOutput(int plot_, java.io.Writer writer_) {
        if (plots == null || plots.length <= plot_ || plots[plot_] == null) return;
        plots[plot_].write(writer_, null);
    }

    public synchronized void plotMLOutput(int plot_) {
        if (plots == null || plots.length <= plot_ || plots[plot_] == null) return;
        plots[plot_].write(new java.io.Writer() {

            public void write(char[] cc_, int offset_, int len_) {
                Plotter.this.write(new String(cc_, offset_, len_));
            }

            public void write(String msg_) {
                Plotter.this.write(msg_);
            }

            public void close() {
            }

            public void flush() {
            }
        }, null);
    }

    public synchronized void plotsOutput() {
        if (plots == null || plots.length == 0) return;
        try {
            for (int i = 0; i < plots.length; i++) {
                if (plots[i] == null) continue;
                Plot plot_ = plots[i];
                String title_ = plot_.getTitle();
                write("###   NEW PLOT" + SEPARATOR + i + SEPARATOR + title_ + SEPARATOR + "'" + plot_.getXLabel() + "'\n");
                int ns_ = plot_.getNumDataSets();
                for (int j = 0; j < ns_; j++) {
                    String legend_ = plot_.getLegend(j);
                    if (legend_ == null) continue;
                    write("#####  NEW SET" + SEPARATOR + i + SEPARATOR + j + SEPARATOR + legend_ + "\n");
                    int np_ = plot_.getNumPoints(j);
                    for (int k = 0; k < np_; k++) {
                        ptolemy.plot.PlotPoint p_ = plot_.getPoint(j, k);
                        double x_ = p_.x;
                        double y_ = p_.y;
                        write(i + SEPARATOR + j + SEPARATOR + x_ + SEPARATOR + y_ + SEPARATOR + legend_ + "\n");
                    }
                }
            }
        } catch (Exception e_) {
            e_.printStackTrace();
            error("plotsOutput()", e_);
        }
    }

    public void load(String fname_) {
        load(fname_, -1, 0);
    }

    public void load(String fname_, int figID_, int setID_) {
        try {
            BufferedReader bin_ = new BufferedReader(new FileReader(fname_));
            String line_;
            for (; ; ) {
                line_ = bin_.readLine();
                if (line_ == null || line_.length() > 0) break;
            }
            if (line_ == null) return;
            bin_.close();
            if (line_.startsWith("###")) {
                System.out.println("Drcl Plot Format");
                plotsLoad(new FileReader(fname_));
            } else if (line_.startsWith("<?xml version")) {
                System.out.println("PlotML");
                plotMLLoad(fname_);
            } else {
                System.out.println("Raw data");
                rawPlotLoad(new FileReader(fname_), figID_, setID_);
            }
        } catch (Exception e_) {
            e_.printStackTrace();
        }
    }

    /**
   * Creates a new plot from a <i>PlotML</i> file.
   */
    public void plotMLLoad(String fname_) {
        int plotID_ = 0;
        if (plots != null) {
            for (; plotID_ < plots.length; plotID_++) if (plots[plotID_] == null) break;
        }
        plotMLLoad(fname_, plotID_);
    }

    /**
   * Creates a new plot of the specified ID from a <i>PlotML</i> file.
   */
    public void plotMLLoad(String fname_, int plotID_) {
        try {
            File file_ = new File(fname_);
            URL base_ = new URL("file", null, file_.getAbsolutePath());
            plotMLLoad(base_, new FileInputStream(file_), plotID_);
        } catch (Exception ex_) {
            System.err.println(ex_.toString());
            ex_.printStackTrace();
        }
    }

    /**
   * Creates a new plot of the specified ID from a <i>PlotML</i> input stream.
   */
    public synchronized void plotMLLoad(URL base_, InputStream in_, int plotID_) {
        int plotid_ = addPlot(plotID_, "Plot " + plotID_);
        Plot plot_ = plots[plotid_];
        PlotBoxMLParser parser_;
        if (plot_ instanceof Plot) {
            parser_ = new PlotMLParser(plot_);
        } else {
            parser_ = new PlotBoxMLParser(plot_);
        }
        try {
            parser_.parse(base_, in_);
            plots[plotID_].repaint();
        } catch (Exception ex_) {
            System.err.println(ex_.toString());
            ex_.printStackTrace();
        }
    }

    /**
   * Creates new plots from a <i>Plots</i> reader.
   */
    public synchronized void plotsLoad(Reader in_) {
        String line_ = null;
        try {
            BufferedReader bin_ = new BufferedReader(in_);
            for (; ; ) {
                line_ = bin_.readLine();
                if (line_ != null) {
                    _plotsParseLine(line_, null);
                } else {
                    if (plots != null) for (int i = 0; i < plots.length; i++) if (plots[i] != null) plots[i].repaint();
                    bin_.close();
                    return;
                }
            }
        } catch (Exception e_) {
            e_.printStackTrace();
            error("'" + line_ + "'", "plotsLoad(Reader)", null, e_);
        }
    }

    public synchronized void rawPlotLoad(Reader in_, int figID_, int setID_) {
        String line_ = null;
        try {
            BufferedReader bin_ = new BufferedReader(in_);
            double[] xy_ = new double[2];
            figID_ = addPlot(figID_, null);
            Plot plot_ = plots[figID_];
            if (setID_ < 0) {
                setID_ = 0;
            }
            for (; ; ) {
                line_ = bin_.readLine();
                if (line_ != null) {
                    _rawPlotsParseLine(line_, xy_);
                    _plot(figID_, setID_, xy_[0], xy_[1], null, null);
                } else {
                    plot_.repaint();
                    bin_.close();
                    return;
                }
            }
        } catch (Exception e_) {
            e_.printStackTrace();
            error("'" + line_ + "'", "rawPlotLoad(Reader)", null, e_);
        }
    }

    void _plotsParseLine(String line_, double[] xy_) {
        String[] ss_ = substrings(line_);
        if (ss_[0].startsWith("#")) {
            if (ss_[0].startsWith("####")) {
                int figID_ = Integer.parseInt(ss_[1]);
                int setID_ = Integer.parseInt(ss_[2]);
                setLegend(figID_, setID_, ss_[3]);
            } else {
                int figID_ = addPlot(Integer.parseInt(ss_[1]), ss_[2]);
                Plot plot_ = plots[figID_];
                if (ss_.length > 3) {
                    String x_ = ss_[3];
                    if (x_.startsWith("'") && x_.endsWith("'")) x_ = x_.substring(1, x_.length() - 1);
                    plot_.setXLabel(x_);
                }
                if (firstPoint == null || firstPoint.length <= figID_) {
                    int[] btmp_ = new int[figID_ + 1];
                    if (firstPoint != null) System.arraycopy(firstPoint, 0, btmp_, 0, firstPoint.length);
                    firstPoint = btmp_;
                }
                firstPoint[figID_] = -1;
            }
            return;
        } else if (ss_.length >= 4) {
            int figID_ = Integer.parseInt(ss_[0]);
            int setID_ = Integer.parseInt(ss_[1]);
            double x_ = Double.valueOf(ss_[2]).doubleValue();
            double y_ = Double.valueOf(ss_[3]).doubleValue();
            Plot plot_ = getPlot(figID_);
            boolean firstPoint_ = (firstPoint[figID_] & (1 << setID_)) > 0;
            if (firstPoint_) plot_.setLegend(setID_, ss_.length > 4 ? ss_[4] : "");
            plot_.addPoint(setID_, x_, y_, !firstPoint_);
            firstPoint[figID_] &= ~(1 << setID_);
            return;
        }
        error(line_, "_plotsParseLine()", null, "unrecognized format");
    }

    void _rawPlotsParseLine(String line_, double[] xy_) {
        String[] ss_ = StringUtil.substrings(line_);
        if (ss_ == null) ; else if (ss_.length == 1) {
            xy_[0] = getTime();
            xy_[1] = Double.valueOf(ss_[0]).doubleValue();
            return;
        } else if (ss_.length == 2) {
            try {
                xy_[0] = Double.valueOf(ss_[0]).doubleValue();
                xy_[1] = Double.valueOf(ss_[1]).doubleValue();
            } catch (Exception e_) {
                e_.printStackTrace();
                drcl.Debug.error("ss_[0] = '" + ss_[0] + "', ss_[1] = '" + ss_[1] + "'\n");
            }
            return;
        }
        error(line_, "_rawPlotsParseLine()", null, "unrecognized format");
    }

    String[] substrings(String line_) {
        Vector v_ = new Vector();
        int i = 0;
        for (; ; ) {
            int j = line_.indexOf(SEPARATOR, i);
            if (j < 0) {
                String s_ = line_.substring(i);
                if (s_.length() > 0) v_.addElement(s_);
                break;
            }
            v_.addElement(line_.substring(i, j));
            i = j + 2;
        }
        String[] ss_ = new String[v_.size()];
        v_.copyInto(ss_);
        return ss_;
    }

    class PlotInfo {

        int figID, setID;

        PlotInfo(int figID_, int setID_) {
            figID = figID_;
            setID = setID_;
        }
    }

    public static void main(String[] args) {
        Plotter p_ = new Plotter("");
        boolean argument_ = true;
        int figID_ = -1, setID_ = 0;
        boolean oneFigure_ = false;
        for (int i = 0; i < args.length; i++) {
            if (argument_ && args[i].startsWith("-")) {
                if (args[i].indexOf("1") > 0) {
                    oneFigure_ = true;
                    figID_ = 0;
                    setID_ = 0;
                }
            } else {
                argument_ = false;
                p_.setRedrawProbability((float) 0.0);
                p_.load(args[i], figID_, setID_);
                if (oneFigure_) setID_++;
            }
        }
    }
}

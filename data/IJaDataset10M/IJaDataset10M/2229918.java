package statechum.analysis.learning;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.swing.SwingUtilities;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;
import statechum.Configuration;
import statechum.GlobalConfiguration;
import statechum.Helper;

public class DrawGraphs {

    /** Determines whether our callbacks are dummies (without a main loop) or active (main loop running).
	 */
    static boolean mainLoopRunning = false;

    public static class ConsoleCallbacks implements RMainLoopCallbacks {

        @SuppressWarnings("unused")
        @Override
        public void rBusy(Rengine arg0, int arg1) {
        }

        @SuppressWarnings("unused")
        @Override
        public String rChooseFile(Rengine arg0, int arg1) {
            return null;
        }

        @SuppressWarnings("unused")
        @Override
        public void rFlushConsole(Rengine arg0) {
        }

        @SuppressWarnings("unused")
        @Override
        public void rLoadHistory(Rengine arg0, String arg1) {
        }

        private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        /** Mostly from rtest.java */
        @SuppressWarnings("unused")
        @Override
        public String rReadConsole(Rengine arg0, String prompt, int arg2) {
            if (!mainLoopRunning) return null;
            System.out.print(prompt);
            try {
                String s = br.readLine();
                return (s == null || s.length() == 0) ? s : s + "\n";
            } catch (Exception e) {
                System.out.println("rReadConsole exception: " + e.getMessage());
            }
            return null;
        }

        @SuppressWarnings("unused")
        @Override
        public void rSaveHistory(Rengine arg0, String arg1) {
        }

        @SuppressWarnings("unused")
        @Override
        public void rShowMessage(Rengine arg0, String arg1) {
            if (mainLoopRunning) System.out.println(arg1);
        }

        @SuppressWarnings("unused")
        @Override
        public void rWriteConsole(Rengine arg0, String arg1, int arg2) {
            if (mainLoopRunning) System.out.println(arg1); else {
                consoleBuffer.append(arg1);
                consoleBuffer.append('\n');
            }
        }

        private StringBuffer consoleBuffer = new StringBuffer();

        public void clearBuffer() {
            consoleBuffer = new StringBuffer();
        }

        public String getBuffer() {
            return consoleBuffer.toString();
        }
    }

    /** R engine - can only be initalised once - second time packages do not load ("setwd(x) cannot change directory")
	 * because F:/R/windows64/R-2.12.2/library is passed to setwd and even if the correct value is passed, package
	 * loading fails.
	 */
    protected static Rengine engine;

    protected static ConsoleCallbacks callbacks = new ConsoleCallbacks();

    public DrawGraphs() {
        if (!Rengine.versionCheck()) throw new IllegalArgumentException("R version mismatch");
        if (engine == null) {
            engine = new Rengine(new String[] { "--vanilla" }, false, callbacks);
            if (!engine.waitForR()) throw new IllegalArgumentException("loading R failed");
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    end();
                }
            });
            eval("library(aplpack)", "loading BagPlot");
        }
    }

    protected static REXP eval(String whatToEval, String errMsg) {
        callbacks.clearBuffer();
        REXP result = engine.eval(whatToEval);
        if (result == null) throw new IllegalArgumentException(errMsg + " : " + callbacks.getBuffer());
        return result;
    }

    protected static <ELEM> String vectorToR(List<ELEM> vector, boolean addQuotes) {
        if (vector.size() == 0) throw new IllegalArgumentException("cannot plot an empty graph");
        StringBuffer result = new StringBuffer();
        result.append("c(");
        boolean startVector = true;
        for (ELEM elem : vector) {
            if (!startVector) result.append(","); else startVector = false;
            if (addQuotes) result.append('\"');
            result.append(elem);
            if (addQuotes) result.append('\"');
        }
        result.append(")");
        return result.toString();
    }

    /**
	 * Formats a box plotting command to R.
	 * 
	 * @param data what to plot
	 * @param names names of boxes
	 * @param colour colour of boxes
	 * @param otherAttrs additional attributes to set, null if not used.
	 * @return The string to be sent to R for evaluation.
	 */
    protected static String boxPlotToString(List<List<Double>> data, List<String> names, String colour, String otherAttrs) {
        if (data.size() == 0) throw new IllegalArgumentException("cannot plot an empty graph");
        if (data.size() == 1 && names != null) throw new IllegalArgumentException("in a graph with one component, names are not used");
        if (data.size() > 1 && names != null && names.size() != data.size()) throw new IllegalArgumentException("mismatch between name and data length");
        StringBuffer result = new StringBuffer();
        result.append("boxplot(");
        boolean firstVectorOfData = true;
        for (List<Double> arg : data) {
            if (!firstVectorOfData) result.append(","); else firstVectorOfData = false;
            result.append(vectorToR(arg, false));
        }
        if (names != null) {
            result.append(",names=");
            result.append(vectorToR(names, true));
        }
        {
            result.append(",col=c(");
            boolean startVector = true;
            for (int i = 0; i < data.size(); ++i) {
                if (!startVector) result.append(","); else startVector = false;
                result.append('\"');
                result.append(colour);
                result.append('\"');
            }
            result.append(")");
        }
        if (otherAttrs != null) {
            result.append(',');
            result.append(otherAttrs);
        }
        result.append(")");
        return result.toString();
    }

    /**
	 * Formats a bag plotting command to R.
	 * 
	 * @param yData what to plot vertically
	 * @param xData what to plot horizontally
	 * @param otherAttrs additional attributes to set, null if not used.
	 * @return The string to be sent to R for evaluation.
	 */
    protected static String bagPlotToString(List<List<Double>> yData, List<Double> xData, String otherAttrs) {
        if (yData.size() == 0) throw new IllegalArgumentException("cannot plot an empty graph");
        if (yData.size() != xData.size()) throw new IllegalArgumentException("mismatch between x and y length");
        StringBuffer result = new StringBuffer();
        result.append("bagplot(");
        StringBuffer yAxisData = new StringBuffer();
        Iterator<Double> xArgIterator = xData.iterator();
        result.append("c(");
        yAxisData.append(",c(");
        boolean startVector = true;
        for (List<Double> yArg : yData) {
            Double xArg = xArgIterator.next();
            for (Double elem : yArg) {
                if (!startVector) {
                    yAxisData.append(",");
                    result.append(',');
                } else startVector = false;
                yAxisData.append(elem);
                result.append(xArg);
            }
        }
        result.append(')');
        yAxisData.append(')');
        result.append(yAxisData);
        if (otherAttrs != null) {
            result.append(',');
            result.append(otherAttrs);
        }
        result.append(")");
        return result.toString();
    }

    boolean javaGDLoaded = false;

    /** Since I cannot pass arguments to RViewer during it construction, static values have to be set and 
	 * then copied into the new object. For this reason, creation of graphs has to be serialised, this is done
	 * by running it all on the Swing thread (avoids a deadlock associated with resizing of the window
	 * and us doing something to it at the same time on different threads).
	 */
    public void drawInteractivePlot(final List<String> dataToPlot, final String title) {
        if (!javaGDLoaded) {
            engine.eval(".setenv <- if (exists(\"Sys.setenv\")) Sys.setenv else Sys.putenv");
            engine.eval(".setenv(\"JAVAGD_CLASS_NAME\"=\"statechum/analysis/learning/RViewer\")");
            eval("library(JavaGD)", "loading JavaGD");
            javaGDLoaded = true;
        }
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    if (RViewer.getGraph(title) == null) {
                        RViewer.setNewGraphName(title);
                        eval("JavaGD(\"aa\")", "JavaGD() failed");
                        REXP devNum = eval("dev.cur()", "failed to do dev.cur");
                        RViewer.getGraph(title).init(devNum.asInt() - 1);
                    }
                    eval("dev.set(" + (RViewer.getGraph(title).getDeviceNumber() + 1) + ")", "failed to do dev.set for " + title);
                    for (String cmd : dataToPlot) eval(cmd, "failed to run plot " + cmd);
                }
            });
        } catch (Exception e) {
            Helper.throwUnchecked("could not draw graph " + title, e);
        }
    }

    /** Draws a plot given the data to plot and stores it in the given file.
	 * 
	 * @param drawingCommand drawing command to pass to R
	 * @param xDim horizontal size in inches, R default is 7.
	 * @param yDim vertical size in inches, R default is 7.
	 * @param fileName where to store result.
	 */
    public void drawPlot(List<String> drawingCommand, double xDim, double yDim, File file) {
        if (xDim < 1) throw new IllegalArgumentException("horizontal size (" + xDim + ") too small");
        if (yDim < 1) throw new IllegalArgumentException("vertical size (" + yDim + ") too small");
        if (file.exists() && file.delete() == false) throw new IllegalArgumentException("cannot delete file " + file.getAbsolutePath());
        String fullName = file.getAbsolutePath().replace(File.separatorChar, '/');
        eval("pdf(\"" + fullName + "\"," + xDim + "," + yDim + ")", "redirection to pdf(" + file.getAbsolutePath() + ") failed");
        for (String cmd : drawingCommand) eval(cmd, "failed to run " + cmd);
        eval("dev.off()", "failed to write to " + file.getAbsolutePath());
    }

    /**
	 * Shuts down jri thread - since jri is not a daemon thread JVM will not exit if this is not called.
	 */
    public static void end() {
        if (engine != null) {
            for (RViewer v : RViewer.nameToGraph.values()) v.gdClose();
            engine.end();
            engine = null;
        }
    }

    /**
	 * Represents a graph.
	 * 
	 * @param <ELEM> type of elements for the X axis, veritical is always a Double
	 */
    public abstract static class RGraph<ELEM extends Comparable<ELEM>> {

        Map<ELEM, List<Double>> collectionOfResults = new TreeMap<ELEM, List<Double>>();

        protected final String xAxis, yAxis;

        protected final File file;

        /** Additional drawing command to append to a plot, such as abline() command. */
        protected List<String> extraCommands = new LinkedList<String>();

        public void addExtraCommand(String cmd) {
            extraCommands.add(cmd);
        }

        public RGraph(String x, String y, File name) {
            xAxis = x;
            yAxis = y;
            file = name;
        }

        protected ELEM xMin = null, xMax = null;

        protected Double yMin = null, yMax = null;

        public void setXboundaries(ELEM min, ELEM max) {
            xMin = min;
            xMax = max;
        }

        public void setYboundaries(double min, double max) {
            yMin = min;
            yMax = max;
        }

        public void add(ELEM el, Double value) {
            if (yMin != null && yMin.doubleValue() > value.doubleValue()) return;
            if (yMax != null && yMax.doubleValue() < value.doubleValue()) return;
            if (xMin != null && xMin.compareTo(el) > 0) return;
            if (xMax != null && xMax.compareTo(el) < 0) return;
            List<Double> list = collectionOfResults.get(el);
            if (list == null) {
                list = new LinkedList<Double>();
                collectionOfResults.put(el, list);
            }
            list.add(value);
        }

        /** Returns a command to draw a graph in R. */
        abstract List<String> getDrawingCommand();

        public void drawInteractive(DrawGraphs gr) {
            List<String> drawingCommands = new LinkedList<String>();
            drawingCommands.addAll(getDrawingCommand());
            drawingCommands.addAll(extraCommands);
            gr.drawInteractivePlot(drawingCommands, file.getName());
        }

        protected double xSize = -1;

        /** Sets the horizonal size of the plot, vertical size is always set to 4 inches. 
		 * If no assignemnt is made, the size is guessed from the number of points on the drawing. 
		 */
        public void setXSize(double newSize) {
            xSize = newSize;
        }

        double ySize = 4;

        public void drawPdf(DrawGraphs gr) {
            assert collectionOfResults.size() > 0;
            double horizSize = xSize;
            if (horizSize <= 0) horizSize = computeHorizSize();
            List<String> drawingCommands = new LinkedList<String>();
            drawingCommands.addAll(getDrawingCommand());
            drawingCommands.addAll(extraCommands);
            gr.drawPlot(drawingCommands, horizSize, ySize, file);
        }

        protected abstract double computeHorizSize();
    }

    public static class RBoxPlot<ELEM extends Comparable<ELEM>> extends RGraph<ELEM> {

        public RBoxPlot(String x, String y, File name) {
            super(x, y, name);
        }

        @Override
        public List<String> getDrawingCommand() {
            List<List<Double>> data = new LinkedList<List<Double>>();
            List<String> names = new LinkedList<String>();
            for (Entry<ELEM, List<Double>> entry : collectionOfResults.entrySet()) {
                data.add(entry.getValue());
                names.add(entry.getKey().toString());
            }
            return Collections.singletonList(boxPlotToString(data, names.size() == 1 ? null : names, "green", "xlab=\"" + xAxis + "\",ylab=\"" + yAxis + "\""));
        }

        @Override
        protected double computeHorizSize() {
            double horizSize = ySize * collectionOfResults.keySet().size() / 5;
            if (horizSize < ySize) horizSize = ySize;
            return horizSize;
        }
    }

    public static class RBagPlot extends RGraph<Double> {

        public RBagPlot(String x, String y, File name) {
            super(x, y, name);
        }

        List<List<Double>> data = null;

        List<Double> names = null;

        public void computeDataSet() {
            data = new LinkedList<List<Double>>();
            names = new LinkedList<Double>();
            for (Entry<Double, List<Double>> entry : collectionOfResults.entrySet()) {
                data.add(entry.getValue());
                names.add(entry.getKey());
            }
        }

        protected Integer limit = null;

        /** By default, R's bagplot limits the number of points for analysis to 300, this one makes it possible to change that value. */
        public void setLimit(int value) {
            limit = value;
        }

        /** Returns a string reflecting the number of points R bagplot analysis will be limited to. */
        protected String formatApproxLimit() {
            return (limit == null ? "" : ", approx.limit=" + limit.intValue());
        }

        @Override
        public List<String> getDrawingCommand() {
            computeDataSet();
            return Collections.singletonList(bagPlotToString(data, names, "xlab=\"" + xAxis + "\",ylab=\"" + yAxis + "\"" + formatApproxLimit()));
        }

        public boolean graphOk() {
            Rectangle2D.Double size = getSize();
            if (size.width < Configuration.fpAccuracy) return false;
            if (size.height < Configuration.fpAccuracy) return false;
            return true;
        }

        /** Computes the data for abline to draw a diagonal. */
        public String computeDiagonal() {
            Rectangle2D.Double size = getSize();
            if (size.width < Configuration.fpAccuracy) throw new IllegalArgumentException("width is too small");
            if (size.height < Configuration.fpAccuracy) throw new IllegalArgumentException("height is too small");
            double k = size.height / size.width;
            double diff = size.y - k * size.x;
            return "abline(" + diff + "," + k + ")";
        }

        public Rectangle2D.Double getSize() {
            Double xValueMin = null, xValueMax = null, yValueMin = null, yValueMax = null;
            Iterator<Entry<Double, List<Double>>> resultIterator = collectionOfResults.entrySet().iterator();
            while (resultIterator.hasNext()) {
                Entry<Double, List<Double>> entry = resultIterator.next();
                if (xValueMin == null) {
                    xValueMin = entry.getKey();
                    xValueMax = entry.getKey();
                }
                if (xValueMin.compareTo(entry.getKey()) > 0) xValueMin = entry.getKey();
                if (xValueMax.compareTo(entry.getKey()) < 0) xValueMax = entry.getKey();
                for (Double y : entry.getValue()) {
                    if (yValueMin == null) {
                        yValueMin = y;
                        yValueMax = y;
                    }
                    if (yValueMin.compareTo(y) > 0) yValueMin = y;
                    if (yValueMax.compareTo(y) < 0) yValueMax = y;
                }
            }
            if (xValueMin == null || yValueMin == null) return new Rectangle2D.Double();
            return new Rectangle2D.Double(xValueMin, yValueMin, xValueMax - xValueMin, yValueMax - yValueMin);
        }

        @Override
        public void drawInteractive(DrawGraphs gr) {
            if (!checkSingleDot()) super.drawInteractive(gr); else if (GlobalConfiguration.getConfiguration().isAssertEnabled()) System.out.println("WARNING: not popping bagplot " + file + " consisting of a single dot due to R bug");
        }

        @Override
        protected double computeHorizSize() {
            return ySize;
        }

        public boolean checkSingleDot() {
            return getSize().width < Configuration.fpAccuracy && getSize().height < Configuration.fpAccuracy;
        }
    }

    /** Draws a square bag plot. */
    public static class SquareBagPlot extends RBagPlot {

        protected final boolean diag;

        protected final double minValue, maxValue;

        /**
		 * 
		 * @param x name of the X axis
		 * @param y name of the Y axis
		 * @param name file where to store .pdf (should include the extension).
		 * @param from the minimal value of x or y.
		 * @param to the maximal value of x or y.
		 * @param diagonal whether to draw a diagonal line
		 */
        public SquareBagPlot(String x, String y, File name, double from, double to, boolean diagonal) {
            super(x, y, name);
            diag = diagonal;
            minValue = from;
            maxValue = to;
        }

        @Override
        public List<String> getDrawingCommand() {
            computeDataSet();
            List<String> result = new LinkedList<String>();
            result.add("plot(" + minValue + ":" + maxValue + ", " + minValue + ":" + maxValue + ", type = \"n\", bty=\"n\",xlab=\"" + xAxis + "\",ylab=\"" + yAxis + "\")");
            result.add(bagPlotToString(data, names, "add=TRUE" + formatApproxLimit()));
            if (diag) result.add("abline(0,1)");
            return result;
        }
    }
}

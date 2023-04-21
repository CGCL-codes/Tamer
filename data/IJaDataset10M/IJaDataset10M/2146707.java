package edu.scripps.fl.curves.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Paint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jfree.util.ShapeUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.scripps.fl.curves.Curve;
import edu.scripps.fl.curves.FitFunction;

/**
 * 
 * @author Mark Southern (southern at scripps dot edu)
 * 
 */
public class CurvePlot {

    private static final Logger log = LoggerFactory.getLogger(CurvePlot.class);

    public static YIntervalSeries sampleFunction2DToSeries(Curve curve, FitFunction f, double start, double end, int samples, Comparable<?> seriesKey) {
        log.debug("Creating function series");
        if (f == null) throw new IllegalArgumentException("Null 'f' argument.");
        if (seriesKey == null) throw new IllegalArgumentException("Null 'seriesKey' argument.");
        if (start >= end) throw new IllegalArgumentException("Requires 'start' < 'end'.");
        if (samples < 2) throw new IllegalArgumentException("Requires 'samples' > 1");
        YIntervalSeries series = new YIntervalSeries(seriesKey);
        series.setMaximumItemCount(samples);
        double step = (end - start) / (double) (samples - 1);
        for (int i = 0; i < samples; i++) {
            double x = start + step * (double) i;
            double value = f.getResponse(curve, x);
            series.add(x, value, value, value);
        }
        log.debug("Created function series");
        return series;
    }

    private JFreeChart chart;

    private int colorIndex = 0;

    private YIntervalSeriesCollection dataset;

    private boolean displayInvalidPoints = false;

    private Font font;

    private final NumberFormat nf = new DecimalFormat("0.##E0");

    private XYPlot plot;

    private int width = 500, height = 400;

    private String xAxisLabel = "Concentration";

    private String yAxisLabel = "Response";

    public CurvePlot() {
        init();
    }

    protected void addCurve(Curve curve, YIntervalSeries validSeries, YIntervalSeries invalidSeries, FitFunction fitFunction, double min, double max) {
        MyXYErrorRenderer renderer = (MyXYErrorRenderer) plot.getRenderer();
        Paint paint = plot.getDrawingSupplier().getNextPaint();
        addSeries(validSeries, paint, true, true);
        if (isDisplayInvalidPoints() && invalidSeries.getItemCount() > 0) {
            int idx = addSeries(invalidSeries, paint, true, false);
            float size = (float) DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE[0].getBounds().getWidth();
            DrawingSupplier ds = this.getDrawingSupplier();
            if (ds instanceof CurvePlotDrawingSupplier) size = (float) ((CurvePlotDrawingSupplier) ds).getShapeSize() / 2;
            size = (float) Math.floor((size - 1) / 2);
            renderer.setSeriesShape(idx, ShapeUtilities.createDiagonalCross(size, size));
        }
        if (null != fitFunction) try {
            YIntervalSeries functionSeries = sampleFunction2DToSeries(curve, fitFunction, min, max, getWidth(), (Comparable<?>) (dataset.getSeriesCount() + 1));
            addSeries(functionSeries, paint, false, false);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public void addCurveAllPoints(Curve curve, FitFunction fitFunction) {
        addCurveAllPoints(curve, fitFunction, "");
    }

    public void addCurveAllPoints(Curve curve, FitFunction fitFunction, String description) {
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        YIntervalSeries validSeries = new YIntervalSeries(description);
        validSeries.setDescription(description);
        YIntervalSeries invalidSeries = new YIntervalSeries("");
        for (int ii = 0; ii < curve.getConcentrations().size(); ii++) {
            Double c = curve.getConcentrations().get(ii);
            Double r = curve.getResponses().get(ii);
            if (curve.getMask().get(ii)) validSeries.add(c, r, r, r); else invalidSeries.add(c, r, r, r);
            min = Math.min(min, c);
            max = Math.max(max, c);
        }
        addCurve(curve, validSeries, invalidSeries, fitFunction, min, max);
    }

    public void addCurveMeanAndStdDev(Curve curve, FitFunction fitFunction, String description) {
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        MultiValueMap validMap = new MultiValueMap();
        MultiValueMap invalidMap = new MultiValueMap();
        for (int ii = 0; ii < curve.getConcentrations().size(); ii++) {
            Double c = curve.getConcentrations().get(ii);
            Double r = curve.getResponses().get(ii);
            if (curve.getMask().get(ii)) validMap.put(c, r); else invalidMap.put(c, r);
            min = Math.min(min, c);
            max = Math.max(max, c);
        }
        addCurve(curve, getSeries(validMap, description), getSeries(invalidMap, ""), fitFunction, min, max);
    }

    public void addLineAt(double response) {
        addLineAt(response, Color.LIGHT_GRAY, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] { 2.0f, 6.0f }, 0.0f));
    }

    public void addLineAt(double response, Paint paint, BasicStroke stroke) {
        Range range = plot.getDomainAxis().getRange();
        double lower = range.getLowerBound();
        double upper = range.getUpperBound();
        YIntervalSeries series = new YIntervalSeries("");
        series.add(lower, response, response, response);
        series.add(upper, response, response, response);
        addSeries(series, paint, false, false);
        MyXYErrorRenderer renderer = (MyXYErrorRenderer) plot.getRenderer();
        int idx = dataset.getSeriesCount() - 1;
        renderer.setSeriesStroke(idx, stroke);
    }

    protected int addSeries(YIntervalSeries series, Paint paint, boolean showShapes, boolean showInLegend) {
        MyXYErrorRenderer renderer = (MyXYErrorRenderer) plot.getRenderer();
        int idx;
        dataset.addSeries(series);
        idx = dataset.getSeriesCount() - 1;
        renderer.setSeriesLinesVisible(idx, !showShapes);
        renderer.setSeriesShapesVisible(idx, showShapes);
        renderer.setSeriesYError(idx, showShapes);
        renderer.setSeriesXError(idx, false);
        renderer.setSeriesPaint(idx, paint);
        renderer.setSeriesVisibleInLegend(idx, showInLegend);
        return idx;
    }

    public DrawingSupplier getDrawingSupplier() {
        return plot.getDrawingSupplier();
    }

    public int getHeight() {
        return height;
    }

    protected YIntervalSeries getSeries(Map<Double, Collection<Double>> map, String description) {
        YIntervalSeries series = new YIntervalSeries(description);
        series.setDescription(description);
        for (Object o : map.keySet()) {
            SummaryStatistics stats = new SummaryStatistics();
            Collection<Double> values = (Collection<Double>) map.get(o);
            for (Double d : values) stats.addValue(d);
            double avg = stats.getMean();
            double stddev = stats.getStandardDeviation();
            series.add((Double) o, avg, avg - stddev, avg + stddev);
        }
        return series;
    }

    public int getWidth() {
        return width;
    }

    public void init() {
        dataset = new YIntervalSeriesCollection();
        LogAxis xAxis = new LogAxis(xAxisLabel) {

            @Override
            public NumberFormat getNumberFormatOverride() {
                return nf;
            }

            protected String createTickLabel(double value) {
                return getNumberFormatOverride().format(value);
            }
        };
        xAxis.setTickUnit(new NumberTickUnit(1.0, nf));
        xAxis.setTickMarksVisible(true);
        NumberAxis yAxis = new NumberAxis(yAxisLabel);
        yAxis.setTickUnit(new NumberTickUnit(25));
        plot = new XYPlot(dataset, xAxis, yAxis, null);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.setRangeMinorGridlinesVisible(true);
        MyXYErrorRenderer renderer = new MyXYErrorRenderer();
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.setBackgroundPaint(Color.WHITE);
        plot.setDrawingSupplier(new CurvePlotDrawingSupplier());
    }

    public boolean isDisplayInvalidPoints() {
        return displayInvalidPoints;
    }

    public void setDisplayInvalidPoints(boolean displayInvalidPoints) {
        this.displayInvalidPoints = displayInvalidPoints;
    }

    public void setDrawingSupplier(DrawingSupplier drawingSupplier) {
        plot.setDrawingSupplier(drawingSupplier);
    }

    public void setFont(Font font) {
        this.font = font;
        plot.getDomainAxis().setTickLabelFont(font);
        plot.getRangeAxis().setTickLabelFont(font);
    }

    public void setFontSize(int size) {
        Font newFont = null;
        if (this.font == null) {
            newFont = new Font("SansSerif", Font.PLAIN, size);
        } else {
            newFont = new Font(font.getName(), font.getStyle(), size);
        }
        setFont(newFont);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setResponseRange(int min, int max) {
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setRange(min, max);
        plot.configureRangeAxes();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void write(OutputStream outputStream) throws IOException {
        ChartUtilities.writeChartAsPNG(outputStream, chart, getWidth(), getHeight());
        outputStream.close();
    }

    public byte[] writeBytes() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsPNG(os, chart, getWidth(), getHeight());
        os.close();
        return os.toByteArray();
    }

    public Image writeImage() {
        return chart.createBufferedImage(getWidth(), getHeight());
    }
}

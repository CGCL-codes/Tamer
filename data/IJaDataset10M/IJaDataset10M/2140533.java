package net.sf.profiler4j.console;

import static javax.swing.BorderFactory.createCompoundBorder;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class MemoryPlotPanel extends JPanel {

    private TimeSeries totalSeries;

    private TimeSeries usedSeries;

    public MemoryPlotPanel(String title) {
        this(5 * 60 * 1000, title);
    }

    public MemoryPlotPanel(int maxAge, String title) {
        super(new BorderLayout());
        totalSeries = new TimeSeries("Committed Memory", Millisecond.class);
        totalSeries.setMaximumItemAge(maxAge);
        usedSeries = new TimeSeries("Used Memory", Millisecond.class);
        usedSeries.setMaximumItemAge(maxAge);
        TimeSeriesCollection seriesCollection = new TimeSeriesCollection();
        seriesCollection.addSeries(totalSeries);
        seriesCollection.addSeries(usedSeries);
        NumberAxis numberAxis = new NumberAxis("Memory (KB)");
        numberAxis.setLabelFont(new Font("SansSerif", 0, 14));
        numberAxis.setTickLabelFont(new Font("SansSerif", 0, 12));
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        DateAxis dateAxis = new DateAxis("Time");
        dateAxis.setTickLabelFont(new Font("SansSerif", 0, 12));
        dateAxis.setLabelFont(new Font("SansSerif", 0, 14));
        dateAxis.setAutoRange(true);
        dateAxis.setLowerMargin(0);
        dateAxis.setUpperMargin(0);
        dateAxis.setTickLabelsVisible(true);
        dateAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
        XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);
        lineRenderer.setSeriesPaint(0, Color.RED);
        lineRenderer.setSeriesPaint(1, Color.GREEN.darker());
        lineRenderer.setStroke(new BasicStroke(2F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        XYPlot xyplot = new XYPlot(seriesCollection, dateAxis, numberAxis, lineRenderer);
        xyplot.setBackgroundPaint(Color.WHITE);
        xyplot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        xyplot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        JFreeChart chart = new JFreeChart(title, new Font("SansSerif", Font.PLAIN, 14), xyplot, true);
        chart.setBackgroundPaint(Color.white);
        ChartPanel panel = new ChartPanel(chart);
        panel.setBorder(createCompoundBorder(createEmptyBorder(8, 8, 8, 8), createLineBorder(Color.LIGHT_GRAY)));
        add(panel);
        setBorder(createEmptyBorder(8, 8, 8, 8));
    }

    public void addSample(Millisecond m, double totalMem, double usedMem) {
        totalSeries.add(m, totalMem / 1024);
        usedSeries.add(m, usedMem / 1024);
    }

    public void reset() {
        totalSeries.clear();
        usedSeries.clear();
    }
}

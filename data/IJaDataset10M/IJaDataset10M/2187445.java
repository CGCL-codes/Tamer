package testes.jfreechart;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo scatter plot with some code showing how to convert between Java2D coordinates and
 * (x, y) coordinates.
 */
public class ScatterPlotDemo3 extends ApplicationFrame implements ChartMouseListener {

    /** The panel used to display the chart. */
    private ChartPanel chartPanel;

    /**
     * A demonstration application showing a scatter plot.
     *
     * @param title  the frame title.
     */
    public ScatterPlotDemo3(final String title) {
        super(title);
        final XYDataset dataset = new SampleXYDataset2();
        JFreeChart chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.addChartMouseListener(this);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        chartPanel.setVerticalAxisTrace(true);
        chartPanel.setHorizontalAxisTrace(true);
        setContentPane(chartPanel);
    }

    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A dataset.
     */
    private JFreeChart createChart(XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createScatterPlot("Scatter Plot Demo", "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, false);
        final NumberAxis domainAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
        return chart;
    }

    /**
     * Callback method for receiving notification of a mouse click on a chart.
     *
     * @param event  information about the event.
     */
    public void chartMouseClicked(ChartMouseEvent event) {
        int x = event.getTrigger().getX();
        int y = event.getTrigger().getY();
        Point2D p = chartPanel.translateScreenToJava2D(new Point(x, y));
        XYPlot plot = chartPanel.getChart().getXYPlot();
        Rectangle2D dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();
        double xx = plot.getDomainAxis().java2DToValue(p.getX(), dataArea, plot.getDomainAxisEdge());
        double yy = plot.getRangeAxis().java2DToValue(p.getY(), dataArea, plot.getRangeAxisEdge());
        double xxx = plot.getDomainAxis().valueToJava2D(xx, dataArea, plot.getDomainAxisEdge());
        double yyy = plot.getRangeAxis().valueToJava2D(yy, dataArea, plot.getRangeAxisEdge());
        Point2D p2 = chartPanel.translateJava2DToScreen(new Point2D.Double(xxx, yyy));
        System.out.println("Mouse coordinates are (" + x + ", " + y + "), in data space = (" + xx + ", " + yy + ").");
        System.out.println("--> (" + p2.getX() + ", " + p2.getY() + ")");
    }

    /**
     * Callback method for receiving notification of a mouse movement on a chart.
     *
     * @param event  information about the event.
     */
    public void chartMouseMoved(ChartMouseEvent event) {
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {
        final ScatterPlotDemo3 demo = new ScatterPlotDemo3("Scatter Plot Demo 3");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}

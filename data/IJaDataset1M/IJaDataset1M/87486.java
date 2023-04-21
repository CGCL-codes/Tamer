package testes.jfreechart;

import java.awt.Color;
import java.awt.GradientPaint;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.MatrixSeriesCollection;
import org.jfree.data.xy.NormalizedMatrixSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo that shows how matrix series can be used for charts that follow a
 * constantly changing grid input.
 *
 * @author Barak Naveh
 *
 * @since Jun 25, 2003
 */
public class BubblyBubblesDemo extends ApplicationFrame {

    /** The default size. */
    private static final int SIZE = 10;

    /** The default title. */
    private static final String TITLE = "Population count at grid locations";

    /**
     * The normalized matrix series is used here to represent a changing
     * population on a grid.
     */
    NormalizedMatrixSeries series;

    /**
     * A demonstration application showing a bubble chart using matrix series.
     *
     * @param title the frame title.
     */
    public BubblyBubblesDemo(final String title) {
        super(title);
        this.series = createInitialSeries();
        final MatrixSeriesCollection dataset = new MatrixSeriesCollection(this.series);
        final JFreeChart chart = ChartFactory.createBubbleChart(TITLE, "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(new GradientPaint(0, 0, Color.white, 0, 1000, Color.blue));
        final XYPlot plot = chart.getXYPlot();
        plot.setForegroundAlpha(0.5f);
        final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setLowerBound(-0.5);
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLowerBound(-0.5);
        final ChartPanel chartPanel = new ChartPanel(chart);
        setContentPane(chartPanel);
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     */
    public static void main(final String[] args) {
        final BubblyBubblesDemo demo = new BubblyBubblesDemo(TITLE);
        demo.pack();
        demo.setSize(800, 600);
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
        final Thread updater = demo.new UpdaterThread();
        updater.setDaemon(true);
        updater.start();
    }

    /**
     * Creates a series.
     * 
     * @return The series.
     */
    private NormalizedMatrixSeries createInitialSeries() {
        final NormalizedMatrixSeries newSeries = new NormalizedMatrixSeries("Sample Grid 1", SIZE, SIZE);
        for (int count = 0; count < SIZE; count++) {
            final int i = (int) (Math.random() * SIZE);
            final int j = (int) (Math.random() * SIZE);
            final int mij = (int) (Math.random() * SIZE);
            newSeries.update(i, j, mij);
        }
        newSeries.setScaleFactor(newSeries.getItemCount());
        return newSeries;
    }

    /**
     * A thread for updating the dataset.
     */
    private class UpdaterThread extends Thread {

        /**
         * @see java.lang.Runnable#run()
         */
        public void run() {
            setPriority(MIN_PRIORITY);
            while (true) {
                final int i = (int) (Math.random() * SIZE);
                final int j = (int) (Math.random() * SIZE);
                series.update(i, j, series.get(i, j) + 1);
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}

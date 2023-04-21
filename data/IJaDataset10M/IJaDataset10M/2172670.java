package testes.jfreechart;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * An example of....
 *
 */
public class TimePeriodValuesDemo2 extends ApplicationFrame {

    /**
     * A demonstration application showing how to....
     *
     * @param title  the frame title.
     */
    public TimePeriodValuesDemo2(final String title) {
        super(title);
        final XYDataset data1 = createDataset();
        final XYItemRenderer renderer1 = new XYBarRenderer();
        final DateAxis domainAxis = new DateAxis("Date");
        final ValueAxis rangeAxis = new NumberAxis("Value");
        final XYPlot plot = new XYPlot(data1, domainAxis, rangeAxis, renderer1);
        final JFreeChart chart = new JFreeChart("Time Period Values Demo", plot);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        chartPanel.setMouseZoomable(true, false);
        setContentPane(chartPanel);
    }

    /**
     * Creates a dataset, consisting of two series of monthly data.
     *
     * @return the dataset.
     */
    public XYDataset createDataset() {
        final TimePeriodValues s1 = new TimePeriodValues("Series 1");
        final Day d1 = new Day();
        final Day d2 = (Day) d1.next();
        final Day d3 = (Day) d2.next();
        final Day d4 = (Day) d3.next();
        final Day d5 = (Day) d4.next();
        final Day d6 = (Day) d5.next();
        final Day d7 = (Day) d6.next();
        s1.add(new SimpleTimePeriod(d6.getStart(), d6.getEnd()), 74.95);
        s1.add(new SimpleTimePeriod(d1.getStart(), d2.getEnd()), 55.75);
        s1.add(new SimpleTimePeriod(d7.getStart(), d7.getEnd()), 90.45);
        s1.add(new SimpleTimePeriod(d3.getStart(), d5.getEnd()), 105.75);
        final TimePeriodValuesCollection dataset = new TimePeriodValuesCollection();
        dataset.addSeries(s1);
        dataset.setDomainIsPointsInTime(false);
        return dataset;
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {
        final TimePeriodValuesDemo2 demo = new TimePeriodValuesDemo2("Time Period Values Demo 2");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}

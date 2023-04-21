package org.skzr.chart4me.demo.time;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.data.time.Day;

/**
 * @jdk 1.6
 * @author skzr.org(E-mail:skzr.org@gmail.com)
 * @version 1.0.0
 */
public class TimePeriodValuesDemo2 extends ApplicationFrame {

    private static final long serialVersionUID = 1L;

    public TimePeriodValuesDemo2(String title) {
        super(title);
        XYDataset dataset = createDataset();
        XYBarRenderer renderer = new XYBarRenderer();
        DateAxis dateAxis = new DateAxis("时间");
        NumberAxis valueAxis = new NumberAxis("值");
        XYPlot plot = new XYPlot(dataset, dateAxis, valueAxis, renderer);
        JFreeChart chart = new JFreeChart("时间段demo", plot);
        ChartUtilities.applyCurrentTheme(chart);
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseZoomable(true);
        panel.setMouseWheelEnabled(true);
        setContentPane(panel);
    }

    private XYDataset createDataset() {
        TimePeriodValues values = new TimePeriodValues("Series 1");
        Day day = new Day(), day1, day2, day3, day4, day5, day6;
        day1 = (Day) day.next();
        day2 = (Day) day1.next();
        day3 = (Day) day2.next();
        day4 = (Day) day3.next();
        day5 = (Day) day4.next();
        day6 = (Day) day5.next();
        values.add(new SimpleTimePeriod(day5.getStart(), day5.getEnd()), 74.950000000000003D);
        values.add(new SimpleTimePeriod(day.getStart(), day1.getEnd()), 55.75D);
        values.add(new SimpleTimePeriod(day6.getStart(), day6.getEnd()), 90.450000000000003D);
        values.add(new SimpleTimePeriod(day2.getStart(), day4.getEnd()), 105.75D);
        TimePeriodValuesCollection dataset = new TimePeriodValuesCollection(values);
        return dataset;
    }

    public static void main(String[] args) {
        TimePeriodValuesDemo2 demo = new TimePeriodValuesDemo2("时间段 Demo2");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}

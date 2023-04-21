package org.bcholmes.jmicro.cif.report.renderer;

import static org.bcholmes.jmicro.cif.report.renderer.ReportLabel.LEGEND_AMOUNT;
import static org.bcholmes.jmicro.cif.report.renderer.ReportLabel.LEGEND_COUNT;
import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import org.bcholmes.jmicro.cif.report.service.LoanReport;
import org.bcholmes.jmicro.cif.report.service.LoanReport.Record;
import org.bcholmes.jmicro.util.swing.StyleSheet;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class LoanReportRenderer {

    private StyleSheet styleSheet = new StyleSheet();

    public JFreeChart render(LoanReport report) {
        TimeSeries countSeries = new TimeSeries(LEGEND_COUNT, Month.class);
        TimeSeries amountSeries = new TimeSeries(LEGEND_AMOUNT, Month.class);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeriesCollection amountDataset = new TimeSeriesCollection();
        for (Map.Entry<Date, Record> entry : report.getAmounts().entrySet()) {
            Month month = toMonth(entry.getKey());
            countSeries.add(month, entry.getValue().getNumberOfLoans());
            amountSeries.add(month, entry.getValue().getTotalAmount());
        }
        dataset.addSeries(countSeries);
        dataset.addSeries(new TimeSeries(LEGEND_AMOUNT, Month.class));
        amountDataset.addSeries(new TimeSeries(LEGEND_COUNT, Month.class));
        amountDataset.addSeries(amountSeries);
        JFreeChart chart = createChart(dataset, amountDataset);
        return chart;
    }

    @SuppressWarnings("serial")
    private JFreeChart createChart(XYDataset dataset1, XYDataset dataset2) {
        ValueAxis domainAxis = new DateAxis(ReportLabel.MONTH.getDescription());
        NumberAxis rangeAxis = new NumberAxis(ReportLabel.NUMBER_OF_LOANS.getDescription());
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        XYPlot plot = new XYPlot(dataset1, domainAxis, rangeAxis, renderer1) {

            public LegendItemCollection getLegendItems() {
                LegendItemCollection result = new LegendItemCollection();
                if (getDataset() != null) {
                    XYItemRenderer r = getRenderer();
                    if (r != null) {
                        LegendItem item = r.getLegendItem(0, 0);
                        result.add(item);
                    }
                }
                if (getDataset(1) != null) {
                    XYItemRenderer renderer2 = getRenderer(1);
                    if (renderer2 != null) {
                        LegendItem item = renderer2.getLegendItem(1, 1);
                        result.add(item);
                    }
                }
                return result;
            }
        };
        JFreeChart chart = new JFreeChart(ReportLabel.LOAN_ACTIVITY.getDescription(), plot);
        chart.setBackgroundPaint(Color.white);
        plot.setBackgroundPaint(this.styleSheet.getBackgroundColor());
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.setRangeAxis(1, new NumberAxis(ReportLabel.AMOUNT.getDescription()));
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer();
        renderer1.setSeriesPaint(0, this.styleSheet.getThemeColor(0));
        renderer2.setSeriesPaint(1, new Color(61, 38, 138));
        plot.setRenderer(1, renderer2);
        return chart;
    }

    private Month toMonth(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        Month month = toMonth(calendar);
        return month;
    }

    private Month toMonth(GregorianCalendar calendar) {
        return new Month(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }
}

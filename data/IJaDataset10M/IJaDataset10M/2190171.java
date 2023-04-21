package com.jStockAnalizer;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;
import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import com.jStockAnalizer.model.Stock;
import com.jStockAnalizer.model.StockPrice;
import com.jStockAnalizer.service.StockService;

public class JIFViewStockChart extends JInternalFrame {

    private Stock stock;

    private JLabel jlRange;

    private JRadioButton jrb3Months;

    private JRadioButton jrb6Months;

    private JRadioButton jrb1Year;

    private JRadioButton jrb2Year;

    private JRadioButton jrb5Year;

    private ButtonGroup bgRange;

    public JIFViewStockChart(Stock stock) {
        super(stock.getSymbol() + " Chart", true, true, true, true);
        this.stock = stock;
        this.init();
        this.pack();
    }

    private void init() {
        jlRange = new JLabel("Range:");
        jrb3Months = new JRadioButton("3 Months");
        jrb3Months.setSelected(true);
        jrb6Months = new JRadioButton("6 Months");
        jrb1Year = new JRadioButton("1 Year");
        jrb2Year = new JRadioButton("2 Years");
        jrb5Year = new JRadioButton("5 Years");
        bgRange = new ButtonGroup();
        bgRange.add(jrb3Months);
        bgRange.add(jrb6Months);
        bgRange.add(jrb1Year);
        bgRange.add(jrb2Year);
        bgRange.add(jrb5Year);
        TimeSeries s1 = new TimeSeries(this.stock.getSymbol(), Day.class);
        TimeSeries s2 = new TimeSeries(this.stock.getSymbol(), Day.class);
        Collection<StockPrice> prices = StockService.getInstance().getStockHistoryData(stock, 3);
        for (StockPrice stockPrice : prices) {
            s1.addOrUpdate(new Day(stockPrice.getDate()), stockPrice.getClose());
            s2.addOrUpdate(new Day(stockPrice.getDate()), stockPrice.getVolume());
        }
        TimeSeriesCollection dataset1 = new TimeSeriesCollection();
        dataset1.addSeries(s1);
        TimeSeriesCollection dataset2 = new TimeSeriesCollection();
        dataset2.addSeries(s2);
        XYItemRenderer renderer1 = new StandardXYItemRenderer();
        NumberAxis rangeAxis1 = new NumberAxis("Price");
        rangeAxis1.setAutoRangeIncludesZero(false);
        XYPlot subplot1 = new XYPlot(dataset1, null, rangeAxis1, renderer1);
        XYItemRenderer renderer2 = new XYBarRenderer();
        NumberAxis rangeAxis2 = new NumberAxis("Volume");
        rangeAxis2.setAutoRangeIncludesZero(false);
        XYPlot subplot2 = new XYPlot(dataset2, null, rangeAxis2, renderer2);
        DateAxis dateAxis = new DateAxis("Date");
        dateAxis.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());
        CombinedDomainXYPlot plot = new CombinedDomainXYPlot(dateAxis);
        plot.add(subplot1, 2);
        plot.add(subplot2, 1);
        JFreeChart chart = new JFreeChart(this.stock.getSymbol(), new Font("SansSerif", Font.BOLD, 12), plot, true);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseZoomable(true, false);
        this.setLayout(new GridBagLayout());
        this.add(jlRange, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
        this.add(jrb3Months, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
        this.add(jrb6Months, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
        this.add(jrb1Year, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
        this.add(jrb2Year, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
        this.add(jrb5Year, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
        this.add(chartPanel, new GridBagConstraints(0, 1, 6, 1, 1.0, 1.0, GridBagConstraints.LINE_END, GridBagConstraints.BOTH, new Insets(10, 10, 0, 10), 0, 0));
    }
}

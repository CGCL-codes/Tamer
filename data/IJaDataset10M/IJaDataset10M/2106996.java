package org.yccheok.jstock.gui.charting;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JDialog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.yccheok.jstock.internationalization.GUIBundle;

/**
 *
 * @author yccheok
 */
public class DynamicChart {

    /** Creates new form DynamicChart */
    public DynamicChart() {
        this.price = new TimeSeries("Price");
        this.price.setMaximumItemAge(2 * 60 * 60);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(this.price);
        JFreeChart freeChart = ChartFactory.createTimeSeriesChart(null, null, null, dataset, false, true, false);
        freeChart.setAntiAlias(true);
        while (freeChart.getSubtitleCount() > 0) {
            freeChart.removeSubtitle(freeChart.getSubtitle(0));
        }
        XYPlot plot = freeChart.getXYPlot();
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        XYItemRenderer renderer1 = plot.getRenderer();
        renderer1.setBaseToolTipGenerator(new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT, new SimpleDateFormat("h:mm:ss a"), new DecimalFormat("0.00#")));
        org.yccheok.jstock.charting.Utils.applyChartTheme(freeChart);
        chartPanel = new ChartPanel(freeChart, true, true, true, false, true);
        chartPanel.setMouseZoomable(false);
    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    public void showNewJDialog(java.awt.Frame parent, String title) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(this.price);
        JFreeChart freeChart = ChartFactory.createTimeSeriesChart(title, GUIBundle.getString("DynamicChart_Date"), GUIBundle.getString("DynamicChart_Price"), dataset, true, true, false);
        freeChart.setAntiAlias(true);
        XYPlot plot = freeChart.getXYPlot();
        NumberAxis rangeAxis1 = (NumberAxis) plot.getRangeAxis();
        DecimalFormat format = new DecimalFormat("00.00");
        rangeAxis1.setNumberFormatOverride(format);
        XYItemRenderer renderer1 = plot.getRenderer();
        renderer1.setBaseToolTipGenerator(new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT, new SimpleDateFormat("h:mm:ss a"), new DecimalFormat("0.00#")));
        org.yccheok.jstock.charting.Utils.applyChartTheme(freeChart);
        ChartPanel _chartPanel = new ChartPanel(freeChart, true, true, true, true, true);
        JDialog dialog = new JDialog(parent, title, false);
        dialog.getContentPane().add(_chartPanel, java.awt.BorderLayout.CENTER);
        dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        final java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setBounds((screenSize.width - 750) >> 1, (screenSize.height - 600) >> 1, 750, 600);
        dialog.setVisible(true);
    }

    public void addPriceObservation(Date date, double y) {
        final int count = this.price.getItemCount();
        if (count > 0) {
            final Date nextStartDate = this.price.getTimePeriod(count - 1).getStart();
            if ((date.getTime() - nextStartDate.getTime()) < 10000) {
                return;
            }
        }
        Second second = new Second(date);
        try {
            this.price.add(second, y);
        } catch (SeriesException exp) {
            log.error(null, exp);
        }
    }

    private final TimeSeries price;

    private final ChartPanel chartPanel;

    private static final Log log = LogFactory.getLog(DynamicChart.class);
}

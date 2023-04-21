package com.bugfree4j.chart.action;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Insets;
import java.awt.geom.Ellipse2D;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.http.HttpSession;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.chart.urls.TimeSeriesURLGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class WebHitChart {

    public static String generateBarChart(Date hitDate, HttpSession session, PrintWriter pw) {
        String filename = null;
        try {
            WebHitDataSet whDataSet = new WebHitDataSet();
            ArrayList list = whDataSet.getDataBySection(hitDate);
            if (list.size() == 0) {
                System.out.println("No data has been found");
                throw new NoDataException();
            }
            Iterator iter = list.listIterator();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            while (iter.hasNext()) {
                WebHit wh = (WebHit) iter.next();
                dataset.addValue(new Long(wh.getHitCount()), "Hits", wh.getSection());
            }
            CategoryAxis categoryAxis = new CategoryAxis("");
            ValueAxis valueAxis = new NumberAxis("");
            BarRenderer renderer = new BarRenderer();
            renderer.setItemURLGenerator(new StandardCategoryURLGenerator("xy_chart.jsp", "series", "section"));
            renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
            Plot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
            JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
            chart.setBackgroundPaint(java.awt.Color.white);
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, 500, 300, info, session);
            pw.flush();
        } catch (NoDataException e) {
            System.out.println(e.toString());
            filename = "public_nodata_500x300.png";
        } catch (Exception e) {
            System.out.println("Exception - " + e.toString());
            e.printStackTrace(System.out);
            filename = "public_error_500x300.png";
        }
        return filename;
    }

    public static String generatePieChart(Date hitDate, HttpSession session, PrintWriter pw) {
        String filename = null;
        return filename;
    }

    public static String generateXYChart(String section, HttpSession session, PrintWriter pw) {
        String filename = null;
        try {
            WebHitDataSet whDataSet = new WebHitDataSet();
            ArrayList list = whDataSet.getDataByHitDate(section);
            if (list.size() == 0) {
                System.out.println("No data has been found");
                throw new NoDataException();
            }
            XYSeries dataSeries = new XYSeries("Hits");
            Iterator iter = list.listIterator();
            while (iter.hasNext()) {
                WebHit wh = (WebHit) iter.next();
                dataSeries.add(wh.getHitDate().getTime(), wh.getHitCount());
            }
            XYSeriesCollection xyDataset = new XYSeriesCollection(dataSeries);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
            StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT, sdf, NumberFormat.getInstance());
            TimeSeriesURLGenerator urlg = new TimeSeriesURLGenerator(sdf, "pie_chart.jsp", "series", "hitDate");
            ValueAxis timeAxis = new DateAxis("");
            NumberAxis valueAxis = new NumberAxis("");
            valueAxis.setAutoRangeIncludesZero(false);
            StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.LINES + StandardXYItemRenderer.SHAPES, ttg, urlg);
            renderer.setShapesFilled(true);
            XYPlot plot = new XYPlot(xyDataset, timeAxis, valueAxis, renderer);
            JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
            chart.setBackgroundPaint(java.awt.Color.white);
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, 500, 300, info, session);
            pw.flush();
        } catch (NoDataException e) {
            System.out.println(e.toString());
            filename = "public_nodata_500x300.png";
        } catch (Exception e) {
            System.out.println("Exception - " + e.toString());
            e.printStackTrace(System.out);
            filename = "public_error_500x300.png";
        }
        return filename;
    }

    public static String generateXYAreaChart(HttpSession session, PrintWriter pw) {
        String filename = null;
        try {
            WebHitDataSet whDataSet = new WebHitDataSet();
            ArrayList sections = whDataSet.getSections();
            Iterator sectionIter = sections.iterator();
            DefaultTableXYDataset dataset = new DefaultTableXYDataset();
            while (sectionIter.hasNext()) {
                String section = (String) sectionIter.next();
                ArrayList list = whDataSet.getDataByHitDate(section);
                XYSeries dataSeries = new XYSeries(section, true, false);
                Iterator webHitIter = list.iterator();
                while (webHitIter.hasNext()) {
                    WebHit webHit = (WebHit) webHitIter.next();
                    dataSeries.add(webHit.getHitDate().getTime(), webHit.getHitCount());
                }
                dataset.addSeries(dataSeries);
            }
            if (dataset.getItemCount() == 0) {
                System.out.println("No data has been found");
                throw new NoDataException();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
            StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator(StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT, sdf, NumberFormat.getInstance());
            TimeSeriesURLGenerator urlg = new TimeSeriesURLGenerator(sdf, "bar_chart.jsp", "series", "hitDate");
            DateAxis xAxis = new DateAxis(null);
            xAxis.setLowerMargin(0.0);
            xAxis.setUpperMargin(0.0);
            NumberAxis yAxis = new NumberAxis(null);
            yAxis.setAutoRangeIncludesZero(true);
            StackedXYAreaRenderer renderer = new StackedXYAreaRenderer(XYAreaRenderer.AREA_AND_SHAPES, ttg, urlg);
            renderer.setSeriesPaint(0, new Color(255, 255, 180));
            renderer.setSeriesPaint(1, new Color(206, 230, 255));
            renderer.setSeriesPaint(2, new Color(255, 230, 230));
            renderer.setSeriesPaint(3, new Color(206, 255, 206));
            renderer.setShapePaint(Color.gray);
            renderer.setShapeStroke(new BasicStroke(0.5f));
            renderer.setShape(new Ellipse2D.Double(-3, -3, 6, 6));
            renderer.setOutline(true);
            XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
            plot.setForegroundAlpha(0.65f);
            yAxis.configure();
            JFreeChart chart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
            chart.setBackgroundPaint(java.awt.Color.white);
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, 500, 300, info, session);
            pw.flush();
        } catch (NoDataException e) {
            System.out.println(e.toString());
            filename = "public_nodata_500x300.png";
        } catch (Exception e) {
            System.out.println("Exception - " + e.toString());
            e.printStackTrace(System.out);
            filename = "public_error_500x300.png";
        }
        return filename;
    }

    public static void main(java.lang.String[] args) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
            PrintWriter pw = new PrintWriter(System.out);
            String filename = WebHitChart.generateBarChart(sdf.parse("01-Aug-2002"), null, pw);
            System.out.println("filename - " + filename);
        } catch (Exception e) {
            System.out.println("Exception - " + e.toString());
            e.printStackTrace();
        }
        return;
    }
}

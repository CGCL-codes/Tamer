package com.umc.helper;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.Rotation;
import com.lowagie.text.Font;
import com.umc.UMCStatistics;

public class Statistic {

    public Statistic() {
        super();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - MM/dd/yyyy");
        String date = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.getDefault()).format(new Date());
        UMCStatistics stats = UMCStatistics.getInstance();
        DefaultPieDataset processedObjects = new DefaultPieDataset();
        JFreeChart jfreechart = ChartFactory.createPieChart3D("", processedObjects, true, true, false);
        TextTitle title = new TextTitle();
        title.setText("Processed Objects [" + date + "]");
        title.setFont(new java.awt.Font("HELVETICA", Font.BOLD, 12));
        jfreechart.setTitle(title);
        PiePlot3D pieplot3d = (PiePlot3D) jfreechart.getPlot();
        pieplot3d.setDarkerSides(true);
        pieplot3d.setStartAngle(290D);
        pieplot3d.setDirection(Rotation.CLOCKWISE);
        pieplot3d.setForegroundAlpha(0.5F);
        pieplot3d.setNoDataMessage("No data to display");
        DefaultPieDataset errorObjects = new DefaultPieDataset();
        errorObjects.setValue("General", new Double(stats.getMovieProcessingErrors()));
        errorObjects.setValue("Refused", new Double(stats.getMoviesRefused()));
        errorObjects.setValue("XML", new Double(stats.getMovieProcessingXMLWarnings()));
        errorObjects.setValue("Pattern", new Double(stats.getMoviesRefusedByIgnorePattern()));
        JFreeChart jfreechart2 = ChartFactory.createPieChart3D("", errorObjects, true, true, false);
        TextTitle title2 = new TextTitle();
        title2.setText("Errors [" + date + "]");
        title2.setFont(new java.awt.Font("HELVETICA", Font.BOLD, 12));
        jfreechart2.setTitle(title2);
        PiePlot3D pieplot3d2 = (PiePlot3D) jfreechart2.getPlot();
        pieplot3d2.setNoDataMessage("No errors");
        pieplot3d2.setDarkerSides(true);
        pieplot3d2.setStartAngle(290D);
        pieplot3d2.setDirection(Rotation.CLOCKWISE);
        pieplot3d2.setForegroundAlpha(0.5F);
        XYSeries xySeries = new XYSeries("");
        double d = 0;
        long sum = 0;
        for (Long l : stats.getJobsProcessingTimes()) {
            xySeries.add(d++, new Double(l / 1000));
            sum += l;
        }
        double avg = 0.0;
        if (stats.getJobsProcessingTimes().size() > 0) avg = (sum / stats.getJobsProcessingTimes().size()) / 1000D;
        XYSeriesCollection xyseriescollection = new XYSeriesCollection(xySeries);
        XYSplineRenderer xysplinerenderer = new XYSplineRenderer();
        xysplinerenderer.setSeriesShapesVisible(0, false);
        NumberAxis xAchse = new NumberAxis();
        xAchse.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYPlot xyplot = new XYPlot(xyseriescollection, xAchse, new NumberAxis(), xysplinerenderer);
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);
        xyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
        ValueMarker marker = new ValueMarker(avg);
        marker.setPaint(Color.BLUE);
        ;
        xyplot.addRangeMarker(marker);
        JFreeChart backgroundWorkerChart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, xyplot, true);
        TextTitle bgtitle = new TextTitle();
        bgtitle.setFont(new java.awt.Font("HELVETICA", Font.BOLD, 12));
        bgtitle.setText("Backgroundworker t/M [" + date + "]");
        backgroundWorkerChart.setTitle(bgtitle);
        try {
            ChartUtilities.saveChartAsPNG(new File(System.getProperty("user.dir") + UMCConstants.fileSeparator + "resources" + UMCConstants.fileSeparator + "Gui" + UMCConstants.fileSeparator + "stat1.png"), jfreechart, 730, 350);
            ChartUtilities.saveChartAsPNG(new File(System.getProperty("user.dir") + UMCConstants.fileSeparator + "resources" + UMCConstants.fileSeparator + "Gui" + UMCConstants.fileSeparator + "stat2.png"), jfreechart2, 730, 350);
            ChartUtilities.saveChartAsPNG(new File(System.getProperty("user.dir") + UMCConstants.fileSeparator + "resources" + UMCConstants.fileSeparator + "Gui" + UMCConstants.fileSeparator + "stat3.png"), backgroundWorkerChart, 730, 350);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

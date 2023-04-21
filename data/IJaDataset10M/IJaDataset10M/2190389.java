/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package myappthatusesjfreechart;

import java.awt.Color;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author Melerek
 */
public class ChartCreator {

    public void DrawPieChart(String title, ArrayList<PieDatasetInput> arrayList)
    {
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        for(PieDatasetInput data : arrayList)
            pieDataset.setValue(data.nazwa, data.wartoœæ);
        //pieDataset.setValue("JavaWorld", new Integer(75));
        //pieDataset.setValue("Other", new Integer(25));

        JFreeChart chart = ChartFactory.createPieChart(title, pieDataset, true, true, true);

        ChartFrame frame = new ChartFrame("Wykres", chart);
        frame.pack();
        frame.setVisible(true);
    }

    public void DrawBarChart3D(String title, String xAxisLabel, String yAxisLabel, ArrayList<BarChart3DInput> arrayList)
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(BarChart3DInput data : arrayList)
            dataset.setValue(data.wartosc, data.group, data.nazwa);
        //dataset.setValue(6, "Profit1", "Jane");


        JFreeChart chart = ChartFactory.createBarChart3D(title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL,
        true, true, false);

        ChartFrame frame = new ChartFrame("Wykres", chart);
        frame.pack();
        frame.setVisible(true);
    }

    public void DrawTimeSeriesChart(String title, String xAxisLabel, String yAxisLabel, ArrayList<TimeSeriesChartInput> arrayList)
    {
        DefaultXYDataset dataset = new DefaultXYDataset();

        for(TimeSeriesChartInput data : arrayList)
            dataset.addSeries(data.nazwa, data.points);

        //dupa = new double[2][3];
        //dupa[0][0] = 3.0;
        //dupa[1][0] = 48.0;
        //dupa[0][1] = 18.5;
        //dupa[1][1] = 6.5888;
        //dataset.addSeries("Prosta1", dupa);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(title, xAxisLabel, yAxisLabel, dataset, true,  true, true );                   // generate URLs?);

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        ChartFrame frame = new ChartFrame("Wykres", chart);
        frame.pack();
        frame.setVisible(true);
    }
}




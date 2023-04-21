package game.report.visualizations;

import game.models.ConnectableModel;
import game.models.GradientTrainable;
import game.trainers.Trainer;
import java.io.File;
import java.awt.*;
import java.util.Vector;
import java.util.ArrayList;
import fakegame.Charts;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Class produces png image file from a training errror graph
 */
public class TrainingErrorGraphImage {

    public static JFreeChart generateTrainingErrorChart(Trainer t) {
        if (t.getBest() == null) {
            return null;
        }
        NumberAxis xAxis = new NumberAxis("Iteration");
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        NumberAxis yAxis = new NumberAxis("Mean Squared Error");
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYPlot plot = new XYPlot(createTrainingErrorDataSet(t.getErrorHistory()), xAxis, yAxis, new StandardXYItemRenderer());
        JFreeChart chart = new JFreeChart("Error plot", plot);
        return chart;
    }

    private static XYDataset createTrainingErrorDataSet(Vector<double[]> trainingErrorHistory) {
        XYSeries seriesT = new XYSeries("Learning Error", false, false);
        XYSeries seriesV = new XYSeries("Validation Error", false, false);
        int index = 1;
        for (double[] th : trainingErrorHistory) {
            seriesT.add(new XYDataItem(index, th[0]));
            if (th[1] >= 0) seriesV.add(new XYDataItem(index, th[1]));
            index++;
        }
        XYSeriesCollection col = new XYSeriesCollection(seriesT);
        if (seriesV.getItemCount() > 0) col.addSeries(seriesV);
        return col;
    }
}

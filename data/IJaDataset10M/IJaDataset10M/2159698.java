package be.abeel.util;

import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYBarDataset;
import be.abeel.graphics.GraphicsFileExport;
import be.abeel.jfreechart.JFreeChartWrapper;

/**
 * Utility methods for frequency maps.
 * 
 * @author Thomas Abeel
 */
public class CountMapTools {

    public static void plot(CountMap<? extends Comparable<?>> freq, String file) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Comparable<?> i : freq.keySet()) {
            dataset.addValue(freq.get(i), "count", i);
        }
        final JFreeChart chart = ChartFactory.createBarChart("CountMap bar chart", "Category", "Count", dataset, PlotOrientation.VERTICAL, true, false, false);
        GraphicsFileExport.exportPNG(new JFreeChartWrapper(chart), file, 800, 600);
    }

    public static void plot(String title, List<FrequencyMap> list, String file, boolean countNormalization, int lower, int upper, String[] labels) {
        int count = 0;
        DefaultXYDataset set = new DefaultXYDataset();
        for (FrequencyMap freq : list) {
            double[][] data = new double[2][freq.keySet().size()];
            int index = 0;
            for (int i : freq.keySet()) {
                data[0][index] = i;
                data[1][index] = freq.get(i);
                index++;
            }
            if (countNormalization) {
                int totalCount = freq.totalCount();
                for (int i = 0; i < data[1].length; i++) data[1][i] /= totalCount;
            }
            set.addSeries(labels != null ? labels[count++] : "freq" + count++, data);
        }
        JFreeChart chart = ChartFactory.createXYBarChart(title, "bin", false, "count", new XYBarDataset(set, 1.0), PlotOrientation.VERTICAL, false, false, false);
        if (countNormalization) {
            chart.getXYPlot().getRangeAxis().setRange(0, 1);
        }
        chart.getXYPlot().getDomainAxis().setRange(lower, upper);
        GraphicsFileExport.exportPNG(new JFreeChartWrapper(chart), file, 1024, 768);
    }
}

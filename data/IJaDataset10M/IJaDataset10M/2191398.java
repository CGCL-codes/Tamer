package gui.pieChart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class PieChartFactory {

    static PieChartFactory factory;

    private PieChartFactory() {
    }

    public static PieChartFactory getFactory() {
        if (factory == null) factory = new PieChartFactory();
        return factory;
    }

    public ChartPanel getPieChart() {
        final PieDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset, "Infos");
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(270, 150));
        return chartPanel;
    }

    public ChartPanel getPieChart(Object[][] objects, String title) {
        JFreeChart chart = createChart(createDataset(objects), title);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(270, 150));
        chartPanel.setMinimumSize(new java.awt.Dimension(270, 150));
        return chartPanel;
    }

    public ChartPanel getPieChartNull() {
        JFreeChart chart = createChart(new DefaultPieDataset(), "");
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(270, 150));
        chartPanel.setMinimumSize(new java.awt.Dimension(270, 150));
        return chartPanel;
    }

    public ChartPanel getPieChart(Object[][] objects, String title, int width, int height) {
        JFreeChart chart = createChart(createDataset(objects), title);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(width, height));
        return chartPanel;
    }

    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private PieDataset createDataset() {
        final DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("One", Math.random() * 100);
        dataset.setValue("Two", Math.random() * 100);
        dataset.setValue("Three", Math.random() * 100);
        dataset.setValue("Four", Math.random() * 100);
        dataset.setValue("Five", Math.random() * 100);
        dataset.setValue("Six", Math.random() * 100);
        return dataset;
    }

    /**
     * Creates a dataset.
     * 
     * @return a dataset.
     */
    private PieDataset createDataset(Object[][] objects) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int i = 0; i < objects[0].length; i++) {
            Number numb = (Number) objects[1][i];
            if (numb.doubleValue() > 0) {
                dataset.setValue(objects[0][i].toString(), numb);
            }
        }
        return dataset;
    }

    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(PieDataset dataset, String title) {
        final JFreeChart chart = ChartFactory.createPieChart(title, dataset, false, true, false);
        return chart;
    }
}

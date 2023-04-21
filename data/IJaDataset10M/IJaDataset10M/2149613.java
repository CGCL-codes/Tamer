package fr.n7.sma.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.RangeType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ResourceChart extends JPanel implements Runnable {

    private static final long serialVersionUID = -3635415156262221343L;

    private ConcurrentHashMap<Integer, Integer> values = new ConcurrentHashMap<Integer, Integer>();

    private int totalDeplNb = 0;

    private int totalNewResourceNb = 0;

    private XYSeries deplSeries;

    private XYSeries meanSeries;

    private XYItemRenderer renderer;

    String name;

    public Dimension getSize() {
        int w = super.getSize().width;
        return new Dimension(w, (int) (w / 2.5));
    }

    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), (int) (getWidth() / 2.5));
    }

    public void run() {
        for (int deplNumber : values.keySet()) {
            deplSeries.add((double) deplNumber, values.get(deplNumber) * deplNumber, false);
        }
        deplSeries.fireSeriesChanged();
        meanSeries.setNotify(true);
        meanSeries.add((double) totalDeplNb, totalDeplNb / (double) totalNewResourceNb);
        meanSeries.setNotify(false);
    }

    public void addNewValue(int deplNumberFromLast) {
        totalDeplNb = totalDeplNb + deplNumberFromLast;
        totalNewResourceNb++;
        synchronized (values) {
            if (values.containsKey(deplNumberFromLast)) {
                values.put(deplNumberFromLast, values.get(deplNumberFromLast) + 1);
            } else {
                values.put(deplNumberFromLast, 1);
            }
            if (totalDeplNb % 100 == 0) meanSeries.add((double) totalDeplNb, totalDeplNb / (double) totalNewResourceNb);
        }
    }

    public void setColor(Color color) {
        renderer.setSeriesPaint(0, new GradientPaint(0, 0, color, 0, 100, Color.white));
    }

    public ResourceChart(String name) {
        setBorder(BorderFactory.createTitledBorder(name));
        deplSeries = new XYSeries(name, false);
        XYSeriesCollection dataSet = new XYSeriesCollection();
        dataSet.addSeries(deplSeries);
        renderer = new XYBarRenderer();
        setColor(Color.blue);
        NumberAxis valueAxis;
        NumberAxis domainAxis;
        XYSeriesCollection meanDataSet = new XYSeriesCollection();
        meanSeries = new XYSeries(name + " (mean)");
        meanSeries.setNotify(false);
        meanDataSet.addSeries(meanSeries);
        valueAxis = new NumberAxis("Moyenne du Nb de dépl nécessaires entre 2 \"" + name + "\".");
        valueAxis.setRangeType(RangeType.POSITIVE);
        domainAxis = new NumberAxis("Nombre total de déplacements");
        domainAxis.setRangeType(RangeType.POSITIVE);
        domainAxis.setAutoRangeIncludesZero(true);
        XYPlot meanplot = new XYPlot(meanDataSet, domainAxis, valueAxis, new XYLineAndShapeRenderer(true, false));
        valueAxis = new NumberAxis("Nb d'occurences x Nb de pas");
        valueAxis.setRangeType(RangeType.POSITIVE);
        domainAxis = new NumberAxis("Nb de pas entre 2 \"" + name + "\".");
        domainAxis.setAutoRangeIncludesZero(true);
        XYPlot plot2 = new XYPlot(dataSet, domainAxis, valueAxis, renderer);
        JFreeChart chart = new JFreeChart(plot2);
        JFreeChart meanChart = new JFreeChart(meanplot);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setRefreshBuffer(true);
        ChartPanel meanChartPanel = new ChartPanel(meanChart);
        JSplitPane tab = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, chartPanel, meanChartPanel);
        tab.setOneTouchExpandable(true);
        tab.setContinuousLayout(true);
        tab.setResizeWeight(.5);
        setLayout(new GridLayout(1, 1));
        add(tab);
        tab.setDividerLocation(0.5);
        dataSet.addChangeListener(chart.getPlot());
    }
}

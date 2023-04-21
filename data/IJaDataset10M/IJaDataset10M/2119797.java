package view.charts;

import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import model.vyborki.VyborkaXY;
import org.jfree.chart.*;
import org.jfree.data.statistics.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class HistogramChart extends ApplicationFrame {

    private JFreeChart chart;

    private int width = 500;

    private int height = 300;

    public HistogramChart(String title, int intervalCount, ArrayList<Double> values) {
        super(title);
        int number = intervalCount;
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        double[] value = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            value[i] = values.get(i);
        }
        dataset.addSeries(title, value, number);
        String plotTitle = "����������� ��������� ������������� ����������� f(x)";
        String xaxis = "x";
        String yaxis = "f(x)";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = false;
        boolean urls = false;
        chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    public static Object[] getYFromVyborki(Object[] vyborki) {
        VyborkaXY v;
        ArrayList al = new ArrayList();
        for (int k = 0; k < vyborki.length; k++) {
            v = (VyborkaXY) vyborki[k];
            al.add(v.getY());
        }
        return al.toArray();
    }

    public void windowClosing(WindowEvent e) {
        this.dispose();
    }

    public void saveChart() {
        try {
            ChartUtilities.saveChartAsPNG(new File("histogram.PNG"), chart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showChart(int intervalCount, ArrayList<Double> values) {
        HistogramChart demo = new HistogramChart("�����������", intervalCount, values);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}

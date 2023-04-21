package statistic.model;

import game.data.GameData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import statistic.StatisticsWorker;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

public class AverageMean implements StatisticsWorker, TreeSelectionListener, KeyListener {

    private final String name = "Histogram";

    private JPanel jPanel1;

    private JTree jTree1;

    private GameData treeData;

    private ChartPanel chartPanel;

    private ChartPanel chartPanel2;

    private int last_key;

    public Class getConfigClass() {
        return null;
    }

    /**
     * @return Name of visualization method
     */
    public String getName() {
        return name;
    }

    /**
     * Creates JPanel interface
     */
    public JPanel run(GameData treeData) {
        this.treeData = treeData;
        jPanel1 = new JPanel();
        GroupLayout layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(layout);
        JScrollPane jScrollPane;
        {
            jTree1 = new JTree(this.getListNames(treeData));
            jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            jScrollPane = new JScrollPane(jTree1);
            jScrollPane.setMinimumSize(new Dimension(100, 50));
            jPanel1.add(jScrollPane);
            jTree1.addTreeSelectionListener(this);
            jTree1.addKeyListener(this);
        }
        {
            chartPanel = new ChartPanel(null);
            jPanel1.add(chartPanel);
            chartPanel2 = new ChartPanel(null);
            jPanel1.add(chartPanel2);
        }
        layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(jScrollPane).addGroup(layout.createParallelGroup().addComponent(chartPanel).addComponent(chartPanel2)));
        layout.setVerticalGroup(layout.createParallelGroup().addComponent(jScrollPane).addGroup(layout.createSequentialGroup().addComponent(chartPanel).addComponent(chartPanel2)));
        return jPanel1;
    }

    private void compute(TreePath[] names) {
        assert names.length <= 1;
        if (!((DefaultMutableTreeNode) names[0].getLastPathComponent()).isLeaf()) {
            switch(last_key) {
                case KeyEvent.VK_DOWN:
                    jTree1.setSelectionPath(new TreePath(((DefaultMutableTreeNode) names[0].getLastPathComponent()).getFirstChild()));
                    jTree1.revalidate();
                    compute(jTree1.getSelectionPaths());
                    break;
                default:
                    jTree1.setSelectionPath(null);
            }
            return;
        }
        last_key = 0;
        HistogramDataset dataset = new HistogramDataset();
        String nodeText = (String) ((DefaultMutableTreeNode) names[0].getLastPathComponent()).getUserObject();
        double[][] dataCopyByOutput = new double[treeData.getONumber()][treeData.getInstanceNumber()];
        int[] dataCopyByOutputLength = new int[treeData.getONumber()];
        double[] dataCopy = new double[treeData.getInstanceNumber()];
        boolean found = false;
        for (int j = 0; j < treeData.getINumber(); j++) {
            if (("Input " + j).compareTo(nodeText) == 0) {
                for (int i = 0; i < treeData.getInstanceNumber(); i++) {
                    int max = 0;
                    for (int o = 0; o < treeData.getONumber(); o++) {
                        if (treeData.getOutputAttrs()[i][o] > treeData.getOutputAttrs()[i][max]) {
                            max = o;
                        }
                    }
                    dataCopyByOutput[max][dataCopyByOutputLength[max]++] = treeData.getInputVectors()[i][j];
                    dataCopy[i] = treeData.getInputVectors()[i][j];
                }
                for (int q = 0; q < treeData.getONumber(); q++) {
                    dataset.addSeries("Output " + q, Arrays.copyOf(dataCopyByOutput[q], dataCopyByOutputLength[q]), 50);
                }
                found = true;
                break;
            }
        }
        if (!found) {
            for (int j = 0; j < treeData.getONumber(); j++) {
                if (("Output " + j).compareTo(nodeText) == 0) {
                    for (int i = 0; i < treeData.getInstanceNumber(); i++) {
                        dataCopy[i] = treeData.getInputVectors()[i][j];
                    }
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(null, "Unable to find selected value\nUnable to proceed", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Arrays.sort(dataCopy);
        JFreeChart chart = ChartFactory.createHistogram("Histogram", "", "", dataset, PlotOrientation.VERTICAL, true, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        BoxAndWhiskerRenderer BWrenderer = new BoxAndWhiskerRenderer();
        DefaultBoxAndWhiskerCategoryDataset BWdataset = new DefaultBoxAndWhiskerCategoryDataset();
        ArrayList<Double> l = new ArrayList<Double>(dataCopy.length);
        for (double aDataCopy : dataCopy) {
            l.add(aDataCopy);
        }
        BWdataset.add(l, "a", "     ");
        final CategoryAxis xAxis = new CategoryAxis("");
        final NumberAxis yAxis = (NumberAxis) plot.getDomainAxis();
        yAxis.setAutoRangeIncludesZero(false);
        BWrenderer.setFillBox(false);
        BWrenderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        final CategoryPlot BWplot = new CategoryPlot(BWdataset, xAxis, yAxis, BWrenderer);
        BWplot.setOrientation(PlotOrientation.HORIZONTAL);
        final JFreeChart BWchart = new JFreeChart("", new Font("SansSerif", Font.BOLD, 14), BWplot, false);
        chartPanel.setChart(chart);
        chartPanel2.setChart(BWchart);
        chartPanel2.setPreferredSize(new Dimension(Integer.MAX_VALUE, 150));
        chartPanel2.setMaximumSize(new Dimension(1000, 150));
    }

    private DefaultMutableTreeNode getListNames(GameData treeData) {
        DefaultMutableTreeNode result = new DefaultMutableTreeNode();
        DefaultMutableTreeNode category = new DefaultMutableTreeNode("Inputs");
        result.add(category);
        for (int i = 0; i < treeData.getINumber(); i++) {
            category.add(new DefaultMutableTreeNode("Input " + i));
        }
        category = new DefaultMutableTreeNode("Outputs");
        result.add(category);
        for (int i = 0; i < treeData.getONumber(); i++) {
            category.add(new DefaultMutableTreeNode("Output " + i));
        }
        return result;
    }

    public void valueChanged(TreeSelectionEvent e) {
        TreePath[] sel = jTree1.getSelectionPaths();
        if (sel == null) {
            return;
        }
        this.compute(sel);
        jPanel1.revalidate();
    }

    public void keyPressed(KeyEvent arg0) {
    }

    public void keyReleased(KeyEvent arg0) {
        last_key = arg0.getKeyCode();
    }

    public void keyTyped(KeyEvent arg0) {
    }
}

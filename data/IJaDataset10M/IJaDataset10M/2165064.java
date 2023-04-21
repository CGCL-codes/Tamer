package preprocessing.automatic.Reporting;

import game.utils.Exceptions.InvalidArgument;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import preprocessing.LoggerManager;
import preprocessing.automatic.GUI.JetColorMap;
import preprocessing.automatic.ResultManager.ResultManagerNode;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cepekm1
 * Date: 04-Jun-2010
 * Time: 11:05:34
 * To change this template use File | Settings | File Templates.
 */
public class MethodUtilizationBestIndividualsReporter implements FinalReporter {

    /**
     * A list of datasets (one dataset for one attribute - one graph)
     */
    private ArrayList<DefaultBoxAndWhiskerCategoryDataset> bawMethUtilDatasets;

    private ArrayList<HashMap<String, ArrayList<Double>>> completeUtilizData;

    public MethodUtilizationBestIndividualsReporter() {
        reset();
    }

    @Override
    public void reset() {
        bawMethUtilDatasets = new ArrayList<DefaultBoxAndWhiskerCategoryDataset>();
    }

    private void gatherData(ResultManagerNode<Object> rootNode) throws InvalidArgument {
        Integer numRuns;
        int numAttributes;
        numRuns = (Integer) rootNode.getValue("numRuns");
        if (numRuns == null) {
            throw new InvalidArgument("The root node does not appear to be root node of several runs of AP algorithm. Root node path " + rootNode.getPathFromRoot());
        }
        ArrayList<ResultManagerNode<Object>> aprunNodes = new ArrayList<ResultManagerNode<Object>>(numRuns);
        for (int run = 0; run < numRuns; run++) {
            String runLabel = "APRun" + String.format("%05d", run);
            aprunNodes.add(rootNode.getChild(runLabel));
        }
        Integer totalGenerations = (Integer) aprunNodes.get(0).getValue("lastGeneration");
        ResultManagerNode node = aprunNodes.get(0).getChild("Generation00001");
        if (node == null) {
            throw new InvalidArgument("The node (index 0) does not appear to be root node of AP algorithm (Child \"Generation00000\" is missing). Root node path " + rootNode.getPathFromRoot());
        }
        ArrayList<HashMap<String, Double>> tmp = (ArrayList<HashMap<String, Double>>) node.getValue("overallUtilization");
        numAttributes = tmp.size();
        for (int i = 0; i < numAttributes; i++) {
            bawMethUtilDatasets.add(new DefaultBoxAndWhiskerCategoryDataset());
        }
        completeUtilizData = new ArrayList<HashMap<String, ArrayList<Double>>>(numAttributes);
        for (int attributeIndex = 0; attributeIndex < numAttributes; attributeIndex++) {
            DefaultBoxAndWhiskerCategoryDataset attributeDataset = bawMethUtilDatasets.get(attributeIndex);
            for (int gen = 1; gen <= totalGenerations; gen++) {
                String genLabel = "Generation" + String.format("%05d", gen);
                HashMap<String, ArrayList<Double>> runUtilMap = new HashMap<String, ArrayList<Double>>();
                for (int run = 0; run < numRuns; run++) {
                    node = aprunNodes.get(run).getChild(genLabel);
                    HashMap<String, Double> attributeUtiliz = ((ArrayList<HashMap<String, Double>>) node.getValue("bestUtilization")).get(attributeIndex);
                    Iterator<Map.Entry<String, Double>> iter = attributeUtiliz.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Double> entry = iter.next();
                        ArrayList<Double> al = runUtilMap.get(entry.getKey());
                        if (al == null) {
                            al = new ArrayList<Double>();
                            al.add((Double) entry.getValue().doubleValue());
                            runUtilMap.put(entry.getKey(), al);
                        } else {
                            al.add((Double) entry.getValue().doubleValue());
                        }
                    }
                }
                Iterator<Map.Entry<String, ArrayList<Double>>> iter = runUtilMap.entrySet().iterator();
                completeUtilizData.add(runUtilMap);
                while (iter.hasNext()) {
                    Map.Entry<String, ArrayList<Double>> entry = iter.next();
                    attributeDataset.add(entry.getValue(), entry.getKey(), gen);
                }
            }
        }
    }

    private ArrayList<JFreeChart> plotCharts() {
        Iterator<DefaultBoxAndWhiskerCategoryDataset> iter = bawMethUtilDatasets.iterator();
        ArrayList<JFreeChart> charts = new ArrayList<JFreeChart>(bawMethUtilDatasets.size());
        int numDataset = bawMethUtilDatasets.get(0).getRowCount();
        Color[] colors = JetColorMap.getJetColorMap(numDataset);
        while (iter.hasNext()) {
            CategoryPlot plot = new CategoryPlot();
            DefaultBoxAndWhiskerCategoryDataset dataset = iter.next();
            for (int rows = 0; rows < numDataset; rows++) {
                DefaultBoxAndWhiskerCategoryDataset baw = new DefaultBoxAndWhiskerCategoryDataset();
                DefaultCategoryDataset catDat = new DefaultCategoryDataset();
                for (int cols = 0; cols < dataset.getColumnCount(); cols++) {
                    BoxAndWhiskerItem bawItem = dataset.getItem(rows, cols);
                    baw.add(bawItem, 0, dataset.getColumnKey(cols));
                    catDat.addValue(bawItem.getMean(), 0, dataset.getColumnKey(cols));
                }
                BoxAndWhiskerRenderer bawRenderer = new BoxAndWhiskerRenderer();
                LineAndShapeRenderer lasRenderer = new LineAndShapeRenderer(true, true);
                bawRenderer.setItemMargin(0.5);
                bawRenderer.setFillBox(false);
                bawRenderer.setSeriesPaint(0, colors[rows]);
                lasRenderer.setSeriesPaint(0, colors[rows]);
                plot.setDataset(rows * 2, baw);
                plot.setRenderer(rows * 2, bawRenderer);
                plot.setDataset(rows * 2 + 1, catDat);
                plot.setRenderer(rows * 2 + 1, lasRenderer);
            }
            CategoryAxis xAxis = new CategoryAxis("Generations");
            NumberAxis yAxis = new NumberAxis("Fitness Value");
            plot.setDomainAxis(xAxis);
            plot.setRangeAxis(yAxis);
            JFreeChart chart = new JFreeChart(plot);
            chart.removeLegend();
            charts.add(chart);
        }
        return charts;
    }

    /**
     * Creates new panel with chart that can be placed into a form.
     *
     * @param rootNode
     * @return A panel with chart.
     */
    @Override
    public JPanel generateFinalPanel(ResultManagerNode<Object> rootNode) {
        JPanel panel = new JPanel(new FlowLayout());
        try {
            gatherData(rootNode);
            ArrayList<JFreeChart> arrCharts = plotCharts();
            JTabbedPane tabbed = new JTabbedPane(arrCharts.size());
            for (int idx = 0; idx < arrCharts.size(); idx++) {
                JPanel tmpPan = new JPanel(new FlowLayout());
                tmpPan.add(new ChartPanel(arrCharts.get(idx), true));
                tabbed.add(String.format("Attribute %d", idx), tmpPan);
            }
            panel.add(tabbed);
        } catch (InvalidArgument e) {
            panel.add(new JLabel(e.getMessage()));
            e.printStackTrace();
            LoggerManager.getInstance().getLogger(LoggerManager.PreprocessingLoggerString).error("ResultManager failed during creating final plot." + e.getMessage());
        }
        return panel;
    }

    @Override
    public ArrayList<JFreeChart> generateFinalChart(ResultManagerNode<Object> rootNode) {
        try {
            gatherData(rootNode);
        } catch (InvalidArgument e) {
            e.printStackTrace();
            LoggerManager.getInstance().getLogger(LoggerManager.PreprocessingLoggerString).error("ResultManager failed during creating final plot." + e.getMessage());
            return null;
        }
        return plotCharts();
    }

    @Override
    public String generateTextData(ResultManagerNode<Object> rootNode) {
        try {
            gatherData(rootNode);
        } catch (InvalidArgument invalidArgument) {
            invalidArgument.printStackTrace();
            return invalidArgument.getMessage();
        }
        String text = "Method name ; Minimal regular value ; 0.25 percentile ; Mean value ; 0.75 percentile ; Max regular value ; Results for each run...\n";
        for (int attrIdx = 0; attrIdx < bawMethUtilDatasets.size(); attrIdx++) {
            DefaultBoxAndWhiskerCategoryDataset bawCat = bawMethUtilDatasets.get(attrIdx);
            HashMap<String, ArrayList<Double>> complUtilMap = completeUtilizData.get(attrIdx);
            for (int row = 0; row < bawCat.getRowCount(); row++) {
                String rowStr = bawCat.getRowKey(row).toString();
                text = text + rowStr + "; ";
                BoxAndWhiskerItem bawItem = bawCat.getItem(row, bawCat.getColumnCount() - 1);
                String tmp = String.format("%e ; %e ; %e ; %e ; %e ; ", (Double) bawItem.getMinRegularValue(), (Double) bawItem.getQ1(), (Double) bawItem.getMean(), (Double) bawItem.getQ3(), (Double) bawItem.getMaxRegularValue());
                Iterator<Double> iter = complUtilMap.get(rowStr).iterator();
                while (iter.hasNext()) {
                    tmp = tmp + String.format("%e ; ", iter.next());
                }
                text = text + tmp;
                text = text + "\n";
            }
            text = text + "\n";
        }
        return text;
    }

    @Override
    public String getReporterBaseName() {
        return "best-utilization";
    }

    @Override
    public boolean isReuseGraph() {
        return false;
    }

    @Override
    public void setReuseGraph(boolean reuseGraph) {
    }
}

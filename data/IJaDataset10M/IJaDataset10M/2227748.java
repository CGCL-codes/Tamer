package preprocessing.statistic.model;

import game.data.Instance;
import game.data.OutputAttribute;
import game.data.TreeData;
import game.models.Model;
import game.models.Models;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import preprocessing.statistic.StatisticsWorker;
import preprocessing.statistic.core.EvaluationFacade;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

public class FalsePositiveNegativeDistribution implements StatisticsWorker, TreeSelectionListener, ChangeListener {

    private final String name = "Distribution of false negative and positive";

    private JRadioButton jRadioButton2;

    private JRadioButton jRadioButton1;

    private TreeData treeData;

    private JPanel jPanel1;

    private JTree jTree1;

    private ChartPanel chartPanel;

    private String axis;

    public Class getConfigClass() {
        return null;
    }

    private DefaultMutableTreeNode getListNames(TreeData treeData) {
        int modelNo = Models.getInstance().getModelsNumber();
        DefaultMutableTreeNode result = new DefaultMutableTreeNode();
        for (int j = 0; j < treeData.getONumber(); j++) {
            String on = ((OutputAttribute) treeData.oAttr.elementAt(j)).getName();
            DefaultMutableTreeNode category = new DefaultMutableTreeNode(on);
            result.add(category);
            int p = 0;
            for (int i = 0; i < Models.getInstance().getModelsNumber(); i++) {
                if (Models.getInstance().getModel(i) != null) if (Models.getInstance().getModel(i).getName().compareTo(on) == 0) {
                    String name = Models.getInstance().getModel(i).getName();
                    if (modelNo > treeData.getONumber()) {
                        name = name + " " + p;
                    }
                    category.add(new DefaultMutableTreeNode(name));
                    p++;
                }
            }
        }
        return result;
    }

    /**
	 * @return Name of visualization method
	 */
    public String getName() {
        return name;
    }

    /**
	 * Create JPanel
	 */
    public JPanel run(TreeData treeData) {
        this.treeData = treeData;
        jPanel1 = new JPanel();
        jPanel1.setLayout(new BorderLayout());
        {
            jTree1 = new JTree(this.getListNames(treeData));
            jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            jPanel1.add(new JScrollPane(jTree1), BorderLayout.WEST);
            jTree1.addTreeSelectionListener(this);
        }
        {
            JPanel jPanel2 = new JPanel();
            jPanel2.setLayout(new BorderLayout());
            jPanel1.add(jPanel2, BorderLayout.CENTER);
            {
                JPanel bottom = new JPanel();
                bottom.setLayout(new GridBagLayout());
                jPanel2.add(bottom, BorderLayout.SOUTH);
                chartPanel = new ChartPanel(null);
                jPanel2.add(chartPanel, BorderLayout.CENTER);
                ButtonGroup buttonGroup1 = new ButtonGroup();
                {
                    jRadioButton1 = new JRadioButton();
                    bottom.add(jRadioButton1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                    jRadioButton1.setText("False negative");
                    buttonGroup1.add(jRadioButton1);
                    jRadioButton1.setSelected(true);
                    jRadioButton1.addChangeListener(this);
                    axis = jRadioButton1.getText();
                }
                {
                    jRadioButton2 = new JRadioButton();
                    jRadioButton2.setLayout(null);
                    bottom.add(jRadioButton2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                    jRadioButton2.setText("False positive");
                    jRadioButton2.addChangeListener(this);
                    buttonGroup1.add(jRadioButton2);
                }
            }
        }
        return jPanel1;
    }

    private void compute(TreePath[] selectedNames) {
        class struct implements Comparator<struct> {

            public double response;

            public double expected;

            public int compare(struct arg0, struct arg1) {
                return Double.compare(arg0.response, arg1.response);
            }
        }
        EvaluationFacade evaluation = EvaluationFacade.getInstance();
        XYSeriesCollection xysc = new XYSeriesCollection();
        XYSeries series;
        for (int ii = 0; ii < selectedNames.length; ii++) {
            if (!((DefaultMutableTreeNode) selectedNames[ii].getLastPathComponent()).isLeaf()) {
                TreePath[] copy = new TreePath[selectedNames.length - 1];
                for (int i = 0; i < copy.length; i++) {
                    if (i < ii) {
                        copy[i] = selectedNames[i];
                    } else {
                        copy[i] = selectedNames[i + 1];
                    }
                }
                jTree1.setSelectionPaths(copy);
                compute(copy);
                return;
            }
        }
        for (TreePath selectedName : selectedNames) {
            String nodeText = (String) ((DefaultMutableTreeNode) selectedName.getLastPathComponent()).getUserObject();
            int modelNumber = Models.getInstance().findNet(nodeText);
            if (jRadioButton1.isSelected()) {
                series = evaluation.falseNegativeGraphData(treeData, modelNumber, nodeText);
            } else {
                series = evaluation.falsePositiveGraphData(treeData, modelNumber, nodeText);
            }
            xysc.addSeries(series);
        }
        JFreeChart jfc = ChartFactory.createXYStepChart("", "treshold", axis, xysc, PlotOrientation.VERTICAL, true, false, false);
        XYPlot plot = (XYPlot) jfc.getPlot();
        plot.setDomainAxis(new NumberAxis());
        plot.getRenderer().setBaseStroke(new BasicStroke(2.0f));
        chartPanel.setChart(jfc);
    }

    public void valueChanged(TreeSelectionEvent e) {
        TreePath[] sel = jTree1.getSelectionPaths();
        if (sel == null) {
            return;
        }
        this.compute(sel);
        jPanel1.revalidate();
    }

    public void stateChanged(ChangeEvent arg0) {
        this.compute(jTree1.getSelectionPaths());
        jPanel1.revalidate();
    }
}

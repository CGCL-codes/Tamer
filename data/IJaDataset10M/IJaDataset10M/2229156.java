package com.pjsofts.eurobudget.report;

import com.jrefinery.chart.*;
import com.jrefinery.data.*;
import com.pjsofts.eurobudget.EBConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.*;
import javax.swing.*;

/**
 *
 * Try with JFreeChart(http://www.object-refinery.com/jfreechart/index.html)
 *
 * @author  pjourdan
 */
public class ReportViewJFreeChart implements ReportView {

    private static final ResourceBundle i8n = EBConstants.i18n;

    /** Data as our report */
    private ReportData data = null;

    /** */
    private JComponent comp = null;

    /** Creates a new instance of ReportViewJFreeChart */
    public ReportViewJFreeChart() {
    }

    /** @return a string used if needed to put the title of the report (e.g. as frame title) */
    public String getTitle() {
        return "�B Graphical Report: " + data.getTitle();
    }

    /** @return a component displaying data of the report
     * could be a JPanel to be used in another more complex window
     * Anyway is not a JFrame and is not a JScrollPane (which is done by container)
     */
    public JComponent getView() {
        return comp;
    }

    /**
     * link to a report data, add this as a listener to the data ?? or should do a controller ?? */
    public void setData(ReportData data) {
        this.data = data;
        updateViewData();
    }

    /**
     * called each time the report data is changed
     * update the view to reflect the last report data set
     * precondition this.data != null
     * postcondition this.comp != null
     */
    public void updateViewData() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        List rootGroups = data.getRootGroups();
        for (Iterator it = rootGroups.iterator(); it.hasNext(); ) {
            ReportGroup aGroup = (ReportGroup) it.next();
            JPanel subPanel = getChartPanel(null, aGroup);
            panel.add(subPanel);
            panel.add(new JSeparator());
        }
        JPanel view = new JPanel();
        view.setLayout(new BorderLayout());
        view.setSize(640, 480);
        view.setMaximumSize(new Dimension(640, 480));
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(640, 480));
        view.add(scrollPane, BorderLayout.CENTER);
        this.comp = view;
    }

    /**
     * @param choice define preferences for this report, may be null (in that case, is based on aGroup)
     * @return graphic chart
     */
    public static JPanel getChartPanel(ReportChoice choice, ReportGroup aGroup) {
        JPanel resultPanel = null;
        int shapeReport = aGroup.getShapeHint();
        if (choice != null && choice.getShape() != null) shapeReport = choice.getShape().intValue();
        if (shapeReport == ReportChoice.SHAPE_BAR) {
            if (aGroup.getDepth() == 0) {
                String[] seriesNames = getSeriesNames(aGroup);
                Object[] categories = getCategories(aGroup);
                Number[][] data = getBarData(aGroup);
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                fillDataSet(dataset, seriesNames, categories, data);
                JFreeChart chart;
                if (choice != null && choice.isShape3D()) {
                    chart = ChartFactory.createVerticalBarChart3D(aGroup.getLabel() + " (" + aGroup.getSum() + ")", i8n.getString("Category"), i8n.getString("Amount"), dataset, true, true, false);
                } else {
                    chart = ChartFactory.createVerticalBarChart(aGroup.getLabel() + " (" + aGroup.getSum() + ")", i8n.getString("Category"), i8n.getString("Amount"), dataset, true, true, false);
                }
                resultPanel = new ChartPanel(chart, true, true, true, true, true);
            } else if (aGroup.getDepth() == 1) {
                resultPanel = new JPanel();
                List groups = aGroup.getList();
                if (groups.size() > 0) {
                    resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
                }
                JLabel reportTitle = new JLabel(aGroup.getLabel() + " (" + aGroup.getSum() + ")");
                resultPanel.add(reportTitle);
                for (Iterator subit = groups.iterator(); subit.hasNext(); ) {
                    ReportGroup subGroup = (ReportGroup) subit.next();
                    String[] seriesNames = getSeriesNames(subGroup);
                    Object[] categories = getCategories(subGroup);
                    Number[][] data = getBarData(subGroup);
                    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                    fillDataSet(dataset, seriesNames, categories, data);
                    JFreeChart chart;
                    if (choice != null && choice.isShape3D()) {
                        chart = ChartFactory.createVerticalBarChart3D(subGroup.getLabel() + " (" + subGroup.getSum() + ")", i8n.getString("Category"), i8n.getString("Amount"), dataset, true, true, false);
                    } else {
                        chart = ChartFactory.createVerticalBarChart(subGroup.getLabel() + " (" + subGroup.getSum() + ")", i8n.getString("Category"), i8n.getString("Amount"), dataset, true, true, false);
                    }
                    ChartPanel chartPanel = new ChartPanel(chart, true, true, true, true, true);
                    chartPanel.setMaximumSize(chartPanel.getPreferredSize());
                    resultPanel.add(chartPanel);
                }
            } else {
                System.err.println("ReportViewJFreeChart.updateViewData: Report depth > 1 when constructing a Pie Chart");
            }
        } else if (shapeReport == ReportChoice.SHAPE_PIE) {
            if (aGroup.getDepth() == 0) {
                String[] seriesNames = getSeriesNames(aGroup);
                Number[] data = getPieData(aGroup);
                DefaultPieDataset dataset = new DefaultPieDataset();
                for (int k = 0; k < data.length; k++) {
                    dataset.setValue(seriesNames[k], data[k]);
                }
                JFreeChart chart;
                if (choice != null && choice.isShape3D()) {
                    chart = ChartFactory.createPie3DChart(aGroup.getLabel() + " (" + aGroup.getSum() + ")", dataset, true, true, false);
                } else {
                    chart = ChartFactory.createPieChart(aGroup.getLabel() + " (" + aGroup.getSum() + ")", dataset, true, true, false);
                }
                resultPanel = new ChartPanel(chart, true, true, true, true, true);
            } else if (aGroup.getDepth() == 1) {
                resultPanel = new JPanel();
                resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
                List groups = aGroup.getList();
                JLabel reportTitle = new JLabel(aGroup.getLabel() + " (" + aGroup.getSum() + ")");
                resultPanel.add(reportTitle);
                for (Iterator subit = groups.iterator(); subit.hasNext(); ) {
                    ReportGroup subGroup = (ReportGroup) subit.next();
                    Number[] data = getPieData(subGroup);
                    String[] seriesNames = getSeriesNames(subGroup);
                    DefaultPieDataset dataset = new DefaultPieDataset();
                    for (int k = 0; k < data.length; k++) {
                        dataset.setValue(seriesNames[k], data[k]);
                    }
                    JFreeChart chart;
                    if (choice != null && choice.isShape3D()) {
                        chart = ChartFactory.createPie3DChart(subGroup.getLabel() + " (" + subGroup.getSum() + ")", dataset, true, true, false);
                    } else {
                        chart = ChartFactory.createPieChart(subGroup.getLabel() + " (" + subGroup.getSum() + ")", dataset, true, true, false);
                    }
                    ChartPanel chartPanel = new ChartPanel(chart, true, true, true, true, true);
                    chartPanel.setMaximumSize(chartPanel.getPreferredSize());
                    resultPanel.add(chartPanel);
                }
            } else {
                System.err.println("ReportViewJFreeChart.updateViewData: Report depth > 1 when constructing a Pie Chart");
            }
        } else if (shapeReport == ReportChoice.SHAPE_LINE) {
            String[] seriesNames = getLineSeriesNames(aGroup);
            Object[] categories = getLineCategories(aGroup);
            Number[][] data = getLineData(aGroup);
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            fillDataSet(dataset, seriesNames, categories, data);
            JFreeChart chart;
            if (choice != null && choice.isShape3D()) {
                chart = ChartFactory.createLineChart(aGroup.getLabel() + " (" + aGroup.getSum() + ")", i8n.getString("Year"), i8n.getString("Amount"), dataset, false, true, false);
            } else {
                chart = ChartFactory.createLineChart(aGroup.getLabel() + " (" + aGroup.getSum() + ")", i8n.getString("Year"), i8n.getString("Amount"), dataset, false, true, false);
            }
            resultPanel = new ChartPanel(chart, true, true, true, true, true);
        } else if (shapeReport == ReportChoice.SHAPE_AREA) {
            String[] seriesNames = getLineSeriesNames(aGroup);
            Object[] categories = getLineCategories(aGroup);
            Number[][] data = getLineData(aGroup);
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            fillDataSet(dataset, seriesNames, categories, data);
            JFreeChart chart;
            if (choice != null && choice.isShape3D()) {
                chart = ChartFactory.createAreaChart(aGroup.getLabel() + " (" + aGroup.getSum() + ")", i8n.getString("Year"), i8n.getString("Amount"), dataset, false, true, false);
            } else {
                chart = ChartFactory.createAreaChart(aGroup.getLabel() + " (" + aGroup.getSum() + ")", i8n.getString("Year"), i8n.getString("Amount"), dataset, false, true, false);
            }
            resultPanel = new ChartPanel(chart, true, true, true, true, true);
        } else {
            resultPanel = new JPanel();
        }
        if (resultPanel != null) {
            resultPanel.setMaximumSize(resultPanel.getPreferredSize());
        }
        return resultPanel;
    }

    /** @param data one report group of depth-1 containing only ReportItems */
    private static String[] getSeriesNames(ReportGroup data) {
        int depth = data.getDepth();
        String[] res = null;
        if (depth == 0) {
            List items = data.getList();
            res = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                ReportItem item = (ReportItem) items.get(i);
                res[i] = item.label;
            }
        } else if (depth == 1) {
            int nbCategories = data.getList().size();
            int nbSeries = data.getMaxChildrenSize();
            res = new String[nbSeries];
            for (int i = 0; i < nbSeries; i++) {
                res[i] = "S" + i;
            }
        } else {
            System.err.println("Report level too high. Don't know how to create graphic for this report group.");
        }
        return res;
    }

    private static Object[] getCategories(ReportGroup data) {
        Object[] res = null;
        int depth = data.getDepth();
        if (depth == 0) {
            res = new Object[1];
            res[0] = data.getLabel();
        } else if (depth == 1) {
            List items = data.getList();
            int nbCategories = items.size();
            res = new Object[nbCategories];
            for (int i = 0; i < nbCategories; i++) {
                ReportGroup item = (ReportGroup) items.get(i);
                res[i] = item.getLabel() + " (" + item.getSum() + ")";
            }
        } else {
            System.err.println("Report level too high. Don't know how to create graphic for this report group.");
        }
        return res;
    }

    /**
     * @return data[ebCategories][1] ( data[series][categories]  )
     *
     */
    private static Number[][] getBarData(ReportGroup data) {
        int depth = data.getDepth();
        Number[][] res = null;
        if (depth == 0) {
            Number[] serie = getOneCategory(data);
            res = new Number[serie.length][1];
            for (int i = 0; i < serie.length; i++) {
                res[i][0] = serie[i];
            }
        } else if (depth == 1) {
            int nbCategories = data.getList().size();
            int nbSeries = data.getMaxChildrenSize();
            res = new Number[nbSeries][nbCategories];
            for (int cat = 0; cat < nbCategories; cat++) {
                Number[] aCatSeries = getCategorySeries((ReportGroup) data.getList().get(cat), nbSeries);
                for (int j = 0; j < nbSeries; j++) {
                    res[j][cat] = aCatSeries[j];
                }
            }
        } else {
            System.err.println("Report level too high. Don't know how to create graphic for this report group.");
        }
        return res;
    }

    /**
     * to create a pie data set
     * @return data[ebCategories]
     *
     */
    private static Number[] getPieData(ReportGroup data) {
        int depth = data.getDepth();
        Number[] res = null;
        if (depth == 0) {
            res = getOneCategory(data);
        } else {
            System.err.println("Report level too high. Don't know how to create graphic for this report group.");
        }
        return res;
    }

    /**
     * @param data group of depth = 0 (containing only report items)
     * @return serie values for one category
     */
    private static Number[] getOneCategory(ReportGroup data) {
        List items = data.getList();
        List tmp = new ArrayList();
        for (Iterator it = items.iterator(); it.hasNext(); ) {
            ReportItem item = (ReportItem) it.next();
            tmp.add(item.value);
        }
        Number[] res = (Number[]) tmp.toArray(new Number[tmp.size()]);
        return res;
    }

    /**
     * @param data group of depth = 0 (containing only report items)
     * @return serie values for one category
     */
    private static Number[] getCategorySeries(ReportGroup data, int nbSeries) {
        Number[] res = new Number[nbSeries];
        List items = data.getList();
        int i = 0;
        for (Iterator it = items.iterator(); it.hasNext(); ) {
            ReportItem item = (ReportItem) it.next();
            res[i] = item.value;
            i++;
        }
        for (; i < nbSeries; i++) {
            res[i] = new Double(0.0d);
        }
        return res;
    }

    /** @param data one report group of depth-1 containing only ReportItems */
    private static Object[] getLineCategories(ReportGroup data) {
        int depth = data.getDepth();
        Object[] res = null;
        if (depth == 0) {
            List items = data.getList();
            res = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                ReportItem item = (ReportItem) items.get(i);
                res[i] = item.label;
            }
        } else if (depth == 1) {
            List years = data.getList();
            List tmp = new ArrayList();
            for (int i = 0; i < years.size(); i++) {
                ReportGroup yearGroup = (ReportGroup) years.get(i);
                String year = yearGroup.getLabel();
                List months = yearGroup.getList();
                for (int j = 0; j < months.size(); j++) {
                    ReportItem monthItem = (ReportItem) months.get(j);
                    String month = monthItem.label;
                    tmp.add(month + " " + year);
                }
            }
            res = (String[]) tmp.toArray(new String[tmp.size()]);
        } else {
            System.err.println("Report level too high. Don't know how to create graphic for this report group.");
        }
        return res;
    }

    /** always one serie only  */
    private static String[] getLineSeriesNames(ReportGroup data) {
        String[] res = null;
        int depth = data.getDepth();
        if (depth < 2) {
            res = new String[1];
            res[0] = data.getLabel();
        } else {
            System.err.println("Report level too high. Don't know how to create graphic for this report group.");
        }
        return res;
    }

    /**
     * @return data[ebCategories][1] ( data[series][categories]  )
     *
     */
    private static Number[][] getLineData(ReportGroup data) {
        int depth = data.getDepth();
        Number[][] res = null;
        if (depth == 0) {
            Number[] serie = getOneCategory(data);
            res = new Number[1][serie.length];
            res[0] = serie;
        } else if (depth == 1) {
            List years = data.getList();
            List tmp = new ArrayList();
            for (int i = 0; i < years.size(); i++) {
                ReportGroup yearGroup = (ReportGroup) years.get(i);
                List months = yearGroup.getList();
                for (int j = 0; j < months.size(); j++) {
                    ReportItem month = (ReportItem) months.get(j);
                    Number nb = month.value;
                    tmp.add(nb);
                }
            }
            Number[] serie = (Number[]) tmp.toArray(new Number[tmp.size()]);
            res = new Number[1][serie.length];
            res[0] = serie;
        } else {
            System.err.println("Report level too high. Don't know how to create graphic for this report group.");
        }
        return res;
    }

    /** 
     * fill the given dataset with values of arrays.
     * @param categories all objects must implement Comparable interface
     */
    public static void fillDataSet(DefaultCategoryDataset dataset, String[] series, Object[] categories, Number[][] data) {
        for (int i = 0; i < series.length; i++) {
            for (int j = 0; j < categories.length; j++) {
                dataset.addValue(data[i][j], (Comparable) series[i], (Comparable) categories[j]);
            }
        }
    }
}

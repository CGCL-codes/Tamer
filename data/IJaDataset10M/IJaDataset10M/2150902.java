package edu.ucla.stat.SOCR.chart;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.NumberFormatException;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;
import edu.ucla.stat.SOCR.chart.gui.HiddenPlot;
import edu.ucla.stat.SOCR.chart.gui.SOCRCategorySeriesLabelGenerator;
import edu.ucla.stat.SOCR.chart.gui.SOCRXYSeriesLabelGenerator;
import edu.ucla.stat.SOCR.util.EditableHeader;

/**
  * A simple demonstration application showing how to create a bar chart.
 */
public class SuperXYChart extends Chart implements PropertyChangeListener {

    protected XYDataset dataset;

    public void init() {
        indLabel = new JLabel("X");
        depLabel = new JLabel("Y");
        super.init();
        depMax = 50;
        indMax = 50;
        updateStatus(url);
        resetExample();
        validate();
    }

    /**
	 *  sample code for generating chart using ChartGenerator_JTable
	 */
    public void doTest() {
        JFreeChart chart;
        ChartGenerator_JTable chartMaker = new ChartGenerator_JTable();
        resetChart();
        showMessageDialog("SuperXYChart doTest get called!");
        int no_series = (dataTable.getColumnCount() - 2) / 2;
        int[][] pairs = new int[no_series][2];
        for (int i = 0; i < no_series; i++) {
            pairs[i][0] = 2 * i;
            pairs[i][1] = 2 * i + 1;
        }
        chart = chartMaker.getXYChart("Line", "Line Chart", "X", "Y", dataTable, no_series, pairs, "");
        chartPanel = new ChartPanel(chart, false);
        setChart();
    }

    protected JFreeChart createLegend(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, "Category", "Value", dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        renderer.setLegendItemLabelGenerator(new SOCRXYSeriesLabelGenerator());
        return chart;
    }

    protected JFreeChart createLegendChart(JFreeChart origchart) {
        JFreeChart legendChart = new JFreeChart("", null, new HiddenPlot(), false);
        legendChart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) origchart.getPlot();
        LegendTitle legendTitle = new LegendTitle(plot, new ColumnArrangement(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, 0), new ColumnArrangement(HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, 0));
        legendChart.addLegend(legendTitle);
        return legendChart;
    }

    protected void setChart() {
        graphPanel.removeAll();
        graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
        chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X, CHART_SIZE_Y));
        if (legendPanelOn) {
            JFreeChart chart2 = createLegendChart(createLegend(dataset));
            legendPanel = new ChartPanel(chart2, false);
        }
        graphPanel.add(chartPanel);
        JScrollPane legendPane = new JScrollPane(legendPanel);
        if (legendPanelOn) {
            legendPane.setPreferredSize(new Dimension(CHART_SIZE_X, CHART_SIZE_Y / 5));
            graphPanel.add(legendPane);
        }
        graphPanel.validate();
        if (tabbedPanelContainer.getTitleAt(tabbedPanelContainer.getSelectedIndex()) != ALL) {
            tabbedPanelContainer.setSelectedIndex(tabbedPanelContainer.indexOfComponent(graphPanel));
            graphPanel.removeAll();
            graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
            graphPanel.add(chartPanel);
            if (legendPanelOn) {
                legendPane = new JScrollPane(legendPanel);
                legendPane.setPreferredSize(new Dimension(CHART_SIZE_X, CHART_SIZE_Y / 5));
                graphPanel.add(legendPane);
            }
            graphPanel.validate();
        } else {
            graphPanel2.removeAll();
            chartPanel.setPreferredSize(new Dimension(CHART_SIZE_X * 2 / 3, CHART_SIZE_Y * 2 / 3));
            graphPanel2.add(chartPanel);
            if (legendPanelOn) {
                legendPane = new JScrollPane(legendPanel);
                legendPane.setPreferredSize(new Dimension(CHART_SIZE_X * 2 / 5, CHART_SIZE_Y * 2 / 5));
                graphPanel2.add(legendPane);
            }
            graphPanel2.validate();
            summaryPanel.validate();
        }
    }

    public void doChart() {
        if (dataTable.isEditing()) dataTable.getCellEditor().stopCellEditing();
        if (!hasExample) {
            showMessageDialog(DATA_MISSING_MESSAGE);
            resetChart();
            return;
        }
        if (dependentIndex < 0 || independentIndex < 0 || independentLength == 0) {
            showMessageDialog(VARIABLE_MISSING_MESSAGE);
            resetChart();
            return;
        }
        dataset = createDataset(false);
        JFreeChart chart = createChart(dataset);
        chartPanel = new ChartPanel(chart, false);
        setChart();
    }

    /**
	   *
	   * @param isDemo data come from demo(true) or dataTable(false)
	   * @return
	   */
    protected XYDataset createDataset(boolean isDemo) {
        if (isDemo) {
            updateStatus("isDemo==true in " + this.getClass().getName() + " class! return null Dataset, check the code!");
            return null;
        } else {
            setArrayFromTable();
            if (independentVarLength != dependentVarLength) {
                showMessageDialog("The number of X and Y doesn't match!");
                resetChart();
                return null;
            }
            String[][] x = new String[xyLength][independentVarLength];
            double[][] y = new double[xyLength][dependentVarLength];
            int[][] skipy = new int[xyLength][dependentVarLength];
            for (int index = 0; index < independentVarLength; index++) for (int i = 0; i < xyLength; i++) x[i][index] = indepValues[i][index];
            try {
                for (int index = 0; index < dependentVarLength; index++) for (int i = 0; i < xyLength; i++) if (depValues[i][index] != null && depValues[i][index] != "null" && depValues[i][index].length() != 0) y[i][index] = Double.parseDouble(depValues[i][index]); else skipy[i][index] = 1;
            } catch (NumberFormatException e) {
                showMessageDialog("dependent Data format error!");
                return null;
            }
            XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeries series;
            try {
                for (int i = 0; i < independentVarLength; i++) {
                    String serieName;
                    if (independentHeaders[i].lastIndexOf(":") != -1) serieName = independentHeaders[i].substring(0, independentHeaders[i].lastIndexOf(":")); else serieName = independentHeaders[i];
                    series = new XYSeries(serieName);
                    for (int j = 0; j < xyLength; j++) {
                        if (x[j][i] != null && x[j][i] != "null" && x[j][i] != "NaN" && x[j][i].length() != 0 && skipy[j][i] != 1) series.add(Double.parseDouble(x[j][i]), y[j][i]);
                    }
                    dataset.addSeries(series);
                }
            } catch (NumberFormatException e) {
                showMessageDialog("independent Data format error!");
                return null;
            }
            return dataset;
        }
    }

    /**
     * Creates a chart.
     *
     * @param dataset  the dataset.
     *
     * @return a chart.
     */
    protected JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, "X", "Y", dataset, PlotOrientation.VERTICAL, !legendPanelOn, true, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }

    /**
     * reset dataTable to default (demo data), and refesh chart
     */
    public void resetExample() {
        dataset = createDataset(true);
        JFreeChart chart = createChart(dataset);
        chartPanel = new ChartPanel(chart, false);
        setChart();
        hasExample = true;
        convertor.dataset2Table(dataset);
        JTable tempDataTable = convertor.getTable();
        resetTableRows(tempDataTable.getRowCount() + 1);
        resetTableColumns(tempDataTable.getColumnCount() + 2);
        for (int i = 0; i < tempDataTable.getColumnCount(); i++) {
            columnModel.getColumn(i).setHeaderValue(tempDataTable.getColumnName(i));
        }
        columnModel = dataTable.getColumnModel();
        dataTable.setTableHeader(new EditableHeader(columnModel));
        for (int i = 0; i < tempDataTable.getRowCount(); i++) for (int j = 0; j < tempDataTable.getColumnCount(); j++) {
            dataTable.setValueAt(tempDataTable.getValueAt(i, j), i, j);
        }
        dataPanel.removeAll();
        dataPanel.add(new JScrollPane(dataTable));
        dataTable.setGridColor(Color.gray);
        dataTable.setShowGrid(true);
        dataTable.doLayout();
        try {
            dataTable.setDragEnabled(true);
        } catch (Exception e) {
        }
        dataPanel.validate();
        int columnCount = dataTable.getColumnCount();
        for (int i = 0; i < columnCount / 2 - 1; i++) {
            addButtonIndependent();
            addButtonDependent();
        }
        updateStatus(url);
    }

    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        System.err.println("From RegCorrAnal:: propertyName =" + propertyName + "!!!");
        if (propertyName.equals("DataUpdate")) {
            dataTable = (JTable) (e.getNewValue());
            dataPanel.removeAll();
            dataPanel.add(new JScrollPane(dataTable));
            dataTable.doLayout();
            System.err.println("From RegCorrAnal:: data UPDATED!!!");
        }
    }

    public Container getDisplayPane() {
        return this.getContentPane();
    }
}

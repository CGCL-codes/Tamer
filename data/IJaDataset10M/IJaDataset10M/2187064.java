package com.yerihyo.yeritools.visualization.jfreechart.lib.model.option;

import java.awt.Paint;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.w3c.dom.Element;
import com.yerihyo.yeritools.collections.AlphaNumericComparator;
import com.yerihyo.yeritools.sql.SQLToolkit;
import com.yerihyo.yeritools.sql.TableConnection;
import com.yerihyo.yeritools.sql.SQLToolkit.Operator;
import com.yerihyo.yeritools.sql.SQLToolkit.DistinctStringListExtractor.TooManyDistinctDataException;
import com.yerihyo.yeritools.sql.WhereTableRowToolkit.ConfigTableRow;
import com.yerihyo.yeritools.swing.AlertDialog;
import com.yerihyo.yeritools.swing.WhereTable;
import com.yerihyo.yeritools.visualization.jfreechart.lib.model.option.OptionManager.AbstractChartOption;
import com.yerihyo.yeritools.visualization.jfreechart.visualizer.ChartToolkit;
import com.yerihyo.yeritools.visualization.jfreechart.visualizer.ChartToolkit.ChartException;
import com.yerihyo.yeritools.xml.XMLToolkit;

public class PieChartOption extends AbstractChartOption {

    public static final String TYPE_NAME = "Pie Chart";

    public int getDefaultLimit() {
        return 200;
    }

    private String targetColumn;

    private String getTargetColumn() {
        return targetColumn;
    }

    @Override
    public void fromXMLItem(Element child) throws Exception {
        if (child.getTagName().equals(OptionManager.TARGET_COLUMN)) {
            this.setTargetColumn(XMLToolkit.getTextContent(child).toString());
        }
    }

    public void setTargetColumn(String columnName) {
        this.targetColumn = columnName;
    }

    @Override
    public CharSequence toXMLItem() {
        StringBuffer buffer = new StringBuffer();
        if (targetColumn != null) {
            buffer.append("<").append(OptionManager.TARGET_COLUMN).append(">");
            buffer.append("<![CDATA[").append(targetColumn).append("]]>");
            buffer.append("</").append(OptionManager.TARGET_COLUMN).append(">");
        }
        return buffer;
    }

    protected void initWhereTable(TableConnection tableConnection) {
        List<String> columnNameList = null;
        try {
            columnNameList = SQLToolkit.getColumnNames(tableConnection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (columnNameList.size() == 0) {
            throw new RuntimeException("A DB table with no column at all!");
        }
        Collections.sort(columnNameList, new AlphaNumericComparator());
        whereTable.clearRowList();
        ConfigTableRow<?> configTableRow = OptionManager.getNominalConfigTableRow(OptionManager.TARGET_COLUMN, columnNameList);
        whereTable.addConfigTableRow(configTableRow);
    }

    @Override
    public String getTypeName() {
        return TYPE_NAME;
    }

    private int limit = getDefaultLimit();

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public PieDataset createDataset(Map<Operator, Map<? extends String, ?>> whereMap) throws SQLException, ChartException {
        TableConnection tableConnection = this.getTableConnection();
        SQLToolkit toolkit = SQLToolkit.createCountingSQLToolkit(tableConnection);
        toolkit.setDoubleWhereMap(whereMap);
        List<String> groupByList = new ArrayList<String>();
        groupByList.add(this.getTargetColumn());
        toolkit.setGroupBy(groupByList);
        toolkit.setMaxRows(this.getLimit() + 1);
        ResultSet rs = toolkit.executeSelect();
        int countRowIndex = 2;
        Map<String, Integer> datasetMap = new HashMap<String, Integer>();
        if (rs != null) {
            for (int i = 0; rs.next(); i++) {
                if (i >= this.getLimit()) {
                    if (rs != null) {
                        rs.close();
                    }
                    toolkit.closeStatement();
                    throw new ChartException("Data too dense: " + ">" + this.getLimit());
                }
                String valueName = rs.getString(1);
                int newValue = rs.getInt(countRowIndex);
                datasetMap.put(valueName, newValue);
            }
        }
        PieDataset dataset = createPieDatasetFromMap(datasetMap);
        rs.close();
        toolkit.closeStatement();
        return dataset;
    }

    public static DefaultPieDataset createPieDatasetFromMap(Map<? extends String, ? extends Integer> map) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<? extends String, ? extends Integer> entry : map.entrySet()) {
            String escapedKey = ChartToolkit.escapeNull(entry.getKey());
            dataset.setValue(escapedKey, entry.getValue());
        }
        return dataset;
    }

    @Override
    public JFreeChart getChart(Map<Operator, Map<? extends String, ?>> whereDoubleMap, StringBuffer buffer) throws Exception {
        JFreeChart jFreeChart = null;
        try {
            jFreeChart = this.getPieChart(whereDoubleMap);
        } catch (SQLException ex) {
            AlertDialog.show("Error while generating JFreeChart.", ex, this.getConfigPanel());
        }
        return jFreeChart;
    }

    public JFreeChart getPieChart(Map<Operator, Map<? extends String, ?>> whereDoubleMap) throws SQLException, ChartException, TooManyDistinctDataException {
        TableConnection tableConnection = this.getTableConnection();
        String columnName = this.getTargetColumn();
        String title = this.getName();
        PieDataset dataset = createDataset(whereDoubleMap);
        if (dataset == null) {
            return null;
        }
        int itemCount = dataset.getItemCount();
        if (itemCount == 0) {
            return null;
        }
        JFreeChart chart = ChartFactory.createPieChart(title, dataset, false, true, false);
        List<String> targetValues = SQLToolkit.getDistinctStrings(tableConnection, columnName);
        Paint[] paints = null;
        if (paints != null) {
            PiePlot piePlot = (PiePlot) chart.getPlot();
            int targetValuesSize = targetValues.size();
            int paintsSize = paints.length;
            for (int i = 0; i < targetValuesSize; i++) {
                String targetValue = ChartToolkit.escapeNull(targetValues.get(i));
                piePlot.setSectionPaint(targetValue, paints[i % paintsSize]);
            }
        }
        return chart;
    }

    @Override
    public void memoryToUI() {
        if (this.getTargetColumn() != null) {
            WhereTable whereTable = (WhereTable) this.getConfigPanel();
            String targetColumn = this.getTargetColumn();
            whereTable.setValue(OptionManager.TARGET_COLUMN, targetColumn);
        }
    }

    @Override
    public void uiToMemory() {
        WhereTable whereTable = (WhereTable) this.getConfigPanel();
        this.setTargetColumn((String) whereTable.getValue(OptionManager.TARGET_COLUMN));
    }
}

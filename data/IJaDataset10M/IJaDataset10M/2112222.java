package com.calipso.reportgenerator.common.jrdataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import com.calipso.reportgenerator.common.QueryMetric;
import com.calipso.reportgenerator.common.QueryDimension;
import com.calipso.reportgenerator.common.QueryDimension;
import com.calipso.reportgenerator.common.QueryMetric;
import java.util.Vector;

/**
 *
 * User: soliveri
 * Date: Dec 16, 2003
 * Time: 2:59:52 PM
 *
 */
public abstract class ReportTableModel implements JRDataSource {

    private TableModel reportTableModel;

    private Vector tableColumnNames;

    private Vector tableData;

    private int groupingDimCount;

    private int nonGroupingDimCount;

    private int commonMetricsCount;

    private int accMetricsCount;

    private int index = -1;

    protected Vector occurrences;

    public ReportTableModel() {
    }

    public boolean next() throws JRException {
        index++;
        return (index < reportTableModel.getRowCount());
    }

    public Object getFieldValue(JRField jrField) throws JRException {
        Object value = null;
        int col = 0;
        String fieldName = jrField.getName();
        for (int i = 0; i < tableColumnNames.size(); i++, col++) {
            if (tableColumnNames.elementAt(i).toString().equals(fieldName)) {
                value = reportTableModel.getValueAt(index, col);
            }
        }
        return value;
    }

    protected void setTableColumnNames(Vector tableColumnNames) {
        this.tableColumnNames = tableColumnNames;
    }

    protected void setTableData(Vector tableData) {
        this.tableData = tableData;
    }

    /**
   * Devuelve un vector que contiene los nombres de las columnas para el modelo
   * @param groupingDimensions dimensiones que agrupan
   * @param nonGroupingDimensions dimensiones que no agrupan
   * @param metricsArray metricas
   * @return nombres de las columnas para el modelo
   */
    protected Vector getTableModelColumns(Object[] groupingDimensions, Object[] nonGroupingDimensions, Object[] metricsArray) {
        Vector columnNames = new Vector();
        for (int i = 0; i < groupingDimensions.length; i++) {
            QueryDimension qd = (QueryDimension) groupingDimensions[i];
            columnNames.add(qd.getName());
        }
        for (int i = 0; i < nonGroupingDimensions.length; i++) {
            QueryDimension qd = (QueryDimension) nonGroupingDimensions[i];
            columnNames.add(qd.getName());
        }
        for (int i = 0; i < metricsArray.length; i++) {
            QueryMetric qm = (QueryMetric) metricsArray[i];
            columnNames.add(qm.getName());
        }
        return columnNames;
    }

    protected void newDefaultTableModel() {
        reportTableModel = new DefaultTableModel(tableData, tableColumnNames);
    }

    protected void newNonDataTableModel() {
        reportTableModel = new DefaultTableModel(new Vector(), tableColumnNames);
    }

    public TableModel getModel() {
        return reportTableModel;
    }

    public int getGroupingDimCount() {
        return groupingDimCount;
    }

    protected void setGroupingDimCount(int groupingDimCount) {
        this.groupingDimCount = groupingDimCount;
    }

    public int getNonGroupingDimCount() {
        return nonGroupingDimCount;
    }

    protected void setNonGroupingDimCount(int nonGroupingDimCount) {
        this.nonGroupingDimCount = nonGroupingDimCount;
    }

    public int getCommonMetricsCount() {
        return commonMetricsCount;
    }

    protected void setCommonMetricsCount(int commonMetricsCount) {
        this.commonMetricsCount = commonMetricsCount;
    }

    public int getAccMetricsCount() {
        return accMetricsCount;
    }

    protected void setAccMetricsCount(int accMetricsCount) {
        this.accMetricsCount = accMetricsCount;
    }

    public Vector getOccurrences() {
        return occurrences;
    }
}

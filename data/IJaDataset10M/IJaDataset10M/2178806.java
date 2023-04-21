package com.calipso.reportgenerator.common.reportresult;

import com.calipso.reportgenerator.common.ReportQuery;
import com.calipso.reportgenerator.common.ReportResult;
import com.calipso.reportgenerator.common.ReportSpec;
import com.calipso.reportgenerator.common.jrdataSource.ReportTableModel;
import com.calipso.reportgenerator.common.jrdataSource.CubeReportTableModel;
import com.calipso.reportgenerator.common.tablemodel.HeaderTableModel;
import com.calipso.reportgenerator.common.exception.InfoException;
import java.util.Vector;
import java.util.Collection;

/**
 *
 * User: soliveri
 * Date: Dec 16, 2003
 * Time: 2:12:49 PM
 *
 */
public class CubeReportResult extends ReportResult {

    private HeaderTableModel rowsModel;

    private HeaderTableModel columnsModel;

    private Vector dataVector;

    private ReportTableModel reportTable = null;

    public CubeReportResult(ReportSpec reportSpec, ReportQuery reportQuery, HeaderTableModel rowsModel, HeaderTableModel columnsModel, Vector dataVector) {
        super(reportSpec, reportQuery);
        this.rowsModel = rowsModel;
        this.columnsModel = columnsModel;
        this.dataVector = dataVector;
    }

    public HeaderTableModel getRowsModel() {
        return rowsModel;
    }

    public HeaderTableModel getColumnsModel() {
        return columnsModel;
    }

    public Vector getDataVector() {
        return dataVector;
    }

    public ReportTableModel getReportTableModel() throws InfoException {
        if (reportTable == null) {
            reportTable = new CubeReportTableModel(this);
        }
        return reportTable;
    }

    public void resetReportTableModel() {
        reportTable = null;
    }

    public Collection getValuesCollection(boolean ascending) {
        return getRowsModel().getDimensionValueNode().getSubNodesList();
    }

    public ReportQuery getReportQuery() {
        return super.getReportQuery();
    }

    public ReportSpec getReportSpec() {
        return super.getReportSpec();
    }
}

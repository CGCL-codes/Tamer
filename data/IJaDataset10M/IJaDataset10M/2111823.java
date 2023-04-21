package com.simpledata.bc.reports.common;

import com.simpledata.bc.reports.base.Subreport;
import com.simpledata.bc.reports.base.SubreportTreeItem;

/**  * Type that defines what a specialized subreport must provide * for working with ReportFactory.  */
public abstract class SpecializedSubreport {

    /** Internal storage of all report data. */
    protected Subreport m_report;

    /**	 * Add one row of data using the local specialization 	 * of the data row abstraction. 	 * 	 * DataRow is best defined an inner class of your Report	 * class and should define all the fields that form one 	 * row of data in your report. 	 * @param row Row to add to report. 	 */
    public void addData(SpecializedDataRow row) {
        m_report.addRow(row.toObjectArray());
    }

    /**	 * Return the report. 	 * @return filled subreport	 */
    public SubreportTreeItem getReport() {
        return (SubreportTreeItem) m_report;
    }
}

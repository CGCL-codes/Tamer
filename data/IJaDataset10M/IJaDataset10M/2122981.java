package org.fantasy.common.grid.report;

import org.fantasy.common.db.bean.ReportCell;
import org.fantasy.common.db.bean.ReportRow;
import org.fantasy.common.grid.bean.Column;
import org.fantasy.common.grid.bean.ReportParam;
import org.fantasy.common.util.Debug;
import org.fantasy.common.util.StringUtil;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  构造Excel
 * @author: 王文成
 * @version: 1.0
 * @since 2009-12-14
 */
public class ReportExcelBuilder extends AbstractReportBuilder {

    private static final Log log = LogFactory.getLog(ReportExcelBuilder.class);

    public ReportExcelBuilder(ReportParam param) throws Exception {
        super(param);
    }

    /**
     * 构建表头
     * 
     * @throws Exception
     */
    @Override
    protected StringBuffer buildHead() throws Exception {
        StringBuffer html = new StringBuffer(1024);
        for (List<Column> columns : this.tableHeads) {
            html.append("<tr>\n");
            for (Column col : columns) {
                if (!isExportable(col)) {
                    continue;
                }
                html.append("<th ");
                if (StringUtil.isValid(col.getColspan())) {
                    if (Integer.parseInt(col.getColspan()) > 1) html.append(" colspan='" + col.getColspan() + "'");
                }
                if (StringUtil.isValid(col.getRowspan())) {
                    if (Integer.parseInt(col.getRowspan()) > 1) html.append(" rowspan='" + col.getRowspan() + "'");
                }
                if (StringUtil.isValid(col.getFieldLabel())) html.append(">" + col.getFieldLabel()); else html.append(">");
                html.append("</th>\n");
            }
            html.append("</tr>\n");
        }
        return html;
    }

    /**
     * 构建表体
     * 
     * @throws Exception
     */
    @Override
    protected StringBuffer buildBody() throws Exception {
        StringBuffer html = new StringBuffer(1024);
        for (int i = 0; i < resultList.size(); i++) {
            ReportRow row = resultList.get(i);
            html.append("<tr height='20'>");
            html.append(buildCells(row));
            html.append("</tr>\n");
        }
        return html;
    }

    /**
     * 构造一行中的所有列
     * 
     * @return
     */
    protected StringBuffer buildCells(ReportRow row) throws Exception {
        StringBuffer html = new StringBuffer(1024);
        for (int i = 0; i < tableFields.size(); i++) {
            Column col = tableFields.get(i);
            if (!isExportable(col)) {
                continue;
            }
            String colName = col.getFieldName();
            String colValue = cellCustom.getValue(row, col);
            ReportCell cell = row.getReportCell(colName);
            if (cell.isTop()) {
                if (cell.getRowspan() > 1) html.append("<td rowspan='" + cell.getRowspan() + "'"); else html.append("<td");
            } else {
                continue;
            }
            html.append(">");
            html.append(colValue);
            html.append("</td>\n");
        }
        return html;
    }

    /**
     * 是否能导出
     * @param col
     * @return
     */
    private boolean isExportable(Column col) {
        return !param.isHideCol(col) && col.isExport();
    }

    @Override
    public Object getReport() throws Exception {
        Debug debug = new Debug("Report");
        debug.start("Building Report");
        StringBuffer html = new StringBuffer(1024 * 8);
        html.append("<table cellspacing='1' cellpadding='3' border='1' style='font-size:12px;'>");
        html.append(this.buildHead());
        html.append("   <tbody>\n");
        html.append(this.buildBody());
        html.append("   </tbody>\n");
        html.append("</table> \n");
        debug.end("Building Report");
        log.info(debug);
        return html;
    }
}

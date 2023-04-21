package jbreport.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import jbreport.Constants;
import jbreport.ReportComposite;
import jbreport.ReportElement;
import jbreport.ReportException;
import jbreport.data.Aggregate;
import jbreport.data.QueryResult;
import jbreport.util.ReportBreak;

/**
 * This will create a table in the document. The table is normally bound to
 * the result of a datasource query.
 *
 * @author Grant Finnemore
 * @version $Revision: 1.1 $
 */
class Table extends ReportCompositeImpl {

    /** The headers of the table */
    private ReportComposite columns;

    /** Do we already have an aggregate row */
    private boolean haveAggregateRow = false;

    public void accept(ReportVisitor visitor, ReportVisitorState state) throws ReportException {
        visitor.visitTable(this, state);
    }

    public void breakEventNotify(ReportBreak source, Object type) throws ReportException {
        ArrayList aggs = new ArrayList();
        List cols = columns.getList(Constants.RE_CHILDREN);
        boolean shouldAdd = false;
        for (int i = 0; i < cols.size(); i++) {
            ReportElement col = (ReportElement) cols.get(i);
            List elems = col.getList(Constants.RE_CHILDREN);
            for (int j = 0; j < elems.size(); j++) {
                ReportElement elem = (ReportElement) elems.get(j);
                if (elem instanceof Aggregate) {
                    aggs.add(elem);
                    shouldAdd = true;
                    break;
                }
            }
            if (aggs.size() <= i) {
                aggs.add(null);
            }
        }
        if (shouldAdd && !haveAggregateRow) {
            haveAggregateRow = true;
            TableRow row = new TableRow();
            addElement(row);
            for (int i = 0; i < aggs.size(); i++) {
                ReportElement elem = (ReportElement) aggs.get(i);
                if (elem != null) {
                    row.addCellData(elem.toString());
                } else {
                    row.addCellData(null);
                }
            }
        }
    }

    public ReportElement copy() {
        Table result = (Table) super.copy();
        result.haveAggregateRow = false;
        return result;
    }

    public ReportElement copy(boolean deep) {
        Table result = (Table) super.copy(deep);
        result.haveAggregateRow = false;
        if (deep == DEEP) {
            if (columns != null) {
                result.columns = (ReportComposite) columns.copy(deep);
                result.columns.setParent(result);
            }
        }
        return result;
    }

    public ReportElement getElement(String property) {
        ReportElement result = null;
        if (Constants.RE_TABLE_HEADER_ROW.equals(property)) {
            result = columns;
        } else {
            result = super.getElement(property);
        }
        return result;
    }

    public void updateData(QueryResult queryResult) throws ReportException {
        if (columns != null) {
            ((AbstractReportElement) columns).updateData(queryResult);
        }
        if (columns == null && queryResult.getColumnCount() > 0) {
            for (int i = 1; i <= queryResult.getColumnCount(); i++) {
                TableColumn col = new TableColumn();
                String value = queryResult.nameForIndex(i);
                col.setString("id", value);
                col.setString("name", value);
                col.setString("cdata", value);
                addColumn(col);
            }
        }
        TableRow row = new TableRow();
        addElement(row);
        row.updateData(queryResult);
    }

    public void xmlEndChild(ReportElement elem) throws ReportException {
        if ("column".equals(elem.getType())) {
            addColumn((TableColumn) elem);
        } else {
            super.xmlEndChild(elem);
        }
    }

    private void addColumn(TableColumn elem) {
        if (columns == null) {
            columns = new TableHeaderRow();
            columns.setParent(this);
        }
        columns.addElement(elem);
    }
}

/**
 * The implementation of the table header row composite.
 */
class TableHeaderRow extends ReportCompositeImpl {

    public TableHeaderRow() {
        elementType = "column_row";
    }

    public void accept(ReportVisitor visitor, ReportVisitorState state) throws ReportException {
        visitor.visitTableHeaderRow(this, state);
    }
}

package org.unitils.dataset.assertstrategy.impl;

import org.unitils.core.UnitilsException;
import org.unitils.dataset.assertstrategy.DatabaseContentLogger;
import org.unitils.dataset.assertstrategy.model.DataSetComparison;
import org.unitils.dataset.assertstrategy.model.TableComparison;
import org.unitils.dataset.database.DataSourceWrapper;
import org.unitils.dataset.model.database.Column;
import org.unitils.dataset.model.database.Row;
import org.unitils.dataset.model.database.TableName;
import org.unitils.dataset.model.database.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.apache.commons.lang.StringUtils.rightPad;

/**
 * todo limit the nr of outputted rows
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public class DefaultDatabaseContentLogger implements DatabaseContentLogger {

    protected DataSourceWrapper dataSourceWrapper;

    protected TableContentRetriever tableContentRetriever;

    public void init(DataSourceWrapper dataSourceWrapper, TableContentRetriever tableContentRetriever) {
        this.dataSourceWrapper = dataSourceWrapper;
        this.tableContentRetriever = tableContentRetriever;
    }

    public String getDatabaseContentForComparison(DataSetComparison dataSetComparison) {
        try {
            StringBuilder contentBuilder = new StringBuilder();
            for (TableComparison tableComparison : dataSetComparison.getTableComparisons()) {
                getActualTableContent(tableComparison, contentBuilder);
            }
            return contentBuilder.toString();
        } catch (UnitilsException e) {
            throw e;
        } catch (Exception e) {
            throw new UnitilsException("Unable to log actual database content for data set assertstrategy.", e);
        }
    }

    protected void getActualTableContent(TableComparison tableComparison, StringBuilder contentBuilder) throws Exception {
        TableName tableName = tableComparison.getTableName();
        Set<String> primaryKeyColumnNames = dataSourceWrapper.getPrimaryKeyColumnNames(tableName);
        Set<Column> columns = dataSourceWrapper.getColumns(tableName);
        TableContents tableContents = tableContentRetriever.getTableContents(tableName, columns, primaryKeyColumnNames);
        try {
            int nrOfColumns = tableContents.getNrOfColumns();
            List<String> columnNames = tableContents.getColumnNames();
            List<List<String>> values = new ArrayList<List<String>>(nrOfColumns);
            List<Integer> columnSizes = new ArrayList<Integer>(nrOfColumns);
            List<Boolean> rowWithExactMatch = new ArrayList<Boolean>();
            contentBuilder.append(tableName);
            contentBuilder.append('\n');
            for (String columnName : columnNames) {
                columnSizes.add(columnName.length());
                values.add(new ArrayList<String>());
            }
            Row row;
            while ((row = tableContents.getRow()) != null) {
                String rowIdentifier = row.getIdentifier();
                rowWithExactMatch.add(tableComparison.isMatchingRow(rowIdentifier));
                List<Value> rowValues = row.getValues();
                for (int i = 0; i < nrOfColumns; i++) {
                    Value value = rowValues.get(i);
                    String valueAsString = "";
                    if (value.getValue() != null) {
                        valueAsString = value.getValue().toString();
                    }
                    values.get(i).add(valueAsString);
                    columnSizes.set(i, Math.max(columnSizes.get(i), valueAsString.length()));
                }
            }
            getContent(columnNames, values, rowWithExactMatch, columnSizes, contentBuilder);
        } finally {
            tableContents.close();
        }
    }

    protected void getContent(List<String> columnNames, List<List<String>> values, List<Boolean> rowWithExactMatch, List<Integer> columnSizes, StringBuilder contentBuilder) {
        int nrOfRows = values.get(0).size();
        if (nrOfRows == 0) {
            contentBuilder.append("   <empty table>");
            return;
        }
        contentBuilder.append("   ");
        for (int i = 0; i < columnNames.size(); i++) {
            contentBuilder.append(rightPad(columnNames.get(i), columnSizes.get(i) + 2));
        }
        contentBuilder.append('\n');
        for (int i = 0; i < nrOfRows; i++) {
            if (rowWithExactMatch.get(i)) {
                contentBuilder.append("-> ");
            } else {
                contentBuilder.append("   ");
            }
            for (int ii = 0; ii < values.size(); ii++) {
                contentBuilder.append(rightPad(values.get(ii).get(i), columnSizes.get(ii) + 2));
            }
            contentBuilder.append('\n');
        }
    }
}

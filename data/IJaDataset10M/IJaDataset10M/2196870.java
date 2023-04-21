package org.sqlexp.view.query.result;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.sqlexp.controls.ExpDataTable;
import org.sqlexp.controls.data.ITableDataSet;
import org.sqlexp.util.SqlUtils;

/**
 * Single table result item.
 * @author Matthieu Réjou
 */
public class TableResultItem extends ResultItem {

    /**
	 * @param parent composite
	 * @param resultSet to display
	 * @throws SQLException if ResultSet can(t be fully read
	 */
    public TableResultItem(final Composite parent, final ResultSet resultSet) throws SQLException {
        super(parent);
        int i;
        ResultSetMetaData metaData = resultSet.getMetaData();
        final int columnCount = metaData.getColumnCount();
        final ArrayList<String[]> data = new ArrayList<String[]>(100);
        String[] line;
        while (resultSet.next()) {
            line = new String[columnCount];
            for (i = 1; i <= columnCount; i++) {
                line[i - 1] = resultSet.getString(i);
            }
            data.add(line);
        }
        ExpDataTable table = new ExpDataTable(this);
        int type, style;
        for (i = 1; i <= columnCount; i++) {
            type = metaData.getColumnType(i);
            if (SqlUtils.isNumeric(type)) {
                style = SWT.RIGHT;
            } else {
                style = SWT.NONE;
            }
            table.addColumn(metaData.getColumnLabel(i), style);
        }
        table.setNullString("<NULL>");
        table.displayData(new ITableDataSet<String[]>() {

            @Override
            public int getLineCount() {
                return data.size();
            }

            @Override
            public String[] getData(final int line) {
                return data.get(line);
            }

            @Override
            public Collection<String[]> getData() {
                return data;
            }
        });
    }
}

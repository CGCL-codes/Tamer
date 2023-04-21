package com.cell.sql;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.cell.sql.CustomColumnSet;
import com.cell.sql.CustomRow;

public class CustomResultSet implements Serializable {

    private static final long serialVersionUID = 1L;

    private final CustomColumnSet colset_;

    private final ArrayList<CustomRow> rows_;

    public CustomResultSet(ResultSet rs) throws SQLException {
        colset_ = new CustomColumnSet(rs);
        String[] col_names = colset_.getColumns();
        rows_ = new ArrayList<CustomRow>();
        while (rs.next()) {
            CustomRow row = new CustomRow(rs, col_names);
            rows_.add(row);
        }
    }

    public Object[][] getData() {
        int row_count = rows_.size();
        Object[][] ret = new Object[row_count][];
        for (int i = 0; i < row_count; ++i) {
            Object[] row = rows_.get(i).getValues();
            ret[i] = row;
        }
        return ret;
    }

    public CustomColumnSet getColumnSet() {
        return colset_;
    }
}

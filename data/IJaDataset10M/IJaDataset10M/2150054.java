package com.cs.util.db.ioc;

import com.cs.util.db.ResultSetMetaInfo;
import java.sql.SQLException;

/**
 * MySqlSet is an IO controller for handling input and output of MySql SET columns.
 * @author dimitris@jmike.gr
 */
public class MySqlSet extends MySqlEnum implements IOController {

    public MySqlSet(ResultSetMetaInfo rsmi, int columnIndex) throws SQLException {
        super(rsmi, columnIndex);
    }

    public MySqlSet(ResultSetMetaInfo rsmi, String table, String column) throws SQLException {
        super(rsmi, table, column);
    }
}

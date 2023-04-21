package com.restsql.db.io;

import com.restsql.db.ColumnInfo;
import com.restsql.db.Database;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * MySQLDataIO can be used to manipulate the data of a MySQL database.
 * @author dimitris@jmike.gr
 */
public class MySQLDataIO implements DataIO {

    private Database db;

    /**
     * Construct a new MySQLDataIO.
     * @param db the parent database.
     */
    public MySQLDataIO(Database db) {
        this.db = db;
    }

    /**
     * Set the designated parameter for the specified PreparedStatement.
     * @param ps the PreparedStatement.
     * @param index the parameter's index.
     * @param table the name of the parent table of the parameter.
     * @param column the column name of the parameter.
     * @param value the value of the parameter.
     * @throws java.sql.SQLException
     */
    public void setParameter(PreparedStatement ps, int index, String table, String column, String value) throws SQLException {
        ColumnInfo columnInfo = db.getColumnInfo(table);
        System.out.println(table + "." + column + " (" + columnInfo.getDatatype(column).toString() + ")");
        switch(columnInfo.getDatatype(column)) {
            case BIGINT:
                {
                    break;
                }
            case BINARY:
                {
                    break;
                }
            case BIT:
                {
                    break;
                }
            case BLOB:
                {
                    break;
                }
            case CHAR:
                {
                    ps.setString(index, value);
                    break;
                }
            case DATE:
                {
                    break;
                }
            case DATETIME:
                {
                    break;
                }
            case DECIMAL:
                {
                    break;
                }
            case DOUBLE:
                {
                    break;
                }
            case ENUM:
                {
                    ps.setString(index, value);
                    break;
                }
            case FLOAT:
                {
                    break;
                }
            case INT:
                {
                    if (columnInfo.isSigned(column)) {
                        final int x = Integer.parseInt(value);
                        ps.setInt(index, x);
                    } else {
                        final long x = Long.parseLong(value);
                        ps.setLong(index, x);
                    }
                    break;
                }
            case LONGBLOB:
                {
                    break;
                }
            case LONGTEXT:
                {
                    ps.setString(index, value);
                    break;
                }
            case MEDIUMBLOB:
                {
                    break;
                }
            case MEDIUMINT:
                {
                    final int x = Integer.parseInt(value);
                    ps.setInt(index, x);
                    break;
                }
            case MEDIUMTEXT:
                {
                    ps.setString(index, value);
                    break;
                }
            case SET:
                {
                    ps.setString(index, value);
                    break;
                }
            case SMALLINT:
                {
                    final int x = Integer.parseInt(value);
                    ps.setInt(index, x);
                    break;
                }
            case TEXT:
                {
                    ps.setString(index, value);
                    break;
                }
            case TIME:
                {
                    break;
                }
            case TIMESTAMP:
                {
                    break;
                }
            case TINYBLOB:
                {
                    break;
                }
            case TINYINT:
                {
                    break;
                }
            case TINYTEXT:
                {
                    ps.setString(index, value);
                    break;
                }
            case VARBINARY:
                {
                    break;
                }
            case VARCHAR:
                {
                    ps.setString(index, value);
                    break;
                }
            case YEAR:
                {
                    break;
                }
            default:
                {
                    ps.setString(index, value);
                    break;
                }
        }
    }
}

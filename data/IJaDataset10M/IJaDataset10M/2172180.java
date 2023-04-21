package jdbchelper;

import java.math.BigDecimal;
import java.sql.*;

/**
 * Author: Erdinc YILMAZEL
 * Date: Jan 30, 2009
 * Time: 4:15:47 PM
 */
public class QueryResult {

    private Connection con;

    private Statement stmt;

    private ResultSet result;

    private JdbcHelper jdbc;

    private Object[] params;

    private String sql;

    private boolean closed;

    private boolean queried;

    private boolean autoClose;

    QueryResult(JdbcHelper jdbc, String sql, Object[] params) {
        this.jdbc = jdbc;
        this.params = params;
        this.sql = sql;
        try {
            con = jdbc.getConnection();
            if (params.length == 0) {
                stmt = con.createStatement();
            } else {
                stmt = jdbc.fillStatement(con.prepareStatement(sql), params);
            }
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e, sql);
            }
            throw new JdbcException("Error running query:\n" + sql + "\n\nError: " + e.getMessage(), e);
        }
    }

    public void setAutoClose(boolean a) {
        autoClose = a;
    }

    public void setFetchSize(int rows) {
        try {
            stmt.setFetchSize(rows);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public void setMaxRows(int rows) {
        try {
            stmt.setMaxRows(rows);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public void setTimeOut(int t) {
        try {
            stmt.setQueryTimeout(t);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    private void query() {
        try {
            if (params.length == 0) {
                result = stmt.executeQuery(sql);
            } else {
                result = ((PreparedStatement) stmt).executeQuery();
            }
            queried = true;
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException("Error running query:\n" + sql + "\n\nError: " + e.getMessage(), e);
        }
    }

    public boolean next() {
        try {
            if (!queried) {
                query();
            }
            boolean next = result.next();
            if (!next && autoClose) {
                close();
            }
            return next;
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public void close() {
        if (!closed) {
            JdbcUtil.close(stmt, result);
            jdbc.freeConnection(con);
            closed = true;
        }
    }

    public boolean isBeforeFirst() {
        try {
            return result.isBeforeFirst();
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public boolean isAfterLast() {
        try {
            return result.isAfterLast();
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public boolean isFirst() {
        try {
            return result.isFirst();
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public boolean isLast() {
        try {
            return result.isLast();
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public void beforeFirst() {
        try {
            result.beforeFirst();
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public void afterLast() {
        try {
            result.afterLast();
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public boolean first() {
        try {
            return result.first();
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public boolean last() {
        try {
            return result.last();
        } catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    public int getRow() {
        try {
            return result.getRow();
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public boolean absolute(int row) {
        try {
            return result.absolute(row);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public boolean relative(int rows) {
        try {
            return result.relative(rows);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public boolean previous() {
        try {
            return result.previous();
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public String getString(int columnIndex) {
        try {
            return result.getString(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public boolean getBoolean(int columnIndex) {
        try {
            return result.getBoolean(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public byte getByte(int columnIndex) {
        try {
            return result.getByte(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public short getShort(int columnIndex) {
        try {
            return result.getShort(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public int getInt(int columnIndex) {
        try {
            return result.getInt(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public long getLong(int columnIndex) {
        try {
            return result.getLong(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public float getFloat(int columnIndex) {
        try {
            return result.getFloat(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public double getDouble(int columnIndex) {
        try {
            return result.getDouble(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public BigDecimal getBigDecimal(int columnIndex) {
        try {
            return result.getBigDecimal(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public byte[] getBytes(int columnIndex) {
        try {
            return result.getBytes(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public java.sql.Date getDate(int columnIndex) {
        try {
            return result.getDate(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public java.sql.Time getTime(int columnIndex) {
        try {
            return result.getTime(columnIndex);
        } catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    public java.sql.Timestamp getTimestamp(int columnIndex) {
        try {
            return result.getTimestamp(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public java.io.InputStream getAsciiStream(int columnIndex) {
        try {
            return result.getAsciiStream(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public java.io.InputStream getBinaryStream(int columnIndex) {
        try {
            return result.getBinaryStream(columnIndex);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public String getString(String columnLabel) {
        try {
            return result.getString(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public boolean getBoolean(String columnLabel) {
        try {
            return result.getBoolean(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public byte getByte(String columnLabel) {
        try {
            return result.getByte(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public short getShort(String columnLabel) {
        try {
            return result.getShort(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public int getInt(String columnLabel) {
        try {
            return result.getInt(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public long getLong(String columnLabel) {
        try {
            return result.getLong(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public float getFloat(String columnLabel) {
        try {
            return result.getFloat(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public double getDouble(String columnLabel) {
        try {
            return result.getDouble(columnLabel);
        } catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    public BigDecimal getBigDecimal(String columnLabel) {
        try {
            return result.getBigDecimal(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public byte[] getBytes(String columnLabel) {
        try {
            return result.getBytes(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public java.sql.Date getDate(String columnLabel) {
        try {
            return result.getDate(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public java.sql.Time getTime(String columnLabel) {
        try {
            return result.getTime(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public java.sql.Timestamp getTimestamp(String columnLabel) {
        try {
            return result.getTimestamp(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public java.io.InputStream getAsciiStream(String columnLabel) {
        try {
            return result.getAsciiStream(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }

    public java.io.InputStream getBinaryStream(String columnLabel) {
        try {
            return result.getBinaryStream(columnLabel);
        } catch (SQLException e) {
            if (jdbc.logger != null) {
                jdbc.logger.log(e);
            }
            throw new JdbcException(e);
        }
    }
}

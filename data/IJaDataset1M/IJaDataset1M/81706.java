package org.enerj.apache.commons.beanutils;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * <p>Mock object that implements enough of
 * <code>java.sql.ResultSetMetaData</code>
 * to exercise the {@link ResultSetDyaClass} functionality.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.4 $ $Date: 2004/02/28 13:18:36 $
 */
public class TestResultSetMetaData implements ResultSetMetaData {

    /**
     * <p>Array of column names and class names for metadata.</p>
     */
    protected String metadata[][] = { { "bigDecimalProperty", "java.math.BigDecimal" }, { "booleanProperty", Boolean.class.getName() }, { "byteProperty", Byte.class.getName() }, { "dateProperty", "java.sql.Date" }, { "doubleProperty", Double.class.getName() }, { "floatProperty", Float.class.getName() }, { "intProperty", Integer.class.getName() }, { "longProperty", Long.class.getName() }, { "nullProperty", "java.lang.String" }, { "shortProperty", Short.class.getName() }, { "stringProperty", "java.lang.String" }, { "timeProperty", "java.sql.Time" }, { "timestampProperty", "java.sql.Timestamp" } };

    public String getColumnClassName(int columnIndex) throws SQLException {
        return (metadata[columnIndex - 1][1]);
    }

    public int getColumnCount() throws SQLException {
        return (metadata.length);
    }

    public String getColumnName(int columnIndex) throws SQLException {
        return (metadata[columnIndex - 1][0]);
    }

    public String getCatalogName(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getColumnDisplaySize(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getColumnLabel(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getColumnType(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getColumnTypeName(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getPrecision(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getScale(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getSchemaName(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getTableName(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isAutoIncrement(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isCaseSensitive(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isCurrency(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isDefinitelyWritable(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int isNullable(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isReadOnly(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isSearchable(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isSigned(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isWritable(int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /** 
     * {@inheritDoc}
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     */
    public boolean isWrapperFor(Class<?> someIface) throws SQLException {
        return false;
    }

    /** 
     * {@inheritDoc}
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     */
    public <T> T unwrap(Class<T> someIface) throws SQLException {
        return null;
    }
}

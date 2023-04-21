package zanderredux.jdbc;

import java.util.Vector;
import zanderredux.jdbc.layout.ResultSetField;

/**
 *
 * @author alex
 */
public class ResultSetMetaData implements java.sql.ResultSetMetaData {

    Vector<ResultSetField> fields;

    /** Creates a new instance of ResultSetMetaData */
    public ResultSetMetaData(Vector<ResultSetField> fields) {
        this.fields = fields;
    }

    public String getCatalogName(int column) throws java.sql.SQLException {
        return "";
    }

    public String getColumnClassName(int column) throws java.sql.SQLException {
        switch(((ResultSetField) fields.get(column - 1)).getType()) {
            case ALPHA:
                return java.lang.String.class.getCanonicalName();
            case INT:
                return java.lang.Integer.class.getCanonicalName();
            case FLOAT:
                return java.lang.Double.class.getCanonicalName();
            default:
                return java.lang.String.class.getCanonicalName();
        }
    }

    public int getColumnCount() throws java.sql.SQLException {
        return fields.size();
    }

    public int getColumnDisplaySize(int column) throws java.sql.SQLException {
        return ((ResultSetField) fields.get(column - 1)).getLength();
    }

    public String getColumnLabel(int column) throws java.sql.SQLException {
        return ((ResultSetField) fields.get(column - 1)).getName();
    }

    public String getColumnName(int column) throws java.sql.SQLException {
        return ((ResultSetField) fields.get(column - 1)).getName();
    }

    public int getColumnType(int column) throws java.sql.SQLException {
        switch(((ResultSetField) fields.get(column - 1)).getType()) {
            case ALPHA:
                return java.sql.Types.VARCHAR;
            case INT:
                return java.sql.Types.BIGINT;
            case FLOAT:
                return java.sql.Types.DOUBLE;
            default:
                return java.sql.Types.VARCHAR;
        }
    }

    public String getColumnTypeName(int column) throws java.sql.SQLException {
        switch(((ResultSetField) fields.get(column - 1)).getType()) {
            case ALPHA:
                return "ALPHA";
            case INT:
                return "INT";
            case FLOAT:
                return "FLOAT";
            default:
                return "ALPHA";
        }
    }

    public int getPrecision(int column) throws java.sql.SQLException {
        return ((ResultSetField) fields.get(column - 1)).getPrecision();
    }

    public int getScale(int column) throws java.sql.SQLException {
        return getPrecision(column);
    }

    public String getSchemaName(int column) throws java.sql.SQLException {
        return "";
    }

    public String getTableName(int column) throws java.sql.SQLException {
        return "";
    }

    public boolean isAutoIncrement(int column) throws java.sql.SQLException {
        return false;
    }

    public boolean isCaseSensitive(int column) throws java.sql.SQLException {
        return true;
    }

    public boolean isCurrency(int column) throws java.sql.SQLException {
        return false;
    }

    public boolean isDefinitelyWritable(int column) throws java.sql.SQLException {
        return false;
    }

    public int isNullable(int column) throws java.sql.SQLException {
        return java.sql.ResultSetMetaData.columnNullableUnknown;
    }

    public boolean isReadOnly(int column) throws java.sql.SQLException {
        return true;
    }

    public boolean isSearchable(int column) throws java.sql.SQLException {
        return false;
    }

    public boolean isSigned(int column) throws java.sql.SQLException {
        return ((ResultSetField) fields.get(column - 1)).getType() == ResultSetField.Type.ALPHA ? false : true;
    }

    public boolean isWritable(int column) throws java.sql.SQLException {
        return false;
    }
}

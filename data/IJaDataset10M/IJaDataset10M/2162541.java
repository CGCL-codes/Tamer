package net.pmonks.DAL.generator.schema;

import java.util.*;
import java.security.*;
import java.sql.*;
import javax.sql.*;
import org.apache.log4j.*;
import org.apache.log4j.xml.*;
import net.pmonks.DAL.generator.config.*;

public class ColumnDefinition {

    /**
     * Log4J category used for logging by the class.
     */
    static Category cat = Category.getInstance(ColumnDefinition.class.getName());

    protected final String DB_CONNECTION_NAME = "CONNECTION_2";

    protected String tableOrViewName = null;

    protected String columnName = null;

    protected boolean readOnly = false;

    protected boolean ignored = false;

    protected int columnType;

    protected int columnSize;

    protected int decimalDigits;

    protected int charOctetLength;

    protected int columnNullable;

    public ColumnDefinition(DBConnectionManager dbConMgr, String tableOrViewName, String columnName, ColumnInfo column) throws Exception, InvalidParameterException, SQLException {
        Connection con = dbConMgr.getConnection(DB_CONNECTION_NAME);
        ResultSet rs = null;
        DatabaseMetaData dmd = null;
        try {
            if (tableOrViewName == null || columnName == null || tableOrViewName.length() <= 0 || columnName.length() <= 0) {
                String msg = "ColumnDefinition constructor was passed a null or empty parameter.";
                cat.error(msg);
                throw new InvalidParameterException(msg);
            }
            this.tableOrViewName = tableOrViewName;
            this.columnName = columnName;
            dmd = con.getMetaData();
            rs = dmd.getColumns(null, null, tableOrViewName, columnName);
            if (!rs.next()) {
                String msg = "No metadata returned for column '" + columnName + "' of table/view '" + tableOrViewName + "'!";
                cat.error(msg);
                throw new Exception(msg);
            }
            columnType = rs.getShort("DATA_TYPE");
            columnSize = rs.getInt("COLUMN_SIZE");
            decimalDigits = rs.getInt("DECIMAL_DIGITS");
            columnNullable = rs.getInt("NULLABLE");
            charOctetLength = rs.getInt("CHAR_OCTET_LENGTH");
            readOnly = false;
            ignored = false;
            if (column != null) {
                if (column.getColumnType() != null) {
                    columnType = column.getColumnType().intValue();
                }
                if (column.getColumnSize() != null) {
                    columnSize = column.getColumnSize().intValue();
                }
                if (column.getDecimalDigits() != null) {
                    decimalDigits = column.getDecimalDigits().intValue();
                }
                if (column.getColumnNullable() != null) {
                    if (column.getColumnNullable().booleanValue()) {
                        columnNullable = DatabaseMetaData.columnNullable;
                    } else {
                        columnNullable = DatabaseMetaData.columnNoNulls;
                    }
                }
                if (column.getCharOctetLength() != null) {
                    charOctetLength = column.getCharOctetLength().intValue();
                }
                if (column.getReadOnlyFlag() != null) {
                    readOnly = column.getReadOnlyFlag().booleanValue();
                }
                if (column.getIgnoredFlag() != null) {
                    ignored = column.getIgnoredFlag().booleanValue();
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        }
    }

    public boolean isReadOnly() {
        return (readOnly);
    }

    public boolean shouldBeIgnored() {
        return (ignored);
    }

    public String getTableOrViewName() {
        return (tableOrViewName);
    }

    public String getColumnName() {
        return (columnName);
    }

    public int getColumnType() {
        return (columnType);
    }

    public int getColumnSize() {
        return (columnSize);
    }

    public int getNumberOfDecimalDigits() {
        return (decimalDigits);
    }

    public int getNumberOfBytes() {
        return (charOctetLength);
    }

    public int getColumnNullability() {
        return (columnNullable);
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof ColumnDefinition) {
            ColumnDefinition other = (ColumnDefinition) obj;
            if ((this.tableOrViewName.equals(other.tableOrViewName)) && (this.columnName.equals(other.columnName))) {
                result = true;
            }
        }
        return (result);
    }

    public static boolean isColumnTypeStandard(int columnType) {
        boolean result = false;
        if (columnType == Types.ARRAY || columnType == Types.BIGINT || columnType == Types.BINARY || columnType == Types.BIT || columnType == Types.BLOB || columnType == Types.CHAR || columnType == Types.CLOB || columnType == Types.DATE || columnType == Types.DECIMAL || columnType == Types.DISTINCT || columnType == Types.DOUBLE || columnType == Types.FLOAT || columnType == Types.INTEGER || columnType == Types.JAVA_OBJECT || columnType == Types.LONGVARBINARY || columnType == Types.LONGVARCHAR || columnType == Types.NULL || columnType == Types.NUMERIC || columnType == Types.OTHER || columnType == Types.REAL || columnType == Types.REF || columnType == Types.SMALLINT || columnType == Types.STRUCT || columnType == Types.TIME || columnType == Types.TIMESTAMP || columnType == Types.TINYINT || columnType == Types.VARBINARY || columnType == Types.VARCHAR) {
            result = true;
        }
        return (result);
    }

    public static String columnTypeToString(int columnType) throws InvalidParameterException {
        String result = null;
        switch(columnType) {
            case Types.ARRAY:
                result = "Array";
                break;
            case Types.BIGINT:
                result = "BigInt";
                break;
            case Types.BINARY:
                result = "Binary";
                break;
            case Types.BIT:
                result = "Bit";
                break;
            case Types.BLOB:
                result = "BLOB";
                break;
            case Types.CHAR:
                result = "Char";
                break;
            case Types.CLOB:
                result = "CLOB";
                break;
            case Types.DATE:
                result = "Date";
                break;
            case Types.DECIMAL:
                result = "Decimal";
                break;
            case Types.DISTINCT:
                result = "Distinct";
                break;
            case Types.DOUBLE:
                result = "Double";
                break;
            case Types.FLOAT:
                result = "Float";
                break;
            case Types.INTEGER:
                result = "Integer";
                break;
            case Types.JAVA_OBJECT:
                result = "JavaObject";
                break;
            case Types.LONGVARBINARY:
                result = "LongVarBinary";
                break;
            case Types.LONGVARCHAR:
                result = "LongVarChar";
                break;
            case Types.NULL:
                result = "Null";
                break;
            case Types.NUMERIC:
                result = "Numeric";
                break;
            case Types.OTHER:
                result = "Other";
                break;
            case Types.REAL:
                result = "Real";
                break;
            case Types.REF:
                result = "Ref";
                break;
            case Types.SMALLINT:
                result = "SmallInt";
                break;
            case Types.STRUCT:
                result = "Struct";
                break;
            case Types.TIME:
                result = "Time";
                break;
            case Types.TIMESTAMP:
                result = "TimeStamp";
                break;
            case Types.TINYINT:
                result = "TinyInt";
                break;
            case Types.VARBINARY:
                result = "VarBinary";
                break;
            case Types.VARCHAR:
                result = "VarChar";
                break;
            default:
                result = "UNKNOWN (typeid=" + columnType + ")";
                break;
        }
        return (result);
    }
}

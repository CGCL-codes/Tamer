package org.jtools.sql.values;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ParameterMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import org.jpattern.io.Input;
import org.jpattern.io.Output;
import org.jtools.sql.SQLType;

public final class ValueMetaData {

    private static final short MASK_AUTOINCREMENT = (short) 0x0001;

    private static final short MASK_CASESENSITIVE = (short) 0x0002;

    private static final short MASK_CURRENCY = (short) 0x0004;

    private static final short MASK_DEFINITELYWRITABLE = (short) 0x0008;

    private static final short MASK_READONLY = (short) 0x0010;

    private static final short MASK_SEARCHABLE = (short) 0x0020;

    private static final short MASK_SIGNED = (short) 0x0040;

    private static final short MASK_WRITABLE = (short) 0x0080;

    private static final short MASK_COLUMNNONULLS = (short) 0x0100;

    private static final short MASK_COLUMNNULLABLE = (short) 0x0200;

    private static final short MASK_NULLABLE = MASK_COLUMNNONULLS | MASK_COLUMNNULLABLE;

    private static final short MASK_MODE_IN = (short) 0x0400;

    private static final short MASK_MODE_OUT = (short) 0x0800;

    private static final short MASK_MODE_INOUT = MASK_MODE_IN | MASK_MODE_OUT;

    private static final short MASK_MODE = MASK_MODE_IN | MASK_MODE_OUT;

    private final String catalogName;

    private final String columnClassName;

    private final String columnLabel;

    private final String columnName;

    private final String columnTypeName;

    private final String schemaName;

    private final String tableName;

    private final int columnDisplaySize;

    private final int columnType;

    private final int precision;

    private final int scale;

    private final short flags;

    private final SQLType sqlType;

    public ValueMetaData(ResultSetMetaData md, int column) throws SQLException {
        catalogName = md.getCatalogName(column);
        columnClassName = md.getColumnClassName(column);
        columnLabel = md.getColumnLabel(column);
        columnName = md.getColumnName(column);
        columnTypeName = md.getColumnTypeName(column);
        schemaName = md.getSchemaName(column);
        tableName = md.getTableName(column);
        columnDisplaySize = md.getColumnDisplaySize(column);
        columnType = md.getColumnType(column);
        precision = md.getPrecision(column);
        scale = md.getScale(column);
        short flags = 0;
        if (md.isAutoIncrement(column)) flags |= MASK_AUTOINCREMENT;
        if (md.isCaseSensitive(column)) flags |= MASK_CASESENSITIVE;
        if (md.isCurrency(column)) flags |= MASK_CURRENCY;
        if (md.isDefinitelyWritable(column)) flags |= MASK_DEFINITELYWRITABLE;
        if (md.isReadOnly(column)) flags |= MASK_READONLY;
        if (md.isSearchable(column)) flags |= MASK_SEARCHABLE;
        if (md.isSigned(column)) flags |= MASK_SIGNED;
        if (md.isWritable(column)) flags |= MASK_WRITABLE;
        switch(md.isNullable(column)) {
            case ResultSetMetaData.columnNoNulls:
                flags |= MASK_COLUMNNONULLS;
                break;
            case ResultSetMetaData.columnNullable:
                flags |= MASK_COLUMNNULLABLE;
                break;
        }
        this.flags = flags;
        sqlType = SQLType.valueOf(columnType);
    }

    public ValueMetaData(ParameterMetaData md, int column) throws SQLException {
        catalogName = null;
        columnClassName = md.getParameterClassName(column);
        columnLabel = null;
        columnName = null;
        columnTypeName = md.getParameterTypeName(column);
        schemaName = null;
        tableName = null;
        columnDisplaySize = 0;
        columnType = md.getParameterType(column);
        precision = md.getPrecision(column);
        scale = md.getScale(column);
        short flags = 0;
        if (md.isSigned(column)) flags |= MASK_SIGNED;
        switch(md.isNullable(column)) {
            case ParameterMetaData.parameterNoNulls:
                flags |= MASK_COLUMNNONULLS;
                break;
            case ParameterMetaData.parameterNullable:
                flags |= MASK_COLUMNNULLABLE;
                break;
        }
        switch(md.getParameterMode(column)) {
            case ParameterMetaData.parameterModeIn:
                flags |= MASK_MODE_IN;
                break;
            case ParameterMetaData.parameterModeInOut:
                flags |= MASK_MODE_INOUT;
                break;
            case ParameterMetaData.parameterModeOut:
                flags |= MASK_MODE_OUT;
                break;
        }
        this.flags = flags;
        sqlType = SQLType.valueOf(columnType);
    }

    public ValueMetaData(Input input, boolean resultset) throws IOException {
        columnClassName = input.getString();
        columnTypeName = input.getString();
        columnType = input.getInt();
        precision = input.getInt();
        scale = input.getInt();
        flags = input.getShort();
        sqlType = SQLType.valueOf(columnType);
        catalogName = resultset ? input.getString() : null;
        columnLabel = resultset ? input.getString() : null;
        columnName = resultset ? input.getString() : null;
        schemaName = resultset ? input.getString() : null;
        tableName = resultset ? input.getString() : null;
        columnDisplaySize = resultset ? input.getInt() : 0;
    }

    public void write(Output out, boolean resultset) throws IOException {
        out.set(columnClassName);
        out.set(columnTypeName);
        out.set(columnType);
        out.set(precision);
        out.set(scale);
        out.set(flags);
        if (resultset) {
            out.set(catalogName);
            out.set(columnLabel);
            out.set(columnName);
            out.set(schemaName);
            out.set(tableName);
            out.set(columnDisplaySize);
        }
    }

    public String getCatalogName() {
        return catalogName;
    }

    public String getColumnClassName() {
        return columnClassName;
    }

    public int getColumnDisplaySize() {
        return columnDisplaySize;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getColumnType() {
        return columnType;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public int getPrecision() {
        return precision;
    }

    public int getScale() {
        return scale;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    private boolean flag(short mask) {
        return (flags & mask) == mask;
    }

    public boolean isAutoIncrement(int column) {
        return flag(MASK_AUTOINCREMENT);
    }

    public boolean isCaseSensitive() {
        return flag(MASK_CASESENSITIVE);
    }

    public boolean isCurrency() {
        return flag(MASK_CURRENCY);
    }

    public boolean isDefinitelyWritable() {
        return flag(MASK_DEFINITELYWRITABLE);
    }

    public int isNullable() {
        switch(flags & MASK_NULLABLE) {
            case MASK_COLUMNNONULLS:
                return ResultSetMetaData.columnNoNulls;
            case MASK_COLUMNNULLABLE:
                return ResultSetMetaData.columnNullable;
        }
        return ResultSetMetaData.columnNullableUnknown;
    }

    public int getParameterMode() {
        switch(flags & MASK_MODE) {
            case MASK_MODE_IN:
                return ParameterMetaData.parameterModeIn;
            case MASK_MODE_INOUT:
                return ParameterMetaData.parameterModeInOut;
            case MASK_MODE_OUT:
                return ParameterMetaData.parameterModeOut;
        }
        return ParameterMetaData.parameterNullableUnknown;
    }

    public boolean isReadOnly() {
        return flag(MASK_READONLY);
    }

    public boolean isSearchable() {
        return flag(MASK_SEARCHABLE);
    }

    public boolean isSigned() {
        return flag(MASK_SIGNED);
    }

    public boolean isWritable() {
        return flag(MASK_WRITABLE);
    }

    public SQLType getSQLType() {
        return sqlType;
    }

    void registerOutParameter(CallableStatement stmt, int parameterIndex) throws SQLException {
        if (isOutParameter()) {
            switch(columnType) {
                case Types.NUMERIC:
                case Types.DECIMAL:
                    stmt.registerOutParameter(parameterIndex, columnType, scale);
                    break;
                case Types.DISTINCT:
                case Types.JAVA_OBJECT:
                case Types.OTHER:
                case Types.REF:
                case Types.STRUCT:
                    stmt.registerOutParameter(parameterIndex, columnType, columnTypeName);
                    break;
                default:
                    stmt.registerOutParameter(parameterIndex, columnType);
            }
        }
    }

    boolean isOutParameter() {
        return (flags & MASK_MODE_OUT) == MASK_MODE_OUT;
    }
}

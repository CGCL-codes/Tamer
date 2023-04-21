package org.datascooter.db.mssql;

import java.text.MessageFormat;
import org.datascooter.bundle.ReferenceAttribute;
import org.datascooter.bundle.SimpleAttribute;
import org.datascooter.exception.BuildClauseException;
import org.datascooter.exception.EntityNotMappedException;
import org.datascooter.impl.SnipBuilder;
import org.datascooter.inface.ISnipBuilder;
import org.datascooter.meta.Column;
import org.datascooter.utils.DBType;
import org.datascooter.utils.SnipUtils;

public class MSSQLSnipBuilder extends SnipBuilder {

    public MSSQLSnipBuilder() {
        super("mssql");
    }

    @Override
    public ISnipBuilder copy() {
        return new MSSQLSnipBuilder();
    }

    @Override
    protected String buildDelete() throws EntityNotMappedException, BuildClauseException {
        buildWhereClause(dataSnip);
        return MessageFormat.format(DELETE_FROM, getCaseSensitiveTableName(bundle), SnipUtils.NN, where.toString());
    }

    @SuppressWarnings("nls")
    @Override
    protected String buildColumnClause(SimpleAttribute attribute) {
        DBType type;
        String typeStr = "";
        String size = "";
        if (attribute instanceof ReferenceAttribute) {
            type = bundle.getId().type;
            size = buildDataLength(bundle.getId().scale, bundle.getId().precision);
        } else {
            type = attribute.type;
            size = buildDataLength(attribute.scale, attribute.precision);
        }
        switch(type) {
            case CHAR:
                typeStr = SnipUtils.CHAR;
                if (size.length() == 0) {
                    size = "(255)";
                }
                break;
            case TIMESTAMP:
                typeStr = "DATETIME";
                size = "";
                break;
            case BIGDECIMAL:
                typeStr = "DECIMAL";
                size = "(22,2)";
                break;
            case LONG:
                typeStr = "BIGINT";
                size = "";
                break;
            case BOOLEAN:
                typeStr = "BIT";
                size = "";
                break;
            case INT:
                typeStr = "INT";
                size = "";
                break;
            case STRING:
                typeStr = "VARCHAR";
                if (size.length() == 0) {
                    size = "(512)";
                }
                break;
            case BLOB:
                typeStr = "IMAGE";
                size = "";
                break;
            case CLOB:
                typeStr = "NTEXT";
                size = "";
                break;
        }
        return SnipUtils.SP + attribute.column + SnipUtils.SP + typeStr + SnipUtils.SP + size + (attribute.nullable ? SnipUtils.NN : SnipUtils.NOT_NULL);
    }

    @Override
    public boolean equalType(SimpleAttribute attribute, Column column) {
        boolean result = false;
        boolean scaled = true;
        switch(attribute.type) {
            case BIGDECIMAL:
                result = "DECIMAL".equalsIgnoreCase(column.dataTypeName);
                break;
            case LONG:
                result = "BIGINT".equalsIgnoreCase(column.dataTypeName);
                scaled = false;
                break;
            case BOOLEAN:
                result = "bit".equalsIgnoreCase(column.dataTypeName);
                break;
            case INT:
                result = "int".equalsIgnoreCase(column.dataTypeName);
                scaled = false;
                break;
            case STRING:
                result = "VARCHAR".equalsIgnoreCase(column.dataTypeName);
                break;
            case CHAR:
                result = "CHAR".equalsIgnoreCase(column.dataTypeName);
                break;
            case BLOB:
                result = "BLOB".equalsIgnoreCase(column.dataTypeName);
                break;
            case CLOB:
                result = "CLOB".equalsIgnoreCase(column.dataTypeName);
                break;
            case TIMESTAMP:
                result = "datetime".equalsIgnoreCase(column.dataTypeName);
                scaled = false;
                break;
        }
        if (scaled) {
            if (result && (attribute.scale != null)) {
                if (column.scale == null) {
                    result = true;
                } else {
                    result = column.scale.equals(attribute.scale + "");
                }
            } else if (result && (attribute.precision != null)) {
                result = attribute.precision.equals(column.precision);
            }
        }
        if (result && (attribute.nullable != null)) {
            result = attribute.nullable.equals(column.isNullable.equalsIgnoreCase("1"));
        }
        if (!result) {
            logSituation(attribute, column);
        }
        return result;
    }
}

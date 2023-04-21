package com.j256.ormlite.stmt.query;

import java.sql.SQLException;
import com.j256.ormlite.field.FieldType;

/**
 * Internal class handling a simple comparison query part where the operation is passed in.
 * 
 * @author graywatson
 */
public class SimpleComparison extends BaseComparison {

    public static final String EQUAL_TO_OPERATION = "=";

    public static final String GREATER_THAN_OPERATION = ">";

    public static final String GREATER_THAN_EQUAL_TO_OPERATION = ">=";

    public static final String LESS_THAN_OPERATION = "<";

    public static final String LESS_THAN_EQUAL_TO_OPERATION = "<=";

    public static final String LIKE_OPERATION = "LIKE";

    public static final String NOT_EQUAL_TO_OPERATION = "<>";

    private final String operation;

    public SimpleComparison(String columnName, FieldType fieldType, Object value, String operation) throws SQLException {
        super(columnName, fieldType, value, true);
        this.operation = operation;
    }

    @Override
    public void appendOperation(StringBuilder sb) {
        sb.append(operation);
        sb.append(' ');
    }
}

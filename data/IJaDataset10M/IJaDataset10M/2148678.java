package com.germinus.xpression.cms.hibernate;

/**
 * This class represents a query criteria.
 * @author  Acheca
 */
public class HQLCondition {

    public static final String AND = "and";

    public static final String OR = "or";

    public static final String IS = "is";

    public static final String IS_NOT = "is not";

    public static final String LIKE = "like";

    public static final String EQUALS = "=";

    public static final String GREATER = ">";

    public static final String MINOR = "<";

    public static final String GREATER_OR_EQUAL = ">=";

    public static final String MINOR_OR_EQUAL = "<=";

    public static final String COLLECTION = "in elements";

    private static final String operations = "and,or,is,is not,like,=,>,<,>=,<=,in elements";

    private String field;

    private Object value;

    private String operation;

    public HQLCondition(String field, Object value, String operation) {
        checkCondition(field, value.toString(), operation);
        this.field = field;
        this.value = value;
        this.operation = operation;
    }

    private void checkCondition(String field, String value, String operation) {
        if (field == null || operation == null) {
            throw new IllegalArgumentException("Null field not allowed");
        }
        if ((value == null) && (operation != IS) && (operation != IS_NOT)) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (operations.indexOf(operation) == -1) {
            throw new IllegalArgumentException("Operation not allowed: " + operation);
        }
    }

    public String toString() {
        String cond;
        if (operation.equals(COLLECTION)) {
            cond = ":" + this.field;
            cond += " " + getOperation();
            cond += "(items." + this.field + ")";
        } else {
            cond = "items." + this.field;
            cond += " " + this.operation + " ";
            cond += ":" + this.field;
        }
        return cond;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    public String getOperation() {
        return operation;
    }
}

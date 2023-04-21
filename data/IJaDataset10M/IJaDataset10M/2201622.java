package com.opensymphony.workflow.query;

import java.io.Serializable;

/**
 * @deprecated use {@link WorkflowExpressionQuery} instead
 * @author Hani
 */
public class WorkflowQuery implements Serializable {

    private static final long serialVersionUID = 8130933224983412376L;

    public static final int EQUALS = 1;

    public static final int LT = 2;

    public static final int GT = 3;

    public static final int BETWEEN = 4;

    public static final int NOT_EQUALS = 5;

    public static final int AND = 6;

    public static final int OR = 7;

    public static final int XOR = 8;

    public static final int OWNER = 1;

    public static final int START_DATE = 2;

    public static final int FINISH_DATE = 3;

    public static final int ACTION = 4;

    public static final int STEP = 5;

    public static final int CALLER = 6;

    public static final int STATUS = 7;

    public static final int HISTORY = 1;

    public static final int CURRENT = 2;

    private Object value;

    private WorkflowQuery left;

    private WorkflowQuery right;

    private int field;

    private int operator;

    private int type;

    public WorkflowQuery() {
    }

    public WorkflowQuery(WorkflowQuery left, int operator, WorkflowQuery right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public WorkflowQuery(int field, int type, int operator, Object value) {
        this.type = type;
        this.operator = operator;
        this.field = field;
        this.value = value;
    }

    public int getField() {
        return field;
    }

    public WorkflowQuery getLeft() {
        return left;
    }

    public int getOperator() {
        return operator;
    }

    public WorkflowQuery getRight() {
        return right;
    }

    public int getType() {
        int qtype = type;
        if (qtype == 0) {
            if (getLeft() != null) {
                qtype = getLeft().getType();
            }
        }
        return qtype;
    }

    public Object getValue() {
        return value;
    }
}

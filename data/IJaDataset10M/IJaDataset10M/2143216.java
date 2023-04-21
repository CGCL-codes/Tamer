package com.netx.bl.R1.core;

public class OrderBy {

    public static enum ORDER {

        ASC, DESC
    }

    ;

    public final Field field;

    public final int column;

    public final ORDER order;

    public OrderBy(Field field, ORDER order) {
        this.field = field;
        this.column = -1;
        this.order = order;
    }

    public OrderBy(int column, ORDER order) {
        this.field = null;
        this.column = column;
        this.order = order;
    }

    public String toString() {
        return (field == null ? "[" + column + "]" : field.getColumnName()) + ' ' + order.toString();
    }
}

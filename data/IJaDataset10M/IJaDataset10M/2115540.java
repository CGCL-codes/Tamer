package com.thoughthole.util.hoarder.workflow.access;

public class ColumnTypes {

    public static final int INVALID = -1;

    public static final int TYPE_STRING = 1;

    public static final int TYPE_INT = 2;

    public static final int TYPE_SHORT = 3;

    public static final int TYPE_LONG = 4;

    public static final int TYPE_FLOAT = 5;

    public static final int TYPE_BOOL = 6;

    public static final int TYPE_BYTE = 7;

    public static final int TYPE_ARRAY = 8;

    public static final int TYPE_CLOB = 9;

    public static final int TYPE_BLOB = 10;

    private ColumnTypes() {
    }

    public static int getType(String type) {
        if (type.equals("java.lang.String")) return TYPE_STRING; else if (type.equals("java.lang.Integer")) return TYPE_INT; else if (type.equals("java.lang.Long")) return TYPE_LONG; else if (type.equals("java.lang.Float")) return TYPE_FLOAT; else if (type.equals("java.lang.Boolean")) return TYPE_BOOL; else if (type.equals("java.lang.Byte")) return TYPE_BYTE; else if (type.equals("java.sql.Array")) return TYPE_ARRAY; else if (type.equals("java.sql.Clob")) return TYPE_CLOB; else if (type.equals("java.sql.Blob")) return TYPE_BLOB; else return INVALID;
    }
}

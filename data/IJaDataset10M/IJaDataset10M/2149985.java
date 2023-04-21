package org.project.trunks.data;

public class FieldType {

    public static final FieldType INTEGER = new FieldType(1, "INTEGER");

    public static final FieldType DATE = new FieldType(2, "DATE");

    public static final FieldType VARCHAR = new FieldType(3, "VARCHAR");

    public static final FieldType BOOLEAN = new FieldType(4, "BOOLEAN");

    public static final FieldType NUMBER = new FieldType(5, "NUMBER");

    public static final FieldType UNKNOWN = new FieldType(6, "UNKNOWN");

    public static final FieldType CLOB = new FieldType(7, "CLOB");

    public static final FieldType BLOB = new FieldType(8, "BLOB");

    /**
   * The internal numerical representation of the alignment
   */
    private int value;

    /**
   * Used when presenting the format information
   */
    private String description;

    /**
   * Private constructor
   *
   * @param val
   * @param s the display string
   */
    public FieldType(FieldType ft) {
        value = ft.getValue();
        description = ft.getDescription();
    }

    /**
   * Private constructor
   *
   * @param val
   * @param s the display string
   */
    protected FieldType(int val, String s) {
        value = val;
        description = s;
    }

    /**
   * getFieldType
   */
    public static FieldType getFieldType(String s) {
        if (s != null) {
            if (s.equalsIgnoreCase("CLOB")) return FieldType.CLOB;
            if (s.equalsIgnoreCase("BLOB")) return FieldType.BLOB;
            if (s.equalsIgnoreCase("BOOLEAN")) return FieldType.BOOLEAN;
            if (s.equalsIgnoreCase("DATE")) return FieldType.DATE;
            if (s.equalsIgnoreCase("DATETIME")) return FieldType.DATE;
            if (s.equalsIgnoreCase("INTEGER")) return FieldType.INTEGER;
            if (s.equalsIgnoreCase("COUNTER")) return FieldType.INTEGER;
            if (s.equalsIgnoreCase("NUMBER")) return FieldType.NUMBER;
            if (s.equalsIgnoreCase("FLOAT")) return FieldType.NUMBER;
            if (s.equalsIgnoreCase("DOUBLE")) return FieldType.NUMBER;
            if (s.equalsIgnoreCase("VARCHAR")) return FieldType.VARCHAR;
            if (s.equalsIgnoreCase("VARCHAR2")) return FieldType.VARCHAR;
            if (s.equalsIgnoreCase("TEXT")) return FieldType.VARCHAR;
        }
        return FieldType.VARCHAR;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public boolean equals(FieldType al) {
        if (this.getValue() == al.getValue()) return true;
        return false;
    }
}

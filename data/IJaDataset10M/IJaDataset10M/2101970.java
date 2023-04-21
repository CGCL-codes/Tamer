package cartographer.utils;

import java.io.*;
import java.util.*;

/**
 *
 */
public class Column {

    /**
   *
   */
    private int columnType;

    /**
   *
   */
    private String columnTypeName;

    /**
   *
   */
    private String columnTypeNameFull;

    /**
   *
   */
    private String columnName;

    /**
   *
   */
    private String columnNameRaw;

    /**
   *
   */
    private String columnNameForMethod;

    /**
   *
   */
    private String columnNameForField;

    /**
   *
   */
    private boolean isNullable;

    /**
   *
   */
    private boolean isSearchable;

    /**
   * Construct a column object to represent the given columnName.
   */
    public Column(String columnPrefix, String columnName) {
        this.columnName = columnName;
        this.columnNameRaw = this.columnName;
        if (this.columnName.startsWith(columnPrefix)) {
            this.columnName = this.columnName.substring(columnPrefix.length());
        }
        StringBuffer sb = new StringBuffer();
        char[] chars = this.columnName.toCharArray();
        sb.append(Character.toUpperCase(chars[0]));
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == '_') {
                i++;
                sb.append(Character.toUpperCase(chars[i]));
            } else {
                sb.append(Character.toLowerCase(chars[i]));
            }
        }
        this.columnNameForMethod = sb.toString();
        sb = new StringBuffer();
        chars = this.columnName.toCharArray();
        sb.append(Character.toLowerCase(chars[0]));
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == '_') {
                i++;
                sb.append(Character.toUpperCase(chars[i]));
            } else {
                sb.append(Character.toLowerCase(chars[i]));
            }
        }
        this.columnNameForField = sb.toString();
    }

    /**
   *
   */
    public int getColumnType() {
        return columnType;
    }

    /**
   *
   */
    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    /**
   *
   */
    public String getColumnTypeName() {
        return columnTypeName;
    }

    /**
   *
   */
    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    /**
   *
   */
    public String getColumnTypeNameFull() {
        return columnTypeNameFull;
    }

    /**
   *
   */
    public void setColumnTypeNameFull(String columnTypeNameFull) {
        this.columnTypeNameFull = columnTypeNameFull;
    }

    /**
   *
   */
    public String getColumnName() {
        return columnName;
    }

    /**
   *
   */
    public String getColumnNameRaw() {
        return columnNameRaw;
    }

    /**
   *
   */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
   *
   */
    public String getColumnNameForMethod() {
        return columnNameForMethod;
    }

    /**
   *
   */
    public void setColumnNameForMethod(String columnNameForMethod) {
        this.columnNameForMethod = columnNameForMethod;
    }

    /**
   *
   */
    public String getColumnNameForField() {
        return columnNameForField;
    }

    /**
   *
   */
    public void setColumnNameForField(String columnNameForField) {
        this.columnNameForField = columnNameForField;
    }

    /**
   *
   */
    public boolean getIsNullable() {
        return isNullable;
    }

    /**
   *
   */
    public void setIsNullable(boolean isNullable) {
        this.isNullable = isNullable;
    }

    /**
   *
   */
    public boolean getIsSearchable() {
        return isSearchable;
    }

    /**
   *
   */
    public void setIsSearchable(boolean isSearchable) {
        this.isSearchable = isSearchable;
    }
}

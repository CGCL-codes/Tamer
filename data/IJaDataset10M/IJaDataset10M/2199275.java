package ossobook.modell;

import java.io.*;

/**
 * defines scheme of a database column
 * @author j.lamprecht
 *
 */
public class Column implements Serializable {

    private String name;

    private String type;

    private String size;

    private String defaultValue;

    private String nullable;

    private String autoincrement;

    /**
	 * definition of different column data
	 * @param name
	 * @param type
	 * @param size
	 * @param defaultValue
	 * @param nullable
	 * @param autoincrement
	 */
    public Column(String name, String type, String size, String defaultValue, String nullable, String autoincrement) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.defaultValue = defaultValue;
        this.nullable = nullable;
        this.autoincrement = autoincrement;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getNullable() {
        return nullable;
    }

    public String getAutoincrement() {
        return autoincrement;
    }

    /**
	 * compares "this" to given column
	 * @param column
	 * @return
	 */
    public boolean equals(Column column) {
        if (name.equals(column.getName()) && type.equals(column.getType()) && size.equals(column.getSize()) && nullable.equals(column.getNullable()) && autoincrement.equals(column.getAutoincrement())) {
            if (defaultValue == column.getDefaultValue() && defaultValue == null) return true; else if (defaultValue == null || column.getDefaultValue() == null) return false; else if (defaultValue.equals(column.getDefaultValue())) return true; else return false;
        } else return false;
    }
}

package se.sics.isl.db.sql;

import java.util.logging.Logger;
import se.sics.isl.db.DBField;

/**
 */
public class SQLDBField extends DBField {

    private static final Logger log = Logger.getLogger(SQLDBField.class.getName());

    public SQLDBField(String name, int type, int size, int flags, Object defaultValue) {
        super(name, type, size, flags, defaultValue);
    }

    protected void addBasicType(StringBuffer sb) {
        sb.append('`').append(name).append('`').append(' ').append(getTypeAsString(type));
        if (defaultValue != null) {
            sb.append(" DEFAULT '").append(defaultValue).append('\'');
        }
        if ((flags & MAY_BE_NULL) == 0) {
            sb.append(" NOT NULL");
        }
        if ((flags & AUTOINCREMENT) != 0) {
            sb.append(" AUTO_INCREMENT");
        }
    }

    protected void addExtraTypeInfo(StringBuffer sb) {
        if ((flags & PRIMARY) != 0) {
            sb.append(", PRIMARY KEY(`").append(name).append("`)");
        }
        if ((flags & INDEX) != 0) {
            sb.append(", INDEX(`").append(name).append("`)");
        }
        if ((flags & UNIQUE) != 0) {
            sb.append(", UNIQUE(`").append(name).append("`)");
        }
    }

    protected void addExtraTypeInfoChange(StringBuffer sb) {
        if ((flags & PRIMARY) != 0) {
            sb.append(", DROP PRIMARY KEY, ADD PRIMARY KEY(`").append(name).append("`)");
        }
        if ((flags & INDEX) != 0) {
            sb.append(", ADD INDEX(`").append(name).append("`)");
        }
        if ((flags & UNIQUE) != 0) {
            sb.append(", ADD UNIQUE(`").append(name).append("`)");
        }
    }

    private String getTypeAsString(int type) {
        switch(type) {
            case INTEGER:
                return "INT";
            case LONG:
                return "BIGINT";
            case TIMESTAMP:
                return "TIMESTAMP";
            case DOUBLE:
                return "DOUBLE";
            case STRING:
                if (size > 255) {
                    return "BLOB";
                } else {
                    return "VARCHAR(" + (size > 0 ? size : 80) + ')';
                }
            case BYTE:
                return "TINYINT";
            default:
                System.err.println("SQLDBField: unknown type: " + type);
                return "INT";
        }
    }
}

package watano.util.db;

import java.sql.Types;
import java.util.List;

public class DBField {

    public String columnName = null;

    public int columnType = Types.NULL;

    private Object value = null;

    public Object getValue() {
        return value;
    }

    public Object setValue(Object value) {
        try {
            this.value = value;
            return this.value;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public DBField(String columnName, int columnType) {
        super();
        this.columnName = columnName;
        this.columnType = columnType;
        value = null;
    }

    public String toString() {
        return this.columnName + ":" + String.valueOf(this.columnType);
    }

    public static boolean setValue(List<DBField> fields, String columnName, Object value) {
        boolean state = false;
        for (DBField field : fields) {
            if (field.columnName.equals(columnName)) {
                if (field.setValue(value) != null) {
                    state = true;
                } else {
                    state = false;
                    System.err.println("DBField.setValue error: invalid type.[columnName = " + columnName + ";columnType=" + field.columnType + ";value=" + value + "]");
                }
            }
            break;
        }
        return state;
    }
}

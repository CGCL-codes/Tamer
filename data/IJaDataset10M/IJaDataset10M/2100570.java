package de.benedetto.database;

import java.sql.*;

public class DB_IntegerField extends DB_Field {

    public DB_IntegerField(String p_name) {
        super(p_name);
    }

    public void setValue(ResultSet rs) throws SQLException {
        value = new Integer(rs.getInt(name));
    }

    public void dbAppendData(SQL_Command cmd) {
        if (value != null) cmd.append(value.toString());
    }

    public int getInt() {
        if (isNull()) return 0;
        return ((Integer) value).intValue();
    }

    public void setInt(int p_int) {
        if (value == null) {
            value = new Integer(p_int);
            changed = true;
        } else {
            if (((Integer) value).intValue() != p_int) {
                value = new Integer(p_int);
                changed = true;
            }
        }
    }
}

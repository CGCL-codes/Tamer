package net.java.ao.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import net.java.ao.EntityManager;

/**
 * @author Daniel Spiewak
 */
class BooleanType extends DatabaseType<Boolean> {

    protected BooleanType() {
        super(Types.BOOLEAN, -1, boolean.class, Boolean.class);
    }

    @Override
    public String getDefaultName() {
        return "BOOLEAN";
    }

    @Override
    public Boolean pullFromDatabase(EntityManager manager, ResultSet res, Class<? extends Boolean> type, String field) throws SQLException {
        return res.getBoolean(field);
    }

    @Override
    public Boolean defaultParseValue(String value) {
        return Boolean.parseBoolean(value.trim());
    }

    @Override
    public boolean valueEquals(Object a, Object b) {
        if (a instanceof Number) {
            if (b instanceof Boolean) {
                return (((Number) a).intValue() == 1) == ((Boolean) b).booleanValue();
            }
        } else if (a instanceof Boolean) {
            if (b instanceof Number) {
                return (((Number) b).intValue() == 1) == ((Boolean) a).booleanValue();
            }
        }
        return a.equals(b);
    }
}

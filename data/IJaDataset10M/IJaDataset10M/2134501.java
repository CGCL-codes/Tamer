package org.apache.harmony.sql.tests.javax.sql.rowset;

import java.sql.Ref;
import java.sql.SQLException;
import java.util.Map;

public class MockRef implements Ref {

    public String getBaseTypeName() throws SQLException {
        return "ref";
    }

    public Object getObject() throws SQLException {
        return null;
    }

    public Object getObject(Map<String, Class<?>> map) throws SQLException {
        return null;
    }

    public void setObject(Object value) throws SQLException {
    }
}

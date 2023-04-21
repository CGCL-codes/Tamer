package org.carp.engine.parameter;

import java.sql.PreparedStatement;

public class ArrayMapParameter implements Parameter {

    public void setValue(PreparedStatement ps, Object value, int index) throws Exception {
        java.sql.Array i = (java.sql.Array) value;
        ps.setArray(index, i);
    }
}

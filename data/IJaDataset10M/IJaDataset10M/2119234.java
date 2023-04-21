package com.manydesigns.portofino.base.cache;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
class DateParameter implements Parameter {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    private final Date value;

    public DateParameter(Date value) {
        if (value == null) throw new IllegalArgumentException("Null values not allowed");
        this.value = new Date(value.getTime());
    }

    public void set(PreparedStatement st, int pos) throws SQLException {
        st.setDate(pos, value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() != getClass()) return false;
        return value.equals(((DateParameter) o).value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

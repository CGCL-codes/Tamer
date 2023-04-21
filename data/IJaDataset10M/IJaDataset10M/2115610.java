package ru.spbu.dorms.geo.rmp.db;

import java.sql.ResultSet;
import java.sql.SQLException;

abstract class AbstractChronologicalRow extends AbstractRow implements ChronologicalRow {

    private int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int value) {
        if (id != -1) {
            throw new IllegalStateException("Current id is not -1");
        }
        this.id = value;
    }

    void loadId(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
    }
}

package net.sourceforge.stonkastorm;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomTypes extends DBTable {

    public Short TypeNo;

    public Short MaxPersons;

    public String Comfort;

    public BigDecimal Price;

    public String Description;

    RoomTypes() {
    }

    RoomTypes(ResultSet r) {
        DEBUG.devnull("RoomTypes: RoomTypes: Creating object from ResultSet");
        try {
            TypeNo = r.getShort(1);
            MaxPersons = r.getShort(2);
            Comfort = r.getString(3);
            Price = r.getBigDecimal(4);
            Description = r.getString(5);
        } catch (SQLException e) {
            DEBUG.error("RoomTypes: RoomTypes: error creating object from ResultSet");
        }
    }
}

package net.sourceforge.stonkastorm;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Reservations extends DBTable {

    public Integer ReservationNo;

    public Integer ClientNo;

    public Short RoomType;

    public Date ReservationFrom;

    public Date ReservationTo;

    public BigDecimal Advance;

    public Date AdvanceDeadline;

    public BigDecimal Price;

    public Boolean Cancelled;

    public Boolean Used;

    Reservations() {
    }

    Reservations(ResultSet r) {
        try {
            ReservationNo = r.getInt(1);
            ClientNo = r.getInt(2);
            RoomType = r.getShort(3);
            ReservationFrom = r.getDate(4);
            ReservationTo = r.getDate(5);
            Advance = r.getBigDecimal(6);
            AdvanceDeadline = r.getDate(7);
            Price = r.getBigDecimal(8);
            Cancelled = r.getBoolean(9);
            Used = r.getBoolean(10);
        } catch (SQLException e) {
            DEBUG.error("Reservations: Reservations: error creating object from ResultSet");
        }
    }
}

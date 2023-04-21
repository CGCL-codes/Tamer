package net.cygeek.tech.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;
import net.cygeek.tech.client.HsHrHolidays;
import java.util.Date;

/**
 * @author Thilina Hasantha
 */
public class Holidays implements IsSerializable {

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private int holidayId;

    public int getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(int holidayId) {
        this.holidayId = holidayId;
    }

    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private boolean recurring;

    public boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public static Holidays getProxy(HsHrHolidays h) {
        if (h == null) return null;
        Holidays c = new Holidays();
        try {
            c.setDate(String.valueOf(h.getDate().getTime()));
        } catch (Exception e) {
        }
        c.setHolidayId(h.getHolidayId());
        c.setLength(h.getLength());
        c.setDescription(h.getDescription());
        c.setRecurring(h.getRecurring());
        return c;
    }

    public static HsHrHolidays getClass(Holidays h) {
        HsHrHolidays c = new HsHrHolidays();
        try {
            try {
                c.setDate(new Date(Long.parseLong(h.getDate())));
            } catch (Exception e) {
            }
            c.setHolidayId(h.getHolidayId());
            c.setLength(h.getLength());
            c.setDescription(h.getDescription());
            c.setRecurring(h.getRecurring());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
}

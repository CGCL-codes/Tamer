package org.neodatis.odb.test.fromusers.gyowanny_queiroz;

import java.util.Date;

/**
 * @author olivier
 *
 */
public class ClassWithDate {

    private String name;

    private Date calendar;

    public ClassWithDate(String name, Date calendar) {
        super();
        this.name = name;
        this.calendar = calendar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCalendar() {
        return calendar;
    }

    public void setCalendar(Date calendar) {
        this.calendar = calendar;
    }
}

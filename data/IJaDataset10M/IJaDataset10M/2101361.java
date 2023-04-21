package test;

import java.util.Date;
import java.io.Serializable;

public class Task implements Serializable {

    private Date start;

    private Date end;

    private String name;

    public Task(Date start, Date end, String name) {
        this.start = start;
        this.end = end;
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package jamieblog;

import java.util.*;
import javax.persistence.*;

@Entity
public class Jamie {

    @Id
    private long id = System.currentTimeMillis();

    @Basic
    public String message;

    @Basic
    private Date created = new Date();

    public Jamie() {
    }

    public Jamie(String msg) {
        message = msg;
    }

    public void setId(long val) {
        id = val;
    }

    public long getId() {
        return id;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setCreated(Date date) {
        created = date;
    }

    public Date getCreated() {
        return created;
    }
}

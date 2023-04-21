package project.entities.institute;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reports", schema = "institute")
public class Reports implements Serializable {

    private int id;

    private int plan;

    private Timestamp time;

    private String type;

    public Reports(int plan, Timestamp time, String type) {
        super();
        this.plan = plan;
        this.time = time;
        this.type = type;
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "Plan", nullable = false, unique = false)
    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    @Column(name = "Time", nullable = false, unique = false)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Column(name = "Type", nullable = false, unique = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Reports [id=" + id + ", plan=" + plan + ", time=" + time + ", type=" + type + "]";
    }
}

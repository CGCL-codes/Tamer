package project.entities.institute;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "plan", schema = "institute")
public class Plan implements Serializable {

    private int id;

    private Timestamp endDate;

    private Timestamp startDate;

    public Plan(Timestamp endDate, Timestamp startDate) {
        super();
        this.endDate = endDate;
        this.startDate = startDate;
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "EndDate", nullable = false, unique = false)
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Column(name = "StartDate", nullable = false, unique = false)
    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "Plan [id=" + id + ", endDate=" + endDate + ", startDate=" + startDate + "]";
    }
}

package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Represents an employee's job title.
 */
@Entity
public class JobTitle implements Serializable {

    @Id
    @Column(name = "JOB_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Basic
    private String title;

    public JobTitle() {
    }

    public JobTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

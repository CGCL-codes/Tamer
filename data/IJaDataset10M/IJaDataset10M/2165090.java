package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;
import java.util.Date;

/** @author Hibernate CodeGenerator */
public class Timekeeping extends BaseData implements Serializable {

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private Integer person;

    /** persistent field */
    private Date start;

    /** persistent field */
    private Date finish;

    /** nullable persistent field */
    private String description;

    /** default constructor */
    public Timekeeping() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

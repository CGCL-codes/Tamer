package test.jpa.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 
 * @author dclarke
 * @since EclipseLink 1.0
 */
@Entity
public abstract class Project implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private String description;

    private Long version;

    private Employee teamLeader;

    public Project() {
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String descrip) {
        this.description = descrip;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int projId) {
        this.id = projId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String projName) {
        this.name = projName;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Employee getTeamLeader() {
        return this.teamLeader;
    }

    public void setTeamLeader(Employee employee) {
        this.teamLeader = employee;
    }
}

package org.buildng.flexmetrics.domain.project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Project {

    private int fId;

    private String fName;

    Project() {
    }

    public Project(String pName) {
        fName = pName;
    }

    @Id
    @GeneratedValue
    int getId() {
        return fId;
    }

    void setId(int pId) {
        fId = pId;
    }

    public String getName() {
        return fName;
    }

    void setName(String pName) {
        fName = pName;
    }
}

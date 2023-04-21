package org.freedom.studentunion.domain;

import java.util.Set;

public class Department {

    private int id;

    private String name;

    private Set<SchoolNews> schoolNewsSet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SchoolNews> getSchoolNewsSet() {
        return schoolNewsSet;
    }

    public void setSchoolNewsSet(Set<SchoolNews> schoolNewsSet) {
        this.schoolNewsSet = schoolNewsSet;
    }
}

package org.jpox.samples.models.company;

/**
 * Project in a company.
 */
public class Project {

    String name;

    long budget;

    public Project(String name, long budget) {
        super();
        this.name = name;
        this.budget = budget;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

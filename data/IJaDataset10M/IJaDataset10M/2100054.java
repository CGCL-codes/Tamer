package org.datanucleus.samples.jpa.query;

import javax.persistence.*;

@Entity
public class Coach {

    @Id
    long id;

    String firstName;

    String lastName;

    int yearsExperience;

    public Coach(long id, String first, String last, int yrs) {
        this.id = id;
        this.firstName = first;
        this.lastName = last;
        this.yearsExperience = yrs;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getYearsExperience() {
        return yearsExperience;
    }
}

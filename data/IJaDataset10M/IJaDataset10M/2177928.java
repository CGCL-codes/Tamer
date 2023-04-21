package oopex.eclipselink2.jpa2.usecases.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@SequenceGenerator(name = "personSequence", sequenceName = "PERSON_SEQ")
@Entity
public class Person {

    public Person() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personSequence")
    private long id;

    private String firstName;

    private String lastName;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String toString() {
        return "Person(" + id + "): " + lastName + ", " + firstName;
    }
}

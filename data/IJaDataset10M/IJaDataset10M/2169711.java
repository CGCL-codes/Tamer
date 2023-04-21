package oopex.eclipselink2.jpa2.fields.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@SequenceGenerator(name = "personSequence", sequenceName = "PERSON_SEQ")
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personSequence")
    private long id;

    private String name;

    @Column(columnDefinition = "DOUBLE")
    private double weight;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String toString() {
        return String.format("Person(%d) name: %s, weight: %f", id, name, weight);
    }
}

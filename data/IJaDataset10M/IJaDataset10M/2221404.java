package org.mariella.persistence.test.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ADRESSE")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "ART", discriminatorType = DiscriminatorType.STRING)
public abstract class Adresse extends Superclass {

    private String strasse;

    private Person person;

    @Column(name = "STREET")
    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        String oldValue = this.strasse;
        this.strasse = strasse;
        propertyChangeSupport.firePropertyChange("strasse", oldValue, strasse);
    }

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        Person oldValue = this.person;
        this.person = person;
        propertyChangeSupport.firePropertyChange("person", oldValue, person);
    }
}

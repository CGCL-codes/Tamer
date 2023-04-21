package org.mariella.persistence.test.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.mariella.persistence.runtime.TrackedList;

@Entity
@Table(name = "PERSON")
public class Person extends Superclass {

    private String name;

    private Person contactPerson;

    private Person contactPersonFor;

    private List<Adresse> adressen = new TrackedList<Adresse>(this, "adressen");

    private List<Adresse> privatAdressen = new TrackedList<Adresse>(this, "privatAdressen");

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange("name", old, name);
    }

    @OneToMany(mappedBy = "person")
    public List<Adresse> getAdressen() {
        return adressen;
    }

    @OneToMany
    @JoinTable(name = "PRIVATADRESSE", joinColumns = @JoinColumn(name = "PERSON_ID"), inverseJoinColumns = @JoinColumn(name = "ADRESSE_ID"))
    public List<Adresse> getPrivatAdressen() {
        return privatAdressen;
    }

    @OneToOne
    @JoinColumn(name = "CONTACT_PERSON_ID")
    public Person getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(Person contactPerson) {
        Person old = this.contactPerson;
        this.contactPerson = contactPerson;
        propertyChangeSupport.firePropertyChange("contactPerson", old, contactPerson);
    }

    @OneToOne(mappedBy = "contactPerson")
    public Person getContactPersonFor() {
        return contactPersonFor;
    }

    public void setContactPersonFor(Person contactPersonFor) {
        Person old = this.contactPersonFor;
        this.contactPersonFor = contactPersonFor;
        propertyChangeSupport.firePropertyChange("contactPersonFor", old, contactPersonFor);
    }
}

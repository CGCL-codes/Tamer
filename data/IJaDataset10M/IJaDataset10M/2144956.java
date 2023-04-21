package com.spring66.training.entity;

import java.util.Collection;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author TwinP
 */
@Entity
@Table(name = "VET")
public class Vet extends BaseEntity {

    private String firstName;

    private String lastName;

    private Collection<Specialty> specialties;

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the specialties
     */
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "VETS_SPECIALTIES", uniqueConstraints = @UniqueConstraint(columnNames = { "vet_id", "name" }), joinColumns = @JoinColumn(name = "vet_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "name", referencedColumnName = "id"))
    @OrderBy("name")
    public Collection<Specialty> getSpecialties() {
        if (this.specialties == null) {
            this.specialties = new HashSet<Specialty>();
        }
        return this.specialties;
    }

    /**
     * @param specialties the specialties to set
     */
    public void setSpecialties(Collection<Specialty> specialties) {
        this.specialties = specialties;
    }

    /**
     * @return the id
     */
    public void addSpecialty(Specialty specialty) {
        getSpecialties().add(specialty);
    }

    @Transient
    public int getNrOfSpecialties() {
        return getSpecialties().size();
    }
}

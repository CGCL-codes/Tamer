package com.spring66.training.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.OrderBy;

/**
 *
 * @author TwinP
 */
@Entity
@Table(name = "owners")
public class Owner extends Person {

    private String address;

    private String city;

    private String telephone;

    private Set<Pet> pets;

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return the pets
     */
    @OneToMany(mappedBy = "owner", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @OrderBy(clause = "name desc")
    @org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Set<Pet> getPets() {
        if (this.pets == null) {
            this.pets = new HashSet<Pet>();
        }
        return this.pets;
    }

    /**
     * @param pets the pets to set
     */
    public void setPets(Set<Pet> pets) {
        this.pets = pets;
    }

    public void addPet(Pet pet) {
        this.getPets().add(pet);
    }
}

package org.sucri.dao;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Wen Yu
 * Date: Aug 30, 2007
 * Time: 11:26:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(catalog = "vpp", name = "PETS")
public class PetsEntity {

    private int id;

    private String ownerid;

    private String name;

    private String species;

    private String gender;

    private String colour;

    private String fed;

    private String born;

    @Basic
    @Column(name = "born", nullable = false, length = 30)
    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    @Basic
    @Column(name = "fed", nullable = false, length = 30)
    public String getFed() {
        return fed;
    }

    public void setFed(String fed) {
        this.fed = fed;
    }

    @Basic
    @Column(name = "colour", nullable = false, length = 15)
    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Basic
    @Column(name = "gender", nullable = false, length = 15)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "species", nullable = false, length = 15)
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 15)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "ownerid", nullable = false, length = 6)
    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    @Id
    @Column(name = "id", nullable = false, length = 10)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

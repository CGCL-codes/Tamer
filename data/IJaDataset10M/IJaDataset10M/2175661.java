package org.objectwiz.testmodel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.objectwiz.model.EntityBase;

/**
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
@Entity
public class Region extends EntityBase {

    private Country country;

    private String name;

    public Region() {
    }

    public Region(Country country, String name) {
        this.country = country;
        this.name = name;
    }

    @ManyToOne
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

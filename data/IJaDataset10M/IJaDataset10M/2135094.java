package org.objectwiz.testmodel;

import javax.persistence.Entity;

/**
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
@Entity
public class Country extends org.objectwiz.model.EntityBase {

    private String name;

    private String phonePrefix;

    public Country() {
    }

    public Country(String name, String phonePrefix) {
        this.name = name;
        this.phonePrefix = phonePrefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonePrefix() {
        return phonePrefix;
    }

    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }
}

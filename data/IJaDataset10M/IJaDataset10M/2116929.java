package org.objectwiz.testmodel;

import javax.persistence.Entity;

/**
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
@Entity
public class Hobby extends org.objectwiz.model.EntityBase {

    private String name;

    private String description;

    public Hobby() {
    }

    public Hobby(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

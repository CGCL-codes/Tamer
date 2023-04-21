package model;

import javax.persistence.*;

/**
 * The SmallProject class demonstrates usage of a way to limit a subclass to its parent's table when JOINED
 * inheritance is used.  This avoids having to have an empty SMALLPROJECT table by setting the table to that
 * of the superclass.
 */
@Entity
@Table(name = "PROJECT")
@DiscriminatorValue("S")
public class SmallProject extends Project {

    private SmallProject() {
        super();
    }

    public SmallProject(String name, String description) {
        this();
        setName(name);
        setDescription(description);
    }
}

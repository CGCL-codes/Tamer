package org.developerservices.moviedb.jpa.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author rene
 *
 */
@Entity
public class City implements Serializable {

    private static final long serialVersionUID = 3990417785466391721L;

    @Id
    @GeneratedValue
    private int id;

    private String name;

    /**
	 * @return the id
	 */
    public int getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }
}

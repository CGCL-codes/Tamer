package org.developerservices.moviedb.jpa.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * @author rene
 *
 */
@Entity
public class ImageEntity implements Serializable {

    private static final long serialVersionUID = -3547630958470471911L;

    @Id
    @GeneratedValue
    private int id;

    private String filename;

    @Lob
    @Column(name = "DATA", columnDefinition = "BLOB (5M)")
    private byte[] data;

    private String name;

    private String description;

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
	 * @return the filename
	 */
    public String getFilename() {
        return filename;
    }

    /**
	 * @param filename the filename to set
	 */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
	 * @return the data
	 */
    public byte[] getData() {
        return data;
    }

    /**
	 * @param data the data to set
	 */
    public void setData(byte[] data) {
        this.data = data;
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

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }
}

package org.emmannuel.virtues.client.common;

import java.io.Serializable;

public class TOVirtue implements Serializable {

    public TOVirtue() {
    }

    public TOVirtue(Integer id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.description = desc;
    }

    private Integer id;

    private String name;

    private String description;

    /**
	 * @return the id
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @param description the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }
}

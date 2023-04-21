package com.angel.architecture.dtos;

import java.io.Serializable;
import com.angel.architecture.persistence.beans.Role;

public class UserRoleDTO implements Serializable {

    private static final long serialVersionUID = 3093580994368353068L;

    private String id;

    private String description;

    private String name;

    public UserRoleDTO() {
        super();
    }

    public UserRoleDTO(Role role) {
        this();
        this.setDescription(role.getDescription());
        this.setName(role.getName());
        this.setId(role.getIdAsString());
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(String id) {
        this.id = id;
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

package uk.ac.rothamsted.ovtk.Graph;

import java.io.Serializable;

/**
 * @author baumbac
 * 
 */
public class Mapping_Method implements Serializable {

    /**
	 * 
	 * @uml.property name="description" multiplicity="(0 1)"
	 */
    private String description;

    /**
	 * 
	 * @uml.property name="id" multiplicity="(0 1)"
	 */
    private String id;

    /**
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
    private String name;

    /**
	 *  
	 */
    public Mapping_Method() {
        super();
    }

    /**
	 * @return Returns the description.
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @return Returns the id.
	 */
    public String getId() {
        return id;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param description
	 *            The description to set.
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @param id
	 *            The id to set.
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @param name
	 *            The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }
}

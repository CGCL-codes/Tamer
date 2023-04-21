package org.escapek.core.internal.model.cmdb.definitions;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PropertyType defines types of property which can be used in CI class definitions
 * PropertyType mostly defines two attributes : 
 * <ul>
 * <li><code>javaType</code> which represent the type of the property as stored in the repository</li>
 * <li><code>dtoType</code> which represent the type of the property exchanged with clients</li>
 * <b>Note:</b> <code>dtoType</code> is optional. Is provided, a type assembler must be available to
 * convert objects to/from dto.
 * </ul>
 * @author nicolasjouanin
 *
 */
@Entity
@Table(name = "PROPERTY_TYPES")
public class PropertyType implements Serializable {

    private static final long serialVersionUID = -7274096486880362374L;

    private Long Id;

    private String typeName;

    private String displayName;

    private String description;

    private String dtoType;

    private String javaType;

    public PropertyType() {
    }

    /**
	 * Gets the Unique Id of the PropertyType
	 * @return the numeric unique ID
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TYPE_ID")
    public Long getId() {
        return Id;
    }

    /**
	 * Sets the PropertyType instance unique-ID. This method should only be used
	 * by persistence manager. 
	 * @param id
	 */
    public void setId(Long id) {
        Id = id;
    }

    /**
	 * Returns the name of this property type instance
	 * @return the link type name string
	 */
    @Column(name = "TYPE_NAME", unique = true)
    public String getTypeName() {
        return typeName;
    }

    /**
	 * Set this property type instance name
	 * @param typeName name to set as link type name
	 */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
	 * Returns a display name for this class
	 * @return display string
	 */
    @Column(name = "DISPLAY_NAME", unique = true)
    public String getDisplayName() {
        return displayName;
    }

    /**
	 * Set the display name of the CI class
	 * @param displayName name to set
	 */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
	 * Returns the description of the ci class
	 * @return description string
	 */
    @Column(name = "DESCRIPTION", length = 2000)
    public String getDescription() {
        return description;
    }

    /**
	 * Set the description of the CI class
	 * @param description name to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "DTO_TYPE")
    public String getDtoType() {
        return dtoType;
    }

    public void setDtoType(String dtoType) {
        this.dtoType = dtoType;
    }

    @Column(name = "JAVA_TYPE")
    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
}

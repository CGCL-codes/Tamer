package org.jcvi.glk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.jcvi.common.core.util.CommonUtil;

/**
 * An <code>ExtentType</code> acts as a project-specific shared type for
 * {@link Extent}s.
 *
 * @author jsitz
 * @author dkatzel
 */
@Entity
@Table(name = "Extent_Type")
public class ExtentType {

    private int id;

    private String name;

    private String description;

    /**
     * Creates a new <code>ExtentType</code>.
     *
     */
    public ExtentType() {
        super();
    }

    /**
     * Creates a new <code>ExtentType</code>.
     *
     * @param id
     * @param name
     * @param description
     */
    public ExtentType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Creates a new <code>ExtentType</code>.
     *
     * @param name
     * @param description
     */
    public ExtentType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Retrieves the id of this Extent type.
     *
     * @return an int.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Extent_Type_id", unique = true, nullable = false, insertable = true, updatable = false)
    public int getId() {
        return id;
    }

    /**
     * Sets the id of this Extent type.
     * @param id A int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the name of this type.
     *
     * @return A {@link String}
     */
    @Column(name = "type", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this type.
     * @param type A String
     */
    public void setName(String type) {
        this.name = type;
    }

    /**
     * Retrieves the description of this Extent Type.
     *
     * @return A {@link String}
     */
    @Column(name = "description", unique = false, nullable = false)
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this Extent Type.
     * @param description A String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getName() == null) ? 0 : this.getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ExtentType)) {
            return false;
        }
        final ExtentType other = (ExtentType) obj;
        return CommonUtil.similarTo(getName(), other.getName());
    }

    @Override
    public String toString() {
        return this.getName();
    }
}

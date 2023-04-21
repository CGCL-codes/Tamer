package de.sonivis.tool.core.datamodel.hib;

import java.util.Collection;
import java.util.Date;
import javax.persistence.Entity;
import de.sonivis.tool.core.datamodel.exceptions.DataModelInstantiationException;

/**
 * Represents an {@link Actor} in the SONIVIS data model.
 * 
 * @author Andreas Erber
 * @author Benedikt Meuthrath
 * @version 0.0.31, 2008-08-24
 */
@Entity
public class Actor extends InfoSpaceItem {

    /**
	 * External identifier, if present
	 */
    private Long externalId;

    /**
	 * Name of this {@link Actor}.
	 * 
	 * May not be <code>null</code>.
	 */
    private String name;

    /**
	 * Time stamp when this {@link Actor} registered.
	 * 
	 * May not be <code>null</code>.
	 */
    private Date registered;

    /**
	 * Default constructor.
	 * 
	 * Provided for persistence purposes. Do not use otherwise.
	 */
    public Actor() {
    }

    /**
	 * Initialization constructor.
	 * 
	 * This is the preferred construction method. The serialID needs not to be
	 * provided the persistence layer will generate one.
	 * 
	 * @param props
	 *            {@link Collection} of {@link InfoSpaceItemProperty properties}
	 * @param representations
	 *            {@link Collection} of {@link GraphItem}s representing this
	 *            {@link InfoSpaceItem}
	 * @param infoSpace
	 *            {@link InfoSpace} this {@link InfoSpaceItem} is contained in
	 * @param externalId
	 *            optional external identifier, may be <code>null</code>
	 * @param infoSpaceItemType
	 *            external type name, may not be <code>null</code>
	 * @param name
	 *            Textual identifier, may not be <code>null</code>
	 * @param registered
	 *            {@link Date} of registration, may not be <code>null</code>
	 * @throws DataModelInstantiationException
	 *             in case of mismatching arguments
	 */
    public Actor(final Collection<InfoSpaceItemProperty> props, final Collection<GraphItem> representations, final InfoSpace infoSpace, final Long externalId, final InfoSpaceItemType infoSpaceItemType, final String name, final Date registered) {
        super(infoSpaceItemType, props, representations, infoSpace);
        this.setExternalId(externalId);
        try {
            this.setName(name);
            this.setRegistered(registered);
        } catch (Exception e) {
            throw new DataModelInstantiationException("Construction of Actor failed when trying to set name or registered.", e);
        }
    }

    /**
	 * Retrieve an external identifier.
	 * 
	 * There is no need for an external identifier so this method may return
	 * null.
	 * 
	 * @return the externalId
	 */
    public Long getExternalId() {
        return this.externalId;
    }

    /**
	 * Reset an external identifier.
	 * 
	 * @param externalId
	 *            the externalId to set, may be <code>null</code>.
	 */
    public void setExternalId(final Long externalId) {
        this.externalId = externalId;
    }

    /**
	 * Reset the name of this {@link Actor}.
	 * 
	 * @param name
	 *            the new name
	 * @throws IllegalArgumentException
	 *             if <code>name</code> is <code>null</code> or empty.
	 */
    public void setName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Parameter name must not be null or empty.");
        }
        this.name = name;
    }

    /**
	 * Retrieve the name of this {@link Actor}.
	 * 
	 * @return the name
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * Reset the value of the timestamp when this {@link Actor} registered.
	 * 
	 * @param registered
	 *            the new value of registered
	 * @throws IllegalArgumentException
	 *             if <code>registered</code> is <code>null</code>
	 */
    public void setRegistered(final Date registered) {
        if (registered == null) {
            throw new IllegalArgumentException("Parameter registered must not be null.");
        }
        this.registered = registered;
    }

    /**
	 * Retrieve the timestamp when this {@link Actor} registered.
	 * 
	 * @return the registration timestamp
	 */
    public Date getRegistered() {
        return this.registered;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((externalId == null) ? 0 : externalId.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((registered == null) ? 0 : registered.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        final Actor other = (Actor) obj;
        if (externalId == null) {
            if (other.externalId != null) return false;
        } else if (!externalId.equals(other.externalId)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (registered == null) {
            if (other.registered != null) return false;
        } else if (!registered.equals(other.registered)) return false;
        return true;
    }
}

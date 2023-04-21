package de.sonivis.tool.core.datamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sonivis.tool.core.InfoSpaceItemTypeConstants;
import de.sonivis.tool.core.datamodel.exceptions.DataModelInstantiationException;

/**
 * Support class representing the application specific type of an
 * {@link InfoSpaceItem}.
 * <p>
 * It is intended to be used for distinguishing, i.e. different sub-types of
 * {@link Actor}s, {@link ContentElement}s, {@link InteractionRelation}s,
 * {@link ContextRelation}s, or {@link ActorContentElementRelation}s.
 * </p>
 * <p>
 * Instances of this class have to be received from
 * {@link InfoSpaceItemTypeManager#getInfoSpaceItemType(String)}. That
 * management method expects the textual identifier of the
 * {@link InfoSpaceItemType}. You can create a new type by passing any string
 * you like. For prepared constants refer to {@link InfoSpaceItemTypeConstants}.
 * </p>
 * 
 * @author Andreas Erber
 * @version $Revision: 1288 $, $Date: 2009-08-18 16:17:42 +0000 (Di, 18 Aug
 *          2009) $
 * @deprecated as of 2009-08-21 use sub-types of the data model entities
 *             instead.
 */
@Deprecated
public class InfoSpaceItemType {

    /**
	 * Logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(InfoSpaceItemType.class);

    /**
	 * Surrogate key.
	 */
    private int serialId;

    /**
	 * Textual identifier.
	 * <p>
	 * May not be <code>null</code> or empty.
	 * </p>
	 */
    private String name;

    /**
	 * Default constructor.
	 * <p>
	 * Provided for persistence purposes. Do not use otherwise.
	 * </p>
	 */
    InfoSpaceItemType() {
    }

    /**
	 * Initialization constructor.
	 * <p>
	 * The specified name may not be <code>null</code> or empty. After
	 * construction it cannot be modified.
	 * </p>
	 * 
	 * @param name
	 *            Type name string
	 * @throws DataModelInstantiationException
	 *             if argument is <code>null</code>.
	 */
    InfoSpaceItemType(final String name) {
        if (name == null || name.isEmpty()) {
            LOGGER.error("There cannot be an InfoSpaceItemType without an individual textual identifier.");
            throw new DataModelInstantiationException("Instantiation failed due to invalid value of parameter name. May not be null or empty.");
        }
        this.name = name;
    }

    /**
	 * Retrieve serial id created by the persistence layer.
	 * 
	 * @return the serialization id of this type
	 */
    public int getSerialId() {
        return this.serialId;
    }

    /**
	 * Reset the serialization ID (only to be accessed by the persistence
	 * layer).
	 * 
	 * @param serialId
	 *            the new serialization id to be set
	 * @throws IllegaArgumentException
	 *             if <i>serialId</i> is negative
	 */
    @SuppressWarnings("unused")
    private void setSerialId(final int serialId) {
        if (serialId < 0) {
            InfoSpaceItemType.LOGGER.error("Cannot set serialization ID to negative value.");
            throw new IllegalArgumentException("Parameter serialId must not be negative when using the setter.");
        }
        this.serialId = serialId;
    }

    /**
	 * Retrieve the type's name.
	 * 
	 * @return name of the type
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * Reset the type's name.
	 * <p>
	 * Passing <code>null</code> or an empty string as parameter will not
	 * change the current setting.
	 * </p>
	 * 
	 * @param name
	 *            new name string to be set
	 */
    @SuppressWarnings("unused")
    private void setName(final String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof InfoSpaceItemType)) {
            return false;
        }
        final InfoSpaceItemType other = (InfoSpaceItemType) obj;
        if (this.name == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!this.name.equals(other.getName())) {
            return false;
        }
        return true;
    }
}

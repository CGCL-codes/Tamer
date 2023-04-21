package com.compendium.core.datamodel;

import java.util.Date;

/**
 * The IdObject is the generic project compendium object
 * used as the basis for any object that requires a unique
 * identification.
 * <p>
 *
 * @author	rema and sajid /  Michelle Bachler
 */
public interface IIdObject extends IPCObject, java.io.Serializable {

    /**
	 * NOTE: The permission system has not been implemented yet.
	 *
	 * Permission property name for use with property change events
	 */
    public static final String PERMISSION_PROPERTY = "permission";

    /** Identifier property name for use with property change events */
    public static final String ID_PROPERTY = "id";

    /**
	 * Returns the unique identifier of this object
	 *
	 * @return String, the object's unique identifier
	 */
    public String getId();

    /**
	 * Sets the unique identifier of this object
	 * This is a SPECIAL operation to be used only when a new object is being generated
	 * Id values cannot be propagated as property changes.
	 *
	 * @param String id, the unique identifier of this object.
	 */
    public void setId(String id);

    /**
	 * NOTE: The permission system has not been implemented yet.
	 *
	 * Returns the permission of this object
	 *
	 * @return the objects permission
	 */
    public int getPermission();

    /**
	 * NOTE: The permission system has not been implemented yet.
	 *
	 * Sets the parmission level of this object.
	 *
	 * @param int permission, the level of permissions on this object.
	 */
    public void setPermission(int permission);

    /**
	 * Returns the author of this object.
	 *
	 * @return String, representing the author of this object.
	 */
    public String getAuthor();

    /**
	 * Set the author value in this object
	 *
	 * @param String Author, the author of this object.
	 */
    public void setAuthorLocal(String author);

    /**
	 * Returns the creation date of this object.
	 *
	 * @return Date, the date this object was created.
	 */
    public Date getCreationDate();

    /**
	 * Sets the date when this object was created.
	 *
	 * @param Date date, the date this object was created.
	 */
    public void setCreationDateLocal(Date date);

    /**
	 * Returns the last modification date of this object.
	 *
	 * @return Date, the date this object was last modified.
	 */
    public Date getModificationDate();

    /**
	 * Sets the last modification date of this object.
	 *
	 * @param Date date, the date this object was last modified.
	 */
    public void setModificationDateLocal(Date date);
}

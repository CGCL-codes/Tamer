package kiff.entity;

import java.util.Date;

/**
 * Interface for entities that track the created and modified date.
 * @author adam
 * @version $Id: TrackAware.java 67 2008-11-24 06:55:33Z a.ruggles $
 */
public interface TrackAware {

    /**
     * Returns the created date.
     * @return the created date to return.
     */
    Date getCreated();

    /**
     * Returns the creator.
     * @return the creator to return.
     */
    User getCreator();

    /**
     * Returns the modified date.
     * @return the modified date to return.
     */
    Date getModified();

    /**
     * Sets created.
     * @param created the created to set.
     */
    void setCreated(final Date created);

    /**
	 * Sets the creator.
	 * @param creator the creator to set.
	 */
    void setCreator(User creator);

    /**
     * Sets modified.
     * @param modified the modified to set.
     */
    void setModified(final Date modified);
}

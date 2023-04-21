package com.colorado.denver.domain.entities;

import java.util.Date;

/**
 * Defines the functionality of supporting lifecycle auditing.
 * @author Dilan Perera
 */
public interface LifecycleAuditable {

    /**
     * Gets the timestamp at which the domain entity was created in the system.
     * @return The timestamp at which the domain entity was created in the system.
     */
    Date getSystemDateCreated();

    /**
     * Sets the timestamp at which the domain entity was created in the system.
     * @param systemDateCreated the timestamp at which the domain entity was created in the system.
     */
    void setSystemDateCreated(Date systemDateCreated);

    /**
     * Gets the timestamp at which the domain entity was last updated in the system.
     * @return The timestamp at which the domain entity was last updated in the system.
     */
    Date getSystemDateLastUpdated();

    /**
     * Sets the timestamp at which the domain entity was last updated in the system.
     * @param systemDateLastUpdated the timestamp at which the domain entity was last updated in the system.
     */
    void setSystemDateLastUpdated(Date systemDateLastUpdated);
}

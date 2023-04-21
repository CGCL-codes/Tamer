package org.tonguetied.audit;

/**
 * This interface marks all classes that should be automatically audited.
 * 
 * @author bsion
 *
 */
public interface Auditable {

    /**
     * Get the unique identifier used as a primary key in relational data store.
     * 
     * @return the unique identifier of the object
     */
    Long getId();

    /**
     * Format the object to display the string value of this object for the 
     * AuditLog.
     * 
     * @return the string value of the object
     */
    String toLogString();
}

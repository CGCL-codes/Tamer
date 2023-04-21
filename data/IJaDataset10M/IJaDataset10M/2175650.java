package org.primordion.user.app.database.auction.datamodel;

/**
 * A marker interface for auditable persistent domain classes.
 *
 * @author Christian Bauer
 */
public interface Auditable {

    public Long getAuditableId();
}

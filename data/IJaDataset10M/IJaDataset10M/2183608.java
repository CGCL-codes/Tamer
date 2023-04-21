package edu.kit.cm.kitcampusguide.model;

import java.util.Date;

/**
 * Offers getters and setters for basic persistent fields.
 * 
 * @author Roland Steinegger, Karlsruhe Institute of Technology
 */
public interface Entity {

    Integer getId();

    void setId(final Integer id);

    String getCreatedBy();

    void setCreatedBy(final String createdBy);

    Date getDateCreated();

    void setDateCreated(final Date dateCreated);

    String getLastUpdatedBy();

    void setLastUpdatedBy(final String lastUpdatedBy);

    Date getDateLastUpdated();

    void setDateLastUpdated(final Date dateLastUpdated);
}

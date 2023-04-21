package org.encuestame.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.encuestame.persistence.domain.security.Account;

/**
 * GeoPointType.
 * @author Picado, Juan juanATencuestame.org
 * @since October 17, 2009
 * @version $Id$
 */
@Entity
@Table(name = "geoPoint_type")
public class GeoPointType {

    /** Location Type Id. */
    private Long locationTypeId;

    /** Type Description **/
    private String locationTypeDescription;

    /** Type Level. **/
    private Integer locationTypeLevel;

    /** {@link Account}. **/
    private Account users;

    /**
     * @return locationTypeId
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "loc_id_type", unique = true, nullable = false, length = 10)
    public Long getLocationTypeId() {
        return this.locationTypeId;
    }

    /**
     * @param locationTypeId locationTypeId
     */
    public void setLocationTypeId(final Long locationTypeId) {
        this.locationTypeId = locationTypeId;
    }

    /**
     * @return locationTypeDescription
     */
    @Column(name = "description")
    public String getLocationTypeDescription() {
        return this.locationTypeDescription;
    }

    /**
     * @param locationTypeDescription locationTypeDescription
     */
    public void setLocationTypeDescription(final String locationTypeDescription) {
        this.locationTypeDescription = locationTypeDescription;
    }

    /**
     * @return locationTypeLevel
     */
    @Column(name = "level")
    public Integer getLocationTypeLevel() {
        return this.locationTypeLevel;
    }

    /**
     * @param locationTypeLevel locationTypeLevel
     */
    public void setLocationTypeLevel(final Integer locationTypeLevel) {
        this.locationTypeLevel = locationTypeLevel;
    }

    /**
     * @return the users
     */
    @ManyToOne()
    public Account getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(final Account users) {
        this.users = users;
    }
}

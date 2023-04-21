package de.iritgo.aktera.address.entity;

import java.io.*;
import javax.persistence.*;

/**
 * @persist.persistent id="Party" name="Party" table="Party" schema="aktera"
 *                     securable="true" am-bypass-allowed="true"
 */
@Entity
@SuppressWarnings("serial")
public class Party implements Serializable {

    /** Id of the anonymous party */
    public static final Integer ANONYMOUS_ID = 0;

    /** Id of the administrator party */
    public static final Integer ADMIN_ID = 1;

    /** Id of the manager party */
    public static final Integer MANAGER_ID = 2;

    /** Primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer partyId;

    /** Foreign user key */
    private Integer userId;

    /** Category (e.g. 'C' for company or 'P' for person) */
    private String category;

    /**
	 * Get the primary key.
	 *
	 * @persist.field name="partyId" db-name="partyId" type="integer"
	 *                primary-key="true" null-allowed="false"
	 *                auto-increment="identity"
	 */
    public Integer getPartyId() {
        return partyId;
    }

    /**
	 * Get the primary key.
	 */
    public Integer getId() {
        return getPartyId();
    }

    /**
	 * Set the primary key.
	 */
    public void setPartyId(Integer partyId) {
        this.partyId = partyId;
    }

    /**
	 * Set the primary key.
	 */
    public void setId(Integer id) {
        setPartyId(id);
    }

    /**
	 * Get the foreign user key.
	 *
	 * @persist.field name="userId" db-name="userId" type="integer"
	 */
    public Integer getUserId() {
        return userId;
    }

    /**
	 * Set the foreign user key.
	 */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
	 * Get the party category.
	 *
	 * @persist.field name="category" db-name="category" type="varchar"
	 *                length="8" null-allowed="false" default-value="U"
	 * @persist.valid-value value="U" descrip="$undefined"
	 * @persist.valid-value value="C" descrip="$company"
	 * @persist.valid-value value="P" descrip="$person"
	 */
    public String getCategory() {
        return category;
    }

    /**
	 * Set the party category.
	 *
	 * @param category The new category.
	 */
    public void setCategory(String category) {
        this.category = category;
    }
}

package influx.dao.hibernate.test.data.impl;

import influx.model.Entity;
import influx.model.EntityEnableProcessingFlag;
import influx.model.EntityId;
import java.util.Date;

/**
 * Test Entity for HibernateDAO test cases
 * 
 * @author avyas
 */
@Entity
public class TestHibernateEntity {

    public static final String PROPERTY_ID = "testIdentifier";

    public static final String PROPERTY_ENABLED = "enabled";

    public static final String PROPERTY_NAME = "name";

    public static final String PROPERTY_DATE = "date";

    public static final String PROPERTY_NON_UNIQUE = "nonUniqueProperty";

    @EntityId
    private Long testIdentifier;

    @EntityEnableProcessingFlag
    private boolean enabled;

    private String name;

    private Date date;

    private String nonUniqueProperty;

    /**
	 * Test identifier
	 * 
	 * @return the testIdentifier
	 */
    public final Long getTestIdentifier() {
        return testIdentifier;
    }

    /**
	 * Test identifier
	 * 
	 * @param testIdentifier
	 *            the testIdentifier to set
	 */
    public final void setTestIdentifier(final Long testIdentifier) {
        this.testIdentifier = testIdentifier;
    }

    /**
	 * Enabled for processing flag
	 * 
	 * @return the enabled
	 */
    public final boolean isEnabled() {
        return enabled;
    }

    /**
	 * Enabled for processing flag
	 * 
	 * @param enabled
	 *            the enabled to set
	 */
    public final void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
	 * Test name
	 * 
	 * @return the name
	 */
    public final String getName() {
        return name;
    }

    /**
	 * Test name
	 * 
	 * @param name
	 *            the name to set
	 */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
	 * Test date
	 * 
	 * @return the date
	 */
    public final Date getDate() {
        return date;
    }

    /**
	 * Test date
	 * 
	 * @param date
	 *            the date to set
	 */
    public final void setDate(final Date date) {
        this.date = date;
    }

    /**
	 * Non-unique property
	 * 
	 * @return the nonUniqueProperty
	 */
    public final String getNonUniqueProperty() {
        return nonUniqueProperty;
    }

    /**
	 * Non-unique property
	 * 
	 * @param nonUniqueProperty
	 *            the nonUniqueProperty to set
	 */
    public final void setNonUniqueProperty(final String nonUniqueProperty) {
        this.nonUniqueProperty = nonUniqueProperty;
    }
}

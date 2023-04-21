package au.gov.naa.digipres.dpr.model.settings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * A simple Class to encapsulate any extra settings or meta-data we may wish to store. 
 * This class is persisted to the
 * RDBMS table extra_settings.
 * 
 */
@Entity
@Table(name = "extra_settings")
public class ExtraSetting {

    protected int id;

    protected String key;

    protected String value;

    public static final String KEY_SCHEMA_VERSION = "schema-version";

    /**
	 * @return Returns the key.
	 */
    @Column(name = "key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
	 * @return Returns the value.
	 */
    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * @return Returns the id.
	 */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "assigned")
    @GenericGenerator(name = "assigned", strategy = "assigned")
    public int getId() {
        return id;
    }

    /**
	 * @param id The id to set.
	 */
    public void setId(int id) {
        this.id = id;
    }
}

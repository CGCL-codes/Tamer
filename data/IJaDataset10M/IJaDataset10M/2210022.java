package org.yum.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * realm model
 * 
 * @author zhhan
 */
public class RealmInfo extends BaseObject {

    private static final long serialVersionUID = 1L;

    private long id;

    private String name;

    private String description;

    public static final String DEFUALT_REALM_NAME = "default";

    public static final String DEFUALT_REALM_DESCRIPTION = "default realm";

    public static final String SYSTEM_REALM_NAME = "system";

    public static final String SYSTEM_REALM_DESCRIPTION = "system realm";

    /**
	 * 
	 */
    public RealmInfo() {
        ;
    }

    /**
	 * @param id
	 *            realm id
	 * @param name
	 *            realm name
	 * @param description
	 *            realm description
	 */
    public RealmInfo(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
	 * @return Returns the description.
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description
	 *            The description to set.
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return Returns the id.
	 */
    public long getId() {
        return id;
    }

    /**
	 * @param id
	 *            The id to set.
	 */
    public void setId(long id) {
        this.id = id;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @see org.yum.model.BaseObject#equals(Object)
	 */
    public boolean equals(Object object) {
        if (!(object instanceof RealmInfo)) {
            return false;
        }
        RealmInfo rhs = (RealmInfo) object;
        return new EqualsBuilder().append(this.id, rhs.id).isEquals();
    }

    /**
	 * @see org.yum.model.BaseObject#hashCode()
	 */
    public int hashCode() {
        return new HashCodeBuilder(-1308304105, 1891574095).append(this.id).toHashCode();
    }

    /**
	 * @see org.yum.model.BaseObject#toString()
	 */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("name", this.name).append("id", this.id).append("description", this.description).toString();
    }
}

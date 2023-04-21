package org.yum.model;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * resource model
 * 
 * @author zhhan
 */
public class ResourceInfo extends BaseObject {

    private static final long serialVersionUID = 1L;

    private long id;

    private String uri;

    private String description;

    private RealmInfo realm;

    private Set roleSet = new HashSet();

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
	 * @return Returns the realm.
	 */
    public RealmInfo getRealm() {
        return realm;
    }

    /**
	 * @param realm
	 *            The realm to set.
	 */
    public void setRealm(RealmInfo realm) {
        this.realm = realm;
    }

    /**
	 * @return Returns the roleSet.
	 */
    public Set getRoleSet() {
        return roleSet;
    }

    /**
	 * @param roleSet
	 *            The roleSet to set.
	 */
    public void setRoleSet(Set roleSet) {
        this.roleSet = roleSet;
    }

    /**
	 * add a role to resource
	 * 
	 * @param role
	 */
    public void addRole(RoleInfo role) {
        this.roleSet.add(role);
    }

    /**
	 * remove role from resource
	 * 
	 * @param role
	 */
    public void removeRole(RoleInfo role) {
        this.roleSet.remove(role);
    }

    /**
	 * clear all role form resource
	 */
    public void clearRole() {
        this.roleSet.clear();
    }

    /**
	 * @return Returns the url.
	 */
    public String getUri() {
        return uri;
    }

    /**
	 * @param url
	 *            The url to set.
	 */
    public void setUri(String url) {
        this.uri = url;
    }

    /**
	 * @see org.yum.model.BaseObject#equals(Object)
	 */
    public boolean equals(Object object) {
        if (!(object instanceof ResourceInfo)) {
            return false;
        }
        ResourceInfo rhs = (ResourceInfo) object;
        return new EqualsBuilder().append(this.id, rhs.id).isEquals();
    }

    /**
	 * @see org.yum.model.BaseObject#hashCode()
	 */
    public int hashCode() {
        return new HashCodeBuilder(-1296184959, -1112704211).append(this.id).toHashCode();
    }

    /**
	 * @see org.yum.model.BaseObject#toString()
	 */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", this.id).append("description", this.description).append("realm", this.realm).append("uri", this.uri).toString();
    }
}

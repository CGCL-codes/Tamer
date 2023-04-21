package it.pallamia.pallamiabo.base;

import java.lang.Comparable;
import java.io.Serializable;

/**
 * This is an object that contains data related to the usergroups table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="usergroups"
 */
public abstract class BaseUsergroups implements Comparable, Serializable {

    public static String REF = "Usergroups";

    public static String PROP_VISIBILITY = "Visibility";

    public static String PROP_DESCR = "Descr";

    public static String PROP_USER = "User";

    public static String PROP_UPDATED = "Updated";

    public static String PROP_CREATED = "Created";

    public static String PROP_ID = "Id";

    public BaseUsergroups() {
        initialize();
    }

    /**
	 * Constructor for primary key
	 */
    public BaseUsergroups(java.lang.Integer id) {
        this.setId(id);
        initialize();
    }

    /**
	 * Constructor for required fields
	 */
    public BaseUsergroups(java.lang.Integer id, java.lang.String descr, java.lang.String visibility, java.util.Date created, java.util.Date updated) {
        this.setId(id);
        this.setDescr(descr);
        this.setVisibility(visibility);
        this.setCreated(created);
        this.setUpdated(updated);
        initialize();
    }

    protected void initialize() {
    }

    private int hashCode = Integer.MIN_VALUE;

    private java.lang.Integer id;

    private java.lang.String descr;

    private java.lang.String visibility;

    private java.util.Date created;

    private java.util.Date updated;

    private it.pallamia.pallamiabo.Users user;

    private java.util.Set<it.pallamia.pallamiabo.Users> users;

    /**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="id"
     */
    public java.lang.Integer getId() {
        return id;
    }

    /**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
    public void setId(java.lang.Integer id) {
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: descr
	 */
    public java.lang.String getDescr() {
        return descr;
    }

    /**
	 * Set the value related to the column: descr
	 * @param descr the descr value
	 */
    public void setDescr(java.lang.String descr) {
        this.descr = descr;
    }

    /**
	 * Return the value associated with the column: visibility
	 */
    public java.lang.String getVisibility() {
        return visibility;
    }

    /**
	 * Set the value related to the column: visibility
	 * @param visibility the visibility value
	 */
    public void setVisibility(java.lang.String visibility) {
        this.visibility = visibility;
    }

    /**
	 * Return the value associated with the column: created
	 */
    public java.util.Date getCreated() {
        return created;
    }

    /**
	 * Set the value related to the column: created
	 * @param created the created value
	 */
    public void setCreated(java.util.Date created) {
        this.created = created;
    }

    /**
	 * Return the value associated with the column: updated
	 */
    public java.util.Date getUpdated() {
        return updated;
    }

    /**
	 * Set the value related to the column: updated
	 * @param updated the updated value
	 */
    public void setUpdated(java.util.Date updated) {
        this.updated = updated;
    }

    /**
	 * Return the value associated with the column: user_id
	 */
    public it.pallamia.pallamiabo.Users getUser() {
        return user;
    }

    /**
	 * Set the value related to the column: user_id
	 * @param user the user_id value
	 */
    public void setUser(it.pallamia.pallamiabo.Users user) {
        this.user = user;
    }

    /**
	 * Return the value associated with the column: Users
	 */
    public java.util.Set<it.pallamia.pallamiabo.Users> getUsers() {
        return users;
    }

    /**
	 * Set the value related to the column: Users
	 * @param users the Users value
	 */
    public void setUsers(java.util.Set<it.pallamia.pallamiabo.Users> users) {
        this.users = users;
    }

    public void addToUsers(it.pallamia.pallamiabo.Users users) {
        if (null == getUsers()) setUsers(new java.util.TreeSet<it.pallamia.pallamiabo.Users>());
        getUsers().add(users);
    }

    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof it.pallamia.pallamiabo.Usergroups)) return false; else {
            it.pallamia.pallamiabo.Usergroups usergroups = (it.pallamia.pallamiabo.Usergroups) obj;
            if (null == this.getId() || null == usergroups.getId()) return false; else return (this.getId().equals(usergroups.getId()));
        }
    }

    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode(); else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public int compareTo(Object obj) {
        if (obj.hashCode() > hashCode()) return 1; else if (obj.hashCode() < hashCode()) return -1; else return 0;
    }

    public String toString() {
        return super.toString();
    }
}

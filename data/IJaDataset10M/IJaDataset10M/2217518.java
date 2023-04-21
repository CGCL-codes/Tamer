package model.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the mmm_company table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="mmm_company"
 */
public abstract class BaseMmmCompany implements Serializable {

    public static String REF = "MmmCompany";

    public static String PROP_NAME = "Name";

    public static String PROP_DESCRIPTION = "Description";

    public static String PROP_ID = "Id";

    public BaseMmmCompany() {
        initialize();
    }

    /**
	 * Constructor for primary key
	 */
    public BaseMmmCompany(java.lang.Integer id) {
        this.setId(id);
        initialize();
    }

    /**
	 * Constructor for required fields
	 */
    public BaseMmmCompany(java.lang.Integer id, java.lang.String name) {
        this.setId(id);
        this.setName(name);
        initialize();
    }

    protected void initialize() {
    }

    private int hashCode = Integer.MIN_VALUE;

    private java.lang.Integer id;

    private java.lang.String name;

    private java.lang.String description;

    private java.util.Set<model.MmmUser> mmmUsers;

    private java.util.Set<model.MmmMission> mmmMissions;

    /**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="ID_COMPANY"
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
	 * Return the value associated with the column: NAME
	 */
    public java.lang.String getName() {
        return name;
    }

    /**
	 * Set the value related to the column: NAME
	 * @param name the NAME value
	 */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
	 * Return the value associated with the column: DESCRIPTION
	 */
    public java.lang.String getDescription() {
        return description;
    }

    /**
	 * Set the value related to the column: DESCRIPTION
	 * @param description the DESCRIPTION value
	 */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
	 * Return the value associated with the column: MmmUsers
	 */
    public java.util.Set<model.MmmUser> getMmmUsers() {
        return mmmUsers;
    }

    /**
	 * Set the value related to the column: MmmUsers
	 * @param mmmUsers the MmmUsers value
	 */
    public void setMmmUsers(java.util.Set<model.MmmUser> mmmUsers) {
        this.mmmUsers = mmmUsers;
    }

    public void addToMmmUsers(model.MmmUser mmmUser) {
        if (null == getMmmUsers()) setMmmUsers(new java.util.TreeSet<model.MmmUser>());
        getMmmUsers().add(mmmUser);
    }

    /**
	 * Return the value associated with the column: MmmMissions
	 */
    public java.util.Set<model.MmmMission> getMmmMissions() {
        return mmmMissions;
    }

    /**
	 * Set the value related to the column: MmmMissions
	 * @param mmmMissions the MmmMissions value
	 */
    public void setMmmMissions(java.util.Set<model.MmmMission> mmmMissions) {
        this.mmmMissions = mmmMissions;
    }

    public void addToMmmMissions(model.MmmMission mmmMission) {
        if (null == getMmmMissions()) setMmmMissions(new java.util.TreeSet<model.MmmMission>());
        getMmmMissions().add(mmmMission);
    }

    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof model.MmmCompany)) return false; else {
            model.MmmCompany mmmCompany = (model.MmmCompany) obj;
            if (null == this.getId() || null == mmmCompany.getId()) return false; else return (this.getId().equals(mmmCompany.getId()));
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

    public String toString() {
        return super.toString();
    }
}

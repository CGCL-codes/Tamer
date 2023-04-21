package model.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the mmm_user table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="mmm_user"
 */
public abstract class BaseMmmUser implements Serializable {

    public static String REF = "MmmUser";

    public static String PROP_STATUS = "Status";

    public static String PROP_EMAIL = "Email";

    public static String PROP_PASSWORD = "Password";

    public static String PROP_FIRST_NAME = "FirstName";

    public static String PROP_ID = "Id";

    public static String PROP_ACRONYM = "Acronym";

    public static String PROP_LAST_NAME = "LastName";

    public BaseMmmUser() {
        initialize();
    }

    /**
	 * Constructor for primary key
	 */
    public BaseMmmUser(java.lang.Integer id) {
        this.setId(id);
        initialize();
    }

    /**
	 * Constructor for required fields
	 */
    public BaseMmmUser(java.lang.Integer id, java.lang.String firstName, java.lang.String lastName, java.lang.String acronym, java.lang.String password) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAcronym(acronym);
        this.setPassword(password);
        initialize();
    }

    protected void initialize() {
    }

    private int hashCode = Integer.MIN_VALUE;

    private java.lang.Integer id;

    private java.lang.String firstName;

    private java.lang.String lastName;

    private java.lang.String acronym;

    private java.lang.String password;

    private java.lang.String email;

    private java.lang.String status;

    private java.util.Set<model.MmmAssign> mmmAssigns;

    private java.util.Set<model.MmmGroup> mmmGroups;

    private java.util.Set<model.MmmCompany> mmmCompanies;

    /**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="ID_USER"
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
	 * Return the value associated with the column: FIRST_NAME
	 */
    public java.lang.String getFirstName() {
        return firstName;
    }

    /**
	 * Set the value related to the column: FIRST_NAME
	 * @param firstName the FIRST_NAME value
	 */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }

    /**
	 * Return the value associated with the column: LAST_NAME
	 */
    public java.lang.String getLastName() {
        return lastName;
    }

    /**
	 * Set the value related to the column: LAST_NAME
	 * @param lastName the LAST_NAME value
	 */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }

    /**
	 * Return the value associated with the column: ACRONYM
	 */
    public java.lang.String getAcronym() {
        return acronym;
    }

    /**
	 * Set the value related to the column: ACRONYM
	 * @param acronym the ACRONYM value
	 */
    public void setAcronym(java.lang.String acronym) {
        this.acronym = acronym;
    }

    /**
	 * Return the value associated with the column: PASSWORD
	 */
    public java.lang.String getPassword() {
        return password;
    }

    /**
	 * Set the value related to the column: PASSWORD
	 * @param password the PASSWORD value
	 */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    /**
	 * Return the value associated with the column: EMAIL
	 */
    public java.lang.String getEmail() {
        return email;
    }

    /**
	 * Set the value related to the column: EMAIL
	 * @param email the EMAIL value
	 */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    /**
	 * Return the value associated with the column: STATUS
	 */
    public java.lang.String getStatus() {
        return status;
    }

    /**
	 * Set the value related to the column: STATUS
	 * @param status the STATUS value
	 */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
	 * Return the value associated with the column: MmmAssigns
	 */
    public java.util.Set<model.MmmAssign> getMmmAssigns() {
        return mmmAssigns;
    }

    /**
	 * Set the value related to the column: MmmAssigns
	 * @param mmmAssigns the MmmAssigns value
	 */
    public void setMmmAssigns(java.util.Set<model.MmmAssign> mmmAssigns) {
        this.mmmAssigns = mmmAssigns;
    }

    public void addToMmmAssigns(model.MmmAssign mmmAssign) {
        if (null == getMmmAssigns()) setMmmAssigns(new java.util.TreeSet<model.MmmAssign>());
        getMmmAssigns().add(mmmAssign);
    }

    /**
	 * Return the value associated with the column: MmmGroups
	 */
    public java.util.Set<model.MmmGroup> getMmmGroups() {
        return mmmGroups;
    }

    /**
	 * Set the value related to the column: MmmGroups
	 * @param mmmGroups the MmmGroups value
	 */
    public void setMmmGroups(java.util.Set<model.MmmGroup> mmmGroups) {
        this.mmmGroups = mmmGroups;
    }

    public void addToMmmGroups(model.MmmGroup mmmGroup) {
        if (null == getMmmGroups()) setMmmGroups(new java.util.TreeSet<model.MmmGroup>());
        getMmmGroups().add(mmmGroup);
    }

    /**
	 * Return the value associated with the column: MmmCompanies
	 */
    public java.util.Set<model.MmmCompany> getMmmCompanies() {
        return mmmCompanies;
    }

    /**
	 * Set the value related to the column: MmmCompanies
	 * @param mmmCompanies the MmmCompanies value
	 */
    public void setMmmCompanies(java.util.Set<model.MmmCompany> mmmCompanies) {
        this.mmmCompanies = mmmCompanies;
    }

    public void addToMmmCompanies(model.MmmCompany mmmCompany) {
        if (null == getMmmCompanies()) setMmmCompanies(new java.util.TreeSet<model.MmmCompany>());
        getMmmCompanies().add(mmmCompany);
    }

    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof model.MmmUser)) return false; else {
            model.MmmUser mmmUser = (model.MmmUser) obj;
            if (null == this.getId() || null == mmmUser.getId()) return false; else return (this.getId().equals(mmmUser.getId()));
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

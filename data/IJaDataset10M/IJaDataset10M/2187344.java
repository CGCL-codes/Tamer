package banking_system.hibernate.base;

import java.io.Serializable;

/**
 * This is an object that contains data related to the DEFAULT_ACCOUNT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="DEFAULT_ACCOUNT"
 */
public abstract class BaseDefaultAccount implements Serializable {

    public static String REF = "DefaultAccount";

    public static String PROP_COMP_ID = "comp_id";

    public static String PROP_ACCESSIBILITY = "accessibility";

    public BaseDefaultAccount() {
        initialize();
    }

    /**
	 * Constructor for primary key
	 */
    public BaseDefaultAccount(banking_system.hibernate.DefaultAccountPK comp_id) {
        this.setComp_id(comp_id);
        initialize();
    }

    protected void initialize() {
    }

    private int hashCode = Integer.MIN_VALUE;

    private banking_system.hibernate.DefaultAccountPK comp_id;

    private banking_system.hibernate.Accessibility accessibility;

    /**
	 * Return the unique identifier of this class
     * @hibernate.id
     */
    public banking_system.hibernate.DefaultAccountPK getComp_id() {
        return comp_id;
    }

    /**
	 * Set the unique identifier of this class
	 * @param comp_id the new ID
	 */
    public void setComp_id(banking_system.hibernate.DefaultAccountPK comp_id) {
        this.comp_id = comp_id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: accessibility
	 */
    public banking_system.hibernate.Accessibility getAccessibility() {
        return accessibility;
    }

    /**
	 * Set the value related to the column: accessibility
	 * @param accessibility the accessibility value
	 */
    public void setAccessibility(banking_system.hibernate.Accessibility accessibility) {
        this.accessibility = accessibility;
    }

    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof banking_system.hibernate.DefaultAccount)) return false; else {
            banking_system.hibernate.DefaultAccount defaultAccount = (banking_system.hibernate.DefaultAccount) obj;
            if (null == this.getComp_id() || null == defaultAccount.getComp_id()) return false; else return (this.getComp_id().equals(defaultAccount.getComp_id()));
        }
    }

    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getComp_id()) return super.hashCode(); else {
                String hashStr = this.getClass().getName() + ":" + this.getComp_id().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString() {
        return super.toString();
    }
}

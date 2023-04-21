package com.peterhi.db;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents an account entity in PeterHi system.
 * The entity is a pair of account name, and password, plus
 * other info.
 * @author HAI YUN TAO
 */
public class Account {

    private Long id;

    private String accountName;

    private String password;

    private String displayName;

    private Calendar registered;

    private boolean loggedIn;

    private Set<School> schools = new HashSet<School>();

    /**
	 * Creates a new instance of an Account entity.
	 * This constructor is used by the data access layer.
	 */
    protected Account() {
    }

    /**
	 * Creates a new instance of an Account entity,
	 * with the specified name, use this to create
	 * a new, transient entity.
	 * @param accountName
	 */
    public Account(String accountName) {
        setAccountName(accountName);
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        setRegistered(c);
    }

    /**
	 * Gets the unique identifier of this account.
	 * The id is artificial.
	 * @return The unique id of the account, or null
	 * if this account is transient (not bound do a db).
	 */
    public Long getId() {
        return id;
    }

    /**
	 * Sets the unique identifier of this account.
	 * This method is used by the data access layer
	 * @param value The unique id of the account
	 */
    protected void setId(Long value) {
        id = value;
    }

    /**
	 * Gets the name of this account.
	 * @return The name of the account
	 */
    public String getAccountName() {
        return accountName;
    }

    /**
	 * Sets the name of this account.
	 * This method is used by the data access layer.
	 * @param value The name of the account
	 */
    protected void setAccountName(String value) {
        accountName = value;
    }

    /**
	 * Gets the canonical (display) name of this account
	 * @return The display name
	 */
    public String getDisplayName() {
        return displayName;
    }

    /**
	 * Sets the canonical (display) name of this account
	 * @param value The new display name
	 */
    public void setDisplayName(String value) {
        displayName = value;
    }

    /**
	 * Gets the password of this account.
	 * @return The password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * Sets the password of this account.
	 * @param value The password
	 */
    public void setPassword(String value) {
        password = value;
    }

    /**
	 * Gets when the account was created (registered).
	 * The time of registration is when this object was
	 * first created using the parameterized constructor.
	 * @return The date and time when this account is created.
	 */
    public Calendar getRegistered() {
        return registered;
    }

    /**
	 * Sets when the account was created (registered).
	 * This method is used by the data access layer
	 * @param value The date of the registration
	 */
    private void setRegistered(Calendar value) {
        registered = value;
    }

    /**
	 * Gets whether the account is currently logged in.
	 * This is a near-realtime check but not a realtime check
	 * @return <c>true</c> if the account is logged in,
	 * otherwise <c>false</c>
	 */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
	 * Sets a flag indicating whether the account is
	 * currently logged in. This flag is near-realtime
	 * but not in-realtime
	 * @param value <c>true</c> if to set the account
	 * as logged in status, otherwise, <c>false</c>
	 */
    public void setLoggedIn(boolean value) {
        loggedIn = value;
    }

    /**
	 * Gets a set of schools this account has registered.
	 * This method is used internally by the data access layer.
	 * @return A set of schools
	 */
    protected Set<School> getSchools() {
        return schools;
    }

    /**
	 * Sets a set of schools this account has registered.
	 * This method is used internally by the data access layer.
	 * @param value A set of schools
	 */
    protected void setSchools(Set<School> value) {
        schools = value;
    }

    /**
	 * Register this account with the specified school.
	 * @param s The school to register
	 */
    public void addSchool(School s) {
        getSchools().add(s);
        s.getStudents().add(this);
    }

    /**
	 * Unsubscribe this account from the a school.
	 * @param s The school to unsubscribe
	 */
    public void removeSchool(School s) {
        getSchools().remove(s);
        s.getStudents().remove(this);
    }

    /**
	 * Gets the number of schools this account has registered.
	 * @return The count of schools registered
	 */
    public int getSchoolCount() {
        return getSchools().size();
    }

    /**
	 * Gets an iterator backed by the school set. 
	 * Use the iterator to go through a list of
	 * schools in which this account has been
	 * enrolled.
	 * @return An iterator backed by the school set
	 */
    public Iterator<School> schoolItor() {
        return getSchools().iterator();
    }

    /**
	 * Get a school entity from a set of
	 * enrolled schools, by providing the
	 * school name.
	 * @param name The name of the school
	 * @return A school that matches the
	 * name specified, or <c>null</c> if
	 * this account is not enrolled into
	 * that school.
	 */
    public School getSchool(String name) {
        for (Iterator<School> itor = schoolItor(); itor.hasNext(); ) {
            School cur = itor.next();
            if (cur.getSchoolName().equals(name)) {
                return cur;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            return super.hashCode();
        }
        return getId().intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Account) {
            Account that = (Account) o;
            return (this.getId() == that.getId());
        } else {
            return super.equals(o);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "id: " + getId() + ", accountName: " + getAccountName() + ", registered: " + getRegistered();
    }
}

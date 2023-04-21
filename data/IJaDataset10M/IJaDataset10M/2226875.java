package com.citep.db.hibernate;

import java.util.HashSet;
import java.util.Set;
import com.citep.business.AccountCategory;
import com.citep.business.Account;

/**
 * Hibernate-specific implementation of edu.luc.cs.citep.business.AccountCategory.
 * 
 * <br><br>The generic getter and setter methods are designed to be used by Hibernate only and
 * are therefore designated as protected.  While some of the public methods required by
 * the interface simply duplicate the functionality of these protected methods, others 
 * do not, instead calling the manager to find and initialize collections from the
 * database.  The Hibernate methods and the public methods have been separated, though
 * to avoid confusion and allow for extensibility.  While this leads to a greater number 
 * of methods, it allows us to do any sort of meta-data tasks like logging or 
 * post-processing on attributes before we return them to the UI or push them back to
 * the DB.
 * 
 * @author mbone, bgonzalez
 * @since 8/10/2005
 */
public class AccountCategoryDAO implements AccountCategory {

    private int id;

    private String description;

    private String name;

    private boolean enabled;

    private Set<Account> accounts = new HashSet();

    /**
	 * @return Returns the description.
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description The description to set.
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return Returns the enabled.
	 */
    public boolean isEnabled() {
        return enabled;
    }

    /**
	 * @param enabled The enabled to set.
	 */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
	 * @return Returns the id.
	 */
    public int getId() {
        return id;
    }

    /**
	 * @param id The id to set.
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the h_accounts.
	 */
    public Set<Account> getAccounts() {
        return accounts;
    }

    /**
	 * @param h_accounts The h_accounts to set.
	 */
    protected void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public String toString() {
        return "[AccountCategory]: id=" + id + " | name: " + name + " | description: " + description + " | enabled: " + enabled;
    }
}

package net.sf.hibernate4gwt.test.domain.stateless;

import net.sf.hibernate4gwt.test.domain.IEmployee;

/**
 * Employee class.
 * Subclass of User
 * @author bruno.marchesson
 *
 */
public class Employee extends User implements IEmployee {

    /**
	 * Serialization ID
	 */
    private static final long serialVersionUID = -2294737766711898873L;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

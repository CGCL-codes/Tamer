package org.powerstone.sample;

import java.util.Date;

public class User {

    private String id;

    private String firstName;

    private String lastName;

    private Date birthday;

    /**
	 * @return Returns the id.
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id
	 *            The id to set.
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return Returns the firstName.
	 */
    public String getFirstName() {
        return firstName;
    }

    /**
	 * @param firstName
	 *            The firstName to set.
	 */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
	 * @return Returns the lastName.
	 */
    public String getLastName() {
        return lastName;
    }

    /**
	 * @param lastName
	 *            The lastName to set.
	 */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
	 * @return Returns this user's birthday
	 */
    public Date getBirthday() {
        return birthday;
    }

    /**
	 * @param birthday
	 *            The birthday to set.
	 */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
	 * @return Returns firstName and lastName
	 */
    public String getFullName() {
        return firstName + ' ' + lastName;
    }
}

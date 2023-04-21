package com.sjakkforum;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class PlayerName {

    private String firstName = "";

    private String lastName = "";

    private String middleName = "";

    public PlayerName() {
    }

    public PlayerName(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    /**
     * @return Returns the firstName.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The firstName to set.
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
     * @param lastName The lastName to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return Returns the middleName.
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName The middleName to set.
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getName() {
        String name = getFirstName();
        if (!getMiddleName().equals("")) {
            name += " " + getMiddleName();
        }
        if (!getLastName().equals("")) {
            name += " " + getLastName();
        }
        return name;
    }

    public boolean equals(Object obj) {
        if (obj instanceof PlayerName == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        PlayerName rhs = (PlayerName) obj;
        return new EqualsBuilder().append(firstName, rhs.firstName).append(middleName, rhs.middleName).append(lastName, rhs.lastName).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(firstName).append(middleName).append(lastName).toHashCode();
    }
}

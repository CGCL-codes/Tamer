package org.dozer.vo.interfacerecursion;

/**
 * @author Christoph Goldner 
 */
public class UserImpl implements User {

    private String firstName;

    private String lastName;

    private UserGroup userGroup;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String aFirstName) {
        firstName = aFirstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String aLastName) {
        lastName = aLastName;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup aUserGroup) {
        userGroup = aUserGroup;
    }
}

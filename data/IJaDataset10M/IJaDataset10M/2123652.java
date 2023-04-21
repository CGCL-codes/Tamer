package fr.albin.jmessagesend.user;

import java.util.List;
import java.util.Vector;

/**
 * A list of UserGroup.
 * @author avigier
 *
 */
public class UserGroups {

    public UserGroups() {
        this.list = new Vector();
    }

    public void add(UserGroup userGroup) {
        this.list.add(userGroup);
    }

    /**
	 * Gets the first UserGroup with the given identifier.
	 * @param identifier
	 * @return the corresponding UserGroup if found, null otherwise.
	 */
    public UserGroup getUserGroupWithIdentifier(String identifier) {
        UserGroup userGroup = null;
        for (int i = 0; i < this.list.size(); i++) {
            userGroup = (UserGroup) this.list.get(i);
            if (userGroup.getIdentifier().compareTo(identifier) == 0) {
                return userGroup;
            }
        }
        return null;
    }

    public UserGroup get(int index) {
        return (UserGroup) this.list.get(index);
    }

    public List getList() {
        return this.list;
    }

    private List list;
}

package edu.asu.quadriga.user;

import edu.asu.quadriga.interfaces.elements.IElement;

public interface IUser extends IElement {

    public abstract void setName(String name);

    /**
	 * User name of user (needs to be unique)
	 * @return
	 */
    public abstract String getName();

    /**
	 * First name of user.
	 * @return
	 */
    public String getFirstName();

    public void setFirstName(String name);

    /**
	 * Last name of user.
	 * @return
	 */
    public String getLastName();

    public void setLastName(String name);

    public abstract void setPassword(String password);

    public abstract String getPassword();

    public abstract void setAdministrating(boolean administrating);

    public abstract boolean isAdministrating();

    public abstract void setUserId(String userId);

    public abstract String getUserId();

    public void setRole(int role);

    public int getRole();
}

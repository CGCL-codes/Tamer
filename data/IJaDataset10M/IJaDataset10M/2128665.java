package pedro.soa.security;

import pedro.system.PedroResources;

public class User {

    public static final User GUEST = new User("guest", "guest");

    public static final User SUPER_USER = new User("superUser", "superUser");

    protected String ID;

    protected String password;

    private String organisation;

    private String ipAddress;

    public User() {
        ID = PedroResources.EMPTY_STRING;
        password = PedroResources.EMPTY_STRING;
        organisation = PedroResources.EMPTY_STRING;
        ipAddress = PedroResources.EMPTY_STRING;
    }

    public User(String ID, String password) {
        this.ID = ID;
        this.password = password;
    }

    /**
	* Set the value of ID.
	* @param ID Value to assign to ID.
	*/
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
	* Set the value of password.
	* @param password Value to assign to password.
	*/
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	* Set the value of organisation.
	* @param organisation Value to assign to organisation.
	*/
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    /**
	* Set the value of ipAddress.
	* @param ipAddress Value to assign to ipAddress.
	*/
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
	* Get the value of ID.
	* @return value of ID.
	*/
    public String getID() {
        return ID;
    }

    /**
	* Get the value of password.
	* @return value of password.
	*/
    public String getPassword() {
        return password;
    }

    /**
	* Get the value of organisation.
	* @return value of organisation.
	*/
    public String getOrganisation() {
        return organisation;
    }

    /**
	* Get the value of ipAddress.
	* @return value of ipAddress.
	*/
    public String getIpAddress() {
        return ipAddress;
    }
}

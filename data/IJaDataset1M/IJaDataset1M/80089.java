package fr.jbrunet.win.ndriveconnector.commons;

/**
 * @author Julien
 *
 */
public class User {

    private String name;

    private String password;

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @param password the password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }
}

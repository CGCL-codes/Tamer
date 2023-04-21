package net.sf.leechget.executor.login;

/**
 * @author Rogiel
 * 
 */
public class ExecutorLogin {

    private String login;

    private String password;

    private String serviceId;

    /**
	 * @return the login
	 */
    public String getLogin() {
        return login;
    }

    /**
	 * @param login
	 *            the login to set
	 */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @param password
	 *            the password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * @return the serviceId
	 */
    public String getServiceId() {
        return serviceId;
    }

    /**
	 * @param serviceId
	 *            the serviceId to set
	 */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}

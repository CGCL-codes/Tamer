package ch.arpage.collaboweb.struts.forms;

/**
 * ActionForm for the login action.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class LoginForm extends AbstractForm {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 1L;

    private String email;

    private String password;

    private String community;

    /**
	 * Returns the community.
	 * @return the community
	 */
    public String getCommunity() {
        return community;
    }

    /**
	 * Set the community.
	 * @param community the community to set
	 */
    public void setCommunity(String community) {
        this.community = community;
    }

    /**
	 * Returns the email.
	 * @return the email
	 */
    public String getEmail() {
        return email;
    }

    /**
	 * Set the email.
	 * @param email the email to set
	 */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
	 * Returns the password.
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * Set the password.
	 * @param password the password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }
}

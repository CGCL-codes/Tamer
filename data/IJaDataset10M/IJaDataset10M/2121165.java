package spring.jeuditechno.domain;

import java.util.Date;

/**
 * @author Rozange
 *
 */
public class User extends DomainObject {

    private String firstName;

    private String lastName;

    private String email;

    private Date birthDate;

    private boolean toSendEmail = false;

    /**
	 * @param id TODO
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param birthDate
	 */
    public User(Integer id, String email, String firstName, String lastName, Date birthDate) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
    }

    public User() {
        super();
    }

    /**
	 * @return the firstName
	 */
    public String getFirstName() {
        return firstName;
    }

    /**
	 * @param firstName the firstName to set
	 */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
	 * @return the lastName
	 */
    public String getLastName() {
        return lastName;
    }

    /**
	 * @param lastName the lastName to set
	 */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
	 * @return the email
	 */
    public String getEmail() {
        return email;
    }

    /**
	 * @param email the email to set
	 */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
	 * @return the birthDate
	 */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
	 * @param birthDate the birthDate to set
	 */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
	 * @return the toSendEmail
	 */
    public boolean isToSendEmail() {
        return toSendEmail;
    }

    /**
	 * @param toSendEmail the toSendEmail to set
	 */
    public void setToSendEmail(boolean toSendEmail) {
        this.toSendEmail = toSendEmail;
    }
}

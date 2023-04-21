package net.pepperbytes.eqc.database.persistentobjects;

import javax.persistence.Entity;
import javax.persistence.Table;
import net.pepperbytes.plaf.database.base.BaseDatabaseObject;

@Entity
@Table(name = "users")
public class User extends BaseDatabaseObject {

    private String userName;

    private String passwordHash;

    private String cellPhoneNumber;

    private String firstName;

    private String lastName;

    /**
	 * @return the userName
	 */
    public String getUserName() {
        return userName;
    }

    /**
	 * @param userName the userName to set
	 */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
	 * @return the passwordHash
	 */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
	 * @param passwordHash the passwordHash to set
	 */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
	 * @return the cellPhoneNumber
	 */
    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    /**
	 * @param cellPhoneNumber the cellPhoneNumber to set
	 */
    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
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
}

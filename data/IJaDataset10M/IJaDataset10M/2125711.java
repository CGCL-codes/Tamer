package officeserver.users;

import java.io.Serializable;
import java.util.logging.Level;
import officeserver.log_error.Log;
import officeserver.log_error.OfficeException;
import officeserver.log_error.UserException;
import officeserver.main.MODULE;
import officeserver.main.Main;

/**
 * @author mramsey3
 * 
 */
public class PersonalInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String firstName = null;

    private String lastName = null;

    private String username = null;

    private String password = null;

    private int loginAttempts;

    private String email = null;

    private int cellPhone;

    private int age;

    private AccessLevel accessLevel;

    private Address address = null;

    private boolean active;

    public PersonalInfo(AccessLevel accessLevel) {
        this("user" + UserList.size(), "user" + UserList.size(), "user" + UserList.size(), "user" + UserList.size(), accessLevel);
    }

    public PersonalInfo(String firstName, String lastName, String username, String password, AccessLevel accessLevel) {
        this(firstName, lastName, username, password, accessLevel, "unknown", 000, 000, new Address());
    }

    public PersonalInfo(String firstName, String lastName, String username, String password, AccessLevel accessLevel, String email, int cellPhone, int age, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
        this.email = email;
        this.cellPhone = cellPhone;
        this.age = age;
        this.address = address;
        this.loginAttempts = 0;
        this.active = true;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(int cellPhone) {
        this.cellPhone = cellPhone;
    }

    /**
     * @author Chris Bayruns
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @author Chris Bayruns
     * @param age
     *            the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @author Chris Bayruns
     * @return the access level
     */
    public AccessLevel getAccessLevel() {
        if (accessLevel == null) {
            accessLevel = AccessLevel.PATIENT;
            return accessLevel;
        } else {
            return accessLevel;
        }
    }

    /**
     * @author Chris Bayruns
     * @param accessLevel
     *            Level to set to
     */
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * @author Chris Bayruns
     * @return The address
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * @author Chris Bayruns
     * @param address
     *            Set the address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @author Chris Bayruns
     * @return The address
     */
    public String getAddressString() {
        return (this.address == null) ? null : this.address.getAddress();
    }

    /**
     * This method checks if the password is the correct one if not, it adds to
     * the attempted logins. After 3 unsucessfull attempts, locks account.
     * 
     * @param password
     *            the password to check
     * @return if they logged in
     * @throws OfficeException
     *             If the account is locked.
     */
    public boolean checkPassword(String password) throws UserException {
        boolean result = false;
        if (this.active) {
            if (password.equals(this.password)) {
                if (this.loginAttempts < Main.MAXLOGINS) {
                    this.loginAttempts = 0;
                    result = true;
                } else {
                    throw (new UserException(Level.INFO, UserException.REASON.ACCOUNT_LOCKED, "User: " + this.username));
                }
            } else {
                this.loginAttempts++;
                if (this.loginAttempts > Main.MAXLOGINS) {
                    throw (new UserException(Level.INFO, UserException.REASON.ACCOUNT_LOCKED, "User: " + this.username));
                } else {
                    throw (new UserException(Level.INFO, UserException.REASON.WRONG_PASSWORD, "User: " + this.username));
                }
            }
        }
        return result;
    }

    /**
     * Self explanitory
     * 
     * @author Chris Bayruns
     */
    public void resetAttempts() {
        this.loginAttempts = 0;
    }

    /**
     * @author Chris Bayruns
     * @return If user is deleted
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * This "Deletes" a user.
     * 
     * @author Chris Bayruns
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * This "UnDeletes" a user.
     * 
     * @author Chris Bayruns
     */
    public void activate() {
        this.active = true;
    }

    /**
     * This method updates a current PersonalInfo with new info from the user.
     * 
     * @author Chris Bayruns
     * @param update
     *            The updated personal info
     */
    public void updateInfo(PersonalInfo update) {
        if (update != null) {
            this.address = (update.getAddress() == null) ? this.address : update.getAddress().clone();
            this.age = update.getAge();
            this.cellPhone = update.getCellPhone();
            this.email = (update.getEmail() == null) ? this.email : new String(update.getEmail());
            this.firstName = (update.getFirstName() == null) ? this.firstName : update.getFirstName();
            this.lastName = (update.getLastName() == null) ? this.lastName : new String(update.getLastName());
            if (update.getUsername() != null) {
                Log.writeToLog(MODULE.USER, Level.INFO, "Username " + this.username + " was changed to " + update.getUsername());
                this.username = new String(update.getUsername());
            }
        }
    }

    /**
     * @author Chris Bayruns
     * @return A clone of the PersonalInfo object with access level and password
     *         removed.
     */
    public PersonalInfo getInfo() {
        PersonalInfo result = new PersonalInfo(this.accessLevel);
        result.setPassword(null);
        result.setAddress(this.address.clone());
        result.setAge(age);
        result.setCellPhone(cellPhone);
        result.setEmail(new String(email));
        result.setFirstName(new String(firstName));
        result.setLastName(new String(lastName));
        result.setUsername(new String(username));
        return result;
    }
}

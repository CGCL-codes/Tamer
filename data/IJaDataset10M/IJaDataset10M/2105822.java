package Repository.Entities;

/**
 * Class ContactPerson stores all the contact information
 * of one person in the system.
 * @author G09
 * @version 0.5.1
 * @since 0.4
 *
 */
public class ContactPerson implements IContactPerson {

    String firstName;

    String lastName;

    String email;

    String address;

    String organization;

    String phone;

    String Fax;

    /**
	 * Creates a new instance of a person to contact.
	 */
    public ContactPerson() {
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAddress() {
        return this.address;
    }

    public String getOrganization() {
        return this.organization;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getFax() {
        return this.Fax;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFax(String Fax) {
        this.Fax = Fax;
    }
}

package org.blueoxygen.papaje.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.blueoxygen.cimande.DefaultPersistence;

/**
 * @author leo
 *
 */
@Entity
@Table(name = "jm_company")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class EmployerCompany extends DefaultPersistence {

    private String name;

    private String homepage;

    private Location location;

    private List<Employer> contactName;

    private String phone;

    private String fax;

    private String email;

    /**
	 * @return the contactName
	 */
    @OneToMany(mappedBy = "company")
    public List<Employer> getContactName() {
        return contactName;
    }

    /**
	 * @param contactName the contactName to set
	 */
    public void setContactName(List<Employer> contactName) {
        this.contactName = contactName;
    }

    /**
	 * @return the phone
	 */
    public String getPhone() {
        return phone;
    }

    /**
	 * @param phone the phone to set
	 */
    public void setPhone(String contactNo) {
        this.phone = contactNo;
    }

    /**
	 * @return the homepage
	 */
    public String getHomepage() {
        return homepage;
    }

    /**
	 * @param homepage the homepage to set
	 */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
	 * @return the location
	 */
    @ManyToOne
    @JoinColumn(name = "location_id")
    public Location getLocation() {
        return location;
    }

    /**
	 * @param location the location to set
	 */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
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
	 * @return the fax
	 */
    public String getFax() {
        return fax;
    }

    /**
	 * @param fax the fax to set
	 */
    public void setFax(String fax) {
        this.fax = fax;
    }
}

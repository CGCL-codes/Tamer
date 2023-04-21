package com.misyshealthcare.connect.doc.ccd;

import java.util.Calendar;
import com.misyshealthcare.connect.base.SharedEnums;
import com.misyshealthcare.connect.base.demographicdata.Address;
import com.misyshealthcare.connect.base.demographicdata.PhoneNumber;

/**
 *  
 *
 * @author Wenzhi Li
 * @version 3.0, Dec 6, 2007
 */
public class SourcePatientInfo {

    private PersonName personName = null;

    private Address[] address = null;

    private PhoneNumber[] phoneNumber = null;

    private Calendar birthdate = null;

    private SharedEnums.SexType gender = null;

    private Organization organization = null;

    private SharedEnums.MartitalStatusType maritalStatus = null;

    /**
	 * @return the personName
	 */
    public PersonName getPersonName() {
        return personName;
    }

    /**
	 * @param personName the personName to set
	 */
    public void setPersonName(PersonName personName) {
        this.personName = personName;
    }

    /**
	 * @return the address
	 */
    public Address[] getAddress() {
        return address;
    }

    /**
	 * @param address the address to set
	 */
    public void setAddress(Address[] address) {
        this.address = address;
    }

    /**
	 * @return the phoneNumber
	 */
    public PhoneNumber[] getPhoneNumber() {
        return phoneNumber;
    }

    /**
	 * @param phoneNumber the phoneNumber to set
	 */
    public void setPhoneNumber(PhoneNumber[] phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
	 * @return the birthdate
	 */
    public Calendar getBirthdate() {
        return birthdate;
    }

    /**
	 * @param birthdate the birthdate to set
	 */
    public void setBirthdate(Calendar birthdate) {
        this.birthdate = birthdate;
    }

    /**
	 * @return the gender
	 */
    public SharedEnums.SexType getGender() {
        return gender;
    }

    /**
	 * @param gender the gender to set
	 */
    public void setGender(SharedEnums.SexType gender) {
        this.gender = gender;
    }

    /**
	 * @return the organization
	 */
    public Organization getOrganization() {
        return organization;
    }

    /**
	 * @param organization the organization to set
	 */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    /**
	 * @return the maritalStatus
	 */
    public SharedEnums.MartitalStatusType getMaritalStatus() {
        return maritalStatus;
    }

    /**
	 * @param maritalStatus the maritalStatus to set
	 */
    public void setMaritalStatus(SharedEnums.MartitalStatusType maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
}

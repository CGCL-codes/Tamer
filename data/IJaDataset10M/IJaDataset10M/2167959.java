package org.grassfield.common.entity;

/**
 * The Class ContactInfo.
 */
public class ContactInfo {

    private long id;

    private String country;

    private String email1;

    private String email2;

    private String email3;

    private String mobile;

    private String telephone;

    private String web;

    private String fax;

    /**
	 * Gets the id.
	 * 
	 * @return the id
	 */
    public long getId() {
        return id;
    }

    /**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
    public void setId(long id) {
        this.id = id;
    }

    /**
	 * Gets the country.
	 * 
	 * @return the country
	 */
    public String getCountry() {
        return country;
    }

    /**
	 * Sets the country.
	 * 
	 * @param country
	 *            the new country
	 */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
	 * Gets the email1.
	 * 
	 * @return the email1
	 */
    public String getEmail1() {
        return email1;
    }

    /**
	 * Sets the email1.
	 * 
	 * @param email1
	 *            the new email1
	 */
    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    /**
	 * Gets the email2.
	 * 
	 * @return the email2
	 */
    public String getEmail2() {
        return email2;
    }

    /**
	 * Sets the email2.
	 * 
	 * @param email2
	 *            the new email2
	 */
    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    /**
	 * Gets the email3.
	 * 
	 * @return the email3
	 */
    public String getEmail3() {
        return email3;
    }

    /**
	 * Sets the email3.
	 * 
	 * @param email3
	 *            the new email3
	 */
    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    /**
	 * Gets the mobile.
	 * 
	 * @return the mobile
	 */
    public String getMobile() {
        return mobile;
    }

    /**
	 * Sets the mobile.
	 * 
	 * @param mobile
	 *            the new mobile
	 */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
	 * Gets the telephone.
	 * 
	 * @return the telephone
	 */
    public String getTelephone() {
        return telephone;
    }

    /**
	 * Sets the telephone.
	 * 
	 * @param telephone
	 *            the new telephone
	 */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
	 * Gets the web.
	 * 
	 * @return the web
	 */
    public String getWeb() {
        return web;
    }

    /**
	 * Sets the web.
	 * 
	 * @param web
	 *            the new web
	 */
    public void setWeb(String web) {
        this.web = web;
    }

    /**
	 * Gets the fax.
	 * 
	 * @return the fax
	 */
    public String getFax() {
        return fax;
    }

    /**
	 * Sets the fax.
	 * 
	 * @param fax
	 *            the new fax
	 */
    public void setFax(String fax) {
        this.fax = fax;
    }

    @Override
    public String toString() {
        return "ContactInfo [country=" + country + ", email1=" + email1 + "]";
    }
}

package org.jabusuite.core.companies;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import org.jabusuite.core.users.JbsUser;
import org.jabusuite.core.utils.JbsBaseObject;

/**
 * The Java Business Suite is a multi-comapny-software. This class represents a company.
 * @author hilwers
 * @date 30.01.2007
 *
 */
@Entity
public class JbsCompany extends JbsBaseObject implements Serializable {

    private static final long serialVersionUID = -8067280665271890313L;

    private String name1;

    private String name2;

    private String name3;

    private String street;

    private String zip;

    private String city;

    private String additionalInfo;

    private String phone;

    private String fax;

    private String website;

    private String emailAddress;

    private List<JbsUser> users;

    private byte[] logo;

    /**
     * @return the users
     */
    @ManyToMany(mappedBy = "companies")
    public List<JbsUser> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<JbsUser> users) {
        this.users = users;
    }

    /**
     * @return the additionalInfo
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * @param additionalInfo the additionalInfo to set
     */
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the name1
     */
    public String getName1() {
        return name1;
    }

    /**
     * @param name1 the name1 to set
     */
    public void setName1(String name1) {
        this.name1 = name1;
    }

    /**
     * @return the name2
     */
    public String getName2() {
        return name2;
    }

    /**
     * @param name2 the name2 to set
     */
    public void setName2(String name2) {
        this.name2 = name2;
    }

    /**
     * @return the name3
     */
    public String getName3() {
        return name3;
    }

    /**
     * @param name3 the name3 to set
     */
    public void setName3(String name3) {
        this.name3 = name3;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip the zip to set
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}

package org.jabusuite.address;

import org.jabusuite.address.letter.Letter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import org.jabusuite.core.utils.EJbsObject;
import org.jabusuite.note.Note;

@Entity
public class Address extends Contact implements Serializable {

    private static final long serialVersionUID = -4945301344857840280L;

    private int adressType;

    private String userNo;

    private String street;

    private String poBox;

    private boolean usePoBox;

    private String countryShort;

    private String countryLong;

    private String zip;

    private String zipPoBox;

    private String city;

    private String homepage;

    private String comments;

    private Date reminder;

    private String additionalInfo;

    private Address parentAddress;

    private double latitude;

    private double longitude;

    private List<Contact> contacts;

    private List<Note> notes;

    public void setStandardValues() {
        super.setStandardValues();
        this.setAdressType(1);
        this.setDeleted(false);
        this.setUserNo("");
        this.setStreet("");
        this.setPoBox("");
        this.setUsePoBox(false);
        this.setCountryShort("");
        this.setCountryLong("");
        this.setZip("");
        this.setZipPoBox("");
        this.setCity("");
        this.setHomepage("");
        this.setComments("");
        this.setReminder(new Date());
        this.setLatitude(0.0);
        this.setLatitude(0.0);
        this.setContacts(new ArrayList<Contact>());
    }

    /**
     * @return Returns the adressType.
     */
    public int getAdressType() {
        return adressType;
    }

    /**
     * @param adressType The adressType to set.
     */
    public void setAdressType(int adressType) {
        this.adressType = adressType;
    }

    /**
     * @return Returns the city.
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city The city to set.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return Returns the comments.
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments The comments to set.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return Returns the countryLong.
     */
    public String getCountryLong() {
        return countryLong;
    }

    /**
     * @param countryLong The countryLong to set.
     */
    public void setCountryLong(String countryLong) {
        this.countryLong = countryLong;
    }

    /**
     * @return Returns the countryShort.
     */
    public String getCountryShort() {
        return countryShort;
    }

    /**
     * @param countryShort The countryShort to set.
     */
    public void setCountryShort(String countryShort) {
        this.countryShort = countryShort;
    }

    /**
     * @return Returns the homepage.
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * @param homepage The homepage to set.
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * @return Returns the poBox.
     */
    public String getPoBox() {
        return poBox;
    }

    /**
     * @param poBox The poBox to set.
     */
    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }

    /**
     * @return Returns the reminder.
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getReminder() {
        return reminder;
    }

    /**
     * @param reminder The reminder to set.
     */
    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }

    /**
     * @return Returns the street.
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street The street to set.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return Returns the usePoBox.
     */
    public boolean isUsePoBox() {
        return usePoBox;
    }

    /**
     * @param usePoBox The usePoBox to set.
     */
    public void setUsePoBox(boolean usePoBox) {
        this.usePoBox = usePoBox;
    }

    /**
     * @return Returns the userNo.
     */
    public String getUserNo() {
        return userNo;
    }

    /**
     * @param userNo The userNo to set.
     */
    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    /**
     * @return Returns the zip.
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip The zip to set.
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * @return Returns the zipPoBox.
     */
    public String getZipPoBox() {
        return zipPoBox;
    }

    /**
     * @param zipPoBox The zipPoBox to set.
     */
    public void setZipPoBox(String zipPoBox) {
        this.zipPoBox = zipPoBox;
    }

    @Column(columnDefinition = "text")
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    /**
     * 
     * @return The address this address belongs to
     */
    @ManyToOne
    public Address getParentAddress() {
        return parentAddress;
    }

    /**
     * 
     * @param parentAddress The address this address belongs to
     */
    public void setParentAddress(Address parentAddress) {
        this.parentAddress = parentAddress;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     * @return The different contacts for this address
     */
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("name1, name2, name3 ASC")
    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public void checkData(boolean doAdditionalChecks) throws EJbsObject {
        super.checkData(doAdditionalChecks);
        if (doAdditionalChecks) {
            if ((this.getStreet() == null) || (this.getStreet().trim().equals(""))) throw new EJbsAddress(EJbsAddress.ET_STREET);
            if ((this.getZip() == null) || (this.getZip().trim().equals(""))) throw new EJbsAddress(EJbsAddress.ET_ZIP);
            if ((this.getCity() == null) || (this.getCity().trim().equals(""))) throw new EJbsAddress(EJbsAddress.ET_CITY);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("noteDate DESC")
    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}

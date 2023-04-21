package org.hil.core.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "children")
public class Children extends AbstractEntity implements Serializable {

    @Column(name = "full_name", length = 200, columnDefinition = "nvarchar(200)")
    private String fullName;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "child_code", length = 30, columnDefinition = "nvarchar(30)")
    private String childCode;

    @Column(name = "gender")
    private boolean gender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_village")
    private Village village;

    @Column(name = "address", length = 200, columnDefinition = "nvarchar(200)")
    private String address;

    @Column(name = "father_name", length = 200, columnDefinition = "nvarchar(200)")
    private String fatherName;

    @Column(name = "father_birth_year")
    private Integer fatherBirthYear;

    @Column(name = "father_id", length = 200, columnDefinition = "nvarchar(200)")
    private String fatherID;

    @Column(name = "father_mobile", length = 200, columnDefinition = "nvarchar(200)")
    private String fatherMobile;

    @Column(name = "mother_name", length = 200, columnDefinition = "nvarchar(200)")
    private String motherName;

    @Column(name = "mother_birth_year")
    private Integer motherBirthYear;

    @Column(name = "mother_id", length = 200, columnDefinition = "nvarchar(200)")
    private String motherID;

    @Column(name = "mother_mobile", length = 200, columnDefinition = "nvarchar(200)")
    private String motherMobile;

    @Column(name = "current_caretaker")
    private Integer currentCaretaker;

    @Column(name = "caretaker_name", length = 200, columnDefinition = "nvarchar(200)")
    private String caretakerName;

    @Column(name = "caretaker_birth_year")
    private Integer caretakerBirthYear;

    @Column(name = "caretaker_id", length = 200, columnDefinition = "nvarchar(200)")
    private String caretakerID;

    @Column(name = "caretaker_mobile", length = 200, columnDefinition = "nvarchar(200)")
    private String caretakerMobile;

    @Column(name = "finished")
    private boolean finished;

    @Column(name = "locked")
    private boolean locked;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "author", length = 200, columnDefinition = "nvarchar(200)")
    private String author;

    @Column(name = "last_author", length = 200, columnDefinition = "nvarchar(200)")
    private String lastAuthor;

    @Column(name = "barcode_date")
    private Date barcodeDate;

    @Column(name = "from_mobile")
    private boolean fromMobile;

    public boolean isFromMobile() {
        return fromMobile;
    }

    public void setFromMobile(boolean fromMobile) {
        this.fromMobile = fromMobile;
    }

    /**
	 * @return the fullName
	 */
    public String getFullName() {
        return fullName;
    }

    /**
	 * @param fullName the fullName to set
	 */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
	 * @return the dateOfBirth
	 */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
	 * @param dateOfBirth the dateOfBirth to set
	 */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
	 * @return the childCode
	 */
    public String getChildCode() {
        return childCode;
    }

    /**
	 * @param childCode the childCode to set
	 */
    public void setChildCode(String childCode) {
        this.childCode = childCode;
    }

    /**
	 * @return the gender
	 */
    public boolean isGender() {
        return gender;
    }

    /**
	 * @param gender the gender to set
	 */
    public void setGender(boolean gender) {
        this.gender = gender;
    }

    /**
	 * @return the village
	 */
    public Village getVillage() {
        return village;
    }

    /**
	 * @param village the village to set
	 */
    public void setVillage(Village village) {
        this.village = village;
    }

    /**
	 * @return the address
	 */
    public String getAddress() {
        return address;
    }

    /**
	 * @param address the address to set
	 */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
	 * @return the fatherName
	 */
    public String getFatherName() {
        return fatherName;
    }

    /**
	 * @param fatherName the fatherName to set
	 */
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    /**
	 * @return the fatherBirthYear
	 */
    public Integer getFatherBirthYear() {
        return fatherBirthYear;
    }

    /**
	 * @param fatherBirthYear the fatherBirthYear to set
	 */
    public void setFatherBirthYear(Integer fatherBirthYear) {
        this.fatherBirthYear = fatherBirthYear;
    }

    /**
	 * @return the fatherID
	 */
    public String getFatherID() {
        return fatherID;
    }

    /**
	 * @param fatherID the fatherID to set
	 */
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    /**
	 * @return the fatherMobile
	 */
    public String getFatherMobile() {
        return fatherMobile;
    }

    /**
	 * @param fatherMobile the fatherMobile to set
	 */
    public void setFatherMobile(String fatherMobile) {
        this.fatherMobile = fatherMobile;
    }

    /**
	 * @return the motherName
	 */
    public String getMotherName() {
        return motherName;
    }

    /**
	 * @param motherName the motherName to set
	 */
    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    /**
	 * @return the motherBirthYear
	 */
    public Integer getMotherBirthYear() {
        return motherBirthYear;
    }

    /**
	 * @param motherBirthYear the motherBirthYear to set
	 */
    public void setMotherBirthYear(Integer motherBirthYear) {
        this.motherBirthYear = motherBirthYear;
    }

    /**
	 * @return the motherID
	 */
    public String getMotherID() {
        return motherID;
    }

    /**
	 * @param motherID the motherID to set
	 */
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    /**
	 * @return the motherMobile
	 */
    public String getMotherMobile() {
        return motherMobile;
    }

    /**
	 * @param motherMobile the motherMobile to set
	 */
    public void setMotherMobile(String motherMobile) {
        this.motherMobile = motherMobile;
    }

    /**
	 * @return the currentCaretaker
	 */
    public Integer getCurrentCaretaker() {
        return currentCaretaker;
    }

    /**
	 * @param currentCaretaker the currentCaretaker to set
	 */
    public void setCurrentCaretaker(Integer currentCaretaker) {
        this.currentCaretaker = currentCaretaker;
    }

    /**
	 * @return the caretakerName
	 */
    public String getCaretakerName() {
        return caretakerName;
    }

    /**
	 * @param caretakerName the caretakerName to set
	 */
    public void setCaretakerName(String caretakerName) {
        this.caretakerName = caretakerName;
    }

    /**
	 * @return the caretakerBirthYear
	 */
    public Integer getCaretakerBirthYear() {
        return caretakerBirthYear;
    }

    /**
	 * @param caretakerBirthYear the caretakerBirthYear to set
	 */
    public void setCaretakerBirthYear(Integer caretakerBirthYear) {
        this.caretakerBirthYear = caretakerBirthYear;
    }

    /**
	 * @return the caretakerID
	 */
    public String getCaretakerID() {
        return caretakerID;
    }

    /**
	 * @param caretakerID the caretakerID to set
	 */
    public void setCaretakerID(String caretakerID) {
        this.caretakerID = caretakerID;
    }

    /**
	 * @return the caretakerMobile
	 */
    public String getCaretakerMobile() {
        return caretakerMobile;
    }

    /**
	 * @param caretakerMobile the caretakerMobile to set
	 */
    public void setCaretakerMobile(String caretakerMobile) {
        this.caretakerMobile = caretakerMobile;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getLastAuthor() {
        return lastAuthor;
    }

    public void setLastAuthor(String lastAuthor) {
        this.lastAuthor = lastAuthor;
    }

    public Date getBarcodeDate() {
        return barcodeDate;
    }

    public void setBarcodeDate(Date barcodeDate) {
        this.barcodeDate = barcodeDate;
    }
}

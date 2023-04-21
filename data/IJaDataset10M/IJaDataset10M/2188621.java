package datagen.example;

import java.util.Date;

public class Person {

    private String recordId;

    private String pairId;

    private String groupId;

    private String firstName;

    private String middleName;

    private String lastName;

    private Date birthDate;

    private String gender;

    private int age;

    private String title;

    private String state;

    private int streetNo;

    private int postcode;

    private String Address;

    private String Culture;

    private String Role;

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public int getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(int streetNo) {
        this.streetNo = streetNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String sex) {
        this.gender = sex;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /**
	 * @return the recordId
	 */
    public String getRecordId() {
        return recordId;
    }

    /**
	 * @param firstName
	 *            the firstName to set
	 */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
	 * @return the firstName
	 */
    public String getFirstName() {
        return firstName;
    }

    /**
	 * @param middleName
	 *            the middleName to set
	 */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
	 * @return the middleName
	 */
    public String getMiddleName() {
        return middleName;
    }

    /**
	 * @param lastName
	 *            the lastName to set
	 */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
	 * @return the lastName
	 */
    public String getLastName() {
        return lastName;
    }

    /**
	 * @param birthDate
	 *            the birthDate to set
	 */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
	 * @return the birthDate
	 */
    public Date getBirthDate() {
        return birthDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAddress() {
        return Address;
    }

    public void setCulture(String culture) {
        Culture = culture;
    }

    public String getCulture() {
        return Culture;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setPairId(String pairId) {
        this.pairId = pairId;
    }

    public String getPairId() {
        return pairId;
    }
}

package org.jaffa.applications.mylife.admin.components.usermaintenance.dto;

import java.util.*;
import org.jaffa.components.dto.HeaderDto;

/** The output for the UserMaintenance.
 */
public class UserMaintenanceCreateInDto {

    /** Holds value of property headerDto. */
    private HeaderDto headerDto;

    /** Holds value of property userName. */
    private String userName;

    /** Holds value of property firstName. */
    private String firstName;

    /** Holds value of property lastName. */
    private String lastName;

    /** Holds value of property password. */
    private String password;

    /** Holds value of property status. */
    private String status;

    /** Holds value of property eMailAddress. */
    private String eMailAddress;

    /** Holds value of property createdOn. */
    private org.jaffa.datatypes.DateTime createdOn;

    /** Holds value of property createdBy. */
    private String createdBy;

    /** Holds value of property lastUpdatedOn. */
    private org.jaffa.datatypes.DateTime lastUpdatedOn;

    /** Holds value of property lastUpdatedBy. */
    private String lastUpdatedBy;

    /** Default Constructor.*/
    public UserMaintenanceCreateInDto() {
    }

    /** Getter for property headerDto.
     * @return Value of property headerDto.
     */
    public HeaderDto getHeaderDto() {
        return headerDto;
    }

    /** Setter for property headerDto.
     * @param headerDto New value of property headerDto.
     */
    public void setHeaderDto(HeaderDto headerDto) {
        this.headerDto = headerDto;
    }

    /** Getter for property userName.
     * @return Value of property userName.
     */
    public String getUserName() {
        return userName;
    }

    /** Setter for property userName.
     * @param userName New value of property userName.
     */
    public void setUserName(String userName) {
        if (userName == null || userName.length() == 0) this.userName = null; else this.userName = userName;
    }

    /** Getter for property firstName.
     * @return Value of property firstName.
     */
    public String getFirstName() {
        return firstName;
    }

    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     */
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.length() == 0) this.firstName = null; else this.firstName = firstName;
    }

    /** Getter for property lastName.
     * @return Value of property lastName.
     */
    public String getLastName() {
        return lastName;
    }

    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     */
    public void setLastName(String lastName) {
        if (lastName == null || lastName.length() == 0) this.lastName = null; else this.lastName = lastName;
    }

    /** Getter for property password.
     * @return Value of property password.
     */
    public String getPassword() {
        return password;
    }

    /** Setter for property password.
     * @param password New value of property password.
     */
    public void setPassword(String password) {
        if (password == null || password.length() == 0) this.password = null; else this.password = password;
    }

    /** Getter for property status.
     * @return Value of property status.
     */
    public String getStatus() {
        return status;
    }

    /** Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(String status) {
        if (status == null || status.length() == 0) this.status = null; else this.status = status;
    }

    /** Getter for property eMailAddress.
     * @return Value of property eMailAddress.
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /** Setter for property eMailAddress.
     * @param eMailAddress New value of property eMailAddress.
     */
    public void setEMailAddress(String eMailAddress) {
        if (eMailAddress == null || eMailAddress.length() == 0) this.eMailAddress = null; else this.eMailAddress = eMailAddress;
    }

    /** Getter for property createdOn.
     * @return Value of property createdOn.
     */
    public org.jaffa.datatypes.DateTime getCreatedOn() {
        return createdOn;
    }

    /** Setter for property createdOn.
     * @param createdOn New value of property createdOn.
     */
    public void setCreatedOn(org.jaffa.datatypes.DateTime createdOn) {
        this.createdOn = createdOn;
    }

    /** Getter for property createdBy.
     * @return Value of property createdBy.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /** Setter for property createdBy.
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(String createdBy) {
        if (createdBy == null || createdBy.length() == 0) this.createdBy = null; else this.createdBy = createdBy;
    }

    /** Getter for property lastUpdatedOn.
     * @return Value of property lastUpdatedOn.
     */
    public org.jaffa.datatypes.DateTime getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    /** Setter for property lastUpdatedOn.
     * @param lastUpdatedOn New value of property lastUpdatedOn.
     */
    public void setLastUpdatedOn(org.jaffa.datatypes.DateTime lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    /** Getter for property lastUpdatedBy.
     * @return Value of property lastUpdatedBy.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /** Setter for property lastUpdatedBy.
     * @param lastUpdatedBy New value of property lastUpdatedBy.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        if (lastUpdatedBy == null || lastUpdatedBy.length() == 0) this.lastUpdatedBy = null; else this.lastUpdatedBy = lastUpdatedBy;
    }

    /** Returns the debug information
     * @return The debug information
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<UserMaintenanceCreateInDto>");
        buf.append("<headerDto>");
        if (headerDto != null) buf.append(headerDto.toString());
        buf.append("</headerDto>");
        buf.append("<userName>");
        if (userName != null) buf.append(userName);
        buf.append("</userName>");
        buf.append("<firstName>");
        if (firstName != null) buf.append(firstName);
        buf.append("</firstName>");
        buf.append("<lastName>");
        if (lastName != null) buf.append(lastName);
        buf.append("</lastName>");
        buf.append("<password>");
        if (password != null) buf.append(password);
        buf.append("</password>");
        buf.append("<status>");
        if (status != null) buf.append(status);
        buf.append("</status>");
        buf.append("<eMailAddress>");
        if (eMailAddress != null) buf.append(eMailAddress);
        buf.append("</eMailAddress>");
        buf.append("<createdOn>");
        if (createdOn != null) buf.append(createdOn);
        buf.append("</createdOn>");
        buf.append("<createdBy>");
        if (createdBy != null) buf.append(createdBy);
        buf.append("</createdBy>");
        buf.append("<lastUpdatedOn>");
        if (lastUpdatedOn != null) buf.append(lastUpdatedOn);
        buf.append("</lastUpdatedOn>");
        buf.append("<lastUpdatedBy>");
        if (lastUpdatedBy != null) buf.append(lastUpdatedBy);
        buf.append("</lastUpdatedBy>");
        buf.append("</UserMaintenanceCreateInDto>");
        return buf.toString();
    }
}

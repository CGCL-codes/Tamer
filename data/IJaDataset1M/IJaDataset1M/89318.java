package net.iskandar.murano_example.dto;

import java.io.Serializable;
import net.iskandar.murano_example.domain.Status;

public class EmployeeUpdateObject implements Serializable {

    public static final int FIRST_NAME_MAX_LENGTH = 20;

    public static final int LAST_NAME_MAX_LENGTH = 20;

    public static final int PHONE_MAX_LENGTH = 30;

    public static final String PHONE_REGEX = "^[\\(\\-\\)\\d\\s]*$";

    private Integer id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Integer positionId;

    private Integer statusId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}

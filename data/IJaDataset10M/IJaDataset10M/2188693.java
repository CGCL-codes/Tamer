package ru.arriah.servicedesk.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ClientBean extends UserBean {

    private int organizationId;

    private int departmentId;

    private int clientTypeId;

    private String position;

    private String www;

    private String email1;

    private String email2;

    private String email3;

    private String tel1;

    private String tel2;

    private String tel3;

    private String icq;

    private String address;

    private Date birthday;

    private String notes;

    private String clientType;

    private String status;

    public ClientBean() {
        super();
    }

    public ClientBean(ResultSet resultSet, String tableAlias) {
        super(resultSet, tableAlias);
        fillFromResultSet(resultSet);
    }

    public ClientBean(ResultSet resultSet) {
        super(resultSet);
        fillFromResultSet(resultSet);
    }

    public void fillFromResultSet(ResultSet resultSet) {
        this.fillFromResultSet(resultSet, "user");
    }

    public void fillFromResultSet(ResultSet resultSet, String table) {
        super.fillFromResultSet(resultSet, table);
        try {
            setOrganizationId(resultSet.getInt("client_settings.organization_id"));
            setClientTypeId(resultSet.getInt("client_settings.clienttype_id"));
            setDepartmentId(resultSet.getInt("client_settings.department_id"));
            setAddress(resultSet.getString("client_settings.address"));
            setNotes(resultSet.getString("client_settings.notes"));
            setEmail2(resultSet.getString("client_settings.email1"));
            setEmail3(resultSet.getString("client_settings.email2"));
            setIcq(resultSet.getString("client_settings.icq"));
            setTel1(resultSet.getString("client_settings.tel1"));
            setTel2(resultSet.getString("client_settings.tel2"));
            setTel3(resultSet.getString("client_settings.tel3"));
            setWww(resultSet.getString("client_settings.www"));
            setPosition(resultSet.getString("client_settings.position"));
            setBirthday(resultSet.getDate("client_settings.birthday"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public int getClientTypeId() {
        return clientTypeId;
    }

    public void setClientTypeId(int clientTypeId) {
        this.clientTypeId = clientTypeId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmail1() {
        return email;
    }

    public void setEmail1(String email1) {
        this.email = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getIcq() {
        return icq;
    }

    public void setIcq(String icq) {
        this.icq = icq;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getTel3() {
        return tel3;
    }

    public void setTel3(String tel3) {
        this.tel3 = tel3;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }
}

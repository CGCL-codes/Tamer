package com.creawor.hz_market.t_advertisement;

import java.io.Serializable;
import java.util.Date;

public class t_advertisement implements Serializable {

    private int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int parm) {
        id = parm;
    }

    private String advertiserment_code;

    private String advertiserment_name;

    public String getAdvertiserment_code() {
        return advertiserment_code;
    }

    public void setAdvertiserment_code(String parm) {
        advertiserment_code = parm;
    }

    private String advertisement_type;

    public String getAdvertisement_type() {
        return advertisement_type;
    }

    public void setAdvertisement_type(String parm) {
        advertisement_type = parm;
    }

    private String release_form;

    public String getRelease_form() {
        return release_form;
    }

    public void setRelease_form(String parm) {
        release_form = parm;
    }

    private String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String parm) {
        picture = parm;
    }

    private String the_content;

    public String getThe_content() {
        return the_content;
    }

    public void setThe_content(String parm) {
        the_content = parm;
    }

    private String specification;

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String parm) {
        specification = parm;
    }

    private Date createdtime;

    public Date getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Date parm) {
        createdtime = parm;
    }

    private Date publishdate;

    public Date getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(Date parm) {
        publishdate = parm;
    }

    private Date deadline;

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date parm) {
        deadline = parm;
    }

    private String vendor;

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String parm) {
        vendor = parm;
    }

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String parm) {
        address = parm;
    }

    private double x = 0;

    public double getX() {
        return x;
    }

    public void setX(double parm) {
        x = parm;
    }

    private double y = 0;

    public double getY() {
        return y;
    }

    public void setY(double parm) {
        y = parm;
    }

    private String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String parm) {
        company = parm;
    }

    public String getAdvertiserment_name() {
        return advertiserment_name;
    }

    public void setAdvertiserment_name(String advertiserment_name) {
        this.advertiserment_name = advertiserment_name;
    }

    private String county;

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    private Date updated_day;

    public Date getUpdated_day() {
        return updated_day;
    }

    public void setUpdated_day(Date updated_day) {
        this.updated_day = updated_day;
    }

    private Date insert_day;

    public Date getInsert_day() {
        return insert_day;
    }

    public void setInsert_day(Date insert_day) {
        this.insert_day = insert_day;
    }

    private String opentype;

    public String getOpentype() {
        return opentype;
    }

    public void setOpentype(String opentype) {
        this.opentype = opentype;
    }

    private Date data_create_day;

    private String contract_code;

    private String contract_sum;

    private String town;

    public String getContract_code() {
        return contract_code;
    }

    public void setContract_code(String contract_code) {
        this.contract_code = contract_code;
    }

    public String getContract_sum() {
        return contract_sum;
    }

    public void setContract_sum(String contract_sum) {
        this.contract_sum = contract_sum;
    }

    public Date getData_create_day() {
        return data_create_day;
    }

    public void setData_create_day(Date data_create_day) {
        this.data_create_day = data_create_day;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}

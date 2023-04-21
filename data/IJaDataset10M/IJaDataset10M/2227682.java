package com.creawor.hz_market.t_channel;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class t_channel_Form extends ActionForm {

    public t_channel_Form() {
        super();
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String strparm) {
        id = strparm;
    }

    private String service_hall_code;

    public String getService_hall_code() {
        return service_hall_code;
    }

    public void setService_hall_code(String strparm) {
        service_hall_code = strparm;
    }

    private String service_hall_name;

    public String getService_hall_name() {
        return service_hall_name;
    }

    public void setService_hall_name(String strparm) {
        service_hall_name = strparm;
    }

    private String channel_type;

    public String getChannel_type() {
        return channel_type;
    }

    public void setChannel_type(String strparm) {
        channel_type = strparm;
    }

    private String star_level;

    public String getStar_level() {
        return star_level;
    }

    public void setStar_level(String strparm) {
        star_level = strparm;
    }

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String strparm) {
        address = strparm;
    }

    private String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String strparm) {
        company = strparm;
    }

    private String contact_man;

    public String getContact_man() {
        return contact_man;
    }

    public void setContact_man(String strparm) {
        contact_man = strparm;
    }

    private String contact_tel;

    public String getContact_tel() {
        return contact_tel;
    }

    public void setContact_tel(String strparm) {
        contact_tel = strparm;
    }

    private String contact_mobile;

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String strparm) {
        contact_mobile = strparm;
    }

    private String rent;

    public String getRent() {
        return rent;
    }

    public void setRent(String strparm) {
        rent = strparm;
    }

    private String town;

    public String getTown() {
        return town;
    }

    public void setTown(String strparm) {
        town = strparm;
    }

    private String towncode;

    public String getTowncode() {
        return towncode;
    }

    public void setTowncode(String strparm) {
        towncode = strparm;
    }

    private String county;

    public String getCounty() {
        return county;
    }

    public void setCounty(String strparm) {
        county = strparm;
    }

    private String county_code;

    public String getCounty_code() {
        return county_code;
    }

    public void setCounty_code(String strparm) {
        county_code = strparm;
    }

    private String x;

    public String getX() {
        return x;
    }

    public void setX(String strparm) {
        x = strparm;
    }

    private String y;

    public String getY() {
        return y;
    }

    public void setY(String strparm) {
        y = strparm;
    }

    private String zoom;

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String strparm) {
        zoom = strparm;
    }

    private String village_xz;

    private String village_zr;

    public String getVillage_xz() {
        return village_xz;
    }

    public void setVillage_xz(String village_xz) {
        this.village_xz = village_xz;
    }

    public String getVillage_zr() {
        return village_zr;
    }

    public void setVillage_zr(String village_zr) {
        this.village_zr = village_zr;
    }

    private String parent;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    private String jindu;

    public String getJindu() {
        return jindu;
    }

    public void setJindu(String strparm) {
        jindu = strparm;
    }

    private String main_type;

    public String getMain_type() {
        return main_type;
    }

    public void setMain_type(String main_type) {
        this.main_type = main_type;
    }

    private String iscomplete;

    public String getIscomplete() {
        return iscomplete;
    }

    public void setIscomplete(String iscomplete) {
        this.iscomplete = iscomplete;
    }
}

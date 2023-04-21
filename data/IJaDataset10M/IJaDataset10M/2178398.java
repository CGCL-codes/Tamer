package org.gary.corpration.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 企业信息
 * @author lengreen
 * 
 */
@Entity
public class Corpration implements lengreen.core.interfaces.Entity {

    /**
	 * 在记录日志中需要用到该方法
	 */
    @Override
    public String toString() {
        return "Corpration [企业地址=" + address + ", createDate=" + createDate + ", 企业邮箱=" + email + ", 企业传真=" + fax + ", id=" + id + ", 企业电话=" + telephone + ", 所在地邮编=" + zipcode + ", lang=" + lang + "]";
    }

    private int id;

    private boolean visible;

    private String aboutUs;

    private String prospects;

    private String culture;

    private String lang;

    private String address;

    private String telephone;

    private String email;

    private String zipcode;

    private String fax;

    private Date createDate;

    public Corpration() {
        setCreateDate(new Date());
        setVisible(true);
    }

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Lob
    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    @Lob
    public String getProspects() {
        return prospects;
    }

    public void setProspects(String prospects) {
        this.prospects = prospects;
    }

    @Lob
    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return createDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}

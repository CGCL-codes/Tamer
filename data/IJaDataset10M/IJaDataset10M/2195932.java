package com.etc.controller.beans;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author magicbank
 */
public class PartnerBeans implements Serializable {

    private static final long serialVersionUID = 2L;

    private int id;

    private String code;

    private String name;

    private String contact;

    private String address;

    private String phone;

    private String fax;

    private String email;

    private String tax;

    private String exceptTax;

    private String active;

    private int typeId;

    private String typeName;

    private int termId;

    private String termName;

    private BigDecimal termLength;

    private String url;

    private BigDecimal credit;

    private BigDecimal debit;

    private int showcode;

    private String detail;

    private String comment;

    public PartnerBeans() {
    }

    public PartnerBeans(int id, String code, String name, String contact, String address, String phone, String fax, String email, String tax, String exceptTax, String active, int typeId, String typeName, int termId, String termName, BigDecimal termLength, String url, BigDecimal credit, BigDecimal debit, int showcode, String detail, String comment) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.phone = phone;
        this.fax = fax;
        this.email = email;
        this.tax = tax;
        this.exceptTax = exceptTax;
        this.active = active;
        this.typeId = typeId;
        this.typeName = typeName;
        this.termId = termId;
        this.termName = termName;
        this.termLength = termLength;
        this.url = url;
        this.credit = credit;
        this.debit = debit;
        this.showcode = showcode;
        this.detail = detail;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getExceptTax() {
        return exceptTax;
    }

    public void setExceptTax(String exceptTax) {
        this.exceptTax = exceptTax;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public BigDecimal getTermLength() {
        return termLength;
    }

    public void setTermLength(BigDecimal termLength) {
        this.termLength = termLength;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public int getShowcode() {
        return showcode;
    }

    public void setShowcode(int showcode) {
        this.showcode = showcode;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setExceptTax(boolean val) {
        if (val) {
            this.exceptTax = "1";
        } else {
            this.exceptTax = "0";
        }
    }

    public boolean isExceptTax() {
        return this.exceptTax.equalsIgnoreCase("1") ? true : false;
    }

    public void setActive(boolean val) {
        if (val) {
            this.active = "1";
        } else {
            this.active = "0";
        }
    }

    public boolean isActive() {
        return this.active.equalsIgnoreCase("1") ? true : false;
    }
}

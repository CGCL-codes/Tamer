package com.be.vo;

public class BankVO {

    private long id;

    private long bankGroup;

    private long bcNr;

    private String branchID;

    private long bcNrNew;

    private String sicNr;

    private long headOffice;

    private long bcType;

    private long sic;

    private long eurosic;

    private long lang;

    private String nameShort;

    private String name;

    private String poBox;

    private String domizil;

    private String city;

    private String state;

    private String zipCode;

    private String telefon;

    private String fax;

    private String areaCode;

    private long countryID;

    private String postAccount;

    private String swiftBic;

    private java.sql.Date validFrom;

    private java.sql.Date validTo;

    public void setId(long id) {
        this.id = id;
    }

    public void setBankGroup(long bankGroup) {
        this.bankGroup = bankGroup;
    }

    public void setBcNr(long bcNr) {
        this.bcNr = bcNr;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public void setBcNrNew(long bcNrNew) {
        this.bcNrNew = bcNrNew;
    }

    public void setSicNr(String sicNr) {
        this.sicNr = sicNr;
    }

    public void setHeadOffice(long headOffice) {
        this.headOffice = headOffice;
    }

    public void setBcType(long bcType) {
        this.bcType = bcType;
    }

    public void setSic(long sic) {
        this.sic = sic;
    }

    public void setEurosic(long eurosic) {
        this.eurosic = eurosic;
    }

    public void setLang(long lang) {
        this.lang = lang;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }

    public void setDomizil(String domizil) {
        this.domizil = domizil;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public void setCountryID(long countryID) {
        this.countryID = countryID;
    }

    public void setPostAccount(String postAccount) {
        this.postAccount = postAccount;
    }

    public void setSwiftBic(String swiftBic) {
        this.swiftBic = swiftBic;
    }

    public void setValidFrom(java.sql.Date validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidTo(java.sql.Date validTo) {
        this.validTo = validTo;
    }

    public long getId() {
        return id;
    }

    public long getBankGroup() {
        return bankGroup;
    }

    public long getBcNr() {
        return bcNr;
    }

    public String getBranchID() {
        return branchID;
    }

    public long getBcNrNew() {
        return bcNrNew;
    }

    public String getSicNr() {
        return sicNr;
    }

    public long getHeadOffice() {
        return headOffice;
    }

    public long getBcType() {
        return bcType;
    }

    public long getSic() {
        return sic;
    }

    public long getEurosic() {
        return eurosic;
    }

    public long getLang() {
        return lang;
    }

    public String getNameShort() {
        return nameShort;
    }

    public String getName() {
        return name;
    }

    public String getPoBox() {
        return poBox;
    }

    public String getDomizil() {
        return domizil;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getFax() {
        return fax;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public long getCountryID() {
        return countryID;
    }

    public String getPostAccount() {
        return postAccount;
    }

    public String getSwiftBic() {
        return swiftBic;
    }

    public java.sql.Date getValidFrom() {
        return validFrom;
    }

    public java.sql.Date getValidTo() {
        return validTo;
    }

    public String toString() {
        return id + ";" + bankGroup + ";" + bcNr + ";" + branchID + ";" + bcNrNew + ";" + sicNr + ";" + headOffice + ";" + bcType + ";" + sic + ";" + eurosic + ";" + lang + ";" + nameShort + ";" + name + ";" + poBox + ";" + domizil + ";" + city + ";" + state + ";" + zipCode + ";" + telefon + ";" + fax + ";" + areaCode + ";" + countryID + ";" + postAccount + ";" + swiftBic + ";" + validFrom + ";" + validTo;
    }

    public void init() {
        id = 0;
        bankGroup = 0;
        bcNr = 0;
        branchID = "";
        bcNrNew = 0;
        sicNr = "";
        headOffice = 0;
        bcType = 0;
        sic = 0;
        eurosic = 0;
        lang = 0;
        nameShort = "";
        name = "";
        poBox = "";
        domizil = "";
        city = "";
        state = "";
        zipCode = "";
        telefon = "";
        fax = "";
        areaCode = "";
        countryID = 0;
        postAccount = "";
        swiftBic = "";
        validFrom = new java.sql.Date(System.currentTimeMillis());
        validTo = new java.sql.Date(System.currentTimeMillis());
    }
}

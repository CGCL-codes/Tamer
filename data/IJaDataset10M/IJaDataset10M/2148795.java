package com.medsol.reports.detail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

/**
 * Created by IntelliJ IDEA. User: vinay Date: 7 Sep, 2008 Time: 3:32:55 PM To
 * change this template use File | Settings | File Templates.
 */
public class HindProdSaleRep {

    private String invoiceType;

    private String invoiceId;

    private BigInteger invoiceNum;

    private Date saleDate;

    private BigDecimal dispSyringe;

    private BigDecimal scalpVan;

    private BigDecimal cannula;

    private BigDecimal glassVan;

    private BigDecimal vaku;

    private BigDecimal safetyBox;

    private BigDecimal unolok;

    private BigDecimal unolokSyr;

    private BigDecimal totalPieces;

    private BigDecimal kojak;

    private BigDecimal dispNeedle;

    private BigDecimal MP;

    private BigDecimal UP;

    private BigDecimal TB;

    private BigDecimal kitKath;

    private BigDecimal kitKathPlus;

    private BigDecimal cathy;

    private Double invAmount;

    private Double invCost;

    private Double edTax;

    private Double othTax;

    private Double freightCharge;

    private String caseQuantity;

    private String companyDescr;

    private BigInteger companyId;

    private String companyName;

    private String ownerName;

    private String addressLine1;

    private String addressLine2;

    private String consignorName;

    private String consignorAddrLn1;

    private String consignorAddrLn2;

    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getConsignorName() {
        return consignorName;
    }

    public void setConsignorName(String consignorName) {
        this.consignorName = consignorName;
    }

    public String getConsignorAddrLn1() {
        return consignorAddrLn1;
    }

    public void setConsignorAddrLn1(String consignorAddrLn1) {
        this.consignorAddrLn1 = consignorAddrLn1;
    }

    public String getConsignorAddrLn2() {
        return consignorAddrLn2;
    }

    public void setConsignorAddrLn2(String consignorAddrLn2) {
        this.consignorAddrLn2 = consignorAddrLn2;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Double getFreightCharge() {
        return freightCharge;
    }

    public void setFreightCharge(Double freightCharge) {
        this.freightCharge = freightCharge;
    }

    public Double getInvCost() {
        return invCost;
    }

    public void setInvCost(Double invCost) {
        this.invCost = invCost;
    }

    public Double getEdTax() {
        return edTax;
    }

    public void setEdTax(Double edTax) {
        this.edTax = edTax;
    }

    public Double getOthTax() {
        return othTax;
    }

    public void setOthTax(Double othTax) {
        this.othTax = othTax;
    }

    public Double getInvAmount() {
        return invAmount;
    }

    public void setInvAmount(Double invAmount) {
        this.invAmount = invAmount;
    }

    public String getInvoiceId() {
        this.invoiceId = this.invoiceType.concat(this.invoiceNum.toString());
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public BigInteger getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(BigInteger invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getDispSyringe() {
        return dispSyringe;
    }

    public void setDispSyringe(BigDecimal dispSyringe) {
        this.dispSyringe = dispSyringe;
    }

    public BigDecimal getScalpVan() {
        return scalpVan;
    }

    public void setScalpVan(BigDecimal scalpVan) {
        this.scalpVan = scalpVan;
    }

    public BigDecimal getCannula() {
        return cannula;
    }

    public void setCannula(BigDecimal cannula) {
        this.cannula = cannula;
    }

    public BigDecimal getGlassVan() {
        return glassVan;
    }

    public void setGlassVan(BigDecimal glassVan) {
        this.glassVan = glassVan;
    }

    public BigDecimal getVaku() {
        return vaku;
    }

    public void setVaku(BigDecimal vaku) {
        this.vaku = vaku;
    }

    public BigDecimal getSafetyBox() {
        return safetyBox;
    }

    public void setSafetyBox(BigDecimal safetyBox) {
        this.safetyBox = safetyBox;
    }

    public BigDecimal getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(BigDecimal totalPieces) {
        this.totalPieces = totalPieces;
    }

    public BigDecimal getUnolok() {
        return unolok;
    }

    public void setUnolok(BigDecimal unolok) {
        this.unolok = unolok;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCaseQuantity() {
        if (caseQuantity == null || caseQuantity.equalsIgnoreCase("")) {
            return "0";
        }
        return caseQuantity;
    }

    public void setCaseQuantity(String caseQuantity) {
        this.caseQuantity = caseQuantity;
    }

    public String getCompanyDescr() {
        return companyDescr;
    }

    public void setCompanyDescr(String companyDescr) {
        this.companyDescr = companyDescr;
    }

    public BigDecimal getKojak() {
        return kojak;
    }

    public void setKojak(BigDecimal kojak) {
        this.kojak = kojak;
    }

    public BigDecimal getDispNeedle() {
        return dispNeedle;
    }

    public void setDispNeedle(BigDecimal dispNeedle) {
        this.dispNeedle = dispNeedle;
    }

    public BigDecimal getMP() {
        return MP;
    }

    public void setMP(BigDecimal mp) {
        MP = mp;
    }

    public BigDecimal getUP() {
        return UP;
    }

    public void setUP(BigDecimal up) {
        UP = up;
    }

    public BigDecimal getTB() {
        return TB;
    }

    public void setTB(BigDecimal tb) {
        TB = tb;
    }

    public BigDecimal getKitKath() {
        return kitKath;
    }

    public void setKitKath(BigDecimal kitKath) {
        this.kitKath = kitKath;
    }

    public BigDecimal getKitKathPlus() {
        return kitKathPlus;
    }

    public void setKitKathPlus(BigDecimal kitKathPlus) {
        this.kitKathPlus = kitKathPlus;
    }

    public BigDecimal getCathy() {
        return cathy;
    }

    public void setCathy(BigDecimal cathy) {
        this.cathy = cathy;
    }

    public BigInteger getCompanyId() {
        return companyId;
    }

    public void setCompanyId(BigInteger companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getUnolokSyr() {
        return unolokSyr;
    }

    public void setUnolokSyr(BigDecimal unolokSyr) {
        this.unolokSyr = unolokSyr;
    }
}

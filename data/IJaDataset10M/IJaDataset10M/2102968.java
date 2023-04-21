package com.medsol.reports.detail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: vinay Date: 7 Sep, 2008 Time: 3:32:55 PM To
 * change this template use File | Settings | File Templates.
 */
public class SIReport {

    private Long saleId;

    private String formName;

    private String companyName;

    private String companyDescr;

    private String addressLine;

    private String address;

    private String phoneDet;

    private String tinNum;

    private String cstNum;

    private String dlNo1;

    private String dlNo2;

    private String invoiceId;

    private String saleDate;

    private String paymentDueDate;

    private String customerName;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String postalCode;

    private String customerDlNo1;

    private String customerDlNo2;

    private String customerTinNum;

    private String customerCstNum;

    private String docThrough;

    private String lrNo;

    private String carrier;

    private String caseQuantity;

    private String cityStateZip;

    private String orderNo;

    private Date orderDate;

    private String invoiceType;

    private Double invAmount;

    private Double invCost;

    private Double invTax;

    private Double dnAmount;

    private Double cnAmount;

    private Double discount;

    private Double postageAmount;

    private String formComments;

    private String description;

    private String declaration;

    private String txtInvTotal;

    private Double exemptSale = 0.0;

    private Double vat4Amt = 0.0;

    private Double vat4On = 0.0;

    private Double vat12Amt = 0.0;

    private Double vat12On = 0.0;

    private Double cess = 0.0;

    private List<SIRepDetailData> detailSubData;

    private List<SIFooterData> siFooterData;

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescr() {
        return companyDescr;
    }

    public void setCompanyDescr(String companyDescr) {
        this.companyDescr = companyDescr;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneDet() {
        return phoneDet;
    }

    public void setPhoneDet(String phoneDet) {
        this.phoneDet = phoneDet;
    }

    public String getTinNum() {
        return tinNum;
    }

    public void setTinNum(String tinNum) {
        this.tinNum = tinNum;
    }

    public String getCstNum() {
        return cstNum;
    }

    public void setCstNum(String cstNum) {
        this.cstNum = cstNum;
    }

    public String getDlNo1() {
        return dlNo1;
    }

    public void setDlNo1(String dlNo1) {
        this.dlNo1 = dlNo1;
    }

    public String getDlNo2() {
        return dlNo2;
    }

    public void setDlNo2(String dlNo2) {
        this.dlNo2 = dlNo2;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCustomerDlNo1() {
        return customerDlNo1;
    }

    public void setCustomerDlNo1(String customerDlNo1) {
        this.customerDlNo1 = customerDlNo1;
    }

    public String getCustomerDlNo2() {
        return customerDlNo2;
    }

    public void setCustomerDlNo2(String customerDlNo2) {
        this.customerDlNo2 = customerDlNo2;
    }

    public String getCustomerTinNum() {
        return customerTinNum;
    }

    public void setCustomerTinNum(String customerTinNum) {
        this.customerTinNum = customerTinNum;
    }

    public String getCustomerCstNum() {
        return customerCstNum;
    }

    public void setCustomerCstNum(String customerCstNum) {
        this.customerCstNum = customerCstNum;
    }

    public String getDocThrough() {
        return docThrough;
    }

    public void setDocThrough(String docThrough) {
        this.docThrough = docThrough;
    }

    public String getLrNo() {
        return lrNo;
    }

    public void setLrNo(String lrNo) {
        this.lrNo = lrNo;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCaseQuantity() {
        return caseQuantity;
    }

    public void setCaseQuantity(String caseQuantity) {
        this.caseQuantity = caseQuantity;
    }

    public List<SIRepDetailData> getDetailSubData() {
        return detailSubData;
    }

    public void setDetailSubData(List<SIRepDetailData> detailSubData) {
        this.detailSubData = detailSubData;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public List<SIFooterData> getSiFooterData() {
        return siFooterData;
    }

    public void setSiFooterData(List<SIFooterData> siFooterData) {
        this.siFooterData = siFooterData;
    }

    public Double getInvAmount() {
        return invAmount;
    }

    public void setInvAmount(Double invAmount) {
        this.invAmount = invAmount;
    }

    public Double getDnAmount() {
        return dnAmount;
    }

    public void setDnAmount(Double dnAmount) {
        this.dnAmount = dnAmount;
    }

    public Double getCnAmount() {
        return cnAmount;
    }

    public void setCnAmount(Double cnAmount) {
        this.cnAmount = cnAmount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getPostageAmount() {
        return postageAmount;
    }

    public void setPostageAmount(Double postageAmount) {
        this.postageAmount = postageAmount;
    }

    public String getFormComments() {
        return formComments;
    }

    public void setFormComments(String formComments) {
        this.formComments = formComments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public String getTxtInvTotal() {
        return txtInvTotal;
    }

    public void setTxtInvTotal(String txtInvTotal) {
        this.txtInvTotal = txtInvTotal;
    }

    public Double getInvCost() {
        return invCost;
    }

    public void setInvCost(Double invCost) {
        this.invCost = invCost;
    }

    public Double getInvTax() {
        return invTax;
    }

    public void setInvTax(Double invTax) {
        this.invTax = invTax;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Double getExemptSale() {
        return exemptSale;
    }

    public void setExemptSale(Double exemptSale) {
        this.exemptSale = exemptSale;
    }

    public Double getVat4Amt() {
        return vat4Amt;
    }

    public void setVat4Amt(Double vat4Amt) {
        this.vat4Amt = vat4Amt;
    }

    public Double getVat4On() {
        return vat4On;
    }

    public void setVat4On(Double vat4On) {
        this.vat4On = vat4On;
    }

    public Double getVat12Amt() {
        return vat12Amt;
    }

    public void setVat12Amt(Double vat12Amt) {
        this.vat12Amt = vat12Amt;
    }

    public Double getVat12On() {
        return vat12On;
    }

    public void setVat12On(Double vat12On) {
        this.vat12On = vat12On;
    }

    public Double getCess() {
        return cess;
    }

    public void setCess(Double cess) {
        this.cess = cess;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getCityStateZip() {
        return cityStateZip;
    }

    public void setCityStateZip(String cityStateZip) {
        this.cityStateZip = cityStateZip;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }
}

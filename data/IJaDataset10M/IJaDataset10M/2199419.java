package org.koossery.adempiere.core.contract.criteria.payment;

import java.math.BigDecimal;
import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;
import org.koossery.adempiere.core.contract.itf.payment.IC_PaymentProcessorDTO;

public class C_PaymentProcessorCriteria extends KTADempiereBaseCriteria implements IC_PaymentProcessorDTO {

    private static final long serialVersionUID = 1L;

    private int ad_Sequence_ID;

    private int c_BankAccount_ID;

    private int c_Currency_ID;

    private int c_PaymentProcessor_ID;

    private BigDecimal commission;

    private BigDecimal costPerTrx;

    private String description;

    private String hostAddress;

    private int hostPort;

    private BigDecimal minimumAmt;

    private String name;

    private String partnerID;

    private String password;

    private String payProcessorClass;

    private String proxyAddress;

    private String proxyLogon;

    private String proxyPassword;

    private int proxyPort;

    private String userID;

    private String vendorID;

    private String isAcceptAMEX;

    private String isAcceptATM;

    private String isAcceptCheck;

    private String isAcceptCorporate;

    private String isAcceptDiners;

    private String isAcceptDirectDebit;

    private String isAcceptDirectDeposit;

    private String isAcceptDiscover;

    private String isAcceptMC;

    private String isAcceptVisa;

    private String isRequireVV;

    private String isActive;

    public int getAd_Sequence_ID() {
        return ad_Sequence_ID;
    }

    public void setAd_Sequence_ID(int ad_Sequence_ID) {
        this.ad_Sequence_ID = ad_Sequence_ID;
    }

    public int getC_BankAccount_ID() {
        return c_BankAccount_ID;
    }

    public void setC_BankAccount_ID(int c_BankAccount_ID) {
        this.c_BankAccount_ID = c_BankAccount_ID;
    }

    public int getC_Currency_ID() {
        return c_Currency_ID;
    }

    public void setC_Currency_ID(int c_Currency_ID) {
        this.c_Currency_ID = c_Currency_ID;
    }

    public int getC_PaymentProcessor_ID() {
        return c_PaymentProcessor_ID;
    }

    public void setC_PaymentProcessor_ID(int c_PaymentProcessor_ID) {
        this.c_PaymentProcessor_ID = c_PaymentProcessor_ID;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getCostPerTrx() {
        return costPerTrx;
    }

    public void setCostPerTrx(BigDecimal costPerTrx) {
        this.costPerTrx = costPerTrx;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public BigDecimal getMinimumAmt() {
        return minimumAmt;
    }

    public void setMinimumAmt(BigDecimal minimumAmt) {
        this.minimumAmt = minimumAmt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(String partnerID) {
        this.partnerID = partnerID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPayProcessorClass() {
        return payProcessorClass;
    }

    public void setPayProcessorClass(String payProcessorClass) {
        this.payProcessorClass = payProcessorClass;
    }

    public String getProxyAddress() {
        return proxyAddress;
    }

    public void setProxyAddress(String proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    public String getProxyLogon() {
        return proxyLogon;
    }

    public void setProxyLogon(String proxyLogon) {
        this.proxyLogon = proxyLogon;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getIsAcceptAMEX() {
        return isAcceptAMEX;
    }

    public void setIsAcceptAMEX(String isAcceptAMEX) {
        this.isAcceptAMEX = isAcceptAMEX;
    }

    public String getIsAcceptATM() {
        return isAcceptATM;
    }

    public void setIsAcceptATM(String isAcceptATM) {
        this.isAcceptATM = isAcceptATM;
    }

    public String getIsAcceptCheck() {
        return isAcceptCheck;
    }

    public void setIsAcceptCheck(String isAcceptCheck) {
        this.isAcceptCheck = isAcceptCheck;
    }

    public String getIsAcceptCorporate() {
        return isAcceptCorporate;
    }

    public void setIsAcceptCorporate(String isAcceptCorporate) {
        this.isAcceptCorporate = isAcceptCorporate;
    }

    public String getIsAcceptDiners() {
        return isAcceptDiners;
    }

    public void setIsAcceptDiners(String isAcceptDiners) {
        this.isAcceptDiners = isAcceptDiners;
    }

    public String getIsAcceptDirectDebit() {
        return isAcceptDirectDebit;
    }

    public void setIsAcceptDirectDebit(String isAcceptDirectDebit) {
        this.isAcceptDirectDebit = isAcceptDirectDebit;
    }

    public String getIsAcceptDirectDeposit() {
        return isAcceptDirectDeposit;
    }

    public void setIsAcceptDirectDeposit(String isAcceptDirectDeposit) {
        this.isAcceptDirectDeposit = isAcceptDirectDeposit;
    }

    public String getIsAcceptDiscover() {
        return isAcceptDiscover;
    }

    public void setIsAcceptDiscover(String isAcceptDiscover) {
        this.isAcceptDiscover = isAcceptDiscover;
    }

    public String getIsAcceptMC() {
        return isAcceptMC;
    }

    public void setIsAcceptMC(String isAcceptMC) {
        this.isAcceptMC = isAcceptMC;
    }

    public String getIsAcceptVisa() {
        return isAcceptVisa;
    }

    public void setIsAcceptVisa(String isAcceptVisa) {
        this.isAcceptVisa = isAcceptVisa;
    }

    public String getIsRequireVV() {
        return isRequireVV;
    }

    public void setIsRequireVV(String isRequireVV) {
        this.isRequireVV = isRequireVV;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}

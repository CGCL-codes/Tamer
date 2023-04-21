package org.koossery.adempiere.core.contract.itf.payment;

import java.math.BigDecimal;
import com.koossery.fmwk.be.transferableObject.ITransferableObject;

public interface IC_PaymentProcessorDTO extends ITransferableObject {

    public int getAd_Sequence_ID();

    public void setAd_Sequence_ID(int ad_Sequence_ID);

    public int getC_BankAccount_ID();

    public void setC_BankAccount_ID(int c_BankAccount_ID);

    public int getC_Currency_ID();

    public void setC_Currency_ID(int c_Currency_ID);

    public int getC_PaymentProcessor_ID();

    public void setC_PaymentProcessor_ID(int c_PaymentProcessor_ID);

    public BigDecimal getCommission();

    public void setCommission(BigDecimal commission);

    public BigDecimal getCostPerTrx();

    public void setCostPerTrx(BigDecimal costPerTrx);

    public String getDescription();

    public void setDescription(String description);

    public String getHostAddress();

    public void setHostAddress(String hostAddress);

    public int getHostPort();

    public void setHostPort(int hostPort);

    public BigDecimal getMinimumAmt();

    public void setMinimumAmt(BigDecimal minimumAmt);

    public String getName();

    public void setName(String name);

    public String getPartnerID();

    public void setPartnerID(String partnerID);

    public String getPassword();

    public void setPassword(String password);

    public String getPayProcessorClass();

    public void setPayProcessorClass(String payProcessorClass);

    public String getProxyAddress();

    public void setProxyAddress(String proxyAddress);

    public String getProxyLogon();

    public void setProxyLogon(String proxyLogon);

    public String getProxyPassword();

    public void setProxyPassword(String proxyPassword);

    public int getProxyPort();

    public void setProxyPort(int proxyPort);

    public String getUserID();

    public void setUserID(String userID);

    public String getVendorID();

    public void setVendorID(String vendorID);

    public String getIsAcceptAMEX();

    public void setIsAcceptAMEX(String isAcceptAMEX);

    public String getIsAcceptATM();

    public void setIsAcceptATM(String isAcceptATM);

    public String getIsAcceptCheck();

    public void setIsAcceptCheck(String isAcceptCheck);

    public String getIsAcceptCorporate();

    public void setIsAcceptCorporate(String isAcceptCorporate);

    public String getIsAcceptDiners();

    public void setIsAcceptDiners(String isAcceptDiners);

    public String getIsAcceptDirectDebit();

    public void setIsAcceptDirectDebit(String isAcceptDirectDebit);

    public String getIsAcceptDirectDeposit();

    public void setIsAcceptDirectDeposit(String isAcceptDirectDeposit);

    public String getIsAcceptDiscover();

    public void setIsAcceptDiscover(String isAcceptDiscover);

    public String getIsAcceptMC();

    public void setIsAcceptMC(String isAcceptMC);

    public String getIsAcceptVisa();

    public void setIsAcceptVisa(String isAcceptVisa);

    public String getIsRequireVV();

    public void setIsRequireVV(String isRequireVV);

    public String getIsActive();

    public void setIsActive(String _isActive);
}

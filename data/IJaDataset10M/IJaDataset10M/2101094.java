package com.intrigueit.myc2i.payment.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PAYPAL_IPN_LOG")
public class PayPalLog implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "IPN_LOG_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ipnLogId;

    @Column(name = "PAYPAL_TXN_ID")
    private String payPalTxnId;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;

    @Column(name = "MC_GROSS")
    private Double grossAmount;

    @Column(name = "MC_CURRENCY")
    private String currency;

    @Column(name = "PAYER_EMAIL")
    private String payerEmail;

    @Column(name = "TXN_TYPE")
    private String txnType;

    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;

    @Column(name = "NOTIFY_VERSION")
    private Double notifyVersion;

    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "VERIFY_STATUS")
    private String verifyStatus;

    public Long getIpnLogId() {
        return ipnLogId;
    }

    public void setIpnLogId(Long ipnLogId) {
        this.ipnLogId = ipnLogId;
    }

    public String getPayPalTxnId() {
        return payPalTxnId;
    }

    public void setPayPalTxnId(String payPalTxnId) {
        this.payPalTxnId = payPalTxnId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getNotifyVersion() {
        return notifyVersion;
    }

    public void setNotifyVersion(Double notifyVersion) {
        this.notifyVersion = notifyVersion;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
	 * @return the verifyStatus
	 */
    public String getVerifyStatus() {
        return verifyStatus;
    }

    /**
	 * @param verifyStatus the verifyStatus to set
	 */
    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }
}

package sigmacinema.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "TB_PaymentHeader")
public class PaymentHeader {

    @Id
    @Column(name = "payment_id", length = 10, nullable = false)
    private String paymentNo;

    @Column(name = "payment_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "transact_id", length = 20, nullable = false)
    private String transactId;

    @Column(name = "bank_name", length = 20, nullable = false)
    private String bankName;

    @Column(name = "account_bank", length = 20, nullable = false)
    private String accountBank;

    @Column(name = "account_name", length = 50, nullable = false)
    private String accountName;

    @OneToMany(mappedBy = "paymentHeader")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private List<PaymentDetail> PaymentDetail;

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactId() {
        return transactId;
    }

    public void setTransactId(String transactId) {
        this.transactId = transactId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public List<PaymentDetail> getPaymentDetail() {
        return PaymentDetail;
    }

    public void setPaymentDetail(List<PaymentDetail> paymentDetail) {
        PaymentDetail = paymentDetail;
    }
}

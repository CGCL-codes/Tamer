package net.sourceforge.javautil.library.finance.account;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import net.sourceforge.javautil.common.xml.annotation.XmlTag;
import net.sourceforge.javautil.library.finance.account.ReconcileTransaction.Type;

/**
 * The base for most financial transactions.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
@XmlTag(name = "transaction")
@Entity
@Table(name = "financial_transaction")
public class FinancialTransaction {

    protected int id;

    protected FinancialAccount account;

    protected String description;

    protected String payee;

    protected BigDecimal totalAmount;

    protected Date posted;

    protected String transactionId;

    protected Type type;

    protected ReconcileTransaction reconcile;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId")
    public FinancialAccount getAccount() {
        return account;
    }

    public void setAccount(FinancialAccount account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Temporal(TemporalType.DATE)
    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Enumerated(EnumType.STRING)
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reconcileId")
    public ReconcileTransaction getReconcile() {
        return reconcile;
    }

    public void setReconcile(ReconcileTransaction reconcile) {
        this.reconcile = reconcile;
    }
}

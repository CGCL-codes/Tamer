package org.opensecurepay.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Transaction entity bean.
 *
 * @author Christoph Bohl
 * @version $Revision: 1.23 $, $Date: 2006/05/19 20:42:48 $
 *
 * @spring.bean name="Transaction"
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TRANSACTION")
public class Transaction extends BaseEntity implements Serializable {

    private Long id;

    private Date executionDate;

    private Double amount;

    private String text;

    private Boolean accepted;

    /**
	 * <pre>
	 *              0..1   payed with   0..1 
	 * Transaction -------------------------> Payment
	 *              transa           payment 
	 * </pre>
	 */
    private Payment payment;

    /**
	 * <pre>
	 *              0..n        on         0..1 
	 * Transaction ----------------------------- Statement
	 *              transactions      statement 
	 * </pre>
	 */
    private Statement statement;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "EXECUTION_DATE", nullable = false)
    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    @Column(name = "AMOUNT", nullable = false, precision = 10, scale = 2)
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(name = "TEXT", nullable = true, length = 250)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "PAYMENT_ID", nullable = true)
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "STATEMENT_ID", nullable = false)
    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
}

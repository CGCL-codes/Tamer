package net.sf.josas.om;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Class for single accounting entries on accounts. A typical accounting
 * operation is assumed to be spread over several accounting entries, the total
 * amount being balanced to zero.
 *
 * @author frederic
 *
 */
@Entity
@Table(name = "ENTRY")
public class AccountingEntry implements Serializable {

    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    /** Unique Id. */
    private long id;

    /** Version. */
    private int version;

    /** Label. */
    private String label;

    /** Amount. */
    private double amount;

    /** Account. */
    private Account account;

    /** Operation. */
    private AccountingOperation operation;

    /**
     * Get the unique Id.
     *
     * @return the unique Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    public final long getId() {
        return id;
    }

    /**
     * Set the unique Id. This function should be invoked only by the
     * persistence engine.
     *
     * @param anId
     *            Id to set.
     */
    public final void setId(final long anId) {
        this.id = anId;
    }

    /**
     * @return the version
     */
    @Version
    public final int getVersion() {
        return version;
    }

    /**
     * @param aVersion the version to set
     */
    public final void setVersion(final int aVersion) {
        this.version = aVersion;
    }

    /**
     * @return the operation amount
     */
    public final double getAmount() {
        return amount;
    }

    /**
     * @param anAmount
     *            value to set
     */
    public final void setAmount(final double anAmount) {
        this.amount = anAmount;
    }

    /**
     * @param anAmount
     *            value to set
     */
    public final void updateAmount(final double anAmount) {
        getAccount().removeEntry(this);
        this.amount = anAmount;
        getAccount().addEntry(this);
    }

    /**
     * @return the label
     */
    public final String getLabel() {
        return label;
    }

    /**
     * @param aLabel
     *            value to set
     */
    public final void setLabel(final String aLabel) {
        label = aLabel;
    }

    /**
     * Get account.
     *
     * @return the account
     */
    @ManyToOne(cascade = CascadeType.ALL)
    public final Account getAccount() {
        return account;
    }

    /**
     * Set account.
     *
     * @param anAccount
     *            the account to set
     */
    public final void setAccount(final Account anAccount) {
        this.account = anAccount;
    }

    /**
     * Get operation.
     *
     * @return the operation
     */
    @ManyToOne(cascade = CascadeType.ALL)
    public final AccountingOperation getOperation() {
        return operation;
    }

    /**
     * Set operation.
     *
     * @param anOperation
     *            the operation to set
     */
    public final void setOperation(final AccountingOperation anOperation) {
        this.operation = anOperation;
    }
}

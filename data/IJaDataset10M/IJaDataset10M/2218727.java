package org.msb.finance.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Transaction} class represents
 * 
 * @author Marc
 * 
 */
public class Transaction {

    private Date date;

    private Payee payee;

    private List<TransactionDetail> details;

    private Transaction relatedTransaction;

    /**
	 * 
	 */
    public Transaction() {
        details = new LinkedList<TransactionDetail>();
    }

    /**
	 * 
	 * @return
	 */
    public BigDecimal getAmount() {
        BigDecimal amount = new BigDecimal(0);
        synchronized (details) {
            for (TransactionDetail detail : this.details) {
                amount = amount.add(detail.getAmount());
            }
        }
        return amount;
    }

    /**
	 * 
	 * @return
	 */
    public Date getDate() {
        return this.date;
    }

    /**
	 * 
	 * @param date
	 */
    public void setDate(Date date) {
        this.date = (Date) date.clone();
    }

    /**
	 * 
	 * @return
	 */
    public Payee getPayee() {
        return this.payee;
    }

    /**
	 * 
	 * @param payee
	 */
    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    /**
	 * Returns the related {@code Transaction} object, if one exists.
	 * 
	 * @return The related {@code Transaction} object, or {@code null}.
	 */
    public Transaction getRelatedTransaction() {
        return relatedTransaction;
    }

    void setRelatedTransaction(Transaction transaction) {
        relatedTransaction = transaction;
    }

    /**
	 * 
	 * @param detail
	 */
    public void addTransactionDetail(TransactionDetail detail) {
        synchronized (details) {
            details.add(detail);
        }
    }

    /**
	 * 
	 * @param detail
	 */
    public void removeTransactionDetail(TransactionDetail detail) {
        synchronized (details) {
            details.remove(detail);
        }
    }

    /**
	 * 
	 * @return
	 */
    public Iterator<TransactionDetail> getTransactionDetails() {
        synchronized (details) {
            return details.iterator();
        }
    }

    /**
	 * 
	 * @return
	 */
    public int getTransactionDetailCount() {
        synchronized (details) {
            return details.size();
        }
    }

    /**
	 * 
	 * @param t1
	 * @param t2
	 */
    public static void linkTransactions(Transaction t1, Transaction t2) {
        if (null == t1 || null == t2) {
            return;
        }
        if (t1.relatedTransaction != t2) {
            unlinkTransaction(t1);
        }
        if (t2.relatedTransaction != t1) {
            unlinkTransaction(t2);
        }
        t1.relatedTransaction = t2;
        t2.relatedTransaction = t1;
    }

    /**
	 * 
	 * @param t
	 */
    public static void unlinkTransaction(Transaction t) {
        if (null == t || null == t.relatedTransaction) {
            return;
        }
        if (t.relatedTransaction.relatedTransaction == t) {
            t.relatedTransaction.relatedTransaction = null;
        }
        t.relatedTransaction = null;
    }
}

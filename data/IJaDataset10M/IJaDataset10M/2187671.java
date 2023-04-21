package org.msb.finance.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * 
 * @author Marc Boudreau
 * 
 */
public class AccountTransactionsUpdate {

    private Date date;

    private BigDecimal balance;

    private List<Transaction> transactions = new ArrayList<Transaction>();

    /**
	 * Returns the date that this account update took place.
	 * 
	 * @return A {@link Date} object containing the date of the update.
	 */
    public Date getDate() {
        return null == date ? null : (Date) date.clone();
    }

    /**
	 * Sets the date that this update took place to the date contained by the provided {@link Date} object.
	 * 
	 * @param date
	 *            The {@link Date} object containing the date to set.
	 */
    public void setDate(Date date) {
        this.date = (null == date ? null : (Date) date.clone());
    }

    /**
	 * Returns the balance of the account at the time the update took place. This value corresponds to the balance
	 * reported by the source entity for the update.
	 * 
	 * @return A {@link BigDecimal} object containing the balance.
	 */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
	 * Sets the balance of the account at the time the update took place to the value contained by the provided
	 * {@link BigDecimal} object.
	 * 
	 * @param balance
	 *            A {@link BigDecimal} object containing the balance to set.
	 */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
	 * Adds the provided {@link Transaction} object to the list of transactions kept by this update object.
	 * 
	 * @param transaction
	 *            The {@link Transaction} object to add.
	 */
    public void addTransaction(Transaction transaction) {
        if (null != transaction) {
            synchronized (transactions) {
                transactions.add(transaction);
            }
        }
    }

    /**
	 * Removes the provided {@link Transaction} object from the list of transactions kept by this update object.
	 * 
	 * @param transaction
	 *            The {@link Transaction} object to remove.
	 */
    public void removeTransaction(Transaction transaction) {
        if (null != transaction) {
            synchronized (transactions) {
                transactions.remove(transaction);
            }
        }
    }

    /**
	 * Returns an {@link Iterator} over {@link Transaction} objects that can be used to iterate over each transaction
	 * kept in this update object.
	 * 
	 * @return An {@link Iterator} over {@link Transaction} objects.
	 */
    public Iterator<Transaction> getTransactions() {
        synchronized (transactions) {
            return transactions.iterator();
        }
    }

    /**
	 * Returns the number of transactions contained in this update object.
	 * 
	 * @return The number of transactions as an {@code int} value.
	 */
    public int getTransactionCount() {
        synchronized (transactions) {
            return transactions.size();
        }
    }
}

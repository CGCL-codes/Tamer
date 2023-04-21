package net.yapbam.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;

/** A balance history.
 * <br>The balance history is an ordered list of periods, during one of these periods, the balance is constant.
 * These periods are represented by BalanceHistoryElement class.
 * @see BalanceHistoryElement
 * @see BalanceData#getBalanceHistory()
 */
public class BalanceHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Comparator<Object> COMPARATOR = new Comparator<Object>() {

        public int compare(Object o1, Object o2) {
            if (o1 instanceof BalanceHistoryElement) {
                return -((BalanceHistoryElement) o1).getRelativePosition((Date) o2);
            } else {
                return ((BalanceHistoryElement) o2).getRelativePosition((Date) o1);
            }
        }
    };

    private boolean minMaxAccurate;

    private double minBalance;

    private double maxBalance;

    private ArrayList<BalanceHistoryElement> elements;

    private ArrayList<Transaction> transactions;

    /** Constructor.
	 * @param intialBalance The initial balance (at the beginning of times).
	 */
    public BalanceHistory(double intialBalance) {
        super();
        this.minMaxAccurate = false;
        this.elements = new ArrayList<BalanceHistoryElement>();
        this.elements.add(new BalanceHistoryElement(intialBalance, null, null));
        this.transactions = new ArrayList<Transaction>();
    }

    /** Returns the minimum balance of the history.
	 * @return history's minimum balance 
	 */
    public double getMinBalance() {
        refreshMinMax();
        return this.minBalance;
    }

    /** Gets the first date between two dates, when the balance is lower or greater than an amount.
	 * @param from first date or null if the time interval starts at the beginning of times
	 * @param to first date or null if the time interval ends at the end of times
	 * @param alert the alert threshold
	 * @return a long representing the first date under amount. A negative long if the balance is never under the amount between from and to,
	 * the date.getTime() of the searched date in other cases. 0 represents the beginning of times.
	 */
    public long getFirstAlertDate(Date from, Date to, AlertThreshold alert) {
        if (alert.isLifeless()) return -1;
        int firstIndex = from == null ? 0 : find(from);
        int lastIndex = to == null ? elements.size() - 1 : find(to);
        for (int i = firstIndex; i <= lastIndex; i++) {
            BalanceHistoryElement element = elements.get(i);
            if (alert.getTrigger(element.getBalance()) != 0) {
                Date result = element.getFrom();
                return result == null ? 0 : result.getTime();
            }
        }
        return -1;
    }

    /** Returns the maximum balance of the history.
	 * @return history's maximum balance 
	 */
    public double getMaxBalance() {
        refreshMinMax();
        return this.maxBalance;
    }

    private void refreshMinMax() {
        if (!minMaxAccurate) {
            this.maxBalance = get(0).getBalance();
            this.minBalance = this.maxBalance;
            for (Iterator<BalanceHistoryElement> iterator = elements.iterator(); iterator.hasNext(); ) {
                double balance = iterator.next().getBalance();
                if (this.maxBalance < balance) this.maxBalance = balance; else if (this.minBalance > balance) this.minBalance = balance;
            }
            minMaxAccurate = true;
        }
    }

    /** Returns the number of history elements (period with the same balance)
	 * @return the number of periods.
	 */
    public int size() {
        return this.elements.size();
    }

    /** Gets an element (period of time with constant balance) of this history. 
	 * @param index of the element
	 * @return the element
	 */
    public BalanceHistoryElement get(int index) {
        return this.elements.get(index);
    }

    private int find(Date date) {
        return Collections.binarySearch(this.elements, date, COMPARATOR);
    }

    /** Gets a specified date's balance. 
	 * @param date the date for which we want to get the balance
	 * @return the balance
	 */
    public double getBalance(Date date) {
        return get(find(date)).getBalance();
    }

    /** Add an amount to the history at a specified date.
	 * @param amount amount to add (may be negative)
	 * @param date date or null if the amount has to be added at the beginning of times
	 *  (ie the initial balance of a newly created account)
	 */
    void add(double amount, Date date) {
        if (date == null) {
            if (minMaxAccurate) {
                this.minBalance += amount;
                this.maxBalance += amount;
            }
            for (Iterator<BalanceHistoryElement> iterator = elements.iterator(); iterator.hasNext(); ) {
                iterator.next().add(amount);
            }
        } else {
            int index = find(date);
            BalanceHistoryElement element = get(index);
            if (!date.equals(element.getFrom())) {
                BalanceHistoryElement el2 = new BalanceHistoryElement(element.getBalance(), date, element.getTo());
                element.setTo(date);
                index++;
                this.elements.add(index, el2);
            } else {
                BalanceHistoryElement previous = this.elements.get(index - 1);
                double future = element.getBalance() + amount;
                if (GlobalData.AMOUNT_COMPARATOR.compare(previous.getBalance(), future) == 0) {
                    this.elements.remove(index);
                    previous.setTo(element.getTo());
                }
            }
            for (int i = index; i < this.elements.size(); i++) {
                element = this.elements.get(i);
                element.add(amount);
            }
            minMaxAccurate = false;
        }
    }

    /** Adds a transaction to the history. 
	 * @param transaction the added transaction
	 */
    void add(Transaction transaction) {
        this.add(transaction.getAmount(), transaction.getValueDate());
        int index = -Collections.binarySearch(transactions, transaction, TransactionComparator.VALUE_DATE_COMPARATOR) - 1;
        transactions.add(index, transaction);
    }

    /** Removes a transaction from the history.
	 * @param transaction the transactio to be removed
	 */
    void remove(Transaction transaction) {
        this.add(-transaction.getAmount(), transaction.getValueDate());
        int index = Collections.binarySearch(transactions, transaction, TransactionComparator.VALUE_DATE_COMPARATOR);
        if (index >= 0) transactions.remove(index);
    }

    /** Gets the number of transactions in the history.
	 * @return an positive or null integer.
	 */
    public int getTransactionsNumber() {
        return transactions.size();
    }

    /** Gets a transaction in the history.
	 * @param index The transaction's index
	 * @return a Transaction
	 */
    public Transaction getTransaction(int index) {
        return transactions.get(index);
    }
}

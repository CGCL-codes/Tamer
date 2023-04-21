package jgnash.engine;

import jgnash.engine.commodity.CommodityHistoryNode;
import jgnash.engine.commodity.CommodityNode;
import jgnash.engine.commodity.CurrencyNode;
import jgnash.engine.commodity.SecurityNode;
import jgnash.util.DateUtils;
import jgnash.util.SortedArray;
import jgnash.xml.XMLData;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * How InvestmentAccounts work.........
 *
 * 1. Fees impact the cash balance of the account
 * 2. The cash balance is adjusted by transferring money in and out of the account
 *    like a regular transaction, or by a single entry transaction to create
 *    adjustments
 * 3. Fees from InvestmentTransactions are subtracted from the cash balance
 *
 * CashBalance + MarketValue = AccountBalance
 *
 * @author Craig Cavanaugh
 * @author Navneet Karnani
 * @author Milind Kamble
 * @author Tom Edelson
 * 
 */
public class InvestmentAccount extends Account {

    private String accountNumber;

    protected String[] securities = null;

    private Object sMutex = new Object();

    public InvestmentAccount() {
        super();
    }

    public InvestmentAccount(CommodityNode node) {
        super(node);
    }

    public AccountType getAccountType() {
        return AccountType.TYPE_INVEST;
    }

    public void setAccountNumber(String account) {
        accountNumber = account;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    /** 
     * Returns the market value of this account.
     * 
     * @return the current market value (which does not include the cash balance,
     * if any)
     */
    public BigDecimal getMarketValue() {
        return getMarketValue(new Date());
    }

    /** Returns the market value of the account at a specified date.
     * The closest market price is used and only investment transactions
     * earlier and inclusive of the specified date are considered.
     *
     * @param end the end date to calculate the market value
     * @return the ending balance
     */
    public BigDecimal getMarketValue(Date end) {
        if (getTransactionCount() > 0) {
            return getMarketValue(transactions.get(0).getDate(), end);
        } else return ZERO;
    }

    /** Returns the market value for an account
     * @param start inclusive start date
     * @param end  inclusive end date
     * @return the market value (which does not include the cash balance,
     * if any)
     */
    public BigDecimal getMarketValue(Date start, Date end) {
        CommodityNode unitOfValue = getCommodityNode();
        byte decimalPlaces = unitOfValue.getScale();
        CommodityNode[] nodes = getSecuritiesArray();
        BigDecimal balance = ZERO;
        InvestmentAccountSummary holdings;
        synchronized (tMutex) {
            holdings = new InvestmentAccountSummary(this, start, end);
        }
        for (int index = 0; index < nodes.length; index++) {
            BigDecimal quantityHeld = holdings.getSecurityHolding(nodes[index]);
            BigDecimal valueOfHolding = quantityHeld.multiply(getMarketPrice(nodes[index], end));
            BigDecimal rounded = valueOfHolding.setScale(decimalPlaces, BigDecimal.ROUND_HALF_EVEN);
            balance = balance.add(rounded);
        }
        return balance;
    }

    /** Returns a market price for the commodity that is closest to the
     * supplied date without exceeding it.
     *
     * @param node commodity to search against
     * @param date date to search against
     * @return share price
     */
    public BigDecimal getMarketPrice(CommodityNode node, Date date) {
        Date testDate = DateUtils.levelDate(date);
        CommodityHistoryNode hNode = node.getHistoryNode(date);
        BigDecimal rate = new BigDecimal("1");
        if (node instanceof SecurityNode) {
            CurrencyNode sCur = ((SecurityNode) node).getReportedCurrency();
            rate = getEngine().getExchangeRate(sCur, getCommodityNode());
        }
        if (hNode != null && testDate.equals(DateUtils.levelDate(hNode.getDate()))) {
            return new BigDecimal(hNode.getPrice()).multiply(rate);
        }
        Date priceDate = null;
        BigDecimal price = ZERO;
        synchronized (tMutex) {
            final int count = getTransactionCount();
            search: for (int i = 0; i < count; i++) {
                Transaction t = getTransactionAt(i);
                if (t instanceof InvestmentTransaction) {
                    if (((InvestmentTransaction) t).getSecurityNode() == node && ((InvestmentTransaction) t).getPrice() != null) {
                        switch(t.getDate().compareTo(testDate)) {
                            case -1:
                                price = ((InvestmentTransaction) t).getPrice();
                                priceDate = t.getDate();
                                break;
                            case 0:
                                return ((InvestmentTransaction) t).getPrice();
                            case 1:
                            default:
                                break search;
                        }
                    }
                }
            }
        }
        if (hNode == null && priceDate == null) {
            return ZERO;
        } else if (priceDate != null && hNode != null) {
            if (priceDate.compareTo(hNode.getDate()) >= 0) {
                return price;
            }
        } else if (hNode == null) {
            return price;
        }
        return new BigDecimal(hNode.getPrice()).multiply(rate);
    }

    /** Returns the cash balance plus the market value of the shares */
    public BigDecimal getBalance() {
        return getCashBalance().add(getMarketValue());
    }

    /**
     * Returns the running balance of the account based on the current
     * market value of the commodities.
     */
    public BigDecimal getBalanceAt(int index) {
        BigDecimal balance = ZERO;
        synchronized (tMutex) {
            for (int i = 0; i <= index; i++) {
                Transaction tran = transactions.get(i);
                if (tran instanceof DoubleEntryInvestmentTransaction) {
                    DoubleEntryInvestmentTransaction t = (DoubleEntryInvestmentTransaction) tran;
                    if (t.getAccount() == this) {
                        balance = balance.add(t.getAmount());
                    }
                } else {
                    balance = balance.add(tran.getAmount(this));
                }
            }
        }
        return balance;
    }

    public BigDecimal getBalance(Date date) {
        return getCashBalance(date).add(getMarketValue(date));
    }

    /** Returns the balance of the transactions inclusive of the start
     * and end dates.<p> The balance includes the cash transactions and is
     * based on current market value.<p>
     * Overrides the super
     * @param start The inclusive start date
     * @param end   The inclusive end date
     * @return      The ending balance
     */
    public BigDecimal getBalance(Date start, Date end) {
        return getCashBalance(start, end).add(getMarketValue(start, end));
    }

    /** Returns the balance of the transactions inclusive of the start
     * and end dates.<p> The balance includes the cash transactions and is
     * based on current market value.<p>
     * Overrides the super
     * @param start The inclusive start date
     * @param end   The inclusive end date
     * @return      The ending balance
     */
    public BigDecimal getCashBalance(Date start, Date end) {
        assert (start != null && end != null);
        BigDecimal balance = ZERO;
        synchronized (tMutex) {
            int count = transactions.size();
            for (int i = 0; i < count; i++) {
                Transaction t = transactions.get(i);
                Date d = t.getDate();
                if (DateUtils.after(d, start) && DateUtils.before(d, end)) {
                    balance = balance.add(t.getAmount(this));
                }
            }
        }
        return balance;
    }

    /** Returns the cash account balance up to and inclusive of the supplied date
     * @param end The inclusive ending date
     * @return The ending cash balance
     */
    public BigDecimal getCashBalance(Date end) {
        return (transactions.size() > 0) ? getCashBalance(transactions.get(0).getDate(), end) : ZERO;
    }

    /** 
     * Returns the cash balance of this account 
     * 
     * @return the current cash balance
     */
    public BigDecimal getCashBalance() {
        return super.getBalance();
    }

    public void addSecurities(CommodityNode[] list) {
        int length = list.length;
        for (int i = 0; i < length; i++) {
            addSecurity(list[i]);
        }
    }

    /**
     * Addes a security to the list, make sure duplicates are not added.  The
     * list is sorted according to numeric code
     */
    private boolean addSecurity(String symbol) {
        synchronized (sMutex) {
            if (securities != null) {
                if (Arrays.binarySearch(securities, symbol) < 0) {
                    securities = (String[]) SortedArray.insert(securities, symbol);
                    return true;
                }
                return false;
            }
            securities = new String[] { symbol };
            return true;
        }
    }

    /**
     * Adds a commodity to the list, make sure duplicates are not added.  The
     * list is sorted according to numeric code
     * 
     * @param node a CommodityNode representing the commodity
     * (or security) to be added.
     * 
     * @return true if the commodity was added; false if it was already there
     */
    public final boolean addSecurity(CommodityNode node) {
        return addSecurity(node.getKey());
    }

    /**
     * Remove a commodity from the list of commodities
     * that may be held in this account.  Note that
     * securities are included under "commodities".
     * 
     * @param node a CommodityNode representing the commodity to be removed
     * 
     * @return true if the commodity was removed;
     * false if it wasn't there to begin with.
     */
    public final boolean removeSecurity(CommodityNode node) {
        return removeSecurity(node.getKey());
    }

    private boolean removeSecurity(String symbol) {
        synchronized (sMutex) {
            if (securities != null) {
                if (securities.length > 1) {
                    if (SortedArray.contains(securities, symbol)) {
                        securities = (String[]) SortedArray.remove(securities, symbol);
                        return true;
                    } else return false;
                } else {
                    if (securities[0].equals(symbol)) {
                        securities = null;
                        return true;
                    } else return false;
                }
            } else return false;
        }
    }

    public CommodityNode[] getSecuritiesArray() {
        if (securities != null) {
            CommodityNode[] list = new CommodityNode[securities.length];
            for (int i = 0; i < list.length; i++) {
                list[i] = engine.getCommodity(securities[i]);
            }
            return list;
        }
        return new CommodityNode[0];
    }

    public boolean containsSecurity(CommodityNode node) {
        return SortedArray.contains(securities, node.getKey());
    }

    /**
     * 
     * Creates and returns a snapshot of summary information about this account:
     * 
     * - how many shares of each security are held (owned) in the account.
     * 
     * @return the summary.
     * 
     */
    public InvestmentAccountSummary getSummary() {
        InvestmentAccountSummary summ;
        synchronized (tMutex) {
            summ = new InvestmentAccountSummary(this);
        }
        return summ;
    }

    /**
     * 
     * Creates and returns a snapshot of summary information about this account:
     * how many shares of each security were held (owned) in the account,
     * as of a given date.
     * 
     * @param endDate the summary will show the number of shares held on
     * this date.
     * 
     * @return the summary.
     * 
     */
    public InvestmentAccountSummary getSummary(Date endDate) {
        InvestmentAccountSummary summ;
        synchronized (tMutex) {
            summ = new InvestmentAccountSummary(this, endDate);
        }
        return summ;
    }

    /** Returns a shallow clone
     * Transactions are not copied
     * Parent account is not copied
     */
    public Object clone() {
        InvestmentAccount a = new InvestmentAccount(getCommodityNode());
        a.setName(getName());
        a.setStatus(getStatus());
        a.setDescription(getDescription());
        a.setNotes(getNotes());
        a.setVisible(isVisible());
        a.setPlaceHolder(isPlaceHolder());
        a.setLocked(isLocked());
        a.setAccountNumber(getAccountNumber());
        a.setCode(getCode());
        a.securities = securities;
        return a;
    }

    public Object marshal(XMLData xml) {
        super.marshal(xml);
        accountNumber = xml.marshal("accountNumber", accountNumber);
        securities = xml.marshal("securities", securities);
        return this;
    }
}

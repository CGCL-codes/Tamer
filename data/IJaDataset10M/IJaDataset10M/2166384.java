package org.jbudget.Core;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jbudget.io.DataManager;

/**
 * A class that represents bank accounts, credit cards, loans, ets...
 * @author petrov
 */
public class Account implements Comparable<Account> {

    /** numeric ID of the account. */
    private final int id;

    /** Name of the account. */
    private String name;

    /** Account Number. */
    private String accountNumber;

    /** Account Type. */
    private AccountType type = AccountType.UNKNOWN;

    /** Day of the month on which the monthly statement is generated.
   *  0 means 'unknown'
   */
    private int statementDay = 0;

    /** Interest rate. For credit accounts this is the interest that is charged
   * by the bank, and for debit accounts this is the interest that is payed by
   * by the bank to the account holders. The interest rate is given in one 
   * hundredths of a per cent (per year). The rate is assumed to be APY (Average
   * Periodic Yield). -1 means that the interest rate is unknown. 
   */
    private int interestRate = -1;

    /** This is either the credit limit (for credit accounts) or minimum balance
   * that has to be maintained in a debit account. A value of zero means
   * that the limit has not been set. -1 means that the limit is unknown.
   */
    private long limit = -1;

    /** Bank Information. */
    private BankInfo bankInfo = null;

    /** List of UIDs of account holders. */
    UserList holders = new UserList();

    /** Date the account was opened. */
    private Date openedOn;

    /** Date the account was closed. */
    private Date closedOn;

    /** If true the account is (by default) not shown in the list of accounts. */
    private boolean hidden = false;

    /** A random note that can be attached to an account. */
    private String note = null;

    /** Special account used to indicate an 'empty' source/destination. */
    public static Account NONE = new Account("None", -1);

    /** Creates a new instance of Account. */
    public Account(String name) {
        this.name = name;
        id = DataManager.getInstance().generateAccountID();
    }

    /** Creates a new instance of Account with fixed ID. Should only be 
   * used by deserializing code.
   */
    private Account(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /** Get the internal numerical ID of the account. */
    public int getID() {
        return id;
    }

    /** Get the name of the account. */
    public String getName() {
        return name;
    }

    /** Change the name of the account. */
    public void setName(String name) {
        this.name = name;
    }

    /** Get the name of the account with the list of account holders appended
   * to it. For example:  acct name (user1, user2, ...) */
    public String getDecoratedName() {
        List<User> users = holders.getUsers();
        if (users.isEmpty()) return name;
        StringBuffer buf = new StringBuffer();
        buf.append(name);
        buf.append("(");
        int count = 0;
        for (User user : users) {
            if (count == 0) buf.append(user.getName()); else if (count < 2) {
                buf.append(", ");
                buf.append(user.getName());
            } else {
                buf.append("...");
                break;
            }
            count++;
        }
        buf.append(")");
        return buf.toString();
    }

    /** Set the name of the financial institution. */
    public void setBankName(String bankName) {
        if (bankInfo == null) bankInfo = new BankInfo();
        bankInfo.setBankName(bankName);
    }

    /** Returns the name of the financial institution. */
    public String getBankName() {
        return bankInfo == null ? "" : bankInfo.getBankName();
    }

    /** Sets the account number. */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /** Returns the account number. */
    public String getAccountNumber() {
        return accountNumber;
    }

    /** Sets the bank address. */
    public void setBankAddress(String bankAddress) {
        if (bankInfo == null) bankInfo = new BankInfo();
        bankInfo.setBankAddress(bankAddress);
    }

    /** Returns the address of the bank that holds the account. */
    public String getBankAddress() {
        return bankInfo == null ? "" : bankInfo.getBankAddress();
    }

    /** Sets the phone number of the bank that holds the account. */
    public void setBankPhone(String bankPhone) {
        if (bankInfo == null) bankInfo = new BankInfo();
        bankInfo.setBankPhone(bankPhone);
    }

    /** Returns the phone number of the bank that holds the account. */
    public String getBankPhone() {
        return bankInfo == null ? "" : bankInfo.getBankPhone();
    }

    /** Sets the list of uids of users who hold the account. */
    public void setAccountHolderUids(List<Integer> uids) {
        holders.setUids(uids);
    }

    /** Sets the list of uids of users who hold the account. */
    public void setAccountHolders(List<User> users) {
        holders.setUsers(users);
    }

    /** Add a uid of a user to the list of users who hold the account. */
    public void addAccountHolder(int uid) {
        holders.addUser(uid);
    }

    /** Add a user to the list of users who hold the account. */
    public void addAccountHolder(User u) {
        holders.addUser(u);
    }

    /** Returns the list of UIDs of users that hold the account. */
    public List<Integer> getAccountHolderUids() {
        return holders.getUids();
    }

    /** Returns the list of  users that hold the account. */
    public List<User> getAccountHolders() {
        return holders.getUsers();
    }

    /** Sets the opening date. */
    public void setOpeningDate(Date openingDate) {
        this.openedOn = openingDate;
    }

    /** Returns the opening date. */
    public Date getOpeningDate() {
        return openedOn;
    }

    /** Sets the closing date. */
    public void setClosingDate(Date closingDate) {
        this.closedOn = closingDate;
    }

    /** Returns the closing date. */
    public Date getClosingDate() {
        return closedOn;
    }

    /** Returns the value of the hidden flag. */
    public boolean isHidded() {
        return hidden;
    }

    /** Get account type. */
    public AccountType getType() {
        return type;
    }

    /** Set account type. */
    public void setType(AccountType type) {
        this.type = type;
    }

    public int getStatementDay() {
        return statementDay;
    }

    public void setStatementDay(int statementDay) {
        this.statementDay = statementDay;
    }

    /** Get interest rate. The interest rate is given in one hundredths of a 
   per cent. */
    public int getInterestRate() {
        return interestRate;
    }

    /** Set interest rate. The interest rate is given in one hundredths of a 
   per cent. */
    public void setInterestRate(int interestRate) {
        this.interestRate = interestRate;
    }

    /** Get credit limit for credit accounts or minimum balance for debit accounts.
   * The value is in cents. */
    public long getLimit() {
        return limit;
    }

    /** Set credit limit for credit accounts or minimum balance for debit accounts.
   * The value is in cents. */
    public void setLimit(long limit) {
        this.limit = limit;
    }

    /** Returns a string note attached to this account. This can be null. */
    public String getNote() {
        return note;
    }

    /** Attaches a string note to this account. This can be null. */
    public void setNote(String note) {
        this.note = note;
    }

    /** Sets the hidden flag to true, so that the account is not shown in the
   *  list of accounts.
   */
    public void hide() {
        hidden = true;
    }

    /** Sets the hidden flag to false, so that the account is shown in the list
   * of accounts.
   */
    public void show() {
        hidden = false;
    }

    /** Compares the accounts by name */
    public int compareTo(Account oa) {
        if (type != oa.type) return type.compareTo(oa.type);
        return name.compareTo(oa.name);
    }

    /** Returns true if the ID s of both accounts are the same */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Account)) return false;
        Account oa = (Account) o;
        return id == oa.id;
    }

    /** compares all fields of the two accounts to check if they are exact
   *  copies of each other
   */
    public boolean isACopy(Account oa) {
        if (equals(oa)) return true;
        boolean result = name.equals(oa.name);
        result = result && type == oa.type;
        result = result && ((bankInfo == null && oa.bankInfo == null) || bankInfo.equals(oa.bankInfo));
        result = result && statementDay == oa.statementDay;
        result = result && interestRate == oa.interestRate;
        result = result && limit == oa.limit;
        result = result && ((accountNumber == null && oa.accountNumber == null) || (accountNumber != null && oa.accountNumber != null && accountNumber.equals(oa.accountNumber)));
        result = result && holders.equals(oa.holders);
        result = result && ((openedOn == null && oa.openedOn == null) || (openedOn != null && oa.openedOn != null && openedOn.equals(oa.openedOn)));
        result = result && ((closedOn == null && oa.closedOn == null) || (closedOn != null && oa.closedOn != null && openedOn.equals(oa.openedOn)));
        result = result && ((note == null && oa.note == null) || note.equals(oa.note));
        return result;
    }

    @Override
    public String toString() {
        if (DataManager.getInstance().getCurrentUser() != User.SYSTEM) return new String(name); else return getDecoratedName();
    }

    @Override
    public int hashCode() {
        return id;
    }

    /** Returns a JDOM XML Element representation of the current account */
    public Element getXmlElement() {
        Element accountElement = new Element("account");
        accountElement.setAttribute("name", name);
        accountElement.setAttribute("id", Integer.toString(id));
        accountElement.setAttribute("hidden", Boolean.toString(hidden));
        accountElement.setAttribute("statement_day", Integer.toString(statementDay));
        accountElement.setAttribute("interest_rate", Integer.toString(interestRate));
        accountElement.setAttribute("limit", Long.toString(limit));
        accountElement.addContent(type.getXmlElement());
        if (bankInfo != null) accountElement.addContent(bankInfo.getXmlElement());
        if (accountNumber != null) {
            Element e = new Element("account_number");
            e.setAttribute("value", accountNumber);
            accountElement.addContent(e);
        }
        if (openedOn != null) {
            Element e = new Element("opening_date");
            e.setAttribute("value", DateFormat.getDateTimeInstance().format(openedOn));
            accountElement.addContent(e);
        }
        if (closedOn != null) {
            Element e = new Element("closing_date");
            e.setAttribute("value", DateFormat.getDateTimeInstance().format(closedOn));
            accountElement.addContent(e);
        }
        if (note != null) {
            Element e = new Element("note");
            e.setAttribute("value", note);
            accountElement.addContent(e);
        }
        Element e = holders.getXmlElement();
        accountElement.addContent(e);
        return accountElement;
    }

    /** Creates a new Account instance from the given JDOM XML Element.
   * @throws JDOMException if the Element is not well formated.
   */
    public static Account getInstance(Element accountElement) throws JDOMException {
        Attribute attr = accountElement.getAttribute("name");
        if (attr == null) throw new JDOMException("Account.getInstance: No name information");
        String name = attr.getValue();
        attr = accountElement.getAttribute("id");
        if (attr == null) throw new JDOMException("Account.getInstance: No id information");
        int id = attr.getIntValue();
        Account account = new Account(name, id);
        attr = accountElement.getAttribute("hidden");
        if (attr != null && attr.getBooleanValue()) account.hide();
        attr = accountElement.getAttribute("statement_day");
        if (attr != null) account.statementDay = attr.getIntValue();
        attr = accountElement.getAttribute("interest_rate");
        if (attr != null) account.interestRate = attr.getIntValue();
        attr = accountElement.getAttribute("limit");
        if (attr != null) account.limit = attr.getLongValue();
        Element e = accountElement.getChild("account_number");
        if (e != null) account.setAccountNumber(e.getAttribute("value").getValue());
        e = accountElement.getChild("account_type");
        if (e == null) account.type = AccountType.UNKNOWN; else account.type = AccountType.getInstance(e);
        e = accountElement.getChild("bank_info");
        if (e != null) account.bankInfo = BankInfo.getInstance(e);
        e = accountElement.getChild("opening_date");
        if (e != null) {
            Date date;
            try {
                date = DateFormat.getDateTimeInstance().parse(e.getAttribute("value").getValue());
            } catch (ParseException ex) {
                throw new JDOMException("Account.getInstance: Could not parse date: '" + e.getText() + "'");
            }
            account.setOpeningDate(date);
        }
        e = accountElement.getChild("closing_date");
        if (e != null) {
            Date date;
            try {
                date = DateFormat.getDateTimeInstance().parse(e.getAttribute("value").getValue());
            } catch (ParseException ex) {
                throw new JDOMException("Account.getInstance: Could not parse date: '" + e.getText() + "'");
            }
            account.setClosingDate(date);
        }
        e = accountElement.getChild("note");
        if (e != null) account.note = e.getAttribute("value").getValue();
        e = accountElement.getChild("uids");
        if (e != null) account.holders = UserList.getInstance(e);
        return account;
    }

    /** A class that is used to identify different account types i.e. Credit Card
   * accounts, Loan, Investment, Deposit etc. */
    public static class AccountType {

        private static List<AccountType> allTypes = new ArrayList<AccountType>();

        public static AccountType UNKNOWN = new AccountType("Unknown", false);

        public static AccountType DEBIT = new AccountType("Debit", false);

        public static AccountType CREDIT = new AccountType("Credit", true);

        /** A human readable string that identifies the account type. */
        private final String name;

        /** Is this a credit/or debit account type? */
        private final boolean isCredit;

        private AccountType(String name, boolean isCredit) {
            this.name = name;
            this.isCredit = isCredit;
            allTypes.add(this);
        }

        @Override
        public String toString() {
            return name;
        }

        public boolean isCredit() {
            return isCredit;
        }

        public boolean isDebit() {
            return !isCredit;
        }

        public int compareTo(AccountType ot) {
            if (this == ot) return 0;
            if (this == DEBIT) return 1;
            if (ot == DEBIT) return -1;
            if (this == CREDIT) return 1;
            return -1;
        }

        /** Returns a JDOM XML Element representation of the current account type */
        public Element getXmlElement() {
            Element element = new Element("account_type");
            element.setAttribute("name", name);
            return element;
        }

        /** Creates a new Account instance from the given JDOM XML Element.
     * @throws JDOMException if the Element is not well formated.
     */
        public static AccountType getInstance(Element element) throws JDOMException {
            Attribute attr = element.getAttribute("name");
            if (attr == null) throw new JDOMException("AccountType.getInstance: No name information");
            String name = attr.getValue();
            for (AccountType type : allTypes) if (type.name.equals(name)) return type;
            return UNKNOWN;
        }
    }

    /** Bank name, address, etc... */
    public static class BankInfo {

        /** Name of the financial institution. */
        private String bankName = "";

        /** Address of the financial institution. */
        private String bankAddress = "";

        /** Phone number of the financial institution. */
        private String bankPhone = "";

        /** Set the name of the financial institution. */
        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        /** Returns the name of the financial institution. */
        public String getBankName() {
            return bankName;
        }

        /** Sets the bank address. */
        public void setBankAddress(String bankAddress) {
            this.bankAddress = bankAddress;
        }

        /** Returns the address of the bank that holds the account. */
        public String getBankAddress() {
            return bankAddress;
        }

        /** Sets the phone number of the bank that holds the account. */
        public void setBankPhone(String bankPhone) {
            this.bankPhone = bankPhone;
        }

        /** Returns the phone number of the bank that holds the account. */
        public String getBankPhone() {
            return bankPhone;
        }

        @Override
        public boolean equals(Object oi) {
            if (!(oi instanceof BankInfo)) return false;
            BankInfo bi = (BankInfo) oi;
            return bankName.equals(bi.bankName) && bankAddress.equals(bi.bankAddress) && bankPhone.equals(bi.bankPhone);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 11 * hash + (this.bankName != null ? this.bankName.hashCode() : 0);
            hash = 11 * hash + (this.bankAddress != null ? this.bankAddress.hashCode() : 0);
            hash = 11 * hash + (this.bankPhone != null ? this.bankPhone.hashCode() : 0);
            return hash;
        }

        /** Returns a JDOM XML Element representation of the current account type */
        public Element getXmlElement() {
            Element element = new Element("bank_info");
            element.setAttribute("name", bankName);
            element.setAttribute("address", bankAddress);
            element.setAttribute("phone", bankPhone);
            return element;
        }

        /** Creates a new BankInfo instance from the given JDOM XML Element. */
        public static BankInfo getInstance(Element element) {
            BankInfo info = new BankInfo();
            Attribute attr = element.getAttribute("name");
            if (attr != null) info.bankName = attr.getValue();
            attr = element.getAttribute("address");
            if (attr != null) info.bankAddress = attr.getValue();
            attr = element.getAttribute("phone");
            if (attr != null) info.bankPhone = attr.getValue();
            return info;
        }
    }

    /** Comparator that compares two accounts by name. */
    public static class NameComparator implements Comparator<Account> {

        public int compare(Account o1, Account o2) {
            return o1.name.compareTo(o2.name);
        }
    }
}

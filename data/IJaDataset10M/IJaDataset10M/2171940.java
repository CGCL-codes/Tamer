package org.jbudget.Core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jbudget.io.DataManager;

/**
 * This is a generalized class that describes some entity that can serve as a
 * source or destination for a transaction. This can be either an Account where
 * money is stored, an IncomeSource -- source of income that can only serve as
 * a source of a transaction, and an ExpenseCategory that describes how money 
 * was spent. An ExpenseCategory can only serve as a destination of a 
 * transaction.
 * @author petrov
 */
public class MoneyPit implements Comparable<MoneyPit> {

    /** Type of this object */
    private final Type type;

    private final Account account;

    private final ExpenseCategory expenseCategory;

    private final IncomeSource incomeSource;

    private final Budget budget;

    /** Empty MoneyPit */
    public static MoneyPit NONE = new MoneyPit();

    /** Creates a MoneyPit with an unknown type. */
    private MoneyPit() {
        type = Type.UNKNOWN;
        this.account = null;
        this.expenseCategory = null;
        this.incomeSource = null;
        this.budget = null;
    }

    /** Creates a new instance of MoneyPit that represents an 
     * {@link Account Account} 
     */
    public MoneyPit(Account account) {
        type = Type.ACCOUNT;
        this.account = account;
        this.expenseCategory = null;
        this.incomeSource = null;
        this.budget = null;
    }

    /** Creates a new instance of MoneyPit that represents an 
     * {@link ExpenseCategory ExpenseCategory} 
     */
    public MoneyPit(ExpenseCategory expenseCategory, Budget budget) {
        type = Type.EXPENSE;
        this.account = null;
        this.expenseCategory = expenseCategory;
        this.incomeSource = null;
        this.budget = budget;
    }

    /** Creates a new instance of MoneyPit that represents an 
     * {@link IncomeSource IncomeSource} 
     */
    public MoneyPit(IncomeSource incomeSource, Budget budget) {
        type = Type.INCOME;
        this.account = null;
        this.expenseCategory = null;
        this.incomeSource = incomeSource;
        this.budget = budget;
    }

    public MoneyPit(MoneyPit pit, Budget budget) {
        this.type = pit.type;
        this.account = pit.account;
        this.expenseCategory = pit.expenseCategory;
        this.incomeSource = pit.incomeSource;
        this.budget = budget;
    }

    /** Returns true if this object can serve as a source of money */
    public boolean canBeSource() {
        return true;
    }

    /** Returns true if this object can serve as a destination of money */
    public boolean canBeDestination() {
        return type != Type.INCOME;
    }

    /** Returns the type of this object */
    public Type getType() {
        return type;
    }

    /** Returns the account which this object represents.
     * @throws IllegalStateException if the type of this object is not ACCOUNT
     */
    public Account getAccount() {
        if (this == NONE) return Account.NONE;
        if (type != Type.ACCOUNT) throw new IllegalStateException("Trying to get account from object that" + " represents " + type.getKey());
        return account;
    }

    /** Returns the ExpenseCategory which this object represents.
     * @throws IllegalStateException if the type of this object is not EXPENSE
     */
    public ExpenseCategory getExpenseCategory() {
        if (this == NONE) return ExpenseCategory.NONE;
        if (type != Type.EXPENSE) throw new IllegalStateException("Trying to get expense category from " + "object that represents " + type.getKey());
        return expenseCategory;
    }

    /** Returns the IncomeSource which this object represents.
     * @throws IllegalStateException if the type of this object is not INCOME
     */
    public IncomeSource getIncomeSource() {
        if (this == NONE) return IncomeSource.NONE;
        if (type != Type.INCOME) throw new IllegalStateException("Trying to get income source from " + "object that represents " + type.getKey());
        return incomeSource;
    }

    /** Returns the Budget associated with this income source.
     * This is only valid for the MoneyPits that correspond to an
     * ExpenseCategory or to an IncomeSource.
     */
    public Budget getBudget() {
        if (type != Type.EXPENSE && type != Type.INCOME) throw new RuntimeException("getBudget() cannot be called for " + "MoneyPits other than EXPENSE or INCOME");
        return budget;
    }

    /** Returns the object that this money pit points to. (Account, 
     * ExpenseCategory or an IncomeSource).
     */
    public Object getContent() {
        if (type == Type.ACCOUNT) return account;
        if (type == Type.EXPENSE) return expenseCategory;
        if (type == Type.INCOME) return incomeSource;
        return Account.NONE;
    }

    /** Returns a String that represents the name of the 
     * Account/IncomeSource/ExpenseCategory which is represented by this object.
     * For this version no preable is used just the name is returned.
     */
    public String getName() {
        if (type == Type.ACCOUNT) return account.getName();
        if (type == Type.EXPENSE) return expenseCategory.getName();
        if (type == Type.INCOME) return incomeSource.getName();
        if (this == NONE) return "None";
        throw new IllegalStateException("Unkown MoneyPit type: " + type.getKey());
    }

    /** Returns a String that represents the name of the 
     * Account/IncomeSource/ExpenseCategory which is represented by this object.
     * For this version short preable is used just the name is returned. The 
     * preamble in the account is "ACCT:", for expense "EXP:", income "INC:".
     */
    public String getNameShort() {
        if (type == Type.ACCOUNT) return "ACCT:" + account.getName();
        if (type == Type.EXPENSE) return "EXP:" + expenseCategory.getName();
        if (type == Type.INCOME) return "INC:" + incomeSource.getName();
        if (this == NONE) return "None";
        throw new IllegalStateException("Unkown MoneyPit type: " + type.getKey());
    }

    /** Returns a String that represents the name of the 
     * Account/IncomeSource/ExpenseCategory which is represented by this object.
     * For this version lont preable is used just the name is returned. The 
     * preamble in the account is "Account:", 
     * for expense "Expense:", 
     * income "Income:".
     */
    public String getNameLong() {
        if (type == Type.ACCOUNT) return "Account:" + account.getName();
        if (type == Type.EXPENSE) return "Expense:" + expenseCategory.getName();
        if (type == Type.INCOME) return "Income:" + incomeSource.getName();
        if (this == NONE) return "None";
        if (type == Type.UNKNOWN) return "Unknown";
        throw new IllegalStateException("Unkown MoneyPit type: " + type.getKey());
    }

    public String getDecoratedName() {
        if (account != null) return account.getDecoratedName();
        if (expenseCategory != null) return expenseCategory.getDecoratedName();
        if (incomeSource != null) return incomeSource.getDecoratedName();
        if (this == NONE) return "None";
        return new String("IVALID");
    }

    public int compareTo(MoneyPit oi) {
        if (oi == null) return 1;
        int key = type.compareTo(oi.type);
        if (key != 0) return key;
        if (type == Type.UNKNOWN) return 0;
        if (type == Type.ACCOUNT) if (oi.type == Type.ACCOUNT) return account.compareTo(oi.account); else return 1; else if (type == Type.EXPENSE) if (oi.type == Type.EXPENSE) return expenseCategory.compareTo(oi.expenseCategory); else if (oi.type == Type.ACCOUNT) return -1; else return 1; else if (oi.type == Type.INCOME) return incomeSource.compareTo(oi.incomeSource); else return -1;
    }

    /** Returns true if the other instance is a copy of the given instance */
    public boolean isACopy(MoneyPit oi) {
        int key = type.compareTo(oi.type);
        if (key != 0) return false;
        if (this == NONE && oi == NONE) return true;
        if (type == Type.ACCOUNT) return account.isACopy(oi.account); else if (type == Type.EXPENSE) return expenseCategory.isACopy(oi.expenseCategory); else return incomeSource.isACopy(oi.incomeSource);
    }

    /** Returns true if the other instance is a copy of the given instance */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MoneyPit)) return false;
        MoneyPit oi = (MoneyPit) o;
        if (oi == this) return true;
        if (type != oi.type) return false;
        if (type == MoneyPit.Type.UNKNOWN) return true;
        if (type == Type.ACCOUNT) return account.equals(oi.account); else if (type == Type.EXPENSE) return expenseCategory.equals(oi.expenseCategory); else return incomeSource.equals(oi.incomeSource);
    }

    @Override
    public String toString() {
        return getContent().toString();
    }

    @Override
    public int hashCode() {
        if (type == Type.ACCOUNT) return 7 * account.hashCode();
        if (type == Type.EXPENSE) return 11 * expenseCategory.hashCode();
        if (type == Type.INCOME) return 17 * incomeSource.hashCode();
        return 0;
    }

    /** Returns a JDOM XML Element representation of the current account 
     */
    public Element getXmlElement() {
        Element element = new Element("money_pit");
        element.setAttribute("type", type.getKey());
        if (type == Type.ACCOUNT) element.setAttribute("account_id", Integer.toString(account.getID())); else if (type == Type.EXPENSE) {
            element.setAttribute("budget_id", Integer.toString(budget.getID()));
            element.setAttribute("expense_category_id", Integer.toString(expenseCategory.getID()));
        } else if (type == Type.INCOME) {
            element.setAttribute("budget_id", Integer.toString(budget.getID()));
            element.setAttribute("income_source_id", Integer.toString(incomeSource.getID()));
        }
        return element;
    }

    /** Returns the MoneyPit object that was saved in a JDOM Element 
     *@throws JDOMException if the Element is not well formated some information
     *        is inconsistent or missing
     *@param element JDOM Element that contians a description of a MoneyPit
     *       object.
     *@param budget a budget that contains the IncomeSources and ExpenseCategories
     *       that could be used as a MoneyPit
     */
    public static MoneyPit getInstance(Element element, Budget budget) throws JDOMException, IOException {
        Attribute attr = element.getAttribute("type");
        if (attr == null) throw new JDOMException("No Type information");
        Type type;
        try {
            type = Type.getType(attr.getValue());
        } catch (IllegalArgumentException ex) {
            throw new JDOMException(ex.getMessage());
        }
        if (type == Type.UNKNOWN) return NONE;
        if (type == Type.ACCOUNT) {
            attr = element.getAttribute("account_id");
            if (attr == null) throw new JDOMException("No account information");
            Account account = DataManager.getInstance().getAccount(attr.getIntValue());
            if (account == null) throw new JDOMException("Bad account information");
            return new MoneyPit(account);
        } else {
            attr = element.getAttribute("budget_id");
            if (attr == null) throw new JDOMException("No budget information");
            if (budget.getID() != attr.getIntValue()) throw new JDOMException("Bad budget argument. The ID doesn't match");
            if (type == Type.EXPENSE) {
                attr = element.getAttribute("expense_category_id");
                if (attr == null) throw new JDOMException("No expense category information");
                int id = attr.getIntValue();
                List<ExpenseCategory> ecL = budget.getAllExpenseCategories();
                for (ExpenseCategory c : ecL) if (c.getID() == id) return new MoneyPit(c, budget);
            } else if (type == Type.INCOME) {
                attr = element.getAttribute("income_source_id");
                if (attr == null) throw new JDOMException("No income source information");
                int id = attr.getIntValue();
                List<IncomeSource> isL = budget.getIncomeSources();
                for (IncomeSource is : isL) if (is.getID() == id) return new MoneyPit(is, budget);
            } else throw new JDOMException("Unknown Type");
        }
        throw new JDOMException("Parsing error");
    }

    /** An inner class that describes the type (Account, IncomeSource, 
     * BudgetCategory) of the parent object 
     */
    public static class Type implements Comparable<Type> {

        /** A description of the Type that is used to save and restore it from
         * files in a human readable form 
         */
        private final String key;

        /** A list of all types */
        private static final List<Type> allTypes = new ArrayList<Type>(5);

        public static Type ACCOUNT = new Type("Account");

        public static Type EXPENSE = new Type("Expense");

        public static Type INCOME = new Type("Income");

        public static Type UNKNOWN = new Type("Unknown");

        private Type(String key) {
            this.key = key;
            allTypes.add(this);
        }

        /** Returns a String describing the current type. The string can be used
         * to get the otginal type by calling {@link #getType(String)} getType}
         */
        public String getKey() {
            return key;
        }

        /** Returns the Type that has the spesified key. 
         * @throws IllegalArguementException if there is no type with the given key.
         */
        public static Type getType(String key) {
            for (Type t : allTypes) if (key.equalsIgnoreCase(t.getKey())) return t;
            throw new IllegalArgumentException("There is no MoneyPit.Type with" + " key: '" + key + "'");
        }

        public int compareTo(Type ot) {
            if (ot == this) return 0;
            return key.compareTo(ot.key);
        }
    }
}

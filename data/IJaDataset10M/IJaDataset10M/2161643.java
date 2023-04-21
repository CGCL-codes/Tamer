package org.jbudget.Core;

import java.io.IOException;
import org.jdom.Element;
import org.jdom.JDOMException;

/** A class that describes a specific sum of money that is allocated to  
 * a particular budget category at specific time intervals. It is a special
 * kind of automatic transaction with null source. The destination is some 
 * ExpenseCategory. The allocations do not lead to actual transfer of any funds
 * from any accounts to the particular ExpenseCategory. They are used for 
 * reserving some funds for this ExpenseCategory. Actual transactions reduce the 
 * amount of available funds. For example. Assume that at the beginning of a 
 * month $500 is allocated fo groceries. Then in the beginning of the month
 * the Groceries ExpenseCategory will show $500 available funds. Every time user
 * buys groceries he will create a transaction with source in some account and 
 * a destination in the Groceries ExpenseCategory. Every such transaction will
 * reduce the amount of available funds. 
 *
 * @author petrov
 */
public class ExpenseAllocation extends AutomaticTransaction {

    /**
     * Creates a new instance of MONTHLY ExpenseAllocation that gets
     * allocated on the first day of the month.
     * 
     * @param expenseCategory the ExpenseCategory for which the allocation is made.
     * @param amount the amount of the allocation.
     */
    public ExpenseAllocation(ExpenseCategory expenseCategory, Budget budget, long amount) {
        super(MoneyPit.NONE, new MoneyPit(expenseCategory, budget), amount);
    }

    /**
     * Creates a new instance ExpenseAllocation.
     * 
     * 
     * @param expenseCategory the ExpenseCategory for which the allocation is made.
     * @param amount the amount of the allocation.
     * @param allocationType the type of the allocation. Monthly, Weekly, etc...
     * @param time the time at which the allocation is made. For example 1 
     *        corresponds to the first day of the (Month, Week, etc), -1 
     *        corresponds to the last day of...
     */
    public ExpenseAllocation(ExpenseCategory expenseCategory, Budget budget, long amount, Type allocationType, int time) {
        super(MoneyPit.NONE, new MoneyPit(expenseCategory, budget), amount, allocationType, time);
    }

    /** Type conversion from an AutomaticTransaction to an ExpenseAllocation */
    public ExpenseAllocation(AutomaticTransaction autoTr) {
        super(autoTr.getSource(), autoTr.getDestination(), autoTr.getAmount(), autoTr.getType(), autoTr.getTime());
        MoneyPit dest = autoTr.getDestination();
        if (dest.getType() != MoneyPit.Type.EXPENSE) throw new IllegalArgumentException("Incorrect destination. " + dest.toString());
    }

    /** Do not use */
    private ExpenseAllocation(MoneyPit source, MoneyPit destination, long amount) {
        super(source, destination, amount);
    }

    /** Do not use */
    private ExpenseAllocation(MoneyPit source, MoneyPit destination, long amount, Type type, int time) {
        super(source, destination, amount, type, time);
    }

    /**
     * Creates a new instance of the ExpenseAllocation attached to a different
     *  expenseCategory 
     * 
     * @param expenseCategory the new {@link ExpenseCategory Budget Category}.
     */
    public ExpenseAllocation newExpenseCategory(ExpenseCategory expenseCategory, Budget budget) {
        return (ExpenseAllocation) newDestination(new MoneyPit(expenseCategory, budget));
    }

    /** Returns the ExpenseCategory object to which this allocation belongs */
    public ExpenseCategory getExpenseCategory() {
        return getDestination().getExpenseCategory();
    }

    /**
     * Creates a new instance of the ExpenseAllocation with different amount 
     * 
     * @param amount the new amount in _cents_.
     */
    @Override
    public ExpenseAllocation newAmount(long amount) {
        return new ExpenseAllocation(MoneyPit.NONE, getDestination(), amount, getType(), getTime());
    }

    /**
     * Creates a new instance of the ExpenseAllocation with a different 
     * destination of funds.
     * 
     * @param destination the new {@link MoneyPit destination} of the transaction.
     */
    @Override
    public ExpenseAllocation newDestination(MoneyPit destination) {
        return new ExpenseAllocation(MoneyPit.NONE, destination, getAmount(), getType(), getTime());
    }

    /**
     * Creates a new instance of the ExpenseAllocation with different time.
     * 
     * @param time the new value of the {@link #time time} parameter.
     */
    @Override
    public ExpenseAllocation newTime(int time) {
        return new ExpenseAllocation(MoneyPit.NONE, getDestination(), getAmount(), getType(), time);
    }

    /** Returns a JDOM XML Element representation of the current expense 
     * allocation 
     */
    @Override
    public Element getXmlElement() {
        Element element = super.getXmlElement();
        element.setName("expense_allocation");
        return element;
    }

    /** Creates an ExpenseAllocation object from an XML JDOM Element.
     * @throws JDOMException if for some reason the operation can not be 
     *         completed.
     * @param element the JDOM Element that contains a description of an
     *        ExpenseAllocation.
     * @param budget a budget that contains the IncomeSources and 
     *        ExpenseCategories that could be used as a source or a destination
     *        of the transaction.
     */
    public static ExpenseAllocation getInstance(Element element, Budget budget) throws JDOMException, IOException {
        AutomaticTransaction autoTr = AutomaticTransaction.getInstance(element, budget);
        return new ExpenseAllocation(autoTr);
    }

    @Override
    public String toString() {
        int dollars = (int) getAmount() / 100;
        int cents = (int) (getAmount() - dollars * 100);
        String result = getType().name + " allocation of " + dollars + " dollars";
        if (cents > 0) result += " and " + cents + " cents";
        return result + "\n" + super.toString();
    }
}

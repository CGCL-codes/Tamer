package org.jbudget.gui.trans.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jbudget.Core.Account;
import org.jbudget.Core.Budget;
import org.jbudget.Core.ExpenseCategory;
import org.jbudget.Core.MoneyPit;
import org.jbudget.Core.User;

/** Model for a JList that contains a list of possible destinations
 * for a transaction. */
public class DestinationsModel extends ModelBase {

    private final Budget budget;

    public DestinationsModel(Budget budget, User user) {
        this.budget = budget;
        itemsList.add(noneLabel);
        if (budget == null) return;
        List<Account> accountsList = new ArrayList<Account>();
        accountsList.addAll(budget.getRegisteredAccounts());
        Collections.sort(accountsList);
        if (accountsList.size() > 0) {
            firstAccountIndex = 3;
            firstIncomeIndex = 5;
            itemsList.add(accountsSeparator);
            itemsList.add(accountsLabel);
            for (Account account : accountsList) {
                if (account.getAccountHolders().contains(user) || user == User.SYSTEM) {
                    MoneyPit item = new MoneyPit(account);
                    itemsList.add(item);
                    if (budget.isAmbiguous(item)) decoratedItems.add(item);
                    firstIncomeIndex++;
                }
            }
        }
        List<ExpenseCategory> expensesList = new ArrayList<ExpenseCategory>();
        expensesList.addAll(budget.getAllExpenseCategoriesSorted());
        if (expensesList.size() > 0) {
            itemsList.add(expensesSeparator);
            itemsList.add(expensesLabel);
            for (ExpenseCategory category : expensesList) {
                if ((user == User.SYSTEM || (!category.getSelectUsers()) || category.getHolders().contains(user)) && category.getHoldsTransactions()) {
                    MoneyPit item = new MoneyPit(category, budget);
                    itemsList.add(item);
                    if (budget.isAmbiguous(item)) decoratedItems.add(item);
                }
            }
        }
    }
}

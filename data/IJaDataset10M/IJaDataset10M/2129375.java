package org.lcelb.accounts.manager.ui.workbench.internal.helpers.transactions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.ui.IWorkbenchPage;
import org.lcelb.accounts.manager.common.helper.MiscConverter;
import org.lcelb.accounts.manager.data.AbstractOwner;
import org.lcelb.accounts.manager.data.Account;
import org.lcelb.accounts.manager.data.Bank;
import org.lcelb.accounts.manager.data.extensions.transaction.MonthTransactions;
import org.lcelb.accounts.manager.data.extensions.transaction.YearTransactions;
import org.lcelb.accounts.manager.data.helper.DataGetter;
import org.lcelb.accounts.manager.data.helper.DataHelper;
import org.lcelb.accounts.manager.data.transaction.AbstractTransaction;
import org.lcelb.accounts.manager.ui.workbench.internal.helpers.ViewHelper;
import org.lcelb.accounts.manager.ui.workbench.views.transactions.TransactionsView;

public class UIRemovalHelper {

    /**
   * Hide a month transaction.
   * 
   * @param page_p
   * @param month_p
   */
    public static void hideMonthTransaction(IWorkbenchPage page_p, MonthTransactions month_p) {
        YearTransactions year = DataGetter.getYearFrom(month_p);
        AbstractOwner activeOwner = DataHelper.getActiveOwner();
        Account account = DataGetter.getAccountFor(year, activeOwner);
        Bank bank = DataHelper.getBankFor(account);
        String secondaryId = MiscConverter.getSecondaryId(bank.getId(), account.getId(), year.getId(), month_p.getId());
        ViewHelper.hideView(page_p, TransactionsView.VIEW_ID, secondaryId);
    }

    /**
   * Hide a year transaction.
   * 
   * @param page_p
   * @param year_p
   */
    public static void hideYearTransaction(IWorkbenchPage page_p, YearTransactions year_p) {
        AbstractTransaction[] monthTransactions = MiscConverter.convertListToArray(year_p.getTransactions());
        for (int monthPosition = 0; monthPosition < monthTransactions.length; monthPosition++) {
            MonthTransactions month = (MonthTransactions) monthTransactions[monthPosition];
            hideMonthTransaction(page_p, month);
        }
    }

    /**
   * Hide an account.
   * 
   * @param page_p
   * @param account_p
   */
    @SuppressWarnings("unchecked")
    public static void hideAccount(IWorkbenchPage page_p, Account account_p) {
        List yearTransactions = new ArrayList(account_p.getTransactions());
        Iterator iterator = yearTransactions.iterator();
        while (iterator.hasNext()) {
            YearTransactions year = (YearTransactions) iterator.next();
            UIRemovalHelper.hideYearTransaction(page_p, year);
        }
    }
}

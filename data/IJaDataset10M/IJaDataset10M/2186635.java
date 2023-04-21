package net.bobgardner.cash;

import static javax.swing.SwingUtilities.invokeLater;
import net.bobgardner.cash.model.*;
import net.bobgardner.cash.view.AccountView;
import org.joda.time.DateMidnight;
import java.math.BigDecimal;

/**
 * Main class for QuickCash.
 * 
 * @author wrg007 (Bob Gardner)
 */
public class App {

    public static void main(String[] args) {
        makeFakeData();
        invokeLater(new Runnable() {

            public void run() {
                new AccountView().setVisible(true);
            }
        });
    }

    private static void makeFakeData() {
        Category c0 = Category.newCategory("Name0", "Desc0");
        Category c1 = Category.newCategory("Name1", "Desc1");
        Account account = Account.newAccount(Cashbox.INSTANCE, "WF Checking", "Wells Fargo", "123456789", Account.Type.CHECKING, "");
        Transaction t = Transaction.newTransaction(account, new DateMidnight(), "Albertsons", "101");
        LineItem.newLineItem(t, BigDecimal.TEN, c0, "Line Item 0");
        LineItem.newLineItem(t, BigDecimal.TEN, c1, "Line Item 1");
        t = Transaction.newTransaction(account, new DateMidnight(), "Sams Club", "");
        LineItem.newLineItem(t, new BigDecimal("-10.00"), c1, "Line Item 0");
        Account.newAccount(Cashbox.INSTANCE, "WF Savings", "Wells Fargo", "234567891", Account.Type.SAVINGS, "");
    }
}

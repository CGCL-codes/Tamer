package nu.esox.accounting;

import javax.swing.*;
import nu.esox.util.*;

@SuppressWarnings("serial")
public class BalanceReportPanel extends ReportPanel {

    public BalanceReportPanel() {
        init("balance-table", new Account.Type[] { Account.TYPE_TURNOVER_ASSET, Account.TYPE_FACILITY_ASSET, Account.TYPE_SHORT_DEPT, Account.TYPE_LONG_DEPT, Account.TYPE_OWN_CAPITAL }, new Section[] { new TypeSection(0), new TypeSection(1), new SumSection("SUMMA TILLGÅNGAR", new int[] { 0, 1 }), new TypeSection(2), new TypeSection(3), new TypeSection(4), new SumSection("SUMMA SKULDER", new int[] { 2, 3, 4 }), new SumSection("Beräknat resultat", new int[] { 0, 1, 2, 3, 4 }) });
    }

    protected JTable createTable(Account account) {
        return new BalanceReportTable(account);
    }

    protected JTable createTable(ObservableList<Account> accounts) {
        return new BalanceReportTable(accounts);
    }

    protected boolean isAccountEmpty(Account a) {
        return (a.getAmount() == 0) && (a.getBudget() == 0) && (a.getIb() == 0);
    }
}

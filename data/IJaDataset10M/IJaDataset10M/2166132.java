package dakside.hacc.modules.transactions;

import dakside.hacc.core.plugins.Function;
import dakside.hacc.core.plugins.Unloadable;
import dakside.hacc.modules.transactions.trxman.TransactionManageView;
import dakside.hacc.modules.transactions.trxtypeman.TransactionTypeManagerView;
import java.awt.Component;

/**
 * Transaction module definition
 * @author Takaji
 */
public class TransactionModule implements Unloadable {

    private static Component viewTrxMan = null;

    private static TransactionTypeManagerView viewTrxTypeMan = null;

    public synchronized void unload() {
        viewTrxMan = null;
    }

    @Function(Text = "TransactionManagement", Description = "Create/edittransactions", IconPath = "TrxManIcon", Category = "Transactions", Location = Function.STARTPAGE)
    public Component getTrxManView() {
        if (viewTrxMan == null) {
            viewTrxMan = new TransactionManageView();
        }
        return viewTrxMan;
    }

    @Function(Text = "TransactionTypeManagement", Description = "Create/edittransactiontypes", IconPath = "icon_trxman.png", Category = "Transactions", Location = Function.STARTPAGE)
    public Component getTrxTypeManView() {
        if (viewTrxTypeMan == null) {
            viewTrxTypeMan = new TransactionTypeManagerView();
        }
        return viewTrxTypeMan;
    }
}

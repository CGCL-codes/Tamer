package org.jbudget.gui.trans;

import java.util.ArrayList;
import java.util.List;
import org.jbudget.Core.Transaction;
import org.jbudget.io.DataManager;

/**
 * A TableModel for displaying a list of Transactions.
 *
 * @author petrov
 */
public class TransactionListModel extends TransactionListModelBase implements DataManager.TransactionListener {

    public TransactionListModel() {
        super();
        DataManager.getInstance().addTransactionListener(this);
    }

    @Override
    public void dispose() {
        dispose_called = true;
        DataManager.getInstance().removeTransactionListener(this);
        super.dispose();
    }

    @Override
    public void finalize() {
        if (!dispose_called) {
            System.out.println("Dispose was not called for TransactionListModel");
            dispose();
        }
    }

    protected List<Transaction> getTransactions() {
        return DataManager.getInstance().getTransactions(filter);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Transaction newValue = (Transaction) aValue;
        Transaction oldInstance = transactions.get(rowIndex);
        transactions.set(rowIndex, newValue);
        DataManager.getInstance().removeTransaction(oldInstance, false);
        DataManager.getInstance().addTransaction(newValue, false);
        fireTableChanged();
    }

    @Override
    public String getColumnName(int index) {
        return "Transactions";
    }

    /** Called when a transaction is added to the month. */
    public void transactionAdded(Transaction transaction) {
        List<Transaction> l = new ArrayList<Transaction>(1);
        l.add(transaction);
        if (!filter.apply(l).isEmpty()) {
            initialize();
        }
    }

    /** Called when a transaction is removed from the month. */
    public void transactionRemoved(List<Transaction.Transfer> transfers, int cDay, int cMonth, int cYear, long id) {
        initialize();
    }

    /** Method that is called when the data directory has changed. */
    @Override
    public void dataSourceChanged() {
        DataManager.getInstance().addTransactionListener(this);
        super.dataSourceChanged();
    }
}

package org.jbudget.gui.trans;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.event.TableModelEvent;
import org.jbudget.Core.Transaction;
import org.jbudget.Core.Transaction.Transfer;
import org.jbudget.Core.TransactionFilter;
import org.jbudget.gui.MainWindow;
import org.jbudget.io.DataManager;
import org.jbudget.io.EventDispatcher.TransactionListener;

/**
 * Class that creates a panel containing a list of transactions and gives the
 * user an opportuninty to manually vefiry transactions using some external
 * source of information (bank statement).
 *
 * @author petrov
 */
public class TransactionVerificationPanel extends TransactionListPanelBase implements TransactionListener {

    /** Local copy of the TransactionFilterClass. */
    protected TransactionFilter filter = null;

    /** List of Verified Transactions. */
    protected Set<Transaction> verifiedTransactions;

    /** Creates a new instance of TransactionVerificationPanel */
    public TransactionVerificationPanel() {
        super();
        itemName = "Transaction";
        initTransactionList();
        initFilter();
        this.listComponent.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent ev) {
                if (ev.getButton() != MouseEvent.BUTTON1) {
                    return;
                }
                int row = listComponent.rowAtPoint(ev.getPoint());
                Transaction transaction = (Transaction) listComponent.getModel().getValueAt(row, 0);
                if (verifiedTransactions.contains(transaction)) {
                    verifiedTransactions.remove(transaction);
                } else {
                    verifiedTransactions.add(transaction);
                }
                listComponent.tableChanged(new TableModelEvent(listComponent.getModel(), row));
            }
        });
        DataManager.getInstance().addTransactionListener(this);
        updateList();
    }

    @Override
    public void dispose() {
        super.dispose();
        DataManager.getInstance().removeTransactionListener(this);
    }

    /** Initializes Transactoin filter associated with this panel. */
    private void initFilter() {
        if (this.filter == null) {
            this.filter = MainWindow.getLastInstance().getTransactionsPanel().getModel().getFilter();
            if (this.filter == null) {
                this.filter = new TransactionFilter();
            } else {
                this.filter = (TransactionFilter) this.filter.clone();
            }
        }
    }

    /** Initializes a list of verified transactions. */
    private void initTransactionList() {
        if (this.verifiedTransactions == null) {
            this.verifiedTransactions = new HashSet<Transaction>();
        } else {
            this.verifiedTransactions.clear();
        }
        initFilter();
        List<Transaction> transactions = this.getModel().getTransactions();
        for (Transaction t : transactions) {
            if (t.isVerified()) {
                this.verifiedTransactions.add(t);
            }
        }
    }

    protected TransactionListModelBase createTableModel() {
        initFilter();
        return new TransactionVerificationListModel(this.filter);
    }

    protected TransactionCellEditor createCellEditor() {
        return new TransactionCellEditor(TransactionEditor.TransactionMode);
    }

    protected TransactionRenderer createCellRenderer() {
        initTransactionList();
        return new TransactionRenderer(verifiedTransactions);
    }

    /** Removes transaction or allocation from a corresponding object. */
    public void removeTransaction(Transaction transaction) {
        DataManager.getInstance().removeTransaction(transaction, false);
    }

    /** Adds transaction to the corresponding date structures. */
    public void addTransaction(Transaction transaction) {
        DataManager.getInstance().addTransaction(transaction, false);
    }

    /** Sets the title for the transaction/allocation dialog */
    public String getDialogTitle() {
        return "New Transaction";
    }

    /** Applies verification state to the current list of transactions. */
    public void applyVerificationState() {
        List<Transaction> toRemove = new ArrayList<Transaction>();
        List<Transaction> toAdd = new ArrayList<Transaction>();
        List<Transaction> allTransactions = this.getModel().getTransactions();
        for (Transaction t : allTransactions) {
            if (!t.isVerified()) {
                continue;
            }
            if (this.verifiedTransactions.contains(t)) {
                continue;
            }
            Transaction unVerified = null;
            if (t.getType() == Transaction.Type.MANUAL_VERIFIED) {
                unVerified = t.getInstance(Transaction.Type.MANUAL);
            } else if (t.getType() == Transaction.Type.AUTOMATIC_VERIFIED) {
                unVerified = t.getInstance(Transaction.Type.AUTOMATIC);
            } else {
                throw new RuntimeException("Invalid transaction type.");
            }
            toRemove.add(t);
            toAdd.add(unVerified);
        }
        for (Transaction t : this.verifiedTransactions) {
            if (t.isVerified()) {
                continue;
            }
            Transaction verified = null;
            if (t.getType() == Transaction.Type.MANUAL) {
                verified = t.getInstance(Transaction.Type.MANUAL_VERIFIED);
            } else if (t.getType() == Transaction.Type.AUTOMATIC) {
                verified = t.getInstance(Transaction.Type.AUTOMATIC_VERIFIED);
            } else {
                throw new RuntimeException("Invalid transaction type.");
            }
            toRemove.add(t);
            toAdd.add(verified);
        }
        DataManager dataManager = DataManager.getInstance();
        for (Transaction t : toRemove) {
            dataManager.removeTransaction(t, true);
        }
        for (Transaction t : toAdd) {
            dataManager.addTransaction(t, true);
        }
    }

    /** Reset the verification state. */
    public void resetVerificationState() {
        this.initTransactionList();
        this.updateList();
    }

    public void transactionAdded(Transaction transaction) {
        if (transaction.isVerified()) {
            this.verifiedTransactions.add(transaction);
            this.updateList();
        }
    }

    public void transactionRemoved(List<Transfer> transfers, int cDay, int cMonth, int cYear, long id) {
        for (Transaction t : this.verifiedTransactions) {
            if (t.getID() == id) {
                this.verifiedTransactions.remove(t);
                break;
            }
        }
    }
}

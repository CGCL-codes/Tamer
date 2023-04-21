package org.lcelb.accounts.manager.data.helper.computation.io;

import org.eclipse.emf.common.notify.Notification;
import org.lcelb.accounts.manager.data.ModelElement;
import org.lcelb.accounts.manager.data.extensions.transaction.DatedTransaction;
import org.lcelb.accounts.manager.data.extensions.transaction.MonthTransactions;
import org.lcelb.accounts.manager.data.extensions.transaction.TransactionPackage;
import org.lcelb.accounts.manager.data.helper.computation.AbstractMonthTotalHandler;

/**
 * Credit/debit handler for specified month.
 * @author lebaton<br>
 *         30 juin 2009
 */
public class IOMonthTotalHandler extends AbstractMonthTotalHandler {

    /**
   * Month totals.
   */
    private AbstractIOMonthTotal[] _monthTotals;

    /**
   * Constructor
   * @param month_p
   */
    public IOMonthTotalHandler(MonthTransactions month_p) {
        super(month_p);
        _monthTotals = new AbstractIOMonthTotal[] { new CreditMonthTotal(month_p), new DebitMonthTotal(month_p) };
    }

    /**
   * @see org.lcelb.accounts.manager.data.helper.computation.AbstractTotalHandler#dispose()
   */
    @Override
    public void dispose() {
        super.dispose();
        if (null != _monthTotals) {
            _monthTotals = null;
        }
    }

    /**
   * @see org.lcelb.accounts.manager.data.helper.computation.AbstractTotalHandler#getClassesToListenTo()
   */
    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends ModelElement>[] getClassesToListenTo() {
        return (Class<? extends ModelElement>[]) new Class<?>[] { DatedTransaction.class };
    }

    /**
   * @see org.lcelb.accounts.manager.data.helper.computation.AbstractMonthHolder#getElements()
   */
    @Override
    public Object[] getElements() {
        return _monthTotals;
    }

    /**
   * @see org.lcelb.accounts.manager.data.helper.computation.AbstractMonthTotalHandler#getElementsToListenTo()
   */
    @Override
    protected ModelElement[] getElementsToListenTo() {
        return null;
    }

    /**
   * @see org.lcelb.accounts.manager.data.helper.computation.AbstractTotalHandler#handleModelElementAdded(java.lang.Object)
   */
    @Override
    protected void handleModelElementAdded(Object modelElement_p) {
        transactionModified(DatedTransaction.class.cast(modelElement_p));
    }

    /**
   * @see org.lcelb.accounts.manager.data.helper.computation.AbstractTotalHandler#handleModelElementModified(java.lang.Object,
   *      java.lang.Object)
   */
    @Override
    protected void handleModelElementModified(Notification notification_p) {
        Object modelElement = notification_p.getNotifier();
        Object feature = notification_p.getFeature();
        if (TransactionPackage.Literals.DATED_TRANSACTION__AMOUNT.equals(feature)) {
            transactionModified(DatedTransaction.class.cast(modelElement));
        }
    }

    /**
   * @see org.lcelb.accounts.manager.data.helper.computation.AbstractTotalHandler#handleModelElementRemoved(java.lang.Object)
   */
    @Override
    protected void handleModelElementRemoved(Object modelElement_p) {
        transactionModified(DatedTransaction.class.cast(modelElement_p));
    }

    /**
   * Mark contained month totals as dirty.
   */
    protected void markDirty() {
        for (AbstractIOMonthTotal total : _monthTotals) {
            total.markDirty();
        }
    }

    /**
   * Transaction modified.
   * @param transaction_p
   */
    protected void transactionModified(DatedTransaction transaction_p) {
        boolean handle = getMonthHolder().beforeOrInsideMonth(transaction_p, true);
        if (handle) {
            markDirty();
            notifyUpdaters();
        }
    }
}

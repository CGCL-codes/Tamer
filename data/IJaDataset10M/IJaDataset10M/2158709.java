package au.com.southsky.cashbooks;

import cashbooks.CashBook;
import cashbooks.CashBook.EventNewCashbook;
import cashbooks.actions.CashBookActions;

public class CashbookBehaviour implements CashBookActions {

    private CashBook cashBook;

    @Override
    public void performOnEntryCreated(EventNewCashbook event) {
        cashBook.setName(event.getName());
        cashBook.setDescription(event.getDescription());
        cashBook.setCustomer(event.getCustomer());
    }

    @Override
    public void setCashBook(CashBook cashBook) {
        this.cashBook = cashBook;
    }
}

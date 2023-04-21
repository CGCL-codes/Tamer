package au.com.southsky.cashbooks;

import cashbooks.Account;
import cashbooks.Account.EventCloseAccount;
import cashbooks.Account.EventNewAccount;
import cashbooks.Account.EventOpenAccount;
import cashbooks.Account.EventRemove;
import cashbooks.actions.AccountActions;

public class AccountBehaviour implements AccountActions {

    private Account account;

    @Override
    public void performOnEntryClosed(EventCloseAccount event) {
    }

    @Override
    public void performOnEntryFinalState(EventRemove event) {
    }

    @Override
    public void performOnEntryOpen(EventOpenAccount event) {
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public void performOnEntryOpen(EventNewAccount event) {
        account.setAccountno(event.getAccountno());
        account.setBsb(event.getBsb());
        account.setDescription(event.getDecription());
        account.setName(event.getName());
        account.setShortname(event.getShortname());
        account.setType(event.getType());
        account.setCustomer(event.getCustomer());
        account.setCashBook(event.getCashBook());
    }
}

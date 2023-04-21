package com.aurorasoftworks.signal.examples.ui.mvc.lwuit;

import com.aurorasoftworks.signal.examples.core.Account;
import com.aurorasoftworks.signal.examples.core.IAccount;
import com.aurorasoftworks.signal.examples.core.IAccountService;

public class EditAccountController extends AbstractAccountController implements IEditAccountEvent {

    private IAccount savedAccount;

    public EditAccountController(IAccountService accountService) {
        super(accountService);
    }

    public void onEditAccount(IAccount account) {
        savedAccount = new Account(account);
        getEditAccountEvent().onEditAccount(account);
    }

    public void onAccountSubmitted(IAccount account) {
        getAccountService().updateAccount(account);
        ((IAccountCreationEvent) getCaller()).onAccountSubmitted(account);
    }

    public void onAccountCancelled(IAccount account) {
        account.restore(savedAccount);
        super.onAccountCancelled(account);
    }
}

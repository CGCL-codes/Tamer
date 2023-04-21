package br.com.visualmidia.persistence;

import br.com.visualmidia.business.Account;
import br.com.visualmidia.business.Money;
import br.com.visualmidia.exception.BusinessException;

public class SetAmountOfAccount extends GDTransaction {

    private static final long serialVersionUID = -631890135992287372L;

    private String accountId;

    private float amount;

    public SetAmountOfAccount(Money amount, String accountId) {
        this.amount = amount.getFloatValue();
        this.accountId = accountId;
    }

    @Override
    protected void execute(PrevalentSystem system) throws BusinessException {
        Account account = (Account) system.accounts.get(accountId);
        account.setAmount(new Money(amount));
    }
}

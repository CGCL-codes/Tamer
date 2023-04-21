package com.fdm.model.accounts;

import java.io.Serializable;
import java.util.Map;
import com.fdm.model.exceptions.BankManagerException;
import com.fdm.model.exceptions.InsufficientFundsException;
import com.fdm.model.exceptions.LessThanZeroException;

public abstract class AccountDecorator implements _Account, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1703853588849899982L;

    protected _Account decoratedAccount;

    public AccountDecorator(_Account decoratedAccount) {
        this.decoratedAccount = decoratedAccount;
    }

    @Override
    public int getAccountNumber() {
        return decoratedAccount.getAccountNumber();
    }

    @Override
    public String getAccountType() {
        return decoratedAccount.getAccountType();
    }

    @Override
    public String getName() {
        return decoratedAccount.getName();
    }

    @Override
    public double getBalance() {
        return decoratedAccount.getBalance();
    }

    @Override
    public double getOverDraftLimit() {
        return decoratedAccount.getOverDraftLimit();
    }

    @Override
    public void withdraw(Double amount) throws InsufficientFundsException, LessThanZeroException {
        decoratedAccount.withdraw(amount);
    }

    @Override
    public void deposit(Double amount) throws LessThanZeroException {
        decoratedAccount.deposit(amount);
    }

    @Override
    public void setAccountNumber(Integer accountNumber) {
        if (accountNumber != null) decoratedAccount.setAccountNumber(accountNumber);
    }

    @Override
    public void setAccountType(String type) {
        if (type != null) decoratedAccount.setAccountType(type);
    }

    @Override
    public void setName(String name) {
        if (name != null) decoratedAccount.setName(name);
    }

    @Override
    public void setBalance(Double balance) throws LessThanZeroException {
        if (balance < 0) throw new LessThanZeroException();
        if (balance != null) decoratedAccount.setBalance(balance);
    }

    @Override
    public void setAttributes(Map<String, Object> parameters) throws BankManagerException {
        this.setAccountNumber((Integer) parameters.get("accountNumber"));
        this.setAccountType((String) parameters.get("accountType"));
        this.setName((String) parameters.get("name"));
        this.setBalance((Double) parameters.get("balance"));
    }
}

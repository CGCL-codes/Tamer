package org.nakedobjects.application.accounts.dom;

import java.util.ArrayList;
import java.util.List;
import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.annotation.Optional;
import org.nakedobjects.applib.value.Date;
import org.nakedobjects.applib.value.Money;
import org.nakedobjects.application.accounts.dom.budget.AbstractItem;
import org.nakedobjects.application.accounts.dom.budget.Cost;
import org.nakedobjects.application.accounts.dom.budget.Income;
import org.nakedobjects.application.accounts.dom.budget.Item;
import org.nakedobjects.application.accounts.dom.budget.When;
import org.nakedobjects.application.accounts.dom.transaction.Credit;
import org.nakedobjects.application.accounts.dom.transaction.Debit;
import org.nakedobjects.application.accounts.dom.transaction.Transfer;

public class Account extends AbstractDomainObject {

    private String name;

    private String description;

    private Money balance = new Money(0, "GBP");

    private List<Transaction> transactions = new ArrayList<Transaction>();

    private List<Item> items = new ArrayList<Item>();

    private List<Alert> alerts = new ArrayList<Alert>();

    public void transferTo(Date date, @Named("Amount") Money amount, Account account, @Named("Note") @Optional String note) {
        Transfer transaction = newTransientInstance(Transfer.class);
        transaction.setToAccount(account);
        addTransaction(date, amount, note, transaction);
    }

    public void addADeposit(Date date, @Named("Amount") Money amount, @Named("Payer") @Optional String payer, @Named("Note") @Optional String note) {
        Credit transaction = newTransientInstance(Credit.class);
        transaction.setPayer(payer);
        addTransaction(date, amount, note, transaction);
    }

    public void addAPayment(Date date, @Named("Amount") Money amount, @Named("Payee") @Optional String payee, @Named("Note") @Optional String note) {
        Debit transaction = newTransientInstance(Debit.class);
        transaction.setPayee(payee);
        addTransaction(date, amount, note, transaction);
    }

    private void addTransaction(Date date, Money amount, String note, Transaction transaction) {
        transaction.setAccount(this);
        transaction.setDate(date);
        transaction.setAmount(amount);
        transaction.setNote(note);
        persist(transaction);
    }

    public void addRegularDeposit(@Named("Amount") Money amount, When when, @Named("Note") @Optional String note) {
        Income income = newTransientInstance(Income.class);
        addItem(amount, when, note, income);
    }

    public void addRegularPayment(@Named("Amount") Money amount, When when, @Named("Note") @Optional String note) {
        Cost cost = newTransientInstance(Cost.class);
        addItem(amount, when, note, cost);
    }

    private void addItem(Money amount, When when, String note, AbstractItem item) {
        item.setAccount(this);
        item.setAmount(amount);
        item.setLastTransaction(new Date());
        item.setDescription(note);
        item.setWhen(when);
        persist(item);
        getItems().add(item);
    }

    public void process(Date date) {
        for (Item item : getItems()) {
            item.process(date);
        }
    }

    @Hidden
    public void addToBalance(Transaction transaction) {
        if (transaction.isCredit()) {
            balance = balance.add(transaction.getAmount());
        } else {
            balance = balance.subtract(transaction.getAmount());
        }
    }

    @Hidden
    public void revertBalance(Transaction transaction) {
        if (transaction.isCredit()) {
            balance = balance.subtract(transaction.getAmount());
        } else {
            balance = balance.add(transaction.getAmount());
        }
    }

    @MemberOrder(sequence = "1")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Optional
    @MemberOrder(sequence = "1.1")
    @Hidden
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @MemberOrder(sequence = "10")
    @Hidden
    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    @MemberOrder(sequence = "10")
    public List<Item> getItems() {
        return items;
    }

    @Disabled
    @MemberOrder(sequence = "4")
    public List<Transaction> getRecentTransactions() {
        return transactions;
    }

    public void addToRecentTransactions(Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeFromRecentTransactions(Transaction transaction) {
        transactions.remove(transaction);
    }

    @Disabled
    @MemberOrder(sequence = "3")
    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public String title() {
        return name;
    }
}

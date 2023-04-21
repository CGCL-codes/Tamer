package net.sf.flophase.app;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import net.sf.flophase.core.exception.AccountExistsException;
import net.sf.flophase.core.exception.InvalidAccountBalanceException;
import net.sf.flophase.core.exception.InvalidAccountException;
import net.sf.flophase.core.exception.InvalidAccountNameException;
import net.sf.flophase.core.exception.InvalidEntryAmountException;
import net.sf.flophase.core.exception.InvalidEntryException;
import net.sf.flophase.core.exception.InvalidTransactionDateException;
import net.sf.flophase.core.exception.InvalidTransactionNameException;
import net.sf.flophase.core.exception.LoadModelException;
import net.sf.flophase.core.exception.SaveModelException;
import net.sf.flophase.core.service.Account;
import net.sf.flophase.core.service.Entry;
import net.sf.flophase.core.service.Model;
import net.sf.flophase.core.service.Transaction;

/**
 * This is the controller for the application. This is the central point for the application to
 * interact with the core.
 */
public interface Controller {

    /**
     * Adds an account to the model.
     * 
     * @param model The model.
     * @param name The account name.
     * @param balance The account balance.
     * @param negThreshold The negative threshold.
     * @param posThreshold The positive threshold.
     * @return The new account.
     * @throws AccountExistsException If an account with the same name already exists.
     * @throws InvalidAccountNameException If the account name is invalid.
     * @throws InvalidAccountBalanceException If the account balance is invalid.
     */
    public Account addAccount(Model model, String name, BigDecimal balance, BigDecimal negThreshold, BigDecimal posThreshold) throws AccountExistsException, InvalidAccountNameException, InvalidAccountBalanceException;

    /**
     * Adds a transaction to the model.
     * 
     * @param model The model.
     * @param name The transaction name.
     * @param date The transaction date.
     * @return The new transaction.
     * @throws InvalidTransactionNameException If the name is invalid.
     * @throws InvalidTransactionDateException If the date is invalid.
     */
    public Transaction addTransaction(Model model, String name, Date date) throws InvalidTransactionNameException, InvalidTransactionDateException;

    /**
     * Adds an entry to a transaction.
     * 
     * @param transaction The transaction.
     * @param entry The entry.
     * @throws InvalidEntryException If the entry is determined to be invalid.
     */
    public void addTransactionEntry(Transaction transaction, Entry entry) throws InvalidEntryException;

    /**
     * Creates an entry.
     * 
     * @param account The entry account.
     * @param amount The entry amount.
     * @return The new entry.
     * @throws InvalidEntryAmountException If the entry amount is determined to be invalid.
     * @throws InvalidAccountException If the account is determined to be invalid.
     */
    public Entry createEntry(Account account, BigDecimal amount) throws InvalidEntryAmountException, InvalidAccountException;

    /**
     * Creates a model.
     * 
     * @return The new model.
     */
    public Model createModel();

    /**
     * Deletes the given accounts from the given model.
     * 
     * @param model The model.
     * @param accounts The accounts.
     */
    public void deleteAccounts(Model model, Collection<Account> accounts);

    /**
     * Deletes the given transaction from the model.
     * 
     * @param model The model.
     * @param transactions The transactions.
     */
    public void deleteTransactions(Model model, Collection<Transaction> transactions);

    /**
     * Edits an account.
     * 
     * @param model The model.
     * @param account The account.
     * @param name The account name.
     * @param balance The account balance.
     * @param negThreshold The negative threshold.
     * @param posThreshold The positive threshold.
     * @throws AccountExistsException If an account with the same name already exists.
     * @throws InvalidAccountNameException If the account name is invalid.
     * @throws InvalidAccountBalanceException If the account balance is invalid.
     */
    public void editAccount(Model model, Account account, String name, BigDecimal balance, BigDecimal negThreshold, BigDecimal posThreshold) throws AccountExistsException, InvalidAccountNameException, InvalidAccountBalanceException;

    /**
     * Opens the model from the given file.
     * 
     * @param file The file.
     * @return The loaded model.
     * @throws LoadModelException If there is an error loading the model.
     */
    public Model openModel(File file) throws LoadModelException;

    /**
     * Saves the given model to the given file.
     * 
     * @param file The file.
     * @param model The model.
     * @throws SaveModelException If there is an error saving the model.
     */
    public void saveModel(File file, Model model) throws SaveModelException;

    /**
     * Sets the balance of the given account.
     * 
     * @param account The account.
     * @param balance The new balance.
     * @throws InvalidAccountBalanceException If the balance is invalid.
     */
    public void setAccountBalance(Account account, BigDecimal balance) throws InvalidAccountBalanceException;

    /**
     * Sets the amount for the given entry.
     * 
     * @param transaction The transaction
     * @param entry The entry.
     * @param newAmount The new amount.
     * @throws InvalidEntryAmountException If the amount is determined to be invalid.
     */
    public void setEntryAmount(Transaction transaction, Entry entry, BigDecimal newAmount) throws InvalidEntryAmountException;

    /**
     * Sets the date of the given transaction.
     * 
     * @param transaction The transaction.
     * @param date The new date.
     * @throws InvalidTransactionDateException If the date is invalid.
     */
    public void setTransactionDate(Transaction transaction, Date date) throws InvalidTransactionDateException;

    /**
     * Sets the name of the given transaction.
     * 
     * @param transaction The transaction.
     * @param name The new name.
     * @throws InvalidTransactionNameException If the name is invalid.
     */
    public void setTransactionName(Transaction transaction, String name) throws InvalidTransactionNameException;
}

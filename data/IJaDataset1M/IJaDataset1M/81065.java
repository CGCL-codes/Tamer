package net.sf.flophase.core.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import net.sf.flophase.core.exception.InvalidAccountException;
import net.sf.flophase.core.exception.InvalidEntryAmountException;
import net.sf.flophase.core.exception.InvalidEntryException;
import net.sf.flophase.core.exception.InvalidTransactionDateException;
import net.sf.flophase.core.exception.InvalidTransactionNameException;
import net.sf.flophase.core.exception.ValidatorFactory;
import com.google.inject.Inject;

/**
 * This class is a service class that performs operations on Transactions.
 */
public class FloTransactionService implements TransactionService {

    /**
     * Transactions fall into different time frames. The time frames are defined for efficiency so
     * that we can separate the loading of transactions based on their date.
     */
    private enum TimeFrame {

        /**
         * The period older than recent but not older than out of scope.
         */
        HISTORIC, /**
         * The period up to the recent number of days before model's current date.
         */
        RECENT, /**
         * The period after the model's current date up to the upcoming number of days.
         */
        UPCOMING, /**
         * The period after the upcoming period.
         */
        LONG_TERM, /**
         * The period before the historic number of days before the model's current date.
         */
        OUT_OF_SCOPE
    }

    /**
     * The number of days from the model's current date that are defined as recent.
     */
    public static final int RECENT_DAYS = 30;

    /**
     * The number of days from the model's current date that are defined as historic.
     */
    public static final int HISTORIC_DAYS = 365;

    /**
     * The number of days after the model's current date that are defined as upcoming.
     */
    public static final int UPCOMING_DAYS = 90;

    /**
     * The number of days after the model's current date that are defined as long term.
     */
    public static final int LONG_TERM_DAYS = 365;

    /**
     * The factory that provides validators.
     */
    private final ValidatorFactory validatorFactory;

    /**
     * The account service.
     */
    private final AccountService accountService;

    /**
     * Creates a new FloTransactionService instance.
     * 
     * @param validatorFactory The validator factory.
     * @param accountService The account service.
     */
    @Inject
    public FloTransactionService(ValidatorFactory validatorFactory, AccountService accountService) {
        this.validatorFactory = validatorFactory;
        this.accountService = accountService;
    }

    @Override
    public void addEntry(Transaction transaction, Entry entry) throws InvalidEntryException {
        validatorFactory.getEntryValidator(transaction.getModel()).validate(entry);
        transaction.addEntry(entry);
    }

    @Override
    public void addTransaction(Model model, Transaction transaction) {
        transaction.setModel(model);
        TimeFrame timeFrame = getTimeFrame(model, transaction.getDate());
        switch(timeFrame) {
            case LONG_TERM:
                model.addLongTermTransaction(transaction);
                break;
            case UPCOMING:
                model.addUpcomingTransaction(transaction);
                break;
            case RECENT:
                model.addRecentTransaction(transaction);
                break;
            case HISTORIC:
                model.addHistoricTransaction(transaction);
                break;
        }
    }

    @Override
    public Entry createEntry(Account account, BigDecimal amount) throws InvalidEntryAmountException, InvalidAccountException {
        validatorFactory.getAccountValidator().validate(account);
        validatorFactory.getEntryAmountValidator().validate(amount);
        return new Entry(account, amount);
    }

    @Override
    public Entry createEntry(Model model, String account, BigDecimal amount) throws InvalidEntryAmountException, InvalidAccountException {
        return createEntry(accountService.getAccount(model, account), amount);
    }

    @Override
    public Transaction createTransaction(String name, Date date, Entry... entries) throws InvalidTransactionNameException, InvalidTransactionDateException {
        validatorFactory.getTransactionNameValidator().validate(name);
        validatorFactory.getTransactionDateValidator().validate(date);
        Transaction transaction = new Transaction(name, date);
        for (Entry entry : entries) {
            transaction.addEntry(entry);
        }
        return transaction;
    }

    /**
     * Determines the time frame for the given date based on the model's current date. Using the
     * model's current date makes the time frame consistent over time.
     * 
     * @param model The model.
     * @param date The date.
     * @return The time frame of the date.
     */
    private TimeFrame getTimeFrame(Model model, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(model.getCurrentDate());
        if (date.after(calendar.getTime())) {
            calendar.add(Calendar.DATE, UPCOMING_DAYS);
            if (date.before(calendar.getTime())) {
                return TimeFrame.UPCOMING;
            } else {
                calendar.add(Calendar.DATE, LONG_TERM_DAYS - UPCOMING_DAYS);
                if (date.before(calendar.getTime())) {
                    return TimeFrame.LONG_TERM;
                }
            }
        } else {
            calendar.add(Calendar.DATE, -RECENT_DAYS);
            if (date.after(calendar.getTime())) {
                return TimeFrame.RECENT;
            } else {
                calendar.add(Calendar.DATE, RECENT_DAYS - HISTORIC_DAYS);
                if (date.after(calendar.getTime())) {
                    return TimeFrame.HISTORIC;
                }
            }
        }
        return TimeFrame.OUT_OF_SCOPE;
    }

    @Override
    public void removeTransaction(Model model, Transaction transaction) {
        TimeFrame timeFrame = getTimeFrame(model, transaction.getDate());
        switch(timeFrame) {
            case LONG_TERM:
                model.removeLongTermTransaction(transaction);
                break;
            case UPCOMING:
                model.removeUpcomingTransaction(transaction);
                break;
            case RECENT:
                model.removeRecentTransaction(transaction);
                break;
            case HISTORIC:
                model.removeHistoricTransaction(transaction);
                break;
        }
    }

    @Override
    public void setAmount(Transaction transaction, Entry entry, BigDecimal amount) throws InvalidEntryAmountException {
        validatorFactory.getEntryAmountValidator().validate(amount);
        entry.setAmount(amount);
        transaction.updateSystemEntries();
    }

    @Override
    public void setDate(Transaction transaction, Date date) throws InvalidTransactionDateException {
        validatorFactory.getTransactionDateValidator().validate(date);
        Model model = transaction.getModel();
        if (model != null) {
            TimeFrame currentTimeFrame = getTimeFrame(model, transaction.getDate());
            TimeFrame newTimeFrame = getTimeFrame(model, date);
            if (currentTimeFrame != newTimeFrame) {
                switch(currentTimeFrame) {
                    case UPCOMING:
                        model.removeUpcomingTransaction(transaction);
                        break;
                    case RECENT:
                        model.removeRecentTransaction(transaction);
                        break;
                    case HISTORIC:
                        model.removeHistoricTransaction(transaction);
                        break;
                }
                switch(newTimeFrame) {
                    case UPCOMING:
                        model.addUpcomingTransaction(transaction);
                        break;
                    case RECENT:
                        model.addRecentTransaction(transaction);
                        break;
                    case HISTORIC:
                        model.addHistoricTransaction(transaction);
                        break;
                }
            }
        }
        transaction.setDate(date);
    }

    @Override
    public void setName(Transaction transaction, String name) throws InvalidTransactionNameException {
        validatorFactory.getTransactionNameValidator().validate(name);
        transaction.setName(name);
    }
}

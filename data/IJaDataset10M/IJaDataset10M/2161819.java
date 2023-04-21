package net.sf.flophase.core.data;

import java.text.DateFormat;
import net.sf.flophase.core.exception.FindDelegateException;
import net.sf.flophase.core.service.AccountService;
import net.sf.flophase.core.service.ModelService;
import net.sf.flophase.core.service.TransactionService;
import com.google.inject.Inject;

/**
 * This class is the default implementation of the DelegateFinder interface.
 */
public class FloDelegateFinder implements DelegateFinder {

    /**
     * Version 1.0 identifier
     */
    public static final String VERSION_10 = "1.0";

    /**
     * Version 1.1 identifier
     */
    public static final String VERSION_11 = "1.1";

    /**
     * Version 1.2 identifier
     */
    public static final String VERSION_12 = "1.2";

    /**
     * The date format that will be used to load in dates.
     */
    private final DateFormat dateFormat;

    /**
     * The account service.
     */
    private final AccountService accountService;

    /**
     * The transaction service.
     */
    private final TransactionService transactionService;

    /**
     * The model service.
     */
    private final ModelService modelService;

    /**
     * Creates a new FloDelegateFinder instance.
     * 
     * @param dateFormat The date format that will be used.
     * @param accountService The account service.
     * @param transactionService The transaction service.
     * @param modelService The model service.
     */
    @Inject
    public FloDelegateFinder(DateFormat dateFormat, AccountService accountService, TransactionService transactionService, ModelService modelService) {
        this.dateFormat = dateFormat;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.modelService = modelService;
    }

    @Override
    public HandlerDelegate findDelegate(String version) throws FindDelegateException {
        if (VERSION_12.equals(version)) {
            return new FloHandler12(dateFormat, accountService, transactionService, modelService);
        } else if (VERSION_11.equals(version)) {
            return new FloHandler11(dateFormat, accountService, transactionService, modelService);
        } else if (VERSION_10.equals(version)) {
            return new FloHandler10(dateFormat, accountService, transactionService, modelService);
        } else {
            if (version == null) {
                throw new FindDelegateException("No version was found.");
            } else {
                throw new FindDelegateException("No handler is available for the version \"" + version + "\".");
            }
        }
    }
}

package jgnash.imports;

import java.math.BigDecimal;
import java.util.Date;
import jgnash.engine.Account;

/**
 * Common interface for imported transactions from OFX and mt940
 *
 * @author Craig Cavanaugh
 * @author Arnout Engelen
 * @author Nicolas Bouillon
 */
public class ImportTransaction {

    public static enum ImportState {

        NEW, EQUAL, IGNORE, NOTEQUAL
    }

    /**
     * Destination account
     */
    public Account account;

    /**
     * Deposits get positive 'amounts', withdrawals negative
     */
    public BigDecimal amount;

    public Date datePosted;

    /**
     * Date user initiated the transaction, optional, may be null
     */
    public Date dateUser = null;

    public String memo = "";

    public String payee = "";

    public String checkNumber = "";

    private ImportState state = ImportState.NEW;

    protected ImportTransaction() {
    }

    public ImportTransaction(Account account, BigDecimal amount, Date datePosted, String memo) {
        this.account = account;
        this.amount = amount;
        this.datePosted = datePosted;
        this.memo = memo;
    }

    public ImportState getState() {
        return state;
    }

    public void setState(ImportState state) {
        this.state = state;
    }
}

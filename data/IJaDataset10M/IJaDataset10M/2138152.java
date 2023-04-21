package OfficeServer.log_error;

import java.util.logging.Level;
import OfficeServer.main.MODULE;

/**
 * @author Chris Bayruns
 * 
 */
public class UserException extends OfficeException {

    public static enum REASON {

        WRONG_PASSWORD, ACCOUNT_LOCKED, NO_ACCOUNT, NOT_LOGGED_IN, ALL_READY_EXISTS, INVALID_ACCESS_LEVEL, NO_SEARCH_EXISTS, NO_ACCESS
    }

    ;

    private REASON reason;

    /**
     * @param level
     *            The level of error
     * @param theReason
     *            The reason enum
     */
    public UserException(Level level, REASON theReason, String message) {
        super(MODULE.USER, level, theReason.toString() + " - " + message);
        reason = theReason;
    }

    /**
     * @author Chris Bayruns
     * @return The reason
     */
    public REASON getReason() {
        return reason;
    }
}

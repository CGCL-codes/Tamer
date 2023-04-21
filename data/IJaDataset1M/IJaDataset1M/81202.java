package com.teletalk.jserver.util;

import java.sql.SQLException;

/**
 * Class containing various useful methods associated with the use of an Oracle database.
 * 
 * @author Tobias L�fstrand
 * 
 * @since 1.3.1
 */
public class OracleUtils {

    /**
    * Checks if an SQLException is a connection error.<p>
    *
    * A connection error can be caused by network errors.
    *
    * @param error an SQLException to check.
    * 
    * @return <code>true</code> if error was a connection error, otherwise <code>false</code>.
    */
    public static boolean isConnectionError(final SQLException error) {
        int errorCode = error.getErrorCode();
        if (errorCode == 17002) {
            return true;
        } else {
            return false;
        }
    }

    /**
    * Checks if an SQLException is a package invalid error.<p>
    *
    * A package invalid error is caused by changes in the db invalidating a
    * package. Also it can occur while a package is being recompiled.
    *
    * @param error an SQLException to check.
    * 
    * @return <code>true</code> if error was a package invalidated error, otherwise <code>false</code>.
    */
    public static boolean isPackageInvalidError(final SQLException error) {
        int errorCode = error.getErrorCode();
        switch(errorCode) {
            case 4068:
            case 4061:
            case 4065:
            case 6508:
            case 6512:
                return true;
            default:
                return false;
        }
    }
}

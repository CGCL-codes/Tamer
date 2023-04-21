package org.silicolife.util.xls2sql.exceptions;

import java.util.HashMap;

public class UserXLS2SQLExceptionMessages {

    /** The given file is not an Excel file */
    public static int ERR_NOT_EXCEL_FILE = 1;

    /** The give workbook is in some way invalid */
    public static int ERR_INVALID_WORKBOOK = 2;

    /** It was not possible to determine the column type of a column */
    public static int ERR_CANNOT_DETERMINE_COLUMN_TYPE = 3;

    /** It was not possible to determine the column header of a column */
    public static int ERR_CANNOT_DETERMINE_COLUMN_HEADER = 4;

    /** A mapping from error codes to messages */
    private static HashMap<Integer, String> errmsgMap = new HashMap<Integer, String>();

    static {
        errmsgMap.put(ERR_NOT_EXCEL_FILE, "The file %s may not be a valid Excel file format.");
        errmsgMap.put(ERR_INVALID_WORKBOOK, "The workbook is invalid.");
        errmsgMap.put(ERR_CANNOT_DETERMINE_COLUMN_TYPE, "The column type of %s!%s cannot be determined due to insufficient values in the column.");
        errmsgMap.put(ERR_CANNOT_DETERMINE_COLUMN_HEADER, "The column header of %s!%s cannot be determined due to a non string value in the first row.");
    }

    /** Maps the errorcode to the message as according to errmsgMap */
    public static String getMessage(int errorCode) {
        String message = null;
        if (UserXLS2SQLExceptionMessages.errmsgMap.get(errorCode) != null) {
            message = UserXLS2SQLExceptionMessages.errmsgMap.get(errorCode);
        }
        return message;
    }
}

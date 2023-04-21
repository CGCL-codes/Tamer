package bm.db;

/**
 * An attempt was made to setProperty a field to a value of a type different than
 * declared.
 *
 * @author <a href="mailto:narciso@elondra.org">Narciso Cerezo</a>
 * @version $Revision:2 $
 */
public class InvalidDataTypeException extends DBException {

    private static final String ERR_CODE = "ERR_INVALID_DATA_TYPE";

    public InvalidDataTypeException(final int errorNumber) {
        super(errorNumber);
        errorCode = ERR_CODE;
    }

    public InvalidDataTypeException(final int errorNumber, final String message) {
        super(errorNumber, message);
        errorCode = ERR_CODE;
    }

    public InvalidDataTypeException(final int errorNumber, final Throwable cause) {
        super(errorNumber, cause);
        errorCode = ERR_CODE;
    }

    public InvalidDataTypeException(final int errorNumber, final String message, final Throwable cause) {
        super(errorNumber, message, cause);
        errorCode = ERR_CODE;
    }
}

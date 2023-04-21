package nl.knaw.dans.common.dbflib;

/**
 * Thrown when trying to add a {@link Field} to a {@link Table} when it is not supported by the
 * {@link Version} specified.
 *
 * @author Vesa Åkerman
 */
public class InvalidFieldTypeException extends DbfLibException {

    private static final long serialVersionUID = 1760090922907515668L;

    InvalidFieldTypeException(final String message) {
        super(message);
    }
}

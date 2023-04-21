package nl.knaw.dans.common.dbflib;

/**
 * Abstract base class for {@link DataValidator} implementations. Stores the connected field.
 *
 * @author Jan van Mansum
 */
abstract class AbstractDataValidator implements DataValidator {

    protected final Field field;

    protected AbstractDataValidator(final Field field) {
        this.field = field;
    }
}

package dakside.hacc.core.helpers.exceptions;

/**
 *
 * @author michael
 */
public class CurrencyNotFound extends Exception {

    public CurrencyNotFound() {
    }

    public CurrencyNotFound(String message) {
        super(message);
    }

    public CurrencyNotFound(Throwable throwable) {
        super(throwable);
    }

    public CurrencyNotFound(String string, Throwable throwable) {
        super(string, throwable);
    }
}

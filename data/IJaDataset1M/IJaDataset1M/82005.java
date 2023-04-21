package issrg.acm;

/**
 * This is an exception the tools can throw to notify that something went wrong during the
 * process of creating an AC.
 *
 * <p>All the Manager code uses this exception to notify the caller, and not only the actual encoding
 * process. The name of the exception is still meaningful, since Manager is designed
 * to create ACs.
 *
 * @author Sassa
 * @version 1.0
 */
public class ACCreationException extends issrg.utils.EmbeddedException {

    public ACCreationException() {
        super("Attribute Certificate Creation Exception");
    }

    public ACCreationException(String what) {
        super(what);
    }

    public ACCreationException(String what, Throwable th) {
        super(what, th);
    }
}

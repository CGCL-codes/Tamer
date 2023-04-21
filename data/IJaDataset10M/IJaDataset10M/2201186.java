package tapioca.dt;

/**
 * An exception thrown if another DT holds the write lock for a given object.
 *
 * @author armbrust@gmail.com (Erick Armbrust)
 */
@SuppressWarnings("serial")
public class WriteLockHeldException extends Exception {

    public WriteLockHeldException(String message) {
        super(message);
    }
}

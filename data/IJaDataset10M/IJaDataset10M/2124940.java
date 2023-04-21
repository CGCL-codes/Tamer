package fido.db;

/**
 * An attempt to remove the root of the ObjectHierarchy, called
 * <i>Object</i>.
 */
public class CannotDeleteRootObjectException extends FidoException {

    /**
	 * Constructs an <CODE>CannotDeleteRootObjectException</CODE>
	 * without a detail message.
	 */
    public CannotDeleteRootObjectException() {
        super();
    }
}

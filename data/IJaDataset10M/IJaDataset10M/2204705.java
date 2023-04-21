package fido.db;

/**
 * An attempt to access a ClassLinkType in the ClassLinkTypeTable by a name
 * which does not exist.
 * @see fido.db.ClassLinkTypeTable
 */
public class ClassLinkTypeNotFoundException extends FidoException {

    /**
	 * Constructs an <CODE>ClassLinkTypeNotFoundException</CODE>
	 * with a detail message.
	 * @param s the detail message
	 */
    public ClassLinkTypeNotFoundException(String s) {
        super(s);
    }
}

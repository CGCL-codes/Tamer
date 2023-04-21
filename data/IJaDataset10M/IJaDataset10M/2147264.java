package fido.db;

/**
 * An attempt to access a Word Classification in the 
 * WordClassificationTable by a rank which does not exist.
 * @see WordClassificationTable
 */
public class ClassificationNotFoundException extends FidoException {

    /**
	 * Constructs an <CODE>ClassificationNotFoundException</CODE>
	 * with a detail message.
	 * @param s the detail message
	 */
    public ClassificationNotFoundException(String s) {
        super(s);
    }

    /**
	 * Constructs an <CODE>ClassificationNotFoundException</CODE>
	 * with a the object not found in the message.
	 * @param s the detail message
	 */
    public ClassificationNotFoundException(int id) {
        super(Integer.toString(id));
    }
}

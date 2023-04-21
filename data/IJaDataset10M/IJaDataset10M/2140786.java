package uk.ac.manchester.cs.snee.metadata.schema;

/**
 * An exception raised if an SQL type cannot be mapped to a
 * Java type 
 * @author Steven Lynden
 */
public class TypeMappingException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2904673399336344661L;

    /**
	 * Construct a new type checking exception.
	 * @param message descriptive message explaining what went wrong.
	 */
    public TypeMappingException(String message) {
        super(message);
    }

    public TypeMappingException(String msg, Throwable e) {
        super(msg, e);
    }
}

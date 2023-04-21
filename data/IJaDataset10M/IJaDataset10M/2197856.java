package uk.ac.manchester.cs.snee.metadata.schema;

/**
 * An exception raised if an problem arises when processing schema
 * metadata.
 */
public class SchemaMetadataException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1026710816037022011L;

    /**
	 * Construct a new exception indicating that the specified
	 * table does not exist.
	 * @param message
	 */
    public SchemaMetadataException(String message) {
        super(message);
    }

    public SchemaMetadataException(String msg, Throwable e) {
        super(msg, e);
    }
}

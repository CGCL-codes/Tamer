package javax.management.relation;

/**
 * This exception is raised when there is no relation type with given name in
 * Relation Service.
 *
 * @since 1.5
 */
public class RelationTypeNotFoundException extends RelationException {

    private static final long serialVersionUID = 1274155316284300752L;

    /**
     * Default constructor, no message put in exception.
     */
    public RelationTypeNotFoundException() {
        super();
    }

    /**
     * Constructor with given message put in exception.
     *
     * @param message the detail message.
     */
    public RelationTypeNotFoundException(String message) {
        super(message);
    }
}

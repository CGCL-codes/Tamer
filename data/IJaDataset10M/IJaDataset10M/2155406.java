package archmapper.main.exceptions;

/**
 * Base class for all exceptions thrown by ArchMapper
 * @author mg
 *
 */
public class ArchMapperException extends RuntimeException {

    private static final long serialVersionUID = 9202144589620854344L;

    public ArchMapperException() {
    }

    public ArchMapperException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public ArchMapperException(String message) {
        super(message);
    }

    public ArchMapperException(String message, Throwable cause) {
        super(message, cause);
    }
}

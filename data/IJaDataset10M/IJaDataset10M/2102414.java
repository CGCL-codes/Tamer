package scap.check.exec;

/**
 * 
 */
public class MappingFileException extends RuntimeException {

    /** the serial version UID */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public MappingFileException() {
    }

    /**
	 * @param message
	 */
    public MappingFileException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public MappingFileException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public MappingFileException(String message, Throwable cause) {
        super(message, cause);
    }
}

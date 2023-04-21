package werkzeugkasten.gainer.exception;

import gnu.io.NoSuchPortException;

public class NoSuchPortRuntimeException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3601171921232982387L;

    public NoSuchPortRuntimeException() {
    }

    public NoSuchPortRuntimeException(NoSuchPortException cause) {
        super(cause);
    }
}

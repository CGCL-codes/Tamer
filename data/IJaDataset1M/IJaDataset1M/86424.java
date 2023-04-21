package image.png;

import Exception.StegoException;

public class PngException extends StegoException {

    private static final long serialVersionUID = -1610069238197217990L;

    public PngException() {
        super();
    }

    public PngException(String message, Throwable cause) {
        super("Png + " + message, cause);
    }

    public PngException(String message) {
        super("Png + " + message);
    }

    public PngException(Throwable cause) {
        super(cause);
    }
}

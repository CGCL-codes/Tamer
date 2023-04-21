package stegj.core.exception;

/**
 * Lanciata qualora non venisse ritrovato alcun dato nascosto nell'immagine
 */
public class DataNotRecognizedException extends Exception {

    /**
	 * Serve per serializzazione..
	 */
    private static final long serialVersionUID = 7814453567296451682L;

    public DataNotRecognizedException(String msg) {
        super(msg);
    }

    public DataNotRecognizedException() {
        super();
    }
}

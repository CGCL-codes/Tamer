package de.uni_trier.st.nevada.io;

/**
 * @author mathias
 *
 * Thrown whenever an <code>Importer</code> or <code>Exporter</code> is not
 * able to handle data appropriately. This may be caused by missing codecs as
 * DOM-Implementations or by wrong versions. Usually this exception causes
 * another <code>Importer</code> or <code>Exporter</code> to try finishing
 * the operation.
 *
 */
public class FileTypeNotSupportedException extends Exception {

    /**
	 *
	 */
    public FileTypeNotSupportedException() {
    }

    /**
	 * @param message
	 * @param cause
	 */
    public FileTypeNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param message
	 */
    public FileTypeNotSupportedException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public FileTypeNotSupportedException(Throwable cause) {
        super(cause);
    }
}

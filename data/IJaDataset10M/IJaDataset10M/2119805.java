package au.gov.naa.digipres.rollingchecker;

/**
 * @author Justin Waddell
 *
 */
public class AIPExceptionItem {

    private String filename;

    private Exception exception;

    private String storedChecksum;

    private String algorithm;

    public AIPExceptionItem(String filename, Exception exception, String storedChecksum, String algorithm) {
        super();
        this.filename = filename;
        this.exception = exception;
        this.storedChecksum = storedChecksum;
        this.algorithm = algorithm;
    }

    /**
	 * @return the filename
	 */
    public String getFilename() {
        return filename;
    }

    /**
	 * @param filename the filename to set
	 */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
	 * @return the exception
	 */
    public Exception getException() {
        return exception;
    }

    /**
	 * @param exception the exception to set
	 */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
	 * @return the storedChecksum
	 */
    public String getStoredChecksum() {
        return storedChecksum;
    }

    /**
	 * @param storedChecksum the storedChecksum to set
	 */
    public void setStoredChecksum(String storedChecksum) {
        this.storedChecksum = storedChecksum;
    }

    /**
	 * @return the algorithm
	 */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
	 * @param algorithm the algorithm to set
	 */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}

package scam.webdav.util;

/**
 * MD5Digest exception class.
 *
 * @author J�ran Stark
 * @author Jan Danils
 * @version $Revision: 1.1.1.1 $
 */
public class MD5DigestException extends Exception {

    /**
     * HTTP status code associated with the exception.
     */
    protected int statusCode;

    /**
     * Constructor.
     *
     * @param statusCode Exception status code
     */
    public MD5DigestException(int statusCode) {
        super(WebdavStatus.getStatusText(statusCode));
        this.statusCode = statusCode;
    }

    public MD5DigestException(int statusCode, String mess) {
        super(mess);
        this.statusCode = statusCode;
    }

    public int getErrorCode() {
        return statusCode;
    }
}

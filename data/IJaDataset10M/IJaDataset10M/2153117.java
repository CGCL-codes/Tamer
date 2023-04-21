package spcp7.imagegallery.abstractionlayer.exception;

/**
 * This exception will be thrown if a connection to the specified content
 * provider could not be established due to authentication reasons.
 * 
 * @author Phillip Merensky
 * @see Exception
 */
public class ContentProviderAuthenticationException extends Exception {

    private String contentProviderName;

    private static final long serialVersionUID = -3971054155255572637L;

    /**
     * @param contentProviderName
     *                The name of the content provider to which the
     *                authentication failed.
     */
    public ContentProviderAuthenticationException(String contentProviderName) {
        this.contentProviderName = contentProviderName;
    }

    /**
     * @param message
     * @param contentProviderName
     *                The name of the content provider to which the
     *                authentication failed.
     */
    public ContentProviderAuthenticationException(String message, String contentProviderName) {
        super(message);
        this.contentProviderName = contentProviderName;
    }

    /**
     * @param cause
     * @param contentProviderName
     *                The name of the content provider to which the
     *                authentication failed.
     */
    public ContentProviderAuthenticationException(Throwable cause, String contentProviderName) {
        super(cause);
        this.contentProviderName = contentProviderName;
    }

    /**
     * @param message
     * @param cause
     * @param contentProviderName
     *                The name of the content provider to which the
     *                authentication failed.
     */
    public ContentProviderAuthenticationException(String message, Throwable cause, String contentProviderName) {
        super(message, cause);
        this.contentProviderName = contentProviderName;
    }

    /**
     * @return the name of the content provider to which the authentication
     *         failed.
     */
    public String getContentProviderName() {
        return this.contentProviderName;
    }
}

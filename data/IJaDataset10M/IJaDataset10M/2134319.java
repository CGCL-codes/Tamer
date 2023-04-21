package uk.ac.ebi.rhea.webapp.pub.exception;

/**
 * An exception which is thrown when the required parameter has a null value
 *
 * @author <a href="mailto:hongcao@ebi.ac.uk">Hong Cao</a>
 * @since 16-12-2010
 */
public class NullParamException extends Exception {

    private String paramName;

    public NullParamException(Throwable cause) {
        super(cause);
    }

    public NullParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullParamException(String message) {
        super(message);
    }

    public NullParamException() {
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}

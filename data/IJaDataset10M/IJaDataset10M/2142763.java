package issrg.utils.webdav;

/**
 * Exception class used to catch various other exceptions in the webdav package.
 * 
 * @author Sean Antony
 * @version 19/03/2007
 */
public class HTTPMessageException extends Exception {

    private String errorMessage;

    public HTTPMessageException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

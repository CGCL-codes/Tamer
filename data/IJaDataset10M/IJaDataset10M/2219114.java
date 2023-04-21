package gwtupload.server.exceptions;

import gwtupload.server.UploadServlet;

/**
 * Exception thrown when the recuest's length exceeds the maximum.  
 * 
 * @author Manolo Carrasco Moñino
 *
 */
public class UploadSizeLimitException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    int actualSize;

    int maxSize;

    public UploadSizeLimitException(long max, long actual) {
        super();
        actualSize = (int) (actual / 1024);
        maxSize = (int) (max / 1024);
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        return UploadServlet.getMessage("size_limit", actualSize, maxSize);
    }

    public int getActualSize() {
        return actualSize;
    }

    public int getMaxSize() {
        return maxSize;
    }
}

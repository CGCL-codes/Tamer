package lu.ng.urlchecker.exception.handling;

import java.io.IOException;
import org.apache.commons.httpclient.HttpMethod;

/**
 * The Interface ExceptionHandler.
 * 
 * @author georgosn
 */
public interface ExceptionHandler {

    /**
     * Hanlde.
     * 
     * @param method
     *            the method
     * @param exception
     *            the exception
     * @param retryCount
     *            the retry count
     * @return true, if successful
     */
    boolean hanlde(HttpMethod method, IOException exception, int retryCount);
}

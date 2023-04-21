package edu.stanford.ejalbert.exceptionhandler;

/**
 * This is an interface to be used by the BrowserLauncherRunner for handling
 * exceptions. Applications should implement this interface to handle
 * exceptions in an application specific manner.
 *
 * @author Jeff Chapman
 */
public interface BrowserLauncherErrorHandler {

    /**
     * Takes an exception and does something with it. Usually the implementing
     * class will want to log the exception or display some information about
     * it to the user.
     *
     * @param ex Exception
     */
    public void handleException(Exception ex);
}

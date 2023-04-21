package org.systemsbiology.apps.corragui.client.controller;

/**
 * Classes that want to be informed of errors during request processing by controllers should 
 * implement this interface. Each Request object takes a IRequestErrorHandler object as argument.
 * 
 * @author Vagisha Sharma
 * @version 1.0
 */
public interface IRequestErrorHandler {

    /**
	 * Handles response to a failed request.
	 * @param caught
	 */
    public abstract void handleRequestFailed(Throwable caught);
}

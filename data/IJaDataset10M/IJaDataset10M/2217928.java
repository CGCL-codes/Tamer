package jwebapp;

import javax.servlet.ServletConfig;

/**
 * The interface is used to handle servlet startup and shutdown events and can be used for any purpose.
 */
public interface InitDestroy {

    /**
     * Handle servlet startup event.  If your application requires any setup prior to fulfilling requests, this is the place to do it.
     * @param config an instance of ServletConfig
     */
    public void init(ServletConfig config) throws Exception;

    /**
     * Handle servlet shutdown event.  If your application requires any processing prior to shutting down, this is the place to do it.
     * @param config an instance of ServletConfig
     */
    public void destroy(ServletConfig config) throws Exception;
}

package net.sf.lightbound;

/**
 * Notified for request events for requests that involve this object.
 * 
 * @author esa
 *
 */
public interface RequestListener {

    /**
   * Called before the request is loaded. The difference between this method
   * and beforePageRender is that this method is called before the incoming
   * data, like form fields, is inspected.
   * 
   * @param request the active request
   */
    public abstract void onRequest(Request request);

    /**
   * Called before the page is rendered. The difference between this method
   * and onRequest is that this method is called after the incoming
   * data, like form fields, is inspected, and right before the page is about
   * to be rendered.
   * 
   * @param request the active request
   */
    public abstract void beforePageRender(Request request);

    /**
   * Called after the page is rendered.
   * 
   * @param request the active request
   */
    public abstract void onRequestFinished(Request request);

    /**
   * Called if an exception occurs while processing the page
   * 
   * @param exception the exception that occurred
   * @throws Exception the resulting exception
   */
    public void onException(Exception exception, Request request) throws Exception;
}

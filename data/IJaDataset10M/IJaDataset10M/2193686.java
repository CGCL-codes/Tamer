package org.jboss.resteasy.spi;

/**
 * Implementations of this interface are registered through the Registry class.
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public interface ResourceFactory {

    /**
    * Class to scan for jax-rs annotations
    *
    * @return
    */
    Class<?> getScannableClass();

    /**
    * Callback after registration has been completed.
    *
    * @param factory allows singleton factories to pre-inject things like @Context references into the singleton instance
    */
    void registered(InjectorFactory factory);

    /**
    * Called per request to obtain a resource instance to invoke http request on.
    *
    * @param request
    * @param response
    * @param factory
    * @return
    */
    Object createResource(HttpRequest request, HttpResponse response, InjectorFactory factory);

    /**
    * Callback when request is finished.  usable for things like @PreDestroy if the underlying factory supports it
    *
    * @param request
    * @param response
    * @param resource
    */
    void requestFinished(HttpRequest request, HttpResponse response, Object resource);

    void unregistered();
}

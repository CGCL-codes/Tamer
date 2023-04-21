package net.sf.oxygen.core.service;

import java.util.List;
import net.sf.oxygen.core.event.ServiceListener;

/**
 * The interface to the object managing services in the oxygen framework. This
 * is the core service meeting point. It allows service suppliers to register
 * services and allows serviceConsumers to discover services.
 * @author <A HREF="mailto:seniorc@users.sourceforge.net?subject=net.sf.oxygen.core.ServiceManager">Chris Senior</A>
 */
public interface ServiceManager {

    /**
   * Register a service object and make it available to others.
   * @param id The service identifier (normally a fully qualified service interface name)
   * @param attributes A set of attributes for the service (may be null)
   * @param service The service object that implements the service interface
   * @return An object representing the registered service, which is required to unregister the service
   * @see #unregister(ServiceRegistration)
   */
    public ServiceRegistration register(String id, ServiceAttributes attributes, Object service);

    /**
   * Unregister a service from the framework - and stop consumer's discovering
   * it.
   * @param reg The registration created when the service was registered
   * @see #register(String, ServiceAttributes, Object)
   */
    public void unregister(ServiceRegistration reg);

    /**
   * Register a service consumer as interested in a group of services. A consumer
   * can only register once for any interest, a null interest implies interest
   * in ALL services registered in the framework.
   * @param sc The consumer who is being registered for call back
   * @param interests The set of interests the consumer has
   */
    public void addServiceConsumer(ServiceConsumer sc, ServiceInterest[] interests);

    /**
   * Register a service consumer as interested in a single service. If this
   * service consumer is already registered for the supplied interest - this call
   * is ignored. A null interest implies the consumer is interested in ALL
   * services registered.
   * @param sc The consumer who is being registered.
   * @param interest The interest the consumer has
   */
    public void addServiceConsumer(ServiceConsumer sc, ServiceInterest interest);

    /**
   * Remove a service consumer from a collection of interests. This is equivalent
   * to calling {@link #removeServiceConsumer(ServiceConsumer, ServiceInterest)}
   * for each interest in the collection. This prevents the consumer receiving
   * any future updates on the interests, and any interests that were previously
   * found are lost - the consumer's {@link ServiceConsumer#losing(ServiceInterest, ServiceReference)}
   * method is called for each previously "found" service. 
   * @param consumer The consumer to be removed
   * @param interests The interests to remove the consumer from
   */
    public void removeServiceConsumer(ServiceConsumer consumer, ServiceInterest[] interests);

    /**
   * Remove a service consumer from an interest. This prevents the consumer receiving
   * any future updates on the interest, and if the interest was previously
   * found becomes lost - so the consumer's {@link ServiceConsumer#losing(ServiceInterest, ServiceReference)}
   * method is called. 
   * @param consumer The consumer to be removed
   * @param interests The interest to remove the consumer from
   */
    public void removeServiceConsumer(ServiceConsumer consumer, ServiceInterest interest);

    /**
   * A list of all interests for a given consumer
   * @param consumer The consumer to get the interests of
   * @return All the interests of the consumer
   */
    public List allInterests(ServiceConsumer consumer);

    /**
   * Provides access to the list of all registered service consumers
   * @return A list of all {@link ServiceConsumer}s
   */
    public List getServiceConsumers();

    /**
   * Provides access to a list of all registered services. This returns a 
   * collection of {@link ServiceReference}s pointing to all currently registered
   * services.
   * <P>NOTE: All services are free to leave the framework at any time - a 
   * service acquired through this method is not guaranteed to be available for
   * any length of time and can be un-registered without warning. All service
   * consumers are recommended to use the event driven {@link ServiceConsumer}
   * model, which allows consumers to respond more effectively to service 
   * discovery and loss.  
   * @return A list of all registered services as {@link ServiceReference}s
   */
    public List getRegisteredServices();

    /**
   * Provides a a list of all registered services for a given interest. This returns a 
   * collection of {@link ServiceReference}s for all currently registered
   * services that match the interest.
   * <P>NOTE: All services are free to leave the framework at any time - a 
   * service acquired through this method is not guaranteed to be available for
   * any length of time and can be un-registered without warning. All service
   * consumers are recommended to use the event driven {@link ServiceConsumer}
   * model, which allows consumers to respond more effectively to service 
   * discovery and loss.  
   * @param interest The service interest description - may match many services
   * @return A list of all registered services matching the interest as {@link ServiceReference}s
   */
    public List getRegisteredServices(ServiceInterest interest);

    /**
   * A convenience method for looking up services of a particular type. Calling
   * this method is identical to calling:
   * <code>
   * this.getRegisteredServices(new ServiceInterest(service));
   * </code>
   * @param service The service identifier to search on
   * @return A list of all registered services matching the service type as {@link ServiceReference}s 
   */
    public List getRegisteredServices(String service);

    /**
   * Add a listener to be notified of service registration and unregistration
   * events.
   * @param listener The listener to add for such events
   */
    public void addServiceListener(ServiceListener listener);

    /**
   * Stop a lsitener being notified of future service registration events 
   * @param listener The listener to remove
   */
    public void removeServiceListener(ServiceListener listener);
}

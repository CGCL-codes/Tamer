package org.exolab.jms.service;

/**
 * A <code>Serviceable</code> which manages a collection of services.
 * <p/>
 * A service may be any object. If it implements the {@link Serviceable}
 * interface, then its lifecycle will be managed by this.
 * <p/>
 * Services may be registered using their class types, or instances.
 * Only a single instance of a particular service may exist at any time.
 * <p/>
 * Services that are registered using their class types will be created
 * when first accessed via {@link #getService}. This will recursively resolve
 * any other services that the service is dependent on.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.1 $ $Date: 2005/08/30 04:56:14 $
 */
public interface Services extends Serviceable {

    /**
     * Add a service of the specified type.
     * <p/>
     * The service will be constructed when it is first accessed via
     * {@link #getService}.
     *
     * @param type the type of the service
     * @throws ServiceAlreadyExistsException if the service already exists
     * @throws ServiceException for any service error
     */
    void addService(Class type) throws ServiceException;

    /**
     * Add a service instance.
     *
     * @param service the service instance
     * @throws ServiceAlreadyExistsException if the service already exists
     * @throws ServiceException for any service error
     */
    void addService(Object service) throws ServiceException;

    /**
     * Returns a service given its type.
     * <p/>
     * If the service has been registered but not constructed, it will be
     * created and any setters populated.
     *
     * @param type the type of the service
     * @return an instance of <code>type</code>
     * @throws ServiceDoesNotExistException if the service doesn't exist, or
     * is dependent on a service which doesn't exist
     * @throws ServiceException for any service error
     */
    Object getService(Class type) throws ServiceException;
}

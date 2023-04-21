package org.davic.net.ca;

import org.davic.mpeg.ElementaryStream;
import org.davic.mpeg.Service;
import org.davic.resources.ResourceClient;
import org.davic.resources.ResourceProxy;

/**
 * The class DescramblerProxy is a proxy for the descrambling resources.
 * <p>
 * An application instantiates an object of this class to descramble a service
 * or elementary streams of a single service. If an application wants to
 * descramble multiple services simultaneously, it should instantiate multiple
 * objects.
 */
public class DescramblerProxy implements ResourceProxy {

    /**
	 * Constructor of a resource proxy for a specific resource client.
	 * @param c the resource client object representing the application as a
	 *            client for the resource management
	 */
    public DescramblerProxy(ResourceClient c) {
    }

    /**
	 * Requests the CA system to perform any user dialogs needed before starting
	 * to descramble the service.
	 * <p>
	 * This version can be used for descrambling the whole service. In case of
	 * CA0 this maps onto ca_pmt with ca_pmt_cmd_id = ok_mmi (Common Interface
	 * specification, section 8.4.3.4). In systems based on the DVB common
	 * interface,the NotAuthorizedException shall never be thrown. After
	 * succesful execution, descrambling can be started with the
	 * startDescrambling method.
	 * @param s service to be descrambled after the dialogue
	 * @exception NoFreeCapacityException raised if the CAModule does not have
	 *                available capacity to perform this action now
	 * @exception ModuleUnavailableException raised if the physical CA module
	 *                has been removed and is not available any more
	 * @exception NotTunedException raised if performing the action would
	 *                require being tuned to the transport stream carrying the
	 *                service and the receiver is not currently tuned to it
	 * @exception org.davic.mpeg.NotAuthorizedException raised if the user is
	 *                not authorized to perform this action
	 */
    public void startDescramblingDialog(Service s) throws CAException, org.davic.mpeg.NotAuthorizedException {
    }

    /**
	 * Requests the CA system to perform any user dialogs needed before starting
	 * to descramble the service.
	 * <p>
	 * This version can be used for descrambling only specified subset of
	 * elementary streams of a service. The elementary streams given in the
	 * parameter array must belong to the same service. If the parameter array
	 * is a zero length array then this method shall have no effect. In case of
	 * CA0 this maps onto ca_pmt with ca_pmt_cmd_id = ok_mmi (Common Interface
	 * specification, section 8.4.3.4). In systems based on the DVB common
	 * interface,the NotAuthorizedException shall never be thrown. After
	 * succesful execution, descrambling can be started with the
	 * startDescrambling method.
	 * @param streams subset of elementary streams of a service to be
	 *            descrambled after the dialogue
	 * @exception NoFreeCapacityException raised if the CAModule does not have
	 *                available capacity to perform this action now
	 * @exception ModuleUnavailableException raised if the physical CA module
	 *                has been removed and is not available any more
	 * @exception NotTunedException raised if performing the action would
	 *                require being tuned to the transport stream carrying the
	 *                service and the receiver is not currently tuned to it
	 * @exception org.davic.mpeg.NotAuthorizedException raised if the user is
	 *                not authorized to perform this action
	 */
    public void startDescramblingDialog(ElementaryStream[] streams) throws CAException, org.davic.mpeg.NotAuthorizedException {
    }

    /**
	 * Stops all existing descrambling initiated by this DescramblerProxy
	 * instance and starts the descrambling of the specified service.
	 * <p>
	 * The module used for descrambling is returned. If descrambling is not
	 * possible, an exception is thrown. This method may result in the CA system
	 * requesting an MMI dialog. In systems based on the DVB common interface
	 * this maps onto ca_pmt with ca_pmt_cmd_id = ok_descrambling (Common
	 * Interface specification,section 8.4.3.4) and the NotAuthorizedException
	 * shall never be thrown.
	 * @param s service to be descrambled
	 * @param requestData Used by the ResourceNotification API in the
	 *            requestRelease method of the ResourceClient interface. Usage
	 *            of this parameter is optional and a null reference may be
	 *            supplied. DescramblerProxy applies from the point of view of
	 *            one application. Methods such as
	 *            <code>startDescrambling</code> and
	 *            <code>stopDescrambling</code> apply on a per-application
	 *            basis and do not impact descrambling on behalf of other
	 *            applications, except subject to platform resource limitations.
	 * @exception NoFreeCapacityException raised if the CAModule does not have
	 *                available capacity to perform this action now
	 * @exception ModuleUnavailableException raised if the physical CA module
	 *                has been removed and is not available any more
	 * @exception NotTunedException raised if performing the action would
	 *                require being tuned to the transport stream carrying the
	 *                service and the receiver is not currently tuned to it
	 * @exception org.davic.mpeg.NotAuthorizedException raised if the user is
	 *                not authorized to perform this action
	 * @return CA module used for descrambling
	 */
    public synchronized CAModule startDescrambling(Service s, Object requestData) throws CAException, org.davic.mpeg.NotAuthorizedException {
        return null;
    }

    /**
	 * Stops all existing descrambling initiated by this DescramblerProxy
	 * instance and starts the descrambling of the specified elementary streams
	 * (that shall be part of the same service).
	 * <p>
	 * The module used for descrambling is returned. If descrambling is not
	 * possible, an exception is thrown. If the <code>streams</code> parameter
	 * is a zero length array then the method shall have no effect. This method
	 * may result in the CA system requesting an MMI dialog. In systems based on
	 * the DVB common interface this maps onto ca_pmt with ca_pmt_cmd_id =
	 * ok_descrambling (Common Interface specification,section 8.4.3.4) and the
	 * NotAuthorizedException shall never be thrown.
	 * @param streams subset of elementary streams of a service to be
	 *            descrambled after the dialogue
	 * @param requestData Used by the ResourceNotification API in the
	 *            requestRelease method of the ResourceClient interface. Usage
	 *            of this parameter is optional and a null reference may be
	 *            supplied.
	 * @exception NoFreeCapacityException raised if the CAModule does not have
	 *                available capacity to perform this action now
	 * @exception ModuleUnavailableException raised if the physical CA module
	 *                has been removed and is not available any more
	 * @exception NotTunedException raised if performing the action would
	 *                require being tuned to the transport stream carrying the
	 *                service and the receiver is not currently tuned to it
	 * @exception org.davic.mpeg.NotAuthorizedException raised if the user is
	 *                not authorized to perform this action
	 * @return CA module used for descrambling
	 */
    public synchronized CAModule startDescrambling(ElementaryStream[] streams, Object requestData) throws CAException, org.davic.mpeg.NotAuthorizedException {
        return null;
    }

    /**
	 * Stops all existing descrambling initiated by this DescramblerProxy
	 * instance and starts the descrambling of the specified service.
	 * <p>
	 * The module to be used for descrambling is indicated as a parameter. If
	 * descrambling is not possible, an exception is thrown. If the
	 * <code>streams</code> parameter is a zero length array then the method
	 * shall have no effect. This method may result in the CA system requesting
	 * an MMI dialog. In systems based on the DVB common interface this maps
	 * onto ca_pmt with ca_pmt_cmd_id = ok_descrambling (Common Interface
	 * specification,section 8.4.3.4) and the NotAuthorizedException shall never
	 * be thrown.
	 * @param s service to be descrambled
	 * @param module CA module to be used for descrambling
	 * @param requestData Used by the ResourceNotification API in the
	 *            requestRelease method of the ResourceClient interface. Usage
	 *            of this parameter is optional and a null reference may be
	 *            supplied.
	 * @exception NoFreeCapacityException raised if the CAModule does not have
	 *                available capacity to perform this action now
	 * @exception ModuleUnavailableException raised if the physical CA module
	 *                has been removed and is not available any more
	 * @exception NotTunedException raised if performing the action would
	 *                require being tuned to the transport stream carrying the
	 *                service and the receiver is not currently tuned to it
	 * @exception org.davic.mpeg.NotAuthorizedException raised if the user is
	 *                not authorized to perform this action
	 */
    public synchronized void startDescrambling(Service s, CAModule module, Object requestData) throws CAException, org.davic.mpeg.NotAuthorizedException {
    }

    /**
	 * Stops all existing descrambling initiated by this DescramblerProxy
	 * instance and starts the descrambling of the specified elementary streams
	 * (that shall be part of the same service). The module to be used for
	 * descrambling is indicated as a parameter. If descrambling is not
	 * possible, an exception is thrown. This method may result in the CA system
	 * requesting an MMI dialog. In systems based on the DVB common interface
	 * this maps onto ca_pmt with ca_pmt_cmd_id = ok_descrambling (Common
	 * Interface specification,section 8.4.3.4) and the NotAuthorizedException
	 * shall never be thrown.
	 * @param streams subset of elementary streams of a service to be
	 *            descrambled after the dialogue
	 * @param module CA module to be used for descrambling
	 * @param requestData Used by the ResourceNotification API in the
	 *            requestRelease method of the ResourceClient interface. Usage
	 *            of this parameter is optional and a null reference may be
	 *            supplied.
	 * @exception NoFreeCapacityException raised if the CAModule does not have
	 *                available capacity to perform this action now
	 * @exception ModuleUnavailableException raised if the physical CA module
	 *                has been removed and is not available any more
	 * @exception NotTunedException raised if performing the action would
	 *                require being tuned to the transport stream carrying the
	 *                service and the receiver is not currently tuned to it
	 * @exception org.davic.mpeg.NotAuthorizedException raised if the user is
	 *                not authorized to perform this action
	 */
    public synchronized void startDescrambling(ElementaryStream[] streams, CAModule module, Object requestData) throws CAException, org.davic.mpeg.NotAuthorizedException {
    }

    /**
	 * Stops descrambling of the service. If no descrambling is being done then
	 * this method has no effect.
	 * @exception ModuleUnavailableException raised if the physical CA module
	 *                has been removed and is not available any more
	 */
    public void stopDescrambling() throws CAException {
    }

    /**
	 * Stops descrambling of the specified elementary streams of the service.
	 * This method stops the descrambling of streams which have been started
	 * through this DescramblerProxy instance, and not any started through any
	 * other instance. This method only effects members of the array of streams
	 * that are being descrambled. There is no effect on any streams listed in
	 * the array that are not being descrambled.
	 * @param streams the array of <code>ElementaryStream</code> s whose
	 *            descrambling is to be stopped.
	 * @exception ModuleUnavailableException raised if the physical CA module
	 *                has been removed and is not available any more
	 */
    public void stopDescrambling(ElementaryStream[] streams) throws CAException {
    }

    /**
	 * Registers a new descrambler event listener.
	 * @param l the listener to be registered
	 */
    public void addDescramblerListener(DescramblerListener l) {
    }

    /**
	 * Removes a registered descrambler event listener.
	 * @param l the listener to be removed
	 */
    public void removeDescramblerListener(DescramblerListener l) {
    }

    /**
	 * Returns the CA module that is associated with the current descrambling
	 * via this proxy.
	 * <p>
	 * If there is no descrambling being active via this proxy at the moment,
	 * this method returns null.
	 * @return CAModule object representing the CA module that is associated
	 *         with the current descrambling activity.
	 */
    public CAModule getCAModule() {
        return null;
    }

    /**
	 * Returns the resource client associated with this resource proxy. This
	 * method implements ResourceProxy.getClient method.
	 * @return resource client
	 */
    public ResourceClient getClient() {
        return null;
    }

    /**
	 * This method returns the elementary streams being descrambled via this
	 * proxy at the moment. If there is no descrambling, the method returns an
	 * empty array.
	 * @returns array of elementary stream being descrambled
	 */
    public ElementaryStream[] getElementaryStreams() {
        return null;
    }
}

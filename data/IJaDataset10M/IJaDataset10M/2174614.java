package jhomenet.comm.client.method;

import java.rmi.*;
import org.apache.log4j.Logger;
import jhomenet.comm.*;
import jhomenet.comm.client.*;
import jhomenet.comm.server.*;

public class RegisterForNotificationMethodCall extends AbstractRemoteMethodCall<Object, RemoteException> {

    /***
     * Logging mechanism.
     */
    private static Logger logger = Logger.getLogger(RegisterForNotificationMethodCall.class.getName());

    /***
     * A reference to the remote hardware registry
     */
    private ServerDescription<RemoteHardwareRegistry> remoteHardwareRegistry;

    private Notifiable notifiable;

    /***
     * Constructor.
     * 
     * @param serverDescription
     */
    public RegisterForNotificationMethodCall(ServerDescription<RemoteHardwareRegistry> serverDescription, Notifiable n) {
        this.remoteHardwareRegistry = serverDescription;
        this.notifiable = n;
    }

    /***
     * Perform the remote method call.
     * 
     * @see jhomenet.comm.client.AbstractRemoteMethodCall#performRemoteCall(java.rmi.Remote)
     */
    public Object performRemoteCall(Remote remote) throws RemoteException {
        try {
            getRemoteObject().registerForNotifications(notifiable);
        } catch (ServerUnavailable sue) {
            logger.error("Exception occurred while registering for notification: " + sue.getMessage());
        }
        return false;
    }

    /***
     * Get a reference to the remote hardware registry object.
     * 
     * @see jhomenet.comm.client.AbstractRemoteMethodCall#getRemoteObject()
     */
    protected RemoteHardwareRegistry getRemoteObject() throws ServerUnavailable {
        return remoteHardwareRegistry.getStub();
    }
}

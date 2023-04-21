package openjmx.adaptor.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * This class is serialized from client to the server, and forwards calls to remote listener. <p>
 * It shadows the fact that the listener is an RMI remote object.
 * It implements java.rmi.Remote as a tagging interface, to avoid dependencies of the specification 1.0
 * implementation from openjmx classes
 *
 * @author <a href="mailto:biorn_steedom@users.sourceforge.net">Simone Bordet</a>
 * @version $Revision: 1.2 $
 */
public class RemoteNotificationListenerSupport implements NotificationListener, Remote {

    /**
	 * When this class is serialized to the server, the stub of this remote object
	 * will be sent to the server, enabling it to call back the client.
	 * This must be the ONLY data member.
	 */
    private RemoteNotificationListener m_clientSideListener;

    public RemoteNotificationListenerSupport(NotificationListener clientListener, boolean iiop) throws RemoteException {
        m_clientSideListener = new RemoteNotificationListenerImpl(clientListener, iiop);
    }

    public RemoteNotificationListenerSupport(NotificationListener clientListener) throws RemoteException {
        m_clientSideListener = new RemoteNotificationListenerImpl(clientListener);
    }

    public RemoteNotificationListenerSupport(NotificationListener clientListener, int port) throws RemoteException {
        m_clientSideListener = new RemoteNotificationListenerImpl(clientListener, port);
    }

    public RemoteNotificationListenerSupport(NotificationListener clientListener, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        m_clientSideListener = new RemoteNotificationListenerImpl(clientListener, port, csf, ssf);
    }

    public void handleNotification(Notification notification, Object handback) {
        try {
            m_clientSideListener.handleNotification(notification, handback);
        } catch (RemoteException x) {
            throw new RuntimeException(x.toString());
        }
    }
}

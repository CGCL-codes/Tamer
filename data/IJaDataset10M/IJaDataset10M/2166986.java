package RMI.server.activation;

import Events.CallListener;
import RMI.client.Notify;
import RMI.server.ServerLogic;
import java.math.BigInteger;
import java.rmi.*;
import java.rmi.activation.*;
import javax.swing.event.EventListenerList;

/** ###
 * Implementation des RemoteInterfaces mit Wakeup Funktionalitaet
 * @author Haiko Michelfeit ice1279@users.sourceforge.net
 */
public class ActivatableImplementation extends Activatable implements RMI.server.activation.CalcRemoteInterface {

    /**
    * liste aller listener die horchen
    */
    private EventListenerList listeners = new EventListenerList();

    /**
     * Konstruktor fuer Activation und Export
     * @param id
     * @param data
     * @throws java.rmi.RemoteException
     * @author Haiko Michelfeit
     */
    public ActivatableImplementation(ActivationID id, MarshalledObject data) throws RemoteException {
        super(id, 0);
        new ServerLogic(this);
    }

    @Override
    protected void finalize() throws Throwable {
        notifyShutdown();
        super.finalize();
    }

    /****************************************************************************
    * Event Methoden
    ****************************************************************************/
    public synchronized void addCallListener(CallListener listener) {
        listeners.add(CallListener.class, listener);
    }

    public synchronized void removeCallListener(CallListener listener) {
        listeners.remove(CallListener.class, listener);
    }

    public synchronized void notifycalcCallRemotelyEvent(Notify notify, int prio, Long calcID, BigInteger start, BigInteger n, BigInteger zahl) throws RemoteException {
        for (CallListener l : listeners.getListeners(CallListener.class)) l.calcCallRemotelyEvent(notify, prio, calcID, start, n, zahl);
    }

    public synchronized void notifycalcBreakRemotelyEvent(Long calcID) throws RemoteException {
        for (CallListener l : listeners.getListeners(CallListener.class)) l.calcBreakRemotelyEvent(calcID);
    }

    public synchronized void notifyShutdown() throws RemoteException {
        for (CallListener l : listeners.getListeners(CallListener.class)) l.shudown();
    }

    /****************************************************************************
    * Abstracte RMI Methoden ueberschreiben
    ****************************************************************************/
    public void calcCallRemotely(Notify notify, int prio, Long calcID, BigInteger start, BigInteger n, BigInteger zahl) throws RemoteException {
        notifycalcCallRemotelyEvent(notify, prio, calcID, start, n, zahl);
    }

    public void calcBreakRemotely(Long calcID) throws RemoteException {
        notifycalcBreakRemotelyEvent(calcID);
    }
}

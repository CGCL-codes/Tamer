package alice.cartago;

import java.rmi.RemoteException;

public interface IActivityContextRemote extends java.rmi.Remote {

    void postNewEvent(Event ev, SensorId sid) throws RemoteException;
}

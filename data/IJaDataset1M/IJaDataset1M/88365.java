package alice.cartago;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class ActivityContextRemote extends UnicastRemoteObject implements IActivityContextRemote {

    private ActivityContext context;

    public ActivityContextRemote(ActivityContext context) throws RemoteException {
        this.context = context;
    }

    public void postNewEvent(Event ev, SensorId sid) throws RemoteException {
        context.postNewEvent(ev, sid);
    }
}

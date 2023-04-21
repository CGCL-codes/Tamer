package co.edu.unal.ungrid.client.worker;

import java.rmi.RemoteException;

public class CancelEventListener extends TaskEventListener {

    public CancelEventListener(SpaceTask st) throws RemoteException {
        super(st);
    }

    public void notified() {
        m_st.cancel();
    }
}

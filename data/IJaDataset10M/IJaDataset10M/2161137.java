package net.sourceforge.mords.store.data.rx;

import java.rmi.RemoteException;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;

/**
 *
 * @author david
 */
public class ListMeds implements PrivilegedExceptionAction {

    private RxDbPrivilege rxdb;

    /** Creates a new instance of ListMeds */
    public ListMeds(RxDbPrivilege rxdb) {
        this.rxdb = rxdb;
    }

    public Object run() {
        try {
            return rxdb.listMeds();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return ex;
        }
    }
}

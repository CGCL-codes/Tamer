package datawave.xmlsync.ejb;

import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;
import java.rmi.RemoteException;
import datawave.xmlsync.*;

/**
 * @author �����
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class XMLSyncBean implements SessionBean {

    public void run(final String xml, final String scriptpath) throws RemoteException {
        try {
            Machine sync = MachinePool.get();
            sync.setxml("/", xml);
            sync.run(scriptpath);
        } catch (Exception e) {
            throw new RemoteException("[[Machine.push]] source : " + e.getMessage());
        }
    }

    public void ejbCreate() {
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void setSessionContext(SessionContext sc) {
    }
}

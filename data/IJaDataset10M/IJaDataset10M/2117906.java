package org.apache.harmony.rmi.activation;

import java.io.BufferedInputStream;
import java.lang.reflect.Constructor;
import java.rmi.MarshalledObject;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.activation.ActivationID;
import java.rmi.activation.UnknownObjectException;
import java.rmi.server.RMIClassLoader;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;
import org.apache.harmony.rmi.common.RMILog;
import org.apache.harmony.rmi.internal.nls.Messages;
import org.apache.harmony.rmi.transport.RMIObjectInputStream;

/**
 * The implementation of ActivationGroup.
 *
 * @author  Victor A. Martynov
 */
public class ActivationGroupImpl extends ActivationGroup {

    private static final long serialVersionUID = 5526311808915869570L;

    private ActivationGroupID groupID;

    private boolean isGroupActive = true;

    private static RMILog rlog = RMILog.getActivationLog();

    private Hashtable active_objects = new Hashtable();

    /**
     *
     */
    private Class[] special_constructor_parameters = { ActivationID.class, MarshalledObject.class };

    /**
     * This main method is used to start new VMs for ActivationGroups. Four
     * parameters needed to create ActivationGroup are: <br>
     * ActivationGroupID <br>
     * ActivationGroupDesc <br>
     * incarnation The parameters needed to create ActivationGroup correctly are
     * passed through the standard input stream in the following order: <br>
     * ActivationGroupID -> ActivationGroupDesc -> incarnation
     */
    public static void main(String args[]) {
        rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.4C"));
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.4F", System.in.available()));
            RMIObjectInputStream ois = new RMIObjectInputStream(new BufferedInputStream(System.in));
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.55", ois));
            ActivationGroupID agid = (ActivationGroupID) ois.readObject();
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.57", agid));
            ActivationGroupDesc agdesc = (ActivationGroupDesc) ois.readObject();
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.74", agdesc));
            long incarnation = ois.readLong();
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.7B", incarnation));
            ActivationGroup.createGroup(agid, agdesc, incarnation);
        } catch (Throwable t) {
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.7C", t));
            t.printStackTrace();
        }
    }

    /**
     * Method from ActivationInstantiator interface.
     */
    public MarshalledObject newInstance(final ActivationID aid, final ActivationDesc adesc) throws ActivationException {
        rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.83"));
        if (!groupID.equals(adesc.getGroupID())) {
            throw new ActivationException(Messages.getString("rmi.36"));
        }
        if (isGroupActive == false) {
            throw new ActivationException(Messages.getString("rmi.37"));
        }
        ActiveObject ao = (ActiveObject) active_objects.get(aid);
        rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.84", ao));
        if (ao != null) {
            return ao.remote_object_stub;
        }
        try {
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.85", adesc.getLocation(), adesc.getClassName()));
            final Class aclass = RMIClassLoader.loadClass(adesc.getLocation(), adesc.getClassName());
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.86", aclass));
            Remote rmt = (Remote) AccessController.doPrivileged(new PrivilegedExceptionAction() {

                public Object run() throws Exception {
                    Constructor aconstructor = aclass.getDeclaredConstructor(special_constructor_parameters);
                    rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.87", aconstructor));
                    aconstructor.setAccessible(true);
                    Object parameters[] = new Object[] { aid, adesc.getData() };
                    return (Remote) aconstructor.newInstance(parameters);
                }
            });
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.88", rmt.getClass()));
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.89", rmt));
            ao = new ActiveObject(rmt);
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.91", ao));
            active_objects.put(aid, ao);
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.8A"));
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.8B"));
            super.activeObject(aid, ao.remote_object_stub);
            return ao.remote_object_stub;
        } catch (Throwable t) {
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.44", t), t);
            return null;
        }
    }

    /**
     * Constructor
     */
    public ActivationGroupImpl(ActivationGroupID agid, MarshalledObject data) throws RemoteException, ActivationException {
        super(agid);
        groupID = agid;
        rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.8C"));
    }

    public boolean inactiveObject(ActivationID id) throws ActivationException, UnknownObjectException, RemoteException {
        ActiveObject ao = (ActiveObject) active_objects.get(id);
        if (ao == null) {
            throw new UnknownObjectException(Messages.getString("rmi.93"));
        }
        Activatable.unexportObject(ao.getImpl(), false);
        super.inactiveObject(id);
        active_objects.remove(id);
        return true;
    }

    public void activeObject(ActivationID id, Remote obj) throws ActivationException, UnknownObjectException, RemoteException {
        ActiveObject ao = new ActiveObject(obj);
        active_objects.put(id, ao);
        super.activeObject(id, ao.getStub());
    }

    private class ActiveObject {

        Remote remote_object_impl;

        MarshalledObject remote_object_stub;

        ActiveObject(Remote rmt) {
            rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.8E"));
            remote_object_impl = rmt;
            try {
                remote_object_stub = new MarshalledObject(rmt);
                rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.8F", remote_object_impl, remote_object_stub));
            } catch (Throwable t) {
                rlog.log(RMILog.VERBOSE, Messages.getString("rmi.log.90", t));
                t.printStackTrace();
            }
        }

        public Remote getImpl() {
            return remote_object_impl;
        }

        public MarshalledObject getStub() {
            return remote_object_stub;
        }
    }
}

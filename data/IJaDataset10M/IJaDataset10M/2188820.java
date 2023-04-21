package org.apache.harmony.rmi.server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteRef;
import org.apache.harmony.rmi.transport.Endpoint;
import org.apache.harmony.rmi.transport.RMIObjectInputStream;
import org.apache.harmony.rmi.transport.RMIObjectOutputStream;

/**
 * Base class for all RemoteRef implementations.
 * It belongs to org.apache.harmony.rmi.transport package because it should have
 * package protected access to ClientDGC implementation.
 *
 * @author  Mikhail A. Markov
 */
public abstract class RemoteRefBase implements RemoteRef {

    private static final long serialVersionUID = 358378173612121423L;

    /** Endpoint this handle refers to. */
    protected Endpoint ep;

    /** Object ID of remote object. */
    protected ObjID objId;

    /** True if this handle is for local object. */
    protected boolean isLocal;

    /**
     * Returns Object ID for the referenced remote object.
     *
     * @return Object ID for the referenced remote object
     */
    public ObjID getObjId() {
        return objId;
    }

    /**
     * @see RemoteRef.remoteEquals(RemoteRef)
     */
    public boolean remoteEquals(RemoteRef obj) {
        if (!(obj instanceof RemoteRefBase)) {
            return false;
        }
        RemoteRefBase ref = (RemoteRefBase) obj;
        return ep.equals(ref.ep) && (objId.equals(ref.objId));
    }

    /**
     * @see RemoteRef.remoteToString()
     */
    public String remoteToString() {
        return getRefClass(null) + "[endpoint:[" + ep + "]" + ((isLocal) ? "(local)" : "(remote)") + ", " + objId + "]";
    }

    /**
     * Returns the value returned by remoteToString() method.
     *
     * @return the value returned by remoteToString() method
     */
    public String toString() {
        return remoteToString();
    }

    /**
     * @see RemoteRef.remoteHashCode()
     */
    public int remoteHashCode() {
        return ((objId != null) ? objId.hashCode() : super.hashCode());
    }

    /**
     * Reads everything left except Endpoint info from the given stream and
     * detects if DGC ack is needed.
     *
     * @param in the stream to read data from
     *
     * @throws IOException if any I/O error occurred
     * @throws ClassNotFoundException if class could not be loaded by current
     *         class loader
     */
    protected void readCommon(ObjectInput in) throws IOException, ClassNotFoundException {
        objId = ObjID.read(in);
        boolean needAck = in.readBoolean();
        if (in instanceof RMIObjectInputStream) {
            RMIObjectInputStream oin = (RMIObjectInputStream) in;
            if (oin.isRemoteCallStream()) {
                oin.needDGCAck(needAck);
            }
        }
        RMIObjectInfo info = ExportManager.getInfo(objId);
        if ((info == null) || !info.sref.remoteEquals(this)) {
            ClientDGC.registerForRenew(this);
        }
    }

    /**
     * Writes everything left except Endpoint info to the given stream.
     *
     * @param out the stream to write the object to
     *
     * @throws IOException if any I/O error occurred or class is not serializable
     */
    protected void writeCommon(ObjectOutput out) throws IOException {
        objId.write(out);
        boolean isResStream = false;
        if (out instanceof RMIObjectOutputStream) {
            RMIObjectOutputStream oout = (RMIObjectOutputStream) out;
            isResStream = oout.isResultStream();
            if (isResStream) {
                ClientDGC.registerForDGCAck(oout.getUID(), this);
            }
        }
        out.writeBoolean(isResStream);
    }
}

package org.nakedobjects.object.io;

import org.nakedobjects.object.persistence.Oid;
import java.io.Serializable;

class Data implements Transferable, Serializable {

    private static final long serialVersionUID = 1L;

    final String className;

    final String resolveState;

    final Oid oid;

    public Data(Oid oid, String resolveState, String className) {
        this.oid = oid;
        this.className = className;
        this.resolveState = resolveState;
    }

    public String toString() {
        return className + "/" + oid;
    }

    public Oid getOid() {
        return oid;
    }

    public void writeData(TransferableWriter data) {
        data.writeString(className);
        data.writeString(resolveState);
        data.writeObject((Transferable) oid);
    }

    public Data(TransferableReader data) {
        className = data.readString();
        resolveState = data.readString();
        oid = (Oid) data.readObject();
    }
}

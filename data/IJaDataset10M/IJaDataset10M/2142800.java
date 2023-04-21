package org.nakedobjects.nof.util.memento;

import java.io.Serializable;
import org.nakedobjects.noa.adapter.Oid;
import org.nakedobjects.nof.core.util.DebugString;

class Data implements Transferable, Serializable {

    private static final long serialVersionUID = 1L;

    final String className;

    final String resolveState;

    final Oid oid;

    public Data(final Oid oid, final String resolveState, final String className) {
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

    public void writeData(final TransferableWriter data) {
        data.writeString(className);
        data.writeString(resolveState);
        data.writeObject((Transferable) oid);
    }

    public Data(final TransferableReader data) {
        className = data.readString();
        resolveState = data.readString();
        oid = (Oid) data.readObject();
    }

    public void debug(DebugString debug) {
        debug.appendln(className);
        debug.appendln(oid.toString());
        debug.appendln(resolveState);
    }
}

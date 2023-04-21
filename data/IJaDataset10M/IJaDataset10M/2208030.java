package org.openware.job.data;

import tcl.lang.Interp;
import java.util.Hashtable;

public class PersistTcl {

    protected Persist persist = null;

    protected Hashtable references = new Hashtable();

    protected Interp interp = null;

    public PersistTcl() {
    }

    public PersistTcl(Interp interp) {
        this.interp = interp;
    }

    public void setInterp(Interp interp) {
        this.interp = interp;
    }

    public String getOid() {
        return persist.getOid().toString();
    }

    public Persist getPersist() {
        return persist;
    }

    public void setPersist(Persist persist) {
        this.persist = persist;
    }

    public void remove() throws PersistException {
        persist.remove();
    }
}

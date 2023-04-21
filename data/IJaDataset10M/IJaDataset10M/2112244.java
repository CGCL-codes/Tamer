package org.apache.felix.upnp.basedriver.importer.core.event.structs;

import java.util.Hashtable;
import org.apache.felix.upnp.basedriver.importer.core.event.thread.Renewer;

public class SidRenewer {

    private Hashtable hash;

    /**
	 * @param hash
	 */
    public SidRenewer() {
        super();
        this.hash = new Hashtable();
    }

    public synchronized void put(String sid, Renewer renewer) {
        hash.put(sid, renewer);
    }

    public synchronized Renewer get(String sid) {
        return (Renewer) hash.get(sid);
    }

    public synchronized void remove(String sid) {
        hash.remove(sid);
    }
}

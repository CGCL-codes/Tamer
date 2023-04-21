package br.ufal.ic.utils;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * SharedMem1.java
 *
 * <p>A full description of this class.
 *
 * @see SomeRelatedClass.
 *
 * @author <a href="mailto:camilapbnunes@hotmail.com">camila</a>.
 * @since 0.1
 * @version 0.1
 *
 */
public class SharedMem implements Remote, Serializable {

    private static Logger logger;

    private static Map<String, CriticalRegion> regions;

    static {
        logger = Logger.getLogger(SharedMem.class.getName());
        regions = new HashMap<String, CriticalRegion>();
    }

    /**
     * Creates a new instance of SharedMem1
     */
    public SharedMem() throws RemoteException {
    }

    /**
     * Gets the critical region.
     *
     * @param _region The name of the region.
     *
     * @return The critical region object.
     */
    public static CriticalRegion getRegion(final String _region) throws RemoteException {
        if (regions.get(_region) == null) {
            CriticalRegion cr;
            cr = new CriticalRegion(_region);
            regions.put(_region, cr);
        }
        return regions.get(_region);
    }
}

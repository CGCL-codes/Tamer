package org.tracfoundation.trac2001.primitive.partition;

import org.tracfoundation.trac2001.TRAC2001;
import org.tracfoundation.trac2001.util.*;

/**
 * pn - read auxiliarly index primitive
 *
 * Read or set the auxiliary index of a partition
 * takes no action if the trac process is not a partition
 *
 * @author Edith Mooers, Trac Foundation http://tracfoundation.org
 * @version 1.0 (c) 2001
 */
public class PN {

    /**
     * Read or set the auxiliary index of a partition
     * takes no action if the trac process is not a partition
     *
     * @param <CODE>TRAC2001</CODE> the trac process.
     */
    public static void action(TRAC2001 trac) {
        if (trac instanceof Partition) {
            Primitive active = trac.getActivePrimitive();
            Partition part = (Partition) trac;
            if (active.length() >= 1) part.setAuxIdx(active.getArg(1)); else active.addValue(part.getAuxIdx());
        }
    }
}

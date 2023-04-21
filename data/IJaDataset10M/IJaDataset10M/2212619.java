package edu.dimacs.mms.boxer;

import java.util.*;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** An auxiliary class for {@link Priors}: stores cross-discrimination
 * priors  */
class CrossDiscPriorSet extends DiscPriorSet {

    /** Looks up the applicable cross-discrimination prior (one of
	L1,L2,L3). This method should be invoked only if this is a
	cross-discrimination priors set.
     */
    Prior get(String cname, int fid) {
        Prior p = null;
        if (cname.equals(Suite.NOT_DIS_NAME)) {
            p = L567.get(new CFKey(cname));
        }
        if (p != null) return p;
        p = L567.get(new CFKey(null, fid));
        if (p != null) return p;
        return L_overall;
    }
}

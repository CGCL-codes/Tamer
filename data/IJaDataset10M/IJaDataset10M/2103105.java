package org.unicore.sets;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.unicore.Unicore;
import org.unicore.outcome.Estimate;

/**
 * An interface to a type-safe Enumeration type for <tt>Estimate</tt>s. 
 *
 * @author S. van den Berghe (fecit)
 *
 * @since AJO 1
 *
 * @version $Id: EstimateEnumeration.java,v 1.2 2004/05/26 16:31:44 svenvdb Exp $
 * 
 **/
public class EstimateEnumeration extends Unicore {

    Enumeration e;

    public EstimateEnumeration(Hashtable in) {
        e = in.elements();
    }

    public EstimateEnumeration(Vector in) {
        e = in.elements();
    }

    public boolean hasMoreElements() {
        return e.hasMoreElements();
    }

    public Estimate nextElement() {
        return (Estimate) e.nextElement();
    }

    static final long serialVersionUID = 1L;
}

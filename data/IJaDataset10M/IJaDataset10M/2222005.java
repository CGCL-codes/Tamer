package org.unicore.sets;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.unicore.Unicore;

/**
 * An interface to a type-safe Enumeration type for <tt>EstimateSet</tt>s. 
 *
 * @author S. van den Berghe (fecit)
 *
 * @since AJO 1
 *
 * @version $Id: EstimateSetEnumeration.java,v 1.2 2004/05/26 16:31:44 svenvdb Exp $
 * 
 **/
public class EstimateSetEnumeration extends Unicore {

    Enumeration e;

    public EstimateSetEnumeration(Hashtable in) {
        e = in.elements();
    }

    public EstimateSetEnumeration(Vector in) {
        e = in.elements();
    }

    public boolean hasMoreElements() {
        return e.hasMoreElements();
    }

    public EstimateSet nextElement() {
        return (EstimateSet) e.nextElement();
    }

    static final long serialVersionUID = 1L;
}

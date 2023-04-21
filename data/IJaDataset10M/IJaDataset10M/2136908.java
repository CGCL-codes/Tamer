package org.unicore.sets;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.unicore.Unicore;
import org.unicore.ajo.Option;

/**
 * An interface to a type-safe Enumeration type for <tt>Option</tt>s. 
 *
 * @author S. van den Berghe (fecit)
 *
 * @since AJO 1
 *
 * @version $Id: OptionEnumeration.java,v 1.2 2004/05/26 16:31:44 svenvdb Exp $
 * 
 **/
public class OptionEnumeration extends Unicore {

    Enumeration e;

    public OptionEnumeration(Hashtable in) {
        e = in.elements();
    }

    public OptionEnumeration(Vector in) {
        e = in.elements();
    }

    public boolean hasMoreElements() {
        return e.hasMoreElements();
    }

    public Option nextElement() {
        return (Option) e.nextElement();
    }

    static final long serialVersionUID = 1L;
}

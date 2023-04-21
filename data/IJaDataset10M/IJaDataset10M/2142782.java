package org.mandarax.xkb.framework;

import java.util.Map;
import org.jdom.Element;
import org.mandarax.kernel.LogicFactory;
import org.mandarax.kernel.Predicate;
import org.mandarax.xkb.XKBException;

/**
 * An adapter class for predicates from the mandarax lib.
 * @see org.mandarax.lib.AbstractPredicate
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.6 
 * @deprecated from v 3.4 - support for new features such as validation will not be added to XKB, please use ZKB instead
 */
public class XMLAdapter4MandaraxLibPredicates extends AbstractXMLAdapter {

    public static final String MANDARAX_LIB_PREDICATE = "mandarax_lib_predicate";

    public static final String NAME = "name";

    /**
 * Export an object, i.e., convert it to an element in the DOM.
 * @param obj an object
 * @param driver the generic driver
 * @param cache a cache used in order to associate the same
 * id with various occurences of the same object
 * @exception an XKBException is thrown if export fails
 */
    public Element exportObject(Object obj, GenericDriver driver, Map cache) throws XKBException {
        check(obj, org.mandarax.lib.AbstractPredicate.class);
        Predicate p = (Predicate) obj;
        Element e = new Element(MANDARAX_LIB_PREDICATE);
        e.setAttribute(NAME, p.getClass().getName());
        return e;
    }

    /**
 * Build an object from an XML element.
 * @param e an element
 * @param driver the generic driver
 * @param cache a cache used to identify objects that have the same id
 * @param lfactory the logic factory used to create objects
 * @exception an XKBException is thrown if export fails
 */
    public Object importObject(Element e, GenericDriver driver, Map cache, LogicFactory lfactory) throws XKBException {
        String className = e.getAttributeValue(NAME);
        Predicate p = null;
        if (org.mandarax.lib.Cut.class.getName().equals(className)) p = new org.mandarax.lib.Cut();
        p = lookupLib(p, className, org.mandarax.lib.date.DateArithmetic.ALL_PREDICATES);
        p = lookupLib(p, className, org.mandarax.lib.math.IntArithmetic.ALL_PREDICATES);
        p = lookupLib(p, className, org.mandarax.lib.math.DoubleArithmetic.ALL_PREDICATES);
        p = lookupLib(p, className, org.mandarax.lib.text.StringArithmetic.ALL_PREDICATES);
        return p;
    }

    /**
 * Lookup the lib for a predicae with a certain class name.
 * Return if the predicate is not null!
 * @return a predicate
 * @param p a predicate
 * @param className the name of a class
 * @param libPredicates an array of predicates
 */
    private Predicate lookupLib(Predicate p, String className, Predicate[] libPredicates) {
        if (p != null) return p;
        for (int i = 0; i < libPredicates.length; i++) {
            if (libPredicates[i].getClass().getName().equals(className)) return libPredicates[i];
        }
        return null;
    }

    /**
 * Get the name of the associated tag (element).
 * @return a string
 */
    public String getTagName() {
        return MANDARAX_LIB_PREDICATE;
    }

    /**
 * Get the kind of object the adapter can export/import.
 * @return a string
 */
    public String getKindOfObject() {
        return GenericDriver.MANDARAX_LIB_PREDICATE;
    }
}

package org.mandarax.zkb.framework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import org.mandarax.kernel.DerivationEventListener;
import org.mandarax.kernel.Fact;
import org.mandarax.kernel.LogicFactory;
import org.mandarax.kernel.Query;
import org.mandarax.zkb.ObjectPersistencyService;
import org.mandarax.zkb.ZKBException;

/**
 * An adapter class for queries.
 * @see org.mandarax.kernel.Query
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 2.3
 */
public class Adapter4Queries_1_1 extends AbstractAdapter {

    /**
 * Export an object, i.e., convert it to an element in the DOM.
 * @param obj an object
 * @param driver the generic driver
 * @param ops the object persistency service
 * @exception a XKBException is thrown if export fails
 */
    public Element exportObject(Object obj, GenericDriver driver, ObjectPersistencyService ops) throws ZKBException {
        check(obj, Query.class);
        Query query = (Query) obj;
        Element e = new Element(QUERY);
        String queryName = query.getName();
        if (queryName == null) queryName = "";
        e.setAttribute(NAME, queryName);
        Fact[] queryFacts = query.getFacts();
        for (int i = 0; i < queryFacts.length; i++) {
            Element el = exportObject(queryFacts[i], TagAndAttributeNames.FACT, driver, ops);
            e.addContent(el);
        }
        exportProperties(e, query);
        DerivationEventListener[] listeners = query.getDerivationEventListeners();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) {
                Element el = exportObject(listeners[i], TagAndAttributeNames.DERIVATION_LISTENER, driver, ops);
                e.addContent(el);
            }
        }
        return e;
    }

    /**
 * Build an object from an XML element.
 * @param e an element
 * @param driver the generic driver
 * @param ops the object persistency service
 * @param lfactory the logic factory used to create objects
 * @exception a XKBException is thrown if import fails
 */
    public Object importObject(Element e, GenericDriver driver, ObjectPersistencyService ops, LogicFactory lfactory) throws ZKBException {
        List list = new ArrayList();
        Adapter adapter4Facts = null;
        List children = e.getChildren(FACT);
        for (Iterator it = children.iterator(); it.hasNext(); ) {
            Element eFact = (Element) it.next();
            if (adapter4Facts == null) adapter4Facts = driver.getAdapter(eFact.getName());
            Fact queryFact = (Fact) adapter4Facts.importObject(eFact, driver, ops, lfactory);
            list.add(queryFact);
        }
        Fact[] facts = new Fact[list.size()];
        for (int i = 0; i < list.size(); i++) facts[i] = (Fact) list.get(i);
        String queryName = e.getAttributeValue(NAME);
        list.clear();
        children = e.getChildren(DERIVATION_LISTENER);
        Adapter adapter4Listeners = null;
        for (Iterator it = children.iterator(); it.hasNext(); ) {
            Element eListeners = (Element) it.next();
            if (adapter4Listeners == null) adapter4Listeners = driver.getAdapter(eListeners.getName());
            DerivationEventListener l = (DerivationEventListener) adapter4Listeners.importObject(eListeners, driver, ops, lfactory);
            if (l == null) LOG_ZKB.warn("Problem importing derivation event listeners, check sourcecode and classpath"); else list.add(l);
        }
        DerivationEventListener[] listeners = new DerivationEventListener[list.size()];
        for (int i = 0; i < list.size(); i++) listeners[i] = (DerivationEventListener) list.get(i);
        Query q = lfactory.createQuery(facts, queryName == null ? "" : queryName);
        if (queryName == null) q.setName(q.toString());
        q.setDerivationEventListeners(listeners);
        importProperties(e, q);
        return q;
    }

    /**
 * Get the name of the associated tag (element).
 * @return a string
 */
    public String getTagName() {
        return QUERY;
    }

    /**
 * Print the DTD associated with this adapter on a string buffer.
 * @param out the buffer to print on. 
 */
    public void printDTD(StringBuffer out) {
        out.append("<!ELEMENT query (atom*,properties?)>\n");
        out.append("<!ATTLIST query name CDATA #REQUIRED>\n");
    }
}

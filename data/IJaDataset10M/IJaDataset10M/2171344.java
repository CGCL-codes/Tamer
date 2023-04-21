package org.mandarax.zkb.framework;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.jdom.Comment;
import org.jdom.Element;
import org.mandarax.kernel.ClauseSet;
import org.mandarax.kernel.ExtendedKnowledgeBase;
import org.mandarax.kernel.Fact;
import org.mandarax.kernel.KnowledgeBase;
import org.mandarax.kernel.LogicFactory;
import org.mandarax.kernel.Query;
import org.mandarax.kernel.Rule;
import org.mandarax.sql.SQLClauseSet;
import org.mandarax.zkb.ObjectPersistencyService;
import org.mandarax.zkb.ZKBException;

/**
 * An adapter class for knowledge bases.
 * @see org.mandarax.kernel.KnowledgeBase
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 2.2
 */
public class Adapter4KnowledgeBases extends AbstractAdapter {

    /**
 * Export an object, i.e., convert it to an element in the DOM.
 * @param obj an object
 * @param driver the generic driver
 * @param ops the object persistency service
 * @exception a ZKBException is thrown if export fails
 */
    public Element exportObject(Object obj, GenericDriver driver, ObjectPersistencyService ops) throws ZKBException {
        check(obj, KnowledgeBase.class);
        int ftl = driver.getFaultToleranceLevel();
        PrintWriter out = driver.getLogWriter();
        KnowledgeBase kb = (KnowledgeBase) obj;
        Element e = new Element(RULE_BASE);
        e.setAttribute(CLASS, kb.getClass().getName());
        if (kb instanceof ExtendedKnowledgeBase) {
            Comparator comp = ((ExtendedKnowledgeBase) kb).getComparator();
            if (comp != null) {
                e.addContent(exportObject(comp, COMPARATOR, driver, ops));
            }
        }
        List clauseSets = kb.getClauseSets();
        for (Iterator it = clauseSets.iterator(); it.hasNext(); ) {
            ClauseSet cs = (ClauseSet) it.next();
            String kindOfObject = getClauseSetType(cs);
            if (kindOfObject == null) {
                String msg = "Cannot figure out what kind of clause set the following object is: " + cs;
                if (ftl == GenericDriver.LOW_FAULT_TOLERANCE) throw new ZKBException(msg); else {
                    out.println("Warning");
                    out.println(msg);
                    out.println("The clause is ignored");
                    LOG_XKB.warn(msg);
                    LOG_XKB.warn("The clause is ignored");
                }
            } else {
                try {
                    Element el = exportObject(cs, kindOfObject, driver, ops);
                    e.addContent(el);
                } catch (ZKBException x) {
                    String msg = "Problems exporting the knowledge base, export failed for " + cs;
                    if (ftl == GenericDriver.LOW_FAULT_TOLERANCE) throw new ZKBException(msg); else {
                        out.println(msg);
                        out.println("See log for details");
                        e.addContent(new Comment(msg));
                        LOG_XKB.error(msg, x);
                    }
                }
            }
        }
        for (Iterator it = kb.queryNames(); it.hasNext(); ) {
            Query q = kb.getQuery((String) it.next());
            try {
                Element el = exportObject(q, GenericDriver.QUERY, driver, ops);
                e.addContent(el);
            } catch (ZKBException x) {
                String msg = "Problems exporting the knowledge base, export failed for query " + q;
                if (ftl == GenericDriver.LOW_FAULT_TOLERANCE) throw new ZKBException(msg); else {
                    out.println(msg);
                    out.println("See log for details");
                    e.addContent(new Comment(msg));
                    LOG_XKB.error(msg, x);
                }
            }
        }
        return e;
    }

    /**
 * Get the type of clause set, e.g. GenericDriver.FACT or GenericDriver.RULE.
 * @param cs a clause set
 * @return a string
 */
    protected String getClauseSetType(ClauseSet cs) {
        if (cs instanceof Rule) return RULE; else if (cs instanceof Fact) return FACT; else if (cs instanceof SQLClauseSet) return SQL_CLAUSE_SET; else return CUSTOM_CLAUSE_SET;
    }

    /**
 * Build an object from an XML element.
 * @param e an element
 * @param driver the generic driver
 * @param ops the object persistency service
 * @param lfactory the logic factory used to create objects
 * @exception a ZKBException is thrown if export fails
 */
    public Object importObject(Element e, GenericDriver driver, ObjectPersistencyService ops, LogicFactory lfactory) throws ZKBException {
        KnowledgeBase kb = null;
        String implClassName = e.getAttributeValue(CLASS);
        if (implClassName != null) {
            try {
                Class implClass = Class.forName(implClassName);
                kb = (KnowledgeBase) implClass.newInstance();
            } catch (ClassNotFoundException x) {
                LOG_ZKB.error("Cannot find class " + implClassName + ", will use default", x);
            } catch (IllegalAccessException x) {
                LOG_ZKB.error("Cannot instanciate class " + implClassName + ", will use default", x);
            } catch (InstantiationException x) {
                LOG_ZKB.error("Cannot instanciate class " + implClassName + ", will use default", x);
            }
        }
        if (kb == null) kb = new org.mandarax.reference.AdvancedKnowledgeBase();
        int ftl = driver.getFaultToleranceLevel();
        PrintWriter out = driver.getLogWriter();
        List children = e.getChildren();
        for (Iterator it = children.iterator(); it.hasNext(); ) {
            Element child = (Element) it.next();
            Adapter adapter = driver.getAdapter(child.getName());
            if (child.getName().equals(COMPARATOR)) {
                Comparator comp = (Comparator) adapter.importObject(child, driver, ops, lfactory);
                if (kb instanceof ExtendedKnowledgeBase) ((ExtendedKnowledgeBase) kb).setComparator(comp);
            } else {
                try {
                    Object obj = adapter.importObject(child, driver, ops, lfactory);
                    if (obj instanceof ClauseSet) kb.add((ClauseSet) obj); else if (obj instanceof Query) kb.addQuery((Query) obj); else throw new ZKBException("Can only import clause sets and queries into repository, import for object failed: " + obj);
                } catch (ZKBException x) {
                    String msg = "Problems importing the knowledge base, import failed for element " + e.getText();
                    if (ftl == GenericDriver.LOW_FAULT_TOLERANCE) throw new ZKBException(msg); else {
                        out.println(msg);
                        out.println("See log for details");
                        LOG_XKB.error(msg, x);
                    }
                }
            }
        }
        return kb;
    }

    /**
 * Get the name of the associated tag (element).
 * @return a string
 */
    public String getTagName() {
        return RULE_BASE;
    }

    /**
 * Get the kind of object the adapter can export/import.
 * @return a string
 */
    public String getKindOfObject() {
        return RULE_BASE;
    }

    /**
 * Print the DTD associated with this adapter on a string buffer.
 * @param out the buffer to print on. 
 */
    public void printDTD(StringBuffer out) {
        out.append("<!ELEMENT rulebase (comparator?,(atom|rule|sql_clause_set|custom_clause_set|query)*)>\n");
        out.append("<!ATTLIST rulebase class CDATA #IMPLIED>\n");
    }
}

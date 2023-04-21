package org.mandarax.xkb.ruleml;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.mandarax.kernel.ConstantTerm;
import org.mandarax.kernel.Fact;
import org.mandarax.kernel.KnowledgeBase;
import org.mandarax.kernel.Predicate;
import org.mandarax.kernel.Rule;
import org.mandarax.kernel.SimplePredicate;
import org.mandarax.kernel.Term;
import org.mandarax.kernel.VariableTerm;
import org.mandarax.xkb.AbstractXKBDriver;
import org.mandarax.xkb.XKBException;

/**
 * Abstract superclass for ruleml drivers.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.9
 */
abstract class AbstractRuleMLDriver extends AbstractXKBDriver {

    public static final String IND = "ind";

    public static final String VAR = "var";

    public static final String _OPR = "_opr";

    public static final String REL = "rel";

    public static final String IMP = "imp";

    public static final String FACT = "fact";

    public static final String _HEAD = "_head";

    public static final String _BODY = "_body";

    public static final String ATOM = "atom";

    public static final String AND = "and";

    public static final String RULEBASE = "rulebase";

    /**
     * Constructor.
     */
    public AbstractRuleMLDriver() {
        super();
    }

    /**
     * Export a constant term, e.g. convert it into an xml element.
     * @return an element (of the jdom tree)
     * @param ct a constant term
     */
    protected Element export(ConstantTerm ct) {
        return exportJavaObject(ct.getObject());
    }

    /**
     * Export aan object
     * @return an element (of the jdom tree)
     * @param obj an arbirtary object
     */
    protected Element exportJavaObject(Object obj) {
        Element e = new Element(IND);
        e.addContent(obj == null ? "null" : obj.toString());
        return e;
    }

    /**
     * Export a fact, e.g. convert it into an xml element.
     * @return an element (of the jdom tree)
     * @param f a fact
     */
    protected Element export(Fact f) {
        Element e = new Element(FACT);
        e.addContent(exportFact2Head(f));
        return e;
    }

    /**
     * Export a knowledge base, e.g. convert it into an xml element.
     * @return an element (of the jdom tree)
     * @param kb a knowledge base
     */
    protected abstract Element export(KnowledgeBase kb);

    /**
     * Export a predicate, e.g. convert it into an xml element.
     * @return an element (of the jdom tree)
     * @param p a predicate
     */
    protected Element export(Predicate p) {
        Element e1 = new Element(_OPR);
        Element e2 = new Element(REL);
        e2.addContent(p.getName());
        e1.addContent(e2);
        return e1;
    }

    /**
     * Export a rule, e.g. convert it into an xml element.
     * @return an element (of the jdom tree)
     * @param r a rule
     */
    protected Element export(Rule r) {
        Element e1 = new Element(IMP);
        e1.addContent(exportFact2Head(r.getHead()));
        Element e2 = new Element(_BODY);
        Element e3 = new Element(AND);
        Fact next = null;
        List body = r.getBody();
        for (Iterator it = body.iterator(); it.hasNext(); ) {
            next = (Fact) it.next();
            e3.addContent(exportFact2Atom(next));
        }
        e2.addContent(e3);
        e1.addContent(e2);
        return e1;
    }

    /**
     * Export a variable term, e.g. convert it into an xml element.
     * @return an element (of the jdom tree)
     * @param v a variable term
     */
    protected Element export(VariableTerm v) {
        Element e = new Element(VAR);
        e.addContent(v.getName());
        return e;
    }

    /**
     * Export a fact, e.g. convert it into an xml element.
     * @return an element (of the jdom tree)
     * @param f a fact
     */
    protected Element exportFact2Atom(Fact f) {
        Element e = new Element(ATOM);
        e.addContent(export(f.getPredicate()));
        Term[] terms = f.getTerms();
        for (int i = 0; i < terms.length; i++) {
            if (terms[i] instanceof ConstantTerm) {
                e.addContent(export((ConstantTerm) terms[i]));
            } else {
                if (terms[i] instanceof VariableTerm) {
                    e.addContent(export((VariableTerm) terms[i]));
                } else {
                    LOG_XKB.warn("Only variable and constant terms are supported by driver " + getName() + ", cannot export " + terms[i]);
                }
            }
        }
        return e;
    }

    /**
     * Export a fact, e.g. convert it into an xml element.
     * @return an element (of the jdom tree)
     * @param f a fact
     */
    protected Element exportFact2Head(Fact f) {
        Element e = new Element(_HEAD);
        e.addContent(exportFact2Atom(f));
        return e;
    }

    /**
     * Export a knowledge base, i.e., convert it into an xml document.
     * @return an xml document
     * @param kb a knowledge base
     * @throws an XKBException is thrown if export fails
     */
    public Document exportKnowledgeBase(KnowledgeBase kb) throws XKBException {
        Document doc = new Document(export(kb));
        Comment comm = new Comment("Export driver used: " + this.getClass().getName());
        doc.addContent(comm);
        return doc;
    }

    /**
     * Get a short text describing the driver.
     * @return a text
     */
    public String getDescription() {
        return "Driver for rulebases as specified by the RULE ML version 0.8 specification";
    }

    /**
     * Import a fact list connected by "AND".
     * @return a list
     * @param e an xml element
     * @throws an XKBException is thrown if import fails
     */
    private List importAnd(Element e) throws XKBException {
        List children = e.getChildren(ATOM);
        List premisses = new Vector(children.size());
        for (Iterator it = children.iterator(); it.hasNext(); ) {
            premisses.add(importAtom((Element) it.next()));
        }
        return premisses;
    }

    /**
     * Import an atom.
     * @return a fact
     * @param e an xml element
     * @throws an XKBException is thrown if import fails
     */
    protected Fact importAtom(Element e) throws XKBException {
        Element ePredicate = e.getChild(_OPR);
        if (ePredicate == null) throw new XKBException("No child <_OPR> found in atom " + e + ", cannot build predicate");
        Predicate p = importPredicate(ePredicate);
        List eTerms = collectChildren(e, IND, VAR);
        Term[] terms = new Term[eTerms.size()];
        for (int i = 0; i < terms.length; i++) {
            terms[i] = importTerm((Element) eTerms.get(i));
        }
        return lfactory.createFact(p, terms);
    }

    /**
     * Import a body.
     * @return a list
     * @param e an xml element
     * @throws an XKBException is thrown if import fails
     */
    protected List importBody(Element e) throws XKBException {
        Element eAnd = e.getChild(AND);
        if (eAnd == null) {
            Element eAtom = e.getChild(ATOM);
            if (eAtom == null) {
                LOG_XKB.warn("Body contains neither and nor atom");
                return new Vector();
            } else {
                List body = new Vector(5);
                body.add(importAtom(eAtom));
                return body;
            }
        } else {
            return importAnd(eAnd);
        }
    }

    /**
     * Import a constant term.
     * @return a constant term
     * @param e an xml element
     * @throws an XKBException is thrown if import fails
     */
    protected ConstantTerm importConstantTerm(Element e) throws XKBException {
        return lfactory.createConstantTerm(importJavaObject(e));
    }

    /**
     * Import a java object.
     * @return a constant term
     * @param e an xml element
     * @throws an XKBException is thrown if import fails
     */
    protected Object importJavaObject(Element e) throws XKBException {
        return e.getText();
    }

    /**
     * Import a fact.
     * @return a fact
     * @param e an xml element
     * @throws an XKBException is thrown if import fails
     */
    protected Fact importFact(Element e) throws XKBException {
        Element eHead = e.getChild(_HEAD);
        if (eHead == null) {
            throw new XKBException("Fact element contains no head");
        }
        return importHead(eHead);
    }

    /**
     * Import a head.
     * @return a fact
     * @param e an xml element
     * @throws an XKBException is thrown if import fails
     */
    protected Fact importHead(Element e) throws XKBException {
        Element eAtom = e.getChild(ATOM);
        if (eAtom == null) {
            throw new XKBException("Head element contains no atom");
        }
        return importAtom(eAtom);
    }

    /**
     * Import a knowledge base, i.e., build it from an xml document.
     * @return a knowledge base
     * @param doc an xml document
     * @throws an XKBException is thrown if import fails
     */
    public KnowledgeBase importKnowledgeBase(Document doc) throws XKBException {
        Element root = doc.getRootElement();
        if (compare(root.getName(), RULEBASE)) {
            return importKnowledgeBase(root);
        } else {
            throw new XKBException("No rulebase root element found in xml source");
        }
    }

    /**
     * Import a knowledge base.
     * @return a knowledge base
     * @param e an xml element
     * @throws an XKBException is thrown if import fails
     */
    protected abstract KnowledgeBase importKnowledgeBase(Element e) throws XKBException;

    /**
     * Import a predicate.
     * @return a predicate
     * @param e an xml element
     * @throws an XKBException is thrown if import fails
     */
    protected Predicate importPredicate(Element e) throws XKBException {
        Element parent = (Element) e.getParent();
        if (parent == null) {
            LOG_XKB.warn("Parent node of element representing a predicate is null, assume that there are no terms");
        }
        int termNumber = (parent == null) ? 0 : countChildren(parent, IND, VAR);
        Element erel = e.getChild(REL);
        if (erel == null) {
            throw new XKBException("No <rel> tag found inside the <_op> tag, cannot built predicate");
        }
        Class[] types = new Class[termNumber];
        for (int i = 0; i < termNumber; i++) {
            types[i] = String.class;
        }
        return new SimplePredicate(erel.getText(), types);
    }

    /**
     * Import a rule.
     * @return a rule
     * @param e an xml element
     * @throws an XKBException is thrown if import fails
     */
    protected Rule importRule(Element e) throws XKBException {
        Element eHead = e.getChild(_HEAD);
        Element eBody = e.getChild(_BODY);
        Fact head = null;
        List body = null;
        if (eHead == null) {
            throw new XKBException("Head of the rule cannot be empty");
        }
        head = importHead(eHead);
        if (eBody == null) {
            LOG_XKB.debug("No body tag found, assume body is empty");
            body = new Vector();
        } else {
            body = importBody(eBody);
        }
        return lfactory.createRule(body, head);
    }

    /**
     * Import a term.
     * @return a term
     * @param e an xml element
     * @throws an XKBException is thrown if import fails
     */
    protected Term importTerm(Element e) throws XKBException {
        if (compare(e.getName(), IND)) {
            return importConstantTerm(e);
        }
        if (compare(e.getName(), VAR)) {
            return importVariableTerm(e);
        }
        throw new XKBException("Only constants and variable terms can e imported by driver " + getName());
    }

    /**
     * Import a variable term.
     * @return a variable term
     * @param e an xml element
     * @throws an XKBException is thrown if import fails
     */
    private VariableTerm importVariableTerm(Element e) throws XKBException {
        String value = e.getText();
        return lfactory.createVariableTerm(value, String.class);
    }
}

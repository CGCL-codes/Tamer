package net.noderunner.exml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is an Element Requirement object, which is used as a node in a parse
 * tree.  It maintains no internal state, so may be used across threads.
 */
public class ElementReq {

    private static class ElementNS {

        public String name;

        public String namespaceURI;

        @Override
        public String toString() {
            return name;
        }
    }

    static final int SOLE = 1;

    static final int SEQUENCE = 2;

    static final int CHOICE = 4;

    static final int STAR = 8;

    static final int PLUS = 16;

    static final int QUESTION = 32;

    static final int PCDATA = 128;

    static final int ANY = 256;

    /**
	 * OR'ed together flags, such as <code>PCDATA | CHOICE</code>.
	 */
    private int kind;

    /**
	 * Contains either a List (isSequence() || isChoice() == true) or
	 * ElementNS (isSole() == true).   If a List, it contains ElementReq
	 * instances.
	 */
    private Object content;

    private ElementReq parent;

    /**
	 * Constructs an empty requirement.  Sub-requirements must be added with
	 * the <code>add</code> method.
	 */
    public ElementReq() {
        kind = 0;
        content = null;
        parent = null;
    }

    /**
	 * Constructs a requirement for a specific element.
	 */
    public ElementReq(String name) {
        this(name, null);
    }

    /**
	 * Constructs a requirement for a specific element.
	 * @param name element name to add
	 * @param namespaceURI namespace element belongs to or <code>null</code>
	 */
    public ElementReq(String name, String namespaceURI) {
        ElementNS ns = new ElementNS();
        ns.name = name;
        ns.namespaceURI = namespaceURI;
        kind = SOLE;
        content = ns;
    }

    /**
	 * Constructs a requirement for a specific element.
	 * For performance reasons, element instances in the Dtd and in the
	 * document must belong to the same string pool instance.
	 * @param e Element
	 */
    public ElementReq(Element e) {
        this(e.getName(), e.getNamespaceURI());
    }

    /**
	 * Adds an ElementReq instance to this tree.
	 * An example use of this would be adding <code>(c | d)</code> to
	 * <code>(a, b)</code> to make <code>(a, b, (c | d))</code>.
	 */
    public void add(ElementReq req) {
        req.parent = this;
        if (content == null) content = new ArrayList<ElementReq>();
        ((List<ElementReq>) content).add(req);
    }

    /**
	 * Returns the i'th child of this ElementReq.
	 */
    public ElementReq getChild(int i) {
        List l = (List) content;
        return (ElementReq) l.get(i);
    }

    /**
	 * Returns the number of children of this ElementReq.
	 */
    public int size() {
        if (content == null) return 0;
        List l = (List) content;
        return l.size();
    }

    /**
	 * Returns the rule that allows that element in a choice.
	 * Thus, looking for 'c' in <code>(a | (b?, c) | d)</code> will return
	 * the rule for <code>c</code>.  If it is not in this sub-tree,
	 * <code>null</code> is returned.
	 */
    public ElementReq followChoice(Element e) {
        int size = size();
        for (int i = 0; i < size; i++) {
            ElementReq subreq = getChild(i);
            if (subreq.isSole()) {
                if (subreq.isElement(e)) {
                    return subreq;
                }
            } else {
                ElementReq subsubreq = subreq.followChoice(e);
                if (subsubreq != null) return subsubreq;
            }
            if (isSequence() && !subreq.isQuestion() && !subreq.isStar()) break;
        }
        return null;
    }

    public void setStar() {
        kind |= STAR;
    }

    public void setQuestion() {
        kind |= QUESTION;
    }

    public void setPlus() {
        kind |= PLUS;
    }

    public void setPCDATA() {
        kind |= PCDATA;
    }

    public void setPCDATA(boolean yes) {
        if (!yes) kind &= ~PCDATA; else setPCDATA();
    }

    public void setANY() {
        kind |= ANY;
    }

    public void setSequence() {
        kind |= SEQUENCE;
    }

    public void setChoice() {
        kind |= CHOICE;
    }

    /**
	 * Sets the type by a character; one of:  <code>? * +</code>.
	 */
    public void setRepeating(int c) {
        if (c == '?') setQuestion();
        if (c == '*') setStar();
        if (c == '+') setPlus();
    }

    public boolean isSequence() {
        return (kind & SEQUENCE) != 0;
    }

    public boolean isChoice() {
        return (kind & CHOICE) != 0;
    }

    public boolean isSole() {
        return (kind & SOLE) != 0;
    }

    public boolean isEmpty() {
        return (kind == 0);
    }

    public boolean isStar() {
        return (kind & STAR) != 0;
    }

    public boolean isPlus() {
        return (kind & PLUS) != 0;
    }

    public boolean isQuestion() {
        return (kind & QUESTION) != 0;
    }

    public boolean isANY() {
        return (kind & ANY) != 0;
    }

    public boolean isPCDATA() {
        return (kind & PCDATA) != 0;
    }

    /**
	 * This must be called before element rules can be tested.
	 * This sets the isQuestion() state
	 * <pre>
	 * (b | c? | d) becomes (b | c | d)?
	 * (b?, c?, d?) becomes (b, c, d)?
	 * (b | c* | d) becomes (b | c* | d)?
	 * (b*, c*, d*) becomes (b*, c*, d*)?
	 * (b*)         becomes (b*)*
	 * </pre>
	 */
    public void normalize() {
        if (!isSequence() && !isChoice()) return;
        boolean flip = isSequence();
        for (int i = 0; i < size(); i++) {
            ElementReq req = getChild(i);
            req.normalize();
            if (isSequence() && !req.isQuestion() && !req.isStar()) flip = false;
            if (isChoice() && (req.isQuestion() || req.isStar())) flip = true;
        }
        if (size() == 1) {
            if (getChild(0).isStar()) setStar();
            if (getChild(0).isPlus()) setPlus();
        }
        if (flip) {
            setQuestion();
        }
    }

    /**
	 * Returns the parent rule for this element requirement.
	 */
    public ElementReq getParent() {
        return parent;
    }

    /**
	 * Returns the location of the occurance of this element in the parent
	 * element requirement.  For example in this tree:
	 * <code>(a, (b, c), d)</code>
	 * for the object <code>(b, c)</code>, this method
	 * will return <code>1</code>.
	 */
    public int getParentIndex() {
        List l = (List) getParent().content;
        return l.indexOf(this);
    }

    /**
	 * Returns a string representation of this rule.
	 */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(32);
        if (isANY()) sb.append("ANY");
        if (isPCDATA()) {
            sb.append("(#PCDATA");
            if (isChoice()) sb.append(" | "); else sb.append(')');
        }
        if (isSequence() || isChoice()) {
            if (!isPCDATA()) sb.append('(');
            Iterator i = ((List) content).iterator();
            while (i.hasNext()) {
                sb.append(i.next());
                if (i.hasNext()) sb.append(isSequence() ? ", " : " | ");
            }
            sb.append(')');
        }
        if (isSole()) {
            sb.append(content.toString());
        }
        if (isEmpty()) sb.append(XmlTags.CONTENTSPEC_EMPTY);
        if (isStar()) sb.append('*');
        if (isPlus()) sb.append('+');
        if (isQuestion()) sb.append('?');
        return sb.toString();
    }

    private boolean eq(String s1, String s2) {
        if (s1 == s2) return true;
        if (s1 == null) return false;
        return s1.equals(s2);
    }

    public boolean isElement(Element e) {
        ElementNS ns = (ElementNS) content;
        return eq(ns.name, e.getName()) && eq(ns.namespaceURI, e.getNamespaceURI());
    }
}

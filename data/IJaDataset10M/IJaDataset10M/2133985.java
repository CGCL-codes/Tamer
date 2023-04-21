package edu.isi.div2.metadesk.datamodel;

import java.util.*;
import org.apache.log4j.Logger;
import edu.isi.div2.sew.SEWConstants;
import edu.isi.div2.triple.tripleapi.*;
import edu.isi.div2.metadesk.util.MetaDeskConstants;
import edu.isi.div2.metadesk.util.Util;
import edu.isi.div2.metadesk.action.ColorizeContexts;
import edu.isi.div2.metadesk.gui.HierarchyTreeNode;
import edu.isi.div2.metadesk.quad.*;

/**
 * This class holds the actual data underlying tree nodes and tree table in the
 * GUI.
 * 
 * @author Sameer Maggon (maggon@isi.edu) $Id: Term.java,v 1.6 2004/10/05
 *         20:27:28 macgreg Exp $
 */
public class Term implements MetaDeskConstants, SEWConstants {

    static Logger logger = Logger.getLogger(Term.class.getName());

    private String value = null;

    private boolean isLiteral = false;

    /** list of listeners listening to change of the semantic data */
    protected Vector listeners = new Vector();

    protected static boolean DEBUG = false;

    public String getURI() {
        if (this.isLiteral) {
            logger.error("Bad call to 'getURI'");
            new Exception().printStackTrace();
            return null;
        }
        return value;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isLiteral() {
        return this.isLiteral;
    }

    public boolean equals(Object other) {
        Term otherTerm = (Term) other;
        return (this.value.equals(otherTerm.value)) && (this.isLiteral == otherTerm.isLiteral);
    }

    /** Converts this Term object to a String representation */
    public String toString() {
        String label = InstanceWorld.getLabelForURI(this.getURI());
        if (label != null) return label;
        return this.getURI();
    }

    public Term(String value, boolean isLiteral) {
        if ((value == null) || (value.equals("") && !isLiteral)) {
            if (value == null) logger.error("Null value passed to 'Term' constructor."); else logger.error("Empty string passed to 'Term' constructor.");
            new Exception().printStackTrace();
        }
        this.value = value;
        this.isLiteral = isLiteral;
    }

    /** Constructor that handles null inputs. */
    public static Term createTerm(String value, boolean isLiteral) {
        if (value == null) return null;
        if (value.equals("") && !isLiteral) return null;
        return new Term(value, isLiteral);
    }

    /** * Listener-related functions** */
    public void listenToHierarchyTreeNode(HierarchyTreeNode node) {
        if (!this.listeners.contains(node)) this.listeners.add(node);
    }

    public void fireDataChanged() {
        Vector listenerz = new Vector(this.listeners);
        for (int i = 0; i < listenerz.size(); i++) {
            HierarchyTreeNode node = (HierarchyTreeNode) listenerz.elementAt(i);
            node.synchronizeTreeNodeWithUnderlyingData();
        }
    }

    public static boolean INCLUDE_ATTRIBUTES_IN_HIERARCHY = true;

    public static String[] biDirectionalParentChildURIs = { SewURI.PARENT_CHILD, RdfsURI.SUB_CLASS_OF, RdfsURI.SUB_PROPERTY_OF, SewURI.HAS_MEMBER, DctermsURI.HAS_PART };

    public static String[] excludedParentChildURIs = { RdfURI.TYPE };

    private static List parentToChildURIs = new ArrayList();

    static {
        parentToChildURIs.add(SewURI.PARENT_CHILD);
        parentToChildURIs.add(SewURI.HAS_MEMBER);
        parentToChildURIs.add(DctermsURI.HAS_PART);
    }

    private static List childToParentURIs = new ArrayList();

    static {
        childToParentURIs.add(RdfsURI.SUB_CLASS_OF);
        childToParentURIs.add(RdfsURI.SUB_PROPERTY_OF);
        childToParentURIs.add(DctermsURI.IS_PART_OF);
    }

    private static List listOfNull = new ArrayList();

    static {
        listOfNull.add(null);
    }

    private Iterator getParentToChildPredicateURIs(boolean includeAttributeChildren) {
        if (includeAttributeChildren) return listOfNull.iterator(); else return parentToChildURIs.iterator();
    }

    private Iterator getChildToParentPredicateURIs() {
        return childToParentURIs.iterator();
    }

    /**
     * Return 'true' if 'uri' is the parentChild URI, or a subproperty of one.
     * Also returns 'true' if 'uri' is 'rdfs:subClassOf'.
     */
    public static boolean isParentChildURI(String uri) {
        for (int i = 0; i < biDirectionalParentChildURIs.length; i++) {
            if (uri.equals(biDirectionalParentChildURIs[i])) return true;
        }
        return false;
    }

    public static boolean isParentChildQuad(Quad quad) {
        String predicateURI = quad.getPredicate();
        if (isParentChildURI(predicateURI)) return true;
        return false;
    }

    /**
     * Return 'true' uri is a predicate we don't want to follow from
     * subject to object when looking for children of parents.
     */
    public static boolean isExcludedParentToChildPredicate(String uri) {
        for (int i = 0; i < excludedParentChildURIs.length; i++) {
            if (uri.equals(excludedParentChildURIs[i])) {
                return true;
            }
        }
        for (int i = 0; i < childToParentURIs.size(); i++) {
            if (uri.equals(childToParentURIs.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return 'true' if 'uri' is the parentChild URI, or a subproperty of one.
     * Also returns 'true' if 'uri' is 'rdfs:subClassOf'.
     */
    public static boolean isChildParentURI(String uri) {
        for (int i = 0; i < childToParentURIs.size(); i++) {
            if (uri.equals(childToParentURIs.get(i))) return true;
        }
        return false;
    }

    /** Return a collection of quads that link 'this' to its children. */
    public Collection getChildQuads(InstanceWorld iw, String context) {
        return getChildQuads(iw, context, INCLUDE_ATTRIBUTES_IN_HIERARCHY);
    }

    /** Return a collection of quads that link 'this' to its children. */
    public List getChildQuads(InstanceWorld iw, String context, boolean includeAttributeQuads) {
        List quads = new ArrayList();
        String uri = this.getURI();
        if (iw.ownsExternalUnloadedFacts(uri)) {
            iw.loadExternalUnloadedFacts(uri);
        }
        for (Iterator it = getParentToChildPredicateURIs(includeAttributeQuads); it.hasNext(); ) {
            String pcPredicate = (String) it.next();
            Iterator it2 = iw.getMatchingQuads(context, uri, pcPredicate, null, false);
            while (it2.hasNext()) {
                Quad quad = (Quad) it2.next();
                if (quad.objectIsLiteral()) continue;
                if (InstanceWorld.isBlankResource(quad.getObject())) continue;
                if (isExcludedParentToChildPredicate(quad.getPredicate())) {
                    continue;
                }
                quads.add(quad);
            }
        }
        for (Iterator it = getChildToParentPredicateURIs(); it.hasNext(); ) {
            Iterator it2 = iw.getMatchingQuads(null, null, (String) it.next(), uri, false);
            while (it2.hasNext()) {
                quads.add(it2.next());
            }
        }
        return quads;
    }

    /** Return a collection of quads that link 'this' to its parents. */
    public Collection getParentQuads(InstanceWorld iw, String context) {
        return getParentQuads(iw, context, INCLUDE_ATTRIBUTES_IN_HIERARCHY);
    }

    /** Return a collection of quads that link 'this' to its parents. */
    public Collection getParentQuads(InstanceWorld iw, String context, boolean includeAttributeQuads) {
        Collection quads = new ArrayList();
        String childURI = this.getURI();
        for (Iterator it = this.getParentToChildPredicateURIs(includeAttributeQuads); it.hasNext(); ) {
            Iterator it2 = iw.getMatchingQuads(context, null, (String) it.next(), childURI, false);
            while (it2.hasNext()) {
                Quad quad = (Quad) it2.next();
                if (isExcludedParentToChildPredicate(quad.getPredicate())) continue;
                quads.add(quad);
            }
        }
        for (Iterator it = getChildToParentPredicateURIs(); it.hasNext(); ) {
            Iterator it2 = iw.getMatchingQuads(null, childURI, (String) it.next(), null, false);
            while (it2.hasNext()) {
                Quad quad = (Quad) it2.next();
                if (quad.objectIsLiteral()) continue;
                if (InstanceWorld.isBlankResource(quad.getObject())) continue;
                quads.add(quad);
            }
        }
        return quads;
    }

    /**
     * Remove duplicates from 'children'; 'this' is the common parent.
     * Tricky: Does this by removing redundant PC quads, favoring a non PC
     * parent child link.
     * For now, the ParentChild link is the only case we handle.
     * TODO: FIGURE OUT HOW TO REMOVE DUPLICATES SAFELY FOR NON-PC CASE:
     * TODO: FIGURE OUT HOW TO REMOVE DUPLICATES SAFELY FOR NON-REDUNDANT CASE:
     */
    private List removeDuplicateChildren(List children, InstanceWorld iw) {
        List allDifferent = new ArrayList();
        List duplicates = new ArrayList();
        for (Iterator it = children.iterator(); it.hasNext(); ) {
            Term child = (Term) it.next();
            if (allDifferent.contains(child)) duplicates.add(child); else allDifferent.add(child);
        }
        if (duplicates.isEmpty()) return children;
        logger.info("REMOVE DUPLICATE CHILDREN FOUND DUPLICATES " + duplicates);
        for (Iterator it = this.getChildQuads(iw, null).iterator(); it.hasNext(); ) {
            Quad quad = (Quad) it.next();
            if (duplicates.contains(quad.getSubject()) || duplicates.contains(quad.getObject())) logger.info("  QUAD " + quad);
        }
        boolean removedAQuad = false;
        for (Iterator it = duplicates.iterator(); it.hasNext(); ) {
            Term duplicate = (Term) it.next();
            for (Iterator it2 = this.getChildQuads(iw, null).iterator(); it2.hasNext(); ) {
                Quad quad = (Quad) it2.next();
                if (quad.getPredicate().equals(SewURI.PARENT_CHILD) && quad.getObject().equals(duplicate.getURI())) {
                    iw.removeQuad(quad);
                    removedAQuad = true;
                    break;
                }
            }
        }
        if (removedAQuad) return getChildren(iw, false); else return children;
    }

    public List getChildren(InstanceWorld iw, boolean sort) {
        List children = new ArrayList();
        String uri = this.getURI();
        Iterator it = this.getChildQuads(iw, null).iterator();
        while (it.hasNext()) {
            Quad quad = (Quad) it.next();
            String childURI;
            if (quad.getSubject().equals(uri)) childURI = quad.getObject(); else childURI = quad.getSubject();
            if (childURI.equals("")) {
                logger.info("PC QUAD CONTAINS EMPTY STRING!!! " + quad);
            }
            children.add(new Term(childURI, false));
        }
        children = removeDuplicateChildren(children, iw);
        if (sort) {
            Object[] orderedChildren = children.toArray();
            Arrays.sort(orderedChildren, new TermComparator());
            children = Arrays.asList(orderedChildren);
        }
        return children;
    }

    public String extractParentFromQuad(Quad quad) {
        String childURI = this.getURI();
        if (quad.getObject().equals(childURI)) return quad.getSubject();
        if (isChildParentURI(quad.getPredicate())) return quad.getObject();
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        logger.info("REJECTING CHILD TO PARENT QUAD " + iw.getFriendlyLabel(this, false) + "  " + quad);
        return null;
    }

    public List getParents(InstanceWorld iw, String context, boolean includeAttributeParents) {
        List parents = new ArrayList();
        String childURI = this.getURI();
        Iterator it = this.getParentQuads(iw, context, includeAttributeParents).iterator();
        while (it.hasNext()) {
            Quad quad = (Quad) it.next();
            String parentURI = this.extractParentFromQuad(quad);
            parents.add(new Term(parentURI, false));
        }
        return parents;
    }

    /**
     * Term 'child' has been added/dragged into context 'context'.
     * Check to see if it references facts in contexts other than
     * 'context' that are external to 'iw'.  If so, mark 'child'
     * with 'MdkURI.OWNS_FACTS_IN_CONTEXT' quads.
     */
    private void markExternalContextReferences(Term child, InstanceWorld iw, String context) {
        List unmarkedExternalContexts = iw.getExternalContexts();
        if (unmarkedExternalContexts.isEmpty()) return;
        unmarkedExternalContexts.remove(context);
        if (unmarkedExternalContexts.isEmpty()) return;
        Iterator it = iw.getMatchingQuads(null, child.getURI(), null, null, false);
        while (it.hasNext()) {
            String quadContext = ((Quad) it.next()).getContext();
            if (quadContext.equals(context)) continue;
            if (unmarkedExternalContexts.contains(quadContext)) {
                iw.addQuad(iw.getBaseContextURI(), child.getURI(), MdkURI.OWNS_FACTS_IN_CONTEXT, quadContext, false);
                unmarkedExternalContexts.remove(quadContext);
                if (unmarkedExternalContexts.isEmpty()) return;
            }
        }
    }

    /** Create a child below 'this' in the context 'context'. */
    public void addChild(Term child, InstanceWorld iw, String context) {
        logger.info("ADD CHILD " + child + "  PARENT " + this + "  CONTEXT " + context);
        Quad quad = iw.createQuad(context, this.getURI(), SewURI.PARENT_CHILD, child.getURI(), false);
        if (quad == null) return;
        iw.addQuad(quad);
        markExternalContextReferences(child, iw, context);
        this.fireDataChanged();
    }

    /** Remove the datamodel quad that connects 'this' to 'child'. */
    public void removeChild(Term child, InstanceWorld iw, String context) {
        logger.info("REMOVE CHILD " + child + "  PARENT " + this);
        for (Iterator it = this.getChildQuads(iw, context).iterator(); it.hasNext(); ) {
            Quad quad = (Quad) it.next();
            String objectURI = quad.getObject();
            String subjectURI = quad.getSubject();
            if (child.getURI().equals(objectURI) || child.getURI().equals(subjectURI)) {
                iw.removeQuad(quad);
                break;
            }
        }
    }

    public void removeChild(Term child, InstanceWorld iw) {
        removeChild(child, iw, null);
    }

    /** Remove the datamodel quad. */
    public void removeChild(Quad quad, InstanceWorld iw) {
        logger.info("REMOVE CHILD " + quad);
        iw.removeQuad(quad);
    }

    /** Remove all children of 'instance'. */
    public void unlinkChildren(InstanceWorld iw) {
        for (Iterator it = this.getChildQuads(iw, null).iterator(); it.hasNext(); ) {
            Quad childQuad = (Quad) it.next();
            this.removeChild(childQuad, iw);
        }
    }

    /** Remove attributes attached to 'this'. */
    public void removeAttributes() {
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        List quads = Util.toList(iw.getQuadsForSubject(this));
        for (Iterator it = quads.iterator(); it.hasNext(); ) {
            Quad quad = (Quad) it.next();
            iw.removeQuad(quad);
        }
    }

    /** Remove all labels attached to 'this' (there should only be one). */
    public void removeLabel() {
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        List quads = Util.toList(iw.getMatchingQuads(null, this.getURI(), RdfsURI.LABEL, null, false));
        for (Iterator it = quads.iterator(); it.hasNext(); ) {
            Quad quad = (Quad) it.next();
            iw.removeQuad(quad);
        }
    }

    public String getHierarchyTreeLabel(Term parentTerm, InstanceWorld iw) {
        String label = iw.getFriendlyLabel(this, true);
        if (INCLUDE_ATTRIBUTES_IN_HIERARCHY && (parentTerm != null)) {
            String parentURI = parentTerm.getURI();
            String predicateLabel = null;
            Quad quad = null;
            for (Iterator it = this.getParentQuads(iw, null).iterator(); it.hasNext(); ) {
                quad = (Quad) it.next();
                if (!quad.getPredicate().equals(SewURI.PARENT_CHILD)) {
                    if (quad.getSubject().equals(parentURI)) {
                        predicateLabel = iw.getFriendlyLabel(quad.getPredicate(), true);
                        break;
                    }
                    if (!quad.getObject().equals(parentURI)) continue;
                    if (quad.getPredicate().equals(RdfsURI.SUB_CLASS_OF)) {
                        predicateLabel = "subclass";
                        break;
                    } else if (quad.getPredicate().equals(RdfsURI.SUB_PROPERTY_OF)) {
                        predicateLabel = "subproperty";
                        break;
                    } else if (quad.getPredicate().equals(DctermsURI.IS_PART_OF)) {
                        predicateLabel = "subpart";
                        break;
                    }
                }
            }
            if (predicateLabel != null) {
                String color = "BLACK";
                color = ColorizeContexts.getContextHTMLColor(quad.getContext());
                label = "<html><i><font color='" + color + "'> " + predicateLabel + "</font></i>: " + label + "</html>";
            }
        }
        return label;
    }

    /**
     * If there is exactly one quad mapping 'parentTerm' to 'childTerm', return it:
     */
    public static Quad getParentChildQuad(Term parentTerm, Term childTerm, InstanceWorld iw) {
        if (parentTerm == null) return null;
        Iterator it = childTerm.getParentQuads(iw, null).iterator();
        Quad matchingQuad = null;
        while (it.hasNext()) {
            Quad quad = (Quad) it.next();
            String quadParent = childTerm.extractParentFromQuad(quad);
            if (quadParent.equals(parentTerm.getURI())) {
                if (matchingQuad == null) matchingQuad = quad; else {
                    logger.warn("Duplicate PC links found");
                    return null;
                }
            }
        }
        return matchingQuad;
    }

    public static String getParentChildQuadContext(Term parentTerm, Term childTerm, InstanceWorld iw) {
        if (parentTerm == null) return null;
        String context = null;
        Iterator it = childTerm.getParentQuads(iw, null).iterator();
        while (it.hasNext()) {
            Quad quad = (Quad) it.next();
            String quadParent;
            if (quad.getObject().equals(childTerm.getURI())) quadParent = quad.getSubject(); else quadParent = quad.getObject();
            if (!quadParent.equals(parentTerm.getURI())) continue;
            String cxt = quad.getContext();
            if (context == null) context = cxt; else if (!context.equals(cxt)) return null;
        }
        return context;
    }

    public static int compareInstancesByLabel(String uri1, String uri2) {
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        String label1 = iw.getMatchingObject(null, uri1, RdfsURI.LABEL);
        String label2 = iw.getMatchingObject(null, uri2, RdfsURI.LABEL);
        if (label1 == null) label1 = Util.getLocalnameForURI(uri1);
        if (label2 == null) label2 = Util.getLocalnameForURI(uri2);
        return label1.compareTo(label2);
    }

    public static int compareTermsByLabel(Term term1, Term term2) {
        if (term1.isLiteral()) return term1.value.compareTo(term2.value); else return compareInstancesByLabel(term1.getURI(), term2.getURI());
    }

    public static int compareQuadsByPredicate(Quad q1, Quad q2) {
        String uri1 = q1.getPredicate();
        String uri2 = q2.getPredicate();
        return compareInstancesByLabel(uri1, uri2);
    }

    public static int compareQuadsByObject(Quad q1, Quad q2) {
        String value1 = q1.getObject();
        String value2 = q2.getObject();
        if (RDFUtil.isURI(value1) && RDFUtil.isURI(value2)) {
            return compareInstancesByLabel(value1, value2);
        } else return value1.compareTo(value2);
    }

    /** Compare quads based on the labels attached to their object references. */
    protected class TermComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            return compareTermsByLabel((Term) o1, (Term) o2);
        }

        public boolean equals(Object o1, Object o2) {
            return o1.equals(o2);
        }
    }

    protected class QuadByObjectComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            return compareQuadsByObject((Quad) o1, (Quad) o2);
        }

        public boolean equals(Object o1, Object o2) {
            return o1.equals(o2);
        }
    }

    /** Compare quads based on predicate labels and then by object labels. */
    protected class QuadByPredicateAndByObjectComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Quad q1 = (Quad) o1;
            Quad q2 = (Quad) o2;
            int comparison = compareQuadsByPredicate(q1, q2);
            if (comparison == 0) comparison = compareQuadsByObject(q1, q2);
            return comparison;
        }

        public boolean equals(Object o1, Object o2) {
            return o1.equals(o2);
        }
    }

    private static Term dummy = new Term("dummy", false);

    public static Comparator createQuadByObjectComparator() {
        return dummy.new QuadByObjectComparator();
    }

    public static Comparator createQuadByPredicateAndByObjectComparator() {
        return dummy.new QuadByPredicateAndByObjectComparator();
    }

    /**
     * declare that the type of this instance is classURI, return a Term for the
     * created class instance, so that we can append label to the class
     * instance, etc
     */
    public Term addType(Term classTerm, InstanceWorld iw) {
        this.addAttributeValueAndReact(RdfURI.TYPE, classTerm, iw);
        return classTerm;
    }

    /** remove the type as given in classURI */
    public void removeType(String classURI) {
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        Quad quad = iw.createQuad(iw.getBaseContextURI(), this.getURI(), RdfURI.TYPE, classURI, false);
        iw.removeQuad(quad);
    }

    /**
     * Delete a resource, i.e., remove from 'model' all statements that
     * reference 'resource'.
     */
    public void deleteResourceTerm() {
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        iw.getQuadConnection().deleteResource(iw.getBaseModel(), this.getURI());
    }

    public boolean hasAttributeValue(String attributeName, String attributeValue, boolean objIsLiteral) {
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        if (attributeName == null || attributeValue == null) return false;
        Iterator it = iw.getMatchingQuads(null, this.getURI(), attributeName, attributeValue, objIsLiteral);
        return it.hasNext();
    }

    /** Add label 'label' to 'this'. */
    public void addLabel(String label, InstanceWorld iw) {
        String oldLabel = iw.getLabel(this);
        if (oldLabel != null) {
            System.err.println("Adding label '" + label + "' without removing the old label " + oldLabel);
            new Exception().printStackTrace();
            return;
        } else iw.addQuad(null, this.getURI(), RdfsURI.LABEL, label, true);
    }

    /**
     * Change the label at 'uri' from 'oldLabel' to 'newLabel' in the context
     * 'context'. Return true if a change actually was made.
     */
    public static boolean relabelTerm(String uri, String oldLabel, String newLabel, String context) {
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        if (newLabel != null && !newLabel.equals(oldLabel) && !"".equals(newLabel)) {
            if (oldLabel != null) {
                Quad quadToRemove = new Quad(context, uri, RdfsURI.LABEL, oldLabel, true);
                iw.getQuadConnection().removeQuad(iw.getBaseModel(), quadToRemove);
            }
            Quad newQuad = new Quad(context, uri, RdfsURI.LABEL, newLabel, true);
            iw.getQuadConnection().addQuad(iw.getBaseModel(), newQuad);
            return true;
        }
        return false;
    }

    /**
     * Assign the node 'this' a new label.
     */
    public void renameTerm(String newLabel, InstanceWorld iw) {
        String oldLabel = iw.getLabel(this);
        if (oldLabel.equals(newLabel)) return;
        if ((newLabel == null) || (newLabel.equals(""))) return;
        logger.info("RENAME NODE " + this + "  " + newLabel);
        if (oldLabel == null) this.addLabel(newLabel, iw); else relabelTerm(this.getURI(), oldLabel, newLabel, iw.getBaseContextURI());
        this.fireDataChanged();
    }

    public ArrayList computeAttributeQuads(InstanceWorld iw) {
        ArrayList quads = new ArrayList();
        String uri = this.getURI();
        Iterator it = iw.getMatchingQuads(null, uri, null, null, false);
        while (it.hasNext()) {
            Quad quad = (Quad) it.next();
            String attributeURI = quad.getPredicate();
            if (attributeURI.equals(SewURI.PARENT_CHILD)) continue;
            if (attributeURI.equals(RdfsURI.LABEL)) continue;
            if ((quad.getPredicate().equals(BLANK_INSTANCE_URI)) && quad.getObject().equals("")) continue;
            quads.add(quad);
        }
        return quads;
    }

    /**
     * 
     * @param iw
     * @return the attribute-value quads of this term and of its aligned terms
     */
    public ArrayList computeAttributeQuadsIncludingThoseOfAlignedNodes(InstanceWorld iw) {
        ArrayList allQuads = new ArrayList();
        allQuads.addAll(this.computeAttributeQuads(iw));
        for (Iterator it = this.getAlignedNodes(iw).iterator(); it.hasNext(); ) {
            Term alignedTerm = (Term) it.next();
            allQuads.addAll(alignedTerm.computeAttributeQuads(iw));
        }
        return allQuads;
    }

    /**
     * get the list of nodes that were aligned with this term
     * @param iw
     * @return a list of Terms.
     */
    public List getAlignedNodes(InstanceWorld iw) {
        List alignedNodeURIs = new ArrayList();
        List alignedTerms = new ArrayList();
        String uri = this.getURI();
        Collection alignments = iw.getMatchingSubjects(null, MetaDeskConstants.HAS_ALIGNED_NODE_URI, uri, false);
        for (Iterator it = alignments.iterator(); it.hasNext(); ) {
            String alignment = (String) it.next();
            Collection alignedNodes = iw.getMatchingObjects(null, alignment, MetaDeskConstants.HAS_ALIGNED_NODE_URI);
            for (Iterator it2 = alignedNodes.iterator(); it2.hasNext(); ) {
                String alignedNodeURI = (String) it2.next();
                if (alignedNodeURI.equals(uri)) continue;
                if (alignedNodeURIs.contains(alignedNodeURI)) continue; else {
                    alignedNodeURIs.add(alignedNodeURI);
                    alignedTerms.add(new Term(alignedNodeURI, false));
                }
            }
        }
        return alignedTerms;
    }

    /**
     * Add an attribute with given attribute name and value.
     * Careful: Assumes that 'value' is a literal if its a string.
     * Call 'fireDataChanged' after asserting an attribute quad.
     */
    public void addAttributeValueAndReact(String attributeURI, Object value, InstanceWorld iw) {
        if ((attributeURI == null) || (value == null)) return;
        logger.debug("ADD ATTRIBUTE VALUE " + this + ", " + attributeURI + ", " + value);
        String context = iw.getBaseContextURI();
        String subjectURI = this.getURI();
        String valueString = null;
        boolean isLiteral = false;
        if (value instanceof String) {
            valueString = (String) value;
            isLiteral = true;
        } else {
            valueString = ((Term) value).getURI();
            isLiteral = false;
            if (InstanceWorld.isBlankResource(valueString)) {
                valueString = "";
                isLiteral = true;
            }
        }
        Quad quad = iw.createQuad(context, subjectURI, attributeURI, valueString, isLiteral);
        iw.addQuad(quad);
        this.fireDataChanged();
    }

    /**
     * Return 'true' if Metadesk can convert 'url' into an RDF
     * model/graph, i.e., if it can load it.
     */
    public static boolean isRDFConvertableURL(String url) {
        if (url == null) return false;
        String testURL = url.toLowerCase();
        return (testURL.endsWith(".rdf") || testURL.endsWith(".xml") || testURL.endsWith(".n3"));
    }

    /**
     * Examine 'term' to see if an RDL-loadable URL can be extracted
     * from it.  If so, return the URL.
     */
    public String extractRDFConvertibleURL() {
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        String url = iw.getLabel(this);
        url = Util.canonicalizeURL(url);
        if (isRDFConvertableURL(url)) return url;
        url = iw.getMatchingString(null, this.getURI(), MdkURI.SOURCE_URL);
        if (isRDFConvertableURL(url)) return url;
        return null;
    }

    /** Return the list of quads with non-literal values for 'this'. */
    public ArrayList getQuadsWithInstanceValues() {
        ArrayList quads = new ArrayList();
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        for (Iterator it = this.computeAttributeQuads(iw).iterator(); it.hasNext(); ) {
            Quad quad = (Quad) it.next();
            if (!quad.objectIsLiteral()) {
                quads.add(quad);
            }
        }
        return quads;
    }

    /** Return the list of Terms which are values for 'this'. */
    public ArrayList getAttributeInstanceValues() {
        InstanceWorld iw = InstanceWorld.getCurrentInstanceWorld();
        ArrayList instanceValues = new ArrayList();
        for (Iterator it = getQuadsWithInstanceValues().iterator(); it.hasNext(); ) {
            Quad quad = (Quad) it.next();
            Term value = iw.getTermForURI(quad.getObject());
            instanceValues.add(value);
        }
        return instanceValues;
    }
}

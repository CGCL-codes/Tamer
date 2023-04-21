package org.tzi.use.uml.sys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.tzi.use.uml.mm.MAssociation;
import org.tzi.use.uml.mm.MAssociationEnd;
import org.tzi.use.util.StringUtil;

/**
 * A link is an instance of an association.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author      Mark Richters 
 */
final class MLinkImpl implements MLink {

    private MAssociation fAssociation;

    /**
     * For each association end we store the corresponding link end.
     */
    private Map fLinkEnds;

    /**
     * Creates a new link for the given association.
     *
     * @param objects List(MObject)
     * @exception MSystemException objects do not conform to the
     *            association ends.
     */
    MLinkImpl(MAssociation assoc, List objects) throws MSystemException {
        fAssociation = assoc;
        if (assoc.associationEnds().size() != objects.size()) throw new IllegalArgumentException("Number of association ends (" + assoc.associationEnds().size() + ") does not match number of passed objects (" + objects.size() + ")");
        fLinkEnds = new HashMap();
        Iterator it1 = assoc.associationEnds().iterator();
        Iterator it2 = objects.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            MAssociationEnd aend = (MAssociationEnd) it1.next();
            MObject obj = (MObject) it2.next();
            fLinkEnds.put(aend, new MLinkEnd(aend, obj));
        }
    }

    /**
     * Returns the association describing this link.
     */
    public MAssociation association() {
        return fAssociation;
    }

    /** 
     * Returns all link ends of this link.
     *
     * @return Set(MLinkEnd)
     */
    public Set linkEnds() {
        Set s = new HashSet(fLinkEnds.size());
        s.addAll(fLinkEnds.values());
        return s;
    }

    /**
     * Returns the set of objects participating in this link.
     *
     * @return Set(MObject).
     */
    public Set linkedObjects() {
        Set s = new HashSet();
        Iterator linkIter = fLinkEnds.values().iterator();
        while (linkIter.hasNext()) {
            MLinkEnd lend = (MLinkEnd) linkIter.next();
            s.add(lend.object());
        }
        return s;
    }

    /**
     * Returns the set of objects participating in this link.
     *
     */
    public MObject[] linkedObjectsAsArray() {
        MObject[] objs = new MObject[fLinkEnds.size()];
        int i = 0;
        for (Iterator it = fAssociation.associationEnds().iterator(); it.hasNext(); ) {
            MLinkEnd lend = linkEnd((MAssociationEnd) it.next());
            objs[i] = lend.object();
            ++i;
        }
        return objs;
    }

    /** 
     * Returns the link end for the given association end.
     */
    public MLinkEnd linkEnd(MAssociationEnd aend) {
        return (MLinkEnd) fLinkEnds.get(aend);
    }

    public int hashCode() {
        int hash = fAssociation.hashCode();
        Iterator it = fLinkEnds.values().iterator();
        while (it.hasNext()) {
            MLinkEnd lend = (MLinkEnd) it.next();
            hash += lend.hashCode();
        }
        return hash;
    }

    /**
     * Two links are equal iff they connect the same objects.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof MLink) return association().equals(((MLink) obj).association()) && linkEnds().equals(((MLink) obj).linkEnds());
        return false;
    }

    public String toString() {
        return "[" + fAssociation.name() + " : (" + StringUtil.fmtSeq(fLinkEnds.values().iterator(), ", ") + ")]";
    }
}

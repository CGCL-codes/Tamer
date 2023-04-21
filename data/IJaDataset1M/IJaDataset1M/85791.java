package org.hypergraphdb.query;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.type.HGAtomType;

/**
 * <p>
 * The <code>SubsumedCondition</code>  examines a given atom and is satisfied
 * if that atom is subsumed by the atom specified in the condition. You can also
 * provide a value instead of a HyperGraph handle by using the <code>SubsumedCondition(Object)</code>
 * constructor.  
 * </p>
 *  
 * @author Borislav Iordanov
 */
public class SubsumedCondition extends SubsumesImpl implements HGQueryCondition, HGAtomPredicate {

    private HGHandle general;

    private Object generalValue;

    private HGAtomPredicate impl;

    private final class AtomBased implements HGAtomPredicate {

        public boolean satisfies(HyperGraph hg, HGHandle specific) {
            HGHandle specificType = hg.getType(specific);
            if (generalValue == null) {
                return ((HGAtomType) hg.get(hg.getType(specific))).subsumes(null, hg.get(specific));
            } else {
                HGHandle h = hg.getHandle(generalValue);
                HGHandle generalType;
                if (h == null) generalType = hg.getTypeSystem().getTypeHandle(generalValue.getClass()); else {
                    generalType = hg.getType(h);
                    if (declaredSubsumption(hg, h, specific)) return true;
                }
                if (!generalType.equals(specificType)) return false; else return ((HGAtomType) hg.get(hg.getType(specific))).subsumes(generalValue, hg.get(specific));
            }
        }
    }

    private final class HandleBased implements HGAtomPredicate {

        public boolean satisfies(HyperGraph hg, HGHandle specific) {
            if (declaredSubsumption(hg, general, specific)) return true;
            HGHandle specificType = hg.getType(specific);
            HGHandle generalType = hg.getType(general);
            if (!generalType.equals(specificType)) return false; else return ((HGAtomType) hg.get(generalType)).subsumes(hg.get(general), hg.get(specific));
        }
    }

    public SubsumedCondition() {
    }

    public SubsumedCondition(Object generalValue) {
        setGeneralValue(generalValue);
    }

    public SubsumedCondition(HGHandle general) {
        setGeneralHandle(general);
    }

    public HGHandle getGeneralHandle() {
        return general;
    }

    public void setGeneralHandle(HGHandle general) {
        this.general = general;
        if (this.general != null) impl = new HandleBased();
    }

    public Object getGeneralValue() {
        return generalValue;
    }

    public void setGeneralValue(Object generalValue) {
        this.generalValue = generalValue;
        if (generalValue != null) impl = new AtomBased();
    }

    public final boolean satisfies(HyperGraph hg, HGHandle specific) {
        return impl.satisfies(hg, specific);
    }

    public int hashCode() {
        return (general != null) ? general.hashCode() : generalValue.hashCode();
    }

    public boolean equals(Object x) {
        if (!(x instanceof SubsumedCondition)) return false; else {
            SubsumedCondition c = (SubsumedCondition) x;
            return general.equals(c.general);
        }
    }
}

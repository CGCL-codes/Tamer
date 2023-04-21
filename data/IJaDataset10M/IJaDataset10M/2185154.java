package org.hypergraphdb.app.xsd.primitive;

import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomType;

/**
 *
 */
public class XSDFloatPrimitive implements HGAtomType {

    private HGAtomType hgdbType = null;

    public void setHyperGraph(HyperGraph hg) {
        hgdbType = hg.getTypeSystem().getAtomType(Float.class);
        hgdbType.setHyperGraph(hg);
    }

    public Object make(HGPersistentHandle handle, LazyRef targetSet, IncidenceSetRef incidenceSet) {
        return hgdbType.make(handle, targetSet, incidenceSet);
    }

    /**
     *
     * @param value String
     * @return boolean
     */
    public boolean evaluateRestrictions(float value) {
        return true;
    }

    public HGPersistentHandle store(Object o) {
        Float instance = (Float) o;
        boolean passes = evaluateRestrictions(instance);
        return hgdbType.store(instance);
    }

    public void release(HGPersistentHandle handle) {
        hgdbType.release(handle);
    }

    public boolean subsumes(Object general, Object specific) {
        return false;
    }
}

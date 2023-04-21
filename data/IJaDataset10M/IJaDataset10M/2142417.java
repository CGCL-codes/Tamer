package org.hypergraphdb.app.xsd.primitive;

import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.type.HGAtomType;

/**
 *
 */
public class XSDGYearMonthPrimitive implements HGAtomType {

    public HyperGraph hg;

    public void setHyperGraph(HyperGraph hg) {
        this.hg = hg;
    }

    /**
     *
     * @param handle HGPersistentHandle
     * @param targetSet LazyRef
     * @param incidenceSet LazyRef
     * @return Object
     */
    public Object make(HGPersistentHandle handle, LazyRef targetSet, IncidenceSetRef incidenceSet) {
        byte[] bytes = hg.getStore().getData(handle);
        XSDGYearMonth result = new XSDGYearMonth();
        result.setMonth(bytes[0]);
        short year = (short) (bytes[1] << 8);
        year |= 0xFF & bytes[2];
        result.setYear(year);
        return result;
    }

    /**
     *
     * @return boolean
     */
    public boolean evaluateRestrictions(XSDGYearMonth value) {
        return true;
    }

    /**
     *
     * @param instance Object
     * @return HGPersistentHandle
     */
    public HGPersistentHandle store(Object o) {
        XSDGYearMonth instance = (XSDGYearMonth) o;
        boolean passes = evaluateRestrictions(instance);
        byte bytes[] = new byte[3];
        bytes[0] = instance.getMonth();
        bytes[1] = (byte) (instance.getYear() >>> 8);
        bytes[2] = (byte) instance.getYear();
        return hg.getStore().store(bytes);
    }

    /**
     *
     * @param handle HGPersistentHandle
     */
    public void release(HGPersistentHandle handle) {
    }

    /**
     *
     * @param general Object
     * @param specific Object
     * @return boolean
     */
    public boolean subsumes(Object general, Object specific) {
        return false;
    }
}

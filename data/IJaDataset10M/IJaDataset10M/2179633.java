package org.hypergraphdb.app.xsd;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.IncidenceSet;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import org.hypergraphdb.handle.UUIDHandleFactory;
import org.hypergraphdb.type.RecordType;
import org.hypergraphdb.HGValueLink;
import org.hypergraphdb.type.HGAtomType;

/**
 * 
 */
public class ComplexTypeConstructor implements HGAtomType {

    public static final HGPersistentHandle HANDLE = UUIDHandleFactory.I.makeHandle("bd99ff0c-5b32-11db-82a1-a78cc7527afc");

    public static final ComplexTypeConstructor INSTANCE = new ComplexTypeConstructor();

    private HyperGraph hg;

    /**
    * 
    */
    private ComplexTypeConstructor() {
    }

    /**
    * 
    * @param handle
    *           HGPersistentHandle
    * @param targetSet
    *           LazyRef
    * @param incidenceSet
    *           LazyRef
    * @return Object
    */
    public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet, IncidenceSetRef incidenceSetRef) {
        IncidenceSet incidenceSet = incidenceSetRef.deref();
        HGValueLink valueLink = (HGValueLink) hg.get(incidenceSet.first());
        RecordType recordType = (RecordType) valueLink.getValue();
        Object result = null;
        Class<?> clazz = ClassGenerator.generateComplexType(hg, ComplexTypeBase.class, recordType);
        try {
            result = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
    * 
    * @param hg
    *           HyperGraph
    */
    public void setHyperGraph(HyperGraph hg) {
        this.hg = hg;
    }

    /**
    * 
    * @param instance
    *           Object
    * @return HGPersistentHandle
    */
    public HGPersistentHandle store(Object instance) {
        {
            return hg.getHandleFactory().makeHandle();
        }
    }

    /**
    * 
    * @param handle
    *           HGPersistentHandle
    */
    public void release(HGPersistentHandle handle) {
    }

    /**
    * 
    * @param general
    *           Object
    * @param specific
    *           Object
    * @return boolean
    */
    public boolean subsumes(Object general, Object specific) {
        return false;
    }
}

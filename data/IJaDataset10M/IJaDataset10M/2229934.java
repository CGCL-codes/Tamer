package org.nakedobjects.runtime.persistence.adapterfactory.pojo;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactoryAbstract;

public class PojoAdapterFactory extends AdapterFactoryAbstract {

    public PojoAdapter createAdapter(Object pojo, Oid oid) {
        return new PojoAdapter(pojo, oid);
    }
}

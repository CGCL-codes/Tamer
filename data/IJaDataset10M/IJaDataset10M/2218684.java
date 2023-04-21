package org.nakedobjects.persistence.adapterfactory.pojox;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.persistence.adapterfactory.AdapterFactoryAbstract;

public class PojoAdapterXFactory extends AdapterFactoryAbstract<PojoAdapterX> {

    public PojoAdapterX createAdapter(Object pojo, Oid oid) {
        return new PojoAdapterX(pojo, oid);
    }
}

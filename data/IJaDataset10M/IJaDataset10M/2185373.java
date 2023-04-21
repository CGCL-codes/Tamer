package org.nakedobjects.nof.core.adapter;

import org.nakedobjects.noa.adapter.NakedObjectFactory;
import org.nakedobjects.noa.adapter.Oid;

public class PojoAdapterFactory implements NakedObjectFactory<PojoAdapter> {

    public PojoAdapter createAdapter(Object pojo, Oid oid) {
        return new PojoAdapter(pojo, oid);
    }
}

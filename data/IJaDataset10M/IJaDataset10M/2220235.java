package org.nakedobjects.remoting.protocol.encoding.internal;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.data.query.PersistenceQueryFindAllInstancesData;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindAllInstances;

public class PersistenceQueryFindAllInstancesEncoder extends PersistenceQueryEncoderAbstract {

    public Class<?> getPersistenceQueryClass() {
        return PersistenceQueryFindAllInstances.class;
    }

    public PersistenceQueryData encode(final PersistenceQuery persistenceQuery) {
        return new PersistenceQueryFindAllInstancesData(persistenceQuery.getSpecification());
    }

    @Override
    protected PersistenceQuery doDecode(final NakedObjectSpecification specification, final PersistenceQueryData persistenceQueryData) {
        return new PersistenceQueryFindAllInstances(specification);
    }

    private PersistenceQueryFindAllInstances downcast(final PersistenceQuery persistenceQuery) {
        return (PersistenceQueryFindAllInstances) persistenceQuery;
    }
}

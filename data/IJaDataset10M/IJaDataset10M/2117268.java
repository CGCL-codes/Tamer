package org.nakedobjects.remoting.data.query;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByPattern;

/**
 * Serializable representation of {@link PersistenceQueryFindByPattern}.
 */
public class PersistenceQueryFindByPatternData extends PersistenceQueryDataAbstract {

    private static final long serialVersionUID = 1L;

    private final ObjectData patternData;

    public PersistenceQueryFindByPatternData(final NakedObjectSpecification noSpec, final ObjectData patternData) {
        super(noSpec);
        this.patternData = patternData;
    }

    public ObjectData getPatternData() {
        return patternData;
    }

    public Class<?> getPersistenceQueryClass() {
        return PersistenceQueryFindByPattern.class;
    }
}

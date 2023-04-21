package org.apache.isis.extensions.jpa.runtime.persistence.objectstore.criteria;

import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.runtime.persistence.query.PersistenceQuery;

public interface PersistenceQueryProcessor<T extends PersistenceQuery> {

    ObjectAdapter[] process(T criteria);
}

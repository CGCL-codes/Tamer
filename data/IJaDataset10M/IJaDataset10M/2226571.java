package org.middleheaven.domain.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.middleheaven.domain.query.ListQuery;
import org.middleheaven.domain.query.Query;
import org.middleheaven.util.identity.Identity;

public abstract class AbstractHashRepository<E> extends AbstractRepository<E> {

    private Map<Identity, E> instances = new HashMap<Identity, E>();

    @Override
    public Query<E> findAll() {
        return new ListQuery<E>() {

            @Override
            protected List<E> list() {
                return new ArrayList<E>(instances.values());
            }
        };
    }

    @Override
    public Query<E> findByIdentity(final Identity id) {
        return new ListQuery<E>() {

            @Override
            protected List<E> list() {
                return Collections.singletonList(instances.get(id));
            }
        };
    }

    @Override
    public void remove(E instance) {
        instances.remove(this.getIdentityFor(instance));
    }

    protected void put(Identity id, E instance) {
        instances.put(id, instance);
    }
}

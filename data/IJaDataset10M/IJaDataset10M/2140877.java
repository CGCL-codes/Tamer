package gloodb.impl;

import gloodb.queries.ObjectIdsQuery;
import gloodb.Repository;
import gloodb.RepositoryInterceptor;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

class ObjectIdsQueryImplementation extends ObjectIdsQuery {

    private static final long serialVersionUID = -3815857981489755249L;

    private Filter filter;

    public Serializable run(Repository repository, Serializable... parameters) {
        Repository runtime;
        if (repository instanceof RepositoryInterceptor) {
            runtime = ((RepositoryInterceptor) repository).getIntercepted();
        } else {
            runtime = repository;
        }
        RepositoryImplementation implementation = ((RepositoryFacade) runtime).getImplementation();
        if (filter != null) {
            List<Serializable> result = implementation.getIds();
            for (Iterator<Serializable> iter = result.iterator(); iter.hasNext(); ) {
                Serializable objectId = iter.next();
                if (!filter.match(objectId)) {
                    iter.remove();
                }
            }
            return (Serializable) result;
        } else {
            return (Serializable) implementation.getIds();
        }
    }

    @Override
    protected ObjectIdsQuery filterWith(Filter userFilter) {
        this.filter = userFilter;
        return this;
    }

    @Override
    public String toString() {
        return "gloodb.ObjectIdQuery";
    }
}

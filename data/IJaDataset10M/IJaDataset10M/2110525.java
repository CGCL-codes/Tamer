package gloodb.operators;

import gloodb.Reference;
import gloodb.Repository;
import java.io.Serializable;

/**
 * Implementation of the ReferenceFetch expression. It fetches all the references
 * in the source collection from the repository.
 * @param <T> the value type.
 */
public class ReferenceFetch<T extends Serializable> extends IterativeExpression<Reference<T>> {

    private static final long serialVersionUID = 1L;

    private final Repository repository;

    /**
     * Creates a fetch expression.
     * @param repository The repository.
     */
    public ReferenceFetch(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void evaluateIteratively(Reference<T> reference) {
        reference.fetch(repository);
    }
}

package net.conquiris.api.index;

/**
 * Interface for indexers.
 * @author Andres Rodriguez
 */
public interface Indexer {

    /**
	 * Prepares a batch of deletions and additions to apply to an index from an initial checkpoint.
	 * @param writer Writer to use.
	 * @throws IndexException if there's a problem with the index.
	 * @throws RuntimeException if an error happens during batch compilation.
	 * @throws InterruptedException if the current task has been interrupted.
	 */
    void index(Writer writer) throws InterruptedException, IndexException;
}

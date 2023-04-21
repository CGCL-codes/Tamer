package hu.ihash.common.service;

import java.io.File;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface Indexer<T> {

    /**
	 * Finds and stores all entities from the file system recursively.
	 * 
	 * @param root
	 */
    public IndexerResults indexRecursively(File[] roots);
}

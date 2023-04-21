package org.hibernate.search.backend.impl.blackhole;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.hibernate.search.backend.UpdatableBackendQueueProcessorFactory;
import org.hibernate.search.spi.WorkerBuildContext;
import org.hibernate.search.backend.LuceneWork;
import org.hibernate.search.store.DirectoryProvider;
import org.hibernate.search.util.LoggerFactory;
import org.slf4j.Logger;

/**
 * This backend does not do anything: the Documents are not
 * sent to any index but are discarded.
 * Useful to identify the bottleneck in indexing performance problems,
 * fully disabling the backend system but still building the Documents
 * needed to update an index (loading data from DB).
 *
 * @author Sanne Grinovero
 */
public class BlackHoleBackendQueueProcessorFactory implements UpdatableBackendQueueProcessorFactory {

    private static final Logger log = LoggerFactory.make();

    private final NoOp noOp = new NoOp();

    public Runnable getProcessor(List<LuceneWork> queue) {
        return noOp;
    }

    public void initialize(Properties props, WorkerBuildContext context) {
        log.warn("initialized \"blackhole\" backend. Index changes will be prepared but discarded!");
    }

    public void close() {
        log.info("closed \"blackhole\" backend.");
    }

    public void updateDirectoryProviders(Set<DirectoryProvider<?>> providers, WorkerBuildContext context) {
        log.warn("update DirectoryProviders \"blackhole\" backend. Index changes will be prepared but discarded!");
    }

    private static class NoOp implements Runnable {

        public void run() {
            log.debug("Discarding a list of LuceneWork");
        }
    }
}

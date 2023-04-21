package org.hibernate.search.spi;

import java.util.Set;
import org.apache.lucene.search.Similarity;
import org.hibernate.search.backend.BackendQueueProcessorFactory;
import org.hibernate.search.backend.LuceneIndexingParameters;
import org.hibernate.search.engine.DocumentBuilderIndexedEntity;
import org.hibernate.search.exception.ErrorHandler;
import org.hibernate.search.spi.internals.DirectoryProviderData;
import org.hibernate.search.store.DirectoryProvider;
import org.hibernate.search.store.optimization.OptimizerStrategy;

/**
 * Build context for the worker and other backend
 * Available after all index, entity metadata are built.
 *
 * @author Emmanuel Bernard
 */
public interface WorkerBuildContext extends BuildContext {

    /**
	 * Register the backend queue processor factory. Should only be called by the Worker implementation.
	 * TODO should we move it to a different interface
	 */
    void setBackendQueueProcessorFactory(BackendQueueProcessorFactory backendQueueProcessorFactory);

    OptimizerStrategy getOptimizerStrategy(DirectoryProvider<?> provider);

    Set<Class<?>> getClassesInDirectoryProvider(DirectoryProvider<?> provider);

    LuceneIndexingParameters getIndexingParameters(DirectoryProvider<?> directoryProvider);

    Similarity getSimilarity(DirectoryProvider<?> directoryProvider);

    DirectoryProviderData getDirectoryProviderData(DirectoryProvider<?> provider);

    ErrorHandler getErrorHandler();

    <T> DocumentBuilderIndexedEntity<T> getDocumentBuilderIndexedEntity(Class<T> managedType);
}

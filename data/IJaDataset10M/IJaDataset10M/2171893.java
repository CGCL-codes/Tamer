package net.sf.joafip.performance.items.service;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.kvstore.service.BlockDataManager;
import net.sf.joafip.kvstore.service.IHeapDataManager;
import net.sf.joafip.logger.JoafipLogger;
import net.sf.joafip.service.FilePersistenceClassNotFoundException;
import net.sf.joafip.service.FilePersistenceDataCorruptedException;
import net.sf.joafip.service.FilePersistenceException;
import net.sf.joafip.service.FilePersistenceInvalidClassException;
import net.sf.joafip.service.FilePersistenceNotSerializableException;
import net.sf.joafip.service.FilePersistenceTooBigForSerializationException;
import net.sf.joafip.store.service.StoreClassNotFoundException;

@NotStorableClass
@StorableAccess
public class SearcherBKM extends AbstractSearcher {

    private static final JoafipLogger LOGGER = JoafipLogger.getLogger(SearcherBKM.class);

    public SearcherBKM(final String pathName) throws FilePersistenceException, FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, StoreClassNotFoundException, FilePersistenceTooBigForSerializationException {
        super(pathName);
    }

    public SearcherBKM(final IHeapDataManager dataManager) throws FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, FilePersistenceException, FilePersistenceTooBigForSerializationException {
        super(dataManager);
    }

    public static void main(final String[] args) {
        final String dataFile = RUNTIME_DIR + "/block.data";
        SearcherBKM searcher;
        try {
            final IHeapDataManager dataManager = new BlockDataManager(dataFile, 1024);
            searcher = new SearcherBKM(dataManager);
            searcher.run();
        } catch (final Throwable throwable) {
            LOGGER.fatal("error", throwable);
        }
    }
}

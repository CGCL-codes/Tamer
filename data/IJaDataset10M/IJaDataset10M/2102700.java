package net.sf.joafip.performance.items.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import net.sf.joafip.entity.EnumFilePersistenceCloseAction;
import net.sf.joafip.kvstore.service.IHeapDataManager;
import net.sf.joafip.logger.JoafipLogger;
import net.sf.joafip.performance.items.entity.Item;
import net.sf.joafip.performance.items.entity.ItemList;
import net.sf.joafip.service.FilePersistenceClassNotFoundException;
import net.sf.joafip.service.FilePersistenceDataCorruptedException;
import net.sf.joafip.service.FilePersistenceException;
import net.sf.joafip.service.FilePersistenceInvalidClassException;
import net.sf.joafip.service.FilePersistenceNotSerializableException;
import net.sf.joafip.service.FilePersistenceTooBigForSerializationException;
import net.sf.joafip.service.IDataAccessSession;
import net.sf.joafip.store.service.StoreClassNotFoundException;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class AbstractSearcher extends AbstractPerfService {

    private final JoafipLogger logger = JoafipLogger.getLogger(getClass());

    public AbstractSearcher(final String pathName) throws FilePersistenceException, FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, StoreClassNotFoundException, FilePersistenceTooBigForSerializationException {
        super(pathName);
    }

    public AbstractSearcher(final IHeapDataManager dataManager) throws FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, FilePersistenceException, FilePersistenceTooBigForSerializationException {
        super(dataManager);
    }

    protected void run() throws FilePersistenceException, FilePersistenceClassNotFoundException, FilePersistenceInvalidClassException, FilePersistenceDataCorruptedException, FilePersistenceNotSerializableException, FileNotFoundException, IOException, FilePersistenceTooBigForSerializationException {
        long maxSearchTime = Long.MIN_VALUE;
        long minSearchTime = Long.MAX_VALUE;
        final long startTime = System.currentTimeMillis();
        final IDataAccessSession session = filePersistence.createDataAccessSession();
        session.open();
        initializeByItemDuration();
        int missingCount = 0;
        final List<Integer> missingList = new LinkedList<Integer>();
        int misMatchCount = 0;
        final List<String> misMatchList = new LinkedList<String>();
        for (int identifier = 0; identifier < NUMBER_OF_ITEM; identifier++) {
            final ItemList itemList = getItemList(session);
            final long startSearchTime = System.currentTimeMillis();
            final Item item = itemList.get(identifier);
            if (item == null) {
                logger.info("missing item for identifier " + identifier);
                missingCount++;
                if (missingCount < 40) {
                    missingList.add(identifier);
                }
            } else {
                if (item.getIdentifier() != identifier) {
                    misMatchCount++;
                    if (misMatchCount < 40) {
                        misMatchList.add(item.getIdentifier() + " for " + identifier);
                    }
                }
            }
            final long currentTime = System.currentTimeMillis();
            final long searchDuration = currentTime - startSearchTime;
            if (searchDuration > maxSearchTime) {
                maxSearchTime = searchDuration;
            }
            if (searchDuration < minSearchTime) {
                minSearchTime = searchDuration;
            }
            if (identifier % BATCH_SIZE == BATCH_SIZE - 1) {
                session.close(EnumFilePersistenceCloseAction.DO_NOT_SAVE);
                session.open();
                final long duration = (currentTime - startTime);
                final long byItem = duration * 1000 / (identifier + 1);
                byItemDuration[byItemIndex++] = (int) byItem;
                logger.info(duration + " mS " + (identifier + 1) + " found, by item " + byItem + " uS");
            }
        }
        session.close(EnumFilePersistenceCloseAction.DO_NOT_SAVE);
        final long endTime = System.currentTimeMillis();
        logger.info("min search time " + minSearchTime);
        logger.info("max search time " + maxSearchTime);
        logger.info((endTime - startTime) + " mS for " + NUMBER_OF_ITEM + " items");
        logger.info("missing count=" + missingCount);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("missing list=\n");
        for (int missing : missingList) {
            stringBuilder.append(missing);
            stringBuilder.append('\n');
        }
        logger.info(stringBuilder.toString());
        logger.info("mismatch count=" + misMatchCount);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mismatch list=\n");
        for (String mismatch : misMatchList) {
            stringBuilder.append(mismatch);
            stringBuilder.append('\n');
        }
        logger.info(stringBuilder.toString());
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(DURATION_BIN));
        objectOutputStream.writeObject(byItemDuration);
        objectOutputStream.close();
    }
}

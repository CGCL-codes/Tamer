package net.sf.joafip.store.service;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.heapfile.service.HeapException;
import net.sf.joafip.heapfile.service.IHeapDataManager;
import net.sf.joafip.store.entity.objectio.ObjectLinkTreeNode;
import net.sf.joafip.store.service.garbage.GarbageException;
import net.sf.joafip.store.service.garbage.GarbageManager;
import net.sf.joafip.store.service.objectio.ObjectIODataRecordNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIOException;
import net.sf.joafip.store.service.objectio.ObjectIOManager;
import org.apache.log4j.Logger;

/**
 * remove operation for {@link Store}<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class StoreRemover {

    private static final Logger _log = Logger.getLogger(StoreRemover.class);

    /** data manager on heap file */
    private final IHeapDataManager dataManager;

    /** manager of garbage candidate record identifier storing and link storing */
    private final GarbageManager garbageManager;

    /** object reading and writing on data file manager */
    private final ObjectIOManager objectIOManager;

    /** manage saving of object */
    private StoreSaver storeSaver;

    StoreRemover(final Store store) {
        dataManager = store.getDataManager();
        garbageManager = store.getGarbageManager();
        objectIOManager = store.getObjectIOManager();
    }

    public void setStoreSaver(final StoreSaver storeSaver) {
        this.storeSaver = storeSaver;
    }

    public int remove(final Long dataRecordIdentifier) throws StoreException {
        final int removed;
        try {
            if (dataManager.haveDataRecord(dataRecordIdentifier)) {
                final ObjectLinkTreeNode referencedObjectLinkTreeNode;
                referencedObjectLinkTreeNode = objectIOManager.getObjectLinkTreeNode(dataRecordIdentifier);
                removed = remove(referencedObjectLinkTreeNode, dataRecordIdentifier);
            } else {
                removed = 0;
            }
        } catch (ObjectIOException exception) {
            throw new StoreException(exception);
        } catch (ObjectIODataRecordNotFoundException exception) {
            throw new StoreException(exception);
        } catch (HeapException exception) {
            throw new StoreException(exception);
        }
        return removed;
    }

    /**
	 * @param objectLinkTreeNode
	 * @param dataRecordIdentifier
	 * @return number of data record removed
	 * @throws StoreException
	 */
    private int remove(final ObjectLinkTreeNode objectLinkTreeNode, final long dataRecordIdentifier) throws StoreException {
        int removed = 0;
        if (_log.isDebugEnabled()) {
            _log.debug("remove data record #" + dataRecordIdentifier);
        }
        try {
            if (garbageManager.hasReferenced(dataRecordIdentifier)) {
                garbageManager.removeLink(dataRecordIdentifier);
                storeSaver.performModificationDone();
            }
            garbageManager.removeGarbageCandidate(dataRecordIdentifier);
            dataManager.deleteHeapFileRecord(dataRecordIdentifier);
            storeSaver.performModificationDone();
            final Long[] referencedList = objectLinkTreeNode.getReferencedList();
            removed++;
            for (Long referencedDataRecordIdentifier : referencedList) {
                removed += remove(referencedDataRecordIdentifier);
            }
        } catch (HeapException exception) {
            throw new StoreException(exception);
        } catch (GarbageException exception) {
            throw new StoreException(exception);
        }
        return removed;
    }
}

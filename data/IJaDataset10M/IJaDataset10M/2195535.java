package net.sf.joafip.store.service.garbage.rbt;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.store.entity.StoreHeader;
import net.sf.joafip.store.service.StoreException;
import net.sf.joafip.store.service.binary.HelperBinaryConversion;
import net.sf.joafip.store.service.heaprecordable.HeapRecordableManager;

/**
 * red black tree node manager for garbage candidate tree<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class GarbageCandidateRBTNodeManager extends AbstractDataRecordIdentifierRBTNodeManager {

    public GarbageCandidateRBTNodeManager(final StoreHeader storeHeader, final HeapRecordableManager heapRecordableManager, final HelperBinaryConversion helperBinaryConversion) {
        super(storeHeader, heapRecordableManager, helperBinaryConversion);
    }

    protected DataRecordIdentifier getRootNodeIdentifier() throws StoreException {
        return storeHeader.getGarbageCandidateRootDataRecordIdentifier();
    }

    protected void setRootNodeIdentifier(final DataRecordIdentifier dataRecordIdentifier) throws StoreException {
        storeHeader.setGarbageCandidateRootDataRecordIdentifier(dataRecordIdentifier);
    }
}

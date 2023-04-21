package net.sf.joafip.store.service.garbage.rbt;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.store.entity.StoreHeader;
import net.sf.joafip.store.service.StoreException;
import net.sf.joafip.store.service.binary.HelperBinaryConversion;
import net.sf.joafip.store.service.heaprecordable.HeapRecordableManager;

/**
 * red black tree node manager for to visit by garbage collector tree<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class ToVisitRBTNodeManager extends AbstractDataRecordIdentifierRBTNodeManager {

    public ToVisitRBTNodeManager(final StoreHeader storeHeader, final HeapRecordableManager heapRecordableManager, final HelperBinaryConversion helperBinaryConversion) {
        super(storeHeader, heapRecordableManager, helperBinaryConversion);
    }

    @Override
    protected DataRecordIdentifier getRootNodeIdentifier() throws StoreException {
        return storeHeader.getToVisitByCollectorRootDataRecordIdentifier();
    }

    @Override
    protected void setRootNodeIdentifier(final DataRecordIdentifier dataRecordIdentifier) throws StoreException {
        storeHeader.setToVisitByCollectorRootDataRecordIdentifier(dataRecordIdentifier);
    }
}

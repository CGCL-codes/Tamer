package net.sf.joafip.store.service.garbage;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.redblacktree.entity.IRBTNode;
import net.sf.joafip.redblacktree.service.IRBTNodeManager;
import net.sf.joafip.redblacktree.service.RBTException;
import net.sf.joafip.store.entity.StoreHeader;
import net.sf.joafip.store.entity.garbage.GarbageReferenceLinkRBTNode;
import net.sf.joafip.store.entity.garbage.ReferenceLink;
import net.sf.joafip.store.service.StoreException;
import net.sf.joafip.store.service.heaprecordable.HeapRecordableException;
import net.sf.joafip.store.service.heaprecordable.HeapRecordableManager;

/**
 * red black tree node manager for garbage reference link tree<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class GarbageReferenceLinkRBTNodeManager implements IRBTNodeManager<ReferenceLink> {

    private final StoreHeader storeHeader;

    private final HeapRecordableManager heapRecordableManager;

    public GarbageReferenceLinkRBTNodeManager(final StoreHeader storeHeader, final HeapRecordableManager heapRecordableManager) {
        super();
        this.storeHeader = storeHeader;
        this.heapRecordableManager = heapRecordableManager;
    }

    public IRBTNode<ReferenceLink> getRootNode() throws RBTException {
        Long dataRecordIdentifier;
        try {
            dataRecordIdentifier = storeHeader.getGarbageReferenceLinkRootDataRecordIdentifier();
        } catch (StoreException exception) {
            throw new RBTException(exception);
        }
        GarbageReferenceLinkRBTNode rootNode = null;
        if (dataRecordIdentifier != null) {
            rootNode = (GarbageReferenceLinkRBTNode) heapRecordableManager.getHeapRecordable(dataRecordIdentifier);
        }
        if (rootNode == null && dataRecordIdentifier != null) {
            rootNode = (GarbageReferenceLinkRBTNode) heapRecordableManager.getHeapRecordable(dataRecordIdentifier);
            if (rootNode == null) {
                rootNode = new GarbageReferenceLinkRBTNode(heapRecordableManager);
                try {
                    rootNode.setDataRecordIdentifier(dataRecordIdentifier, true);
                    if (!heapRecordableManager.readAndSetState(rootNode)) {
                        throw new RBTException("failed read and set state");
                    }
                } catch (HeapRecordableException exception) {
                    throw new RBTException(exception);
                }
            }
        }
        return rootNode;
    }

    public IRBTNode<ReferenceLink> newSentinel() {
        return new GarbageReferenceLinkRBTNode();
    }

    public void setRootNode(final IRBTNode rootNode) throws RBTException {
        final GarbageReferenceLinkRBTNode garbageReferenceLinkRootNode = (GarbageReferenceLinkRBTNode) rootNode;
        try {
            if (rootNode == null) {
                storeHeader.setGarbageReferenceLinkRootDataRecordIdentifier(null);
            } else {
                storeHeader.setGarbageReferenceLinkRootDataRecordIdentifier(garbageReferenceLinkRootNode.getDataRecordIdentifier());
            }
        } catch (StoreException exception) {
            throw new RBTException(exception);
        }
    }
}

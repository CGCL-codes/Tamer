package net.sf.joafip.store.service.garbage.recordmgr;

import java.util.LinkedList;
import java.util.List;
import net.sf.joafip.Fortest;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.logger.JoafipLogger;
import net.sf.joafip.redblacktree.entity.IRBTNode;
import net.sf.joafip.redblacktree.service.RBTException;
import net.sf.joafip.redblacktree.service.RedBlackTree;
import net.sf.joafip.store.entity.StoreHeader;
import net.sf.joafip.store.entity.garbage.GarbageReferenceLinkRBTNode;
import net.sf.joafip.store.entity.garbage.ReferenceLink;
import net.sf.joafip.store.entity.garbage.ReferenceLinkGarbageException;
import net.sf.joafip.store.service.binary.HelperBinaryConversion;
import net.sf.joafip.store.service.garbage.GarbageException;
import net.sf.joafip.store.service.garbage.rbt.GarbageReferenceLinkRBTNodeManager;
import net.sf.joafip.store.service.heaprecordable.HeapRecordableException;
import net.sf.joafip.store.service.heaprecordable.HeapRecordableManager;

/**
 * manage reference link record management in file<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class LinkRecordManager {

    private static final JoafipLogger LOGGER = JoafipLogger.getLogger(LinkRecordManager.class);

    /** a constant empty list */
    private static final List<DataRecordIdentifier> EMPTY_LIST = new LinkedList<DataRecordIdentifier>();

    private final HeapRecordableManager heapRecordableManager;

    /** red black tree manager for reference link node storing */
    private final RedBlackTree<ReferenceLink> garbageReferenceLinkTree;

    private final HelperBinaryConversion helperBinaryConversion;

    public LinkRecordManager(final StoreHeader storeHeader, final HeapRecordableManager heapRecordableManager, final HelperBinaryConversion helperBinaryConversion) {
        super();
        this.heapRecordableManager = heapRecordableManager;
        this.helperBinaryConversion = helperBinaryConversion;
        final GarbageReferenceLinkRBTNodeManager garbageReferenceLinkRBTNodeManager;
        garbageReferenceLinkRBTNodeManager = new GarbageReferenceLinkRBTNodeManager(storeHeader, heapRecordableManager, helperBinaryConversion);
        garbageReferenceLinkTree = new RedBlackTree<ReferenceLink>(garbageReferenceLinkRBTNodeManager, false, true);
    }

    /**
	 * 
	 * @param referencedDataRecordIdentifier
	 * @return reference link or null if not found
	 * @throws GarbageException
	 * @throws ReferenceLinkGarbageException
	 */
    public ReferenceLink searchByReferenced(final DataRecordIdentifier referencedDataRecordIdentifier) throws GarbageException, ReferenceLinkGarbageException {
        final ReferenceLink referenceLink = new ReferenceLink(referencedDataRecordIdentifier);
        final GarbageReferenceLinkRBTNode node;
        final ReferenceLink foundReferenceLink;
        try {
            node = (GarbageReferenceLinkRBTNode) garbageReferenceLinkTree.search(referenceLink);
            if (node == null) {
                foundReferenceLink = null;
                if (LOGGER.debugEnabled) {
                    LOGGER.debug("reference link record for referenced data record #" + referencedDataRecordIdentifier + " not found");
                }
            } else {
                foundReferenceLink = node.getElement();
                if (LOGGER.debugEnabled) {
                    LOGGER.debug("found reference link record " + node.getDataRecordIdentifier() + " for referenced " + referencedDataRecordIdentifier);
                }
            }
        } catch (RBTException exception) {
            rbtExceptionCause(exception);
            throw new InternalError();
        }
        return foundReferenceLink;
    }

    public void appendNewReferenceLink(final ReferenceLink referenceLink) throws GarbageException, ReferenceLinkGarbageException {
        GarbageReferenceLinkRBTNode node = referenceLink.getNode();
        if (node != null) {
            throw new GarbageException("already in file");
        }
        node = new GarbageReferenceLinkRBTNode(heapRecordableManager, helperBinaryConversion);
        try {
            referenceLink.setNode(node);
            node.setElement(referenceLink);
            heapRecordableManager.attach(node);
            garbageReferenceLinkTree.append(node);
            node.setStateHaveChanged();
        } catch (RBTException exception) {
            rbtExceptionCause(exception);
            throw new InternalError();
        } catch (HeapRecordableException exception) {
            throw new GarbageException(exception);
        }
        if (LOGGER.debugEnabled) {
            LOGGER.debug("add reference link record #" + node.getDataRecordIdentifier() + ", reference link " + referenceLink);
        }
    }

    public void deleteReferenceLink(final ReferenceLink referenceLink) throws GarbageException, ReferenceLinkGarbageException {
        final GarbageReferenceLinkRBTNode node = referenceLink.getNode();
        if (node == null) {
            throw new GarbageException("does not exist in file");
        }
        try {
            garbageReferenceLinkTree.deleteExistingNode(node);
            final DataRecordIdentifier dataRecordIdentifier = node.getDataRecordIdentifier();
            heapRecordableManager.delete(dataRecordIdentifier);
            if (LOGGER.debugEnabled) {
                LOGGER.debug("removed reference link record " + dataRecordIdentifier + " for data record object " + referenceLink.getReferencedDataRecordIdentifier());
            }
        } catch (RBTException exception) {
            rbtExceptionCause(exception);
            throw new InternalError();
        } catch (HeapRecordableException exception) {
            throw new GarbageException(exception);
        }
    }

    /**
	 * remove all link to a referenced, must exist<br>
	 * 
	 * @param referencedDataRecordIdentifier
	 *            data record identifier for referenced object
	 * @throws GarbageException
	 * @throws ReferenceLinkGarbageException
	 */
    public void removeLink(final DataRecordIdentifier referencedDataRecordIdentifier) throws GarbageException, ReferenceLinkGarbageException {
        final ReferenceLink referenceLink = searchByReferenced(referencedDataRecordIdentifier);
        if (referenceLink.getNode() != null) {
            deleteReferenceLink(referenceLink);
        }
    }

    /**
	 * remove all link to a referenced, can not exist<br>
	 * 
	 * @param referencedDataRecordIdentifier
	 *            data record identifier for referenced object
	 * @throws GarbageException
	 * @throws ReferenceLinkGarbageException
	 */
    public void removeLinkIfExist(final DataRecordIdentifier referencedDataRecordIdentifier) throws GarbageException, ReferenceLinkGarbageException {
        final ReferenceLink referenceLink = searchByReferenced(referencedDataRecordIdentifier);
        if (referenceLink != null) {
            deleteReferenceLink(referenceLink);
        }
    }

    public void updateReferenceLink(final ReferenceLink referenceLink) throws GarbageException {
        final GarbageReferenceLinkRBTNode node = referenceLink.getNode();
        if (node == null) {
            throw new GarbageException("does not exist in file");
        }
        try {
            node.setStateHaveChanged();
        } catch (HeapRecordableException exception) {
            throw new GarbageException(exception);
        }
        if (LOGGER.debugEnabled) {
            LOGGER.debug("update referece link record " + node.getDataRecordIdentifier());
        }
    }

    /**
	 * 
	 * @param dataRecordIdentifier
	 *            data record identifier for object
	 * @return true if object for data record identifier is referenced
	 * @throws GarbageException
	 * @throws ReferenceLinkGarbageException
	 */
    public boolean isReferenced(final DataRecordIdentifier dataRecordIdentifier) throws GarbageException, ReferenceLinkGarbageException {
        final boolean isReferenced;
        final ReferenceLink referenceLink = new ReferenceLink(dataRecordIdentifier);
        try {
            isReferenced = (GarbageReferenceLinkRBTNode) garbageReferenceLinkTree.search(referenceLink) != null;
        } catch (RBTException exception) {
            rbtExceptionCause(exception);
            throw new InternalError();
        }
        return isReferenced;
    }

    /**
	 * get all referencing for a referenced<br>
	 * 
	 * @param referencedDataRecordIdentifier
	 *            data record identifier for referenced object
	 * @return
	 * @throws GarbageException
	 * @throws ReferenceLinkGarbageException
	 */
    public List<DataRecordIdentifier> getReferencingForReferenced(final DataRecordIdentifier referencedDataRecordIdentifier) throws GarbageException, ReferenceLinkGarbageException {
        final List<DataRecordIdentifier> list;
        try {
            ReferenceLink referenceLink = new ReferenceLink(referencedDataRecordIdentifier);
            final GarbageReferenceLinkRBTNode node = (GarbageReferenceLinkRBTNode) garbageReferenceLinkTree.search(referenceLink);
            if (node == null) {
                list = EMPTY_LIST;
                if (LOGGER.debugEnabled) {
                    LOGGER.debug("not referencing of data record #" + referencedDataRecordIdentifier);
                }
            } else {
                referenceLink = node.getElement();
                list = referenceLink.getRefencingDataRecordIdentifier();
                if (LOGGER.debugEnabled) {
                    LOGGER.debug("referencing of data record #" + referencedDataRecordIdentifier + " is " + list);
                }
            }
        } catch (RBTException exception) {
            rbtExceptionCause(exception);
            throw new InternalError();
        }
        return list;
    }

    private void rbtExceptionCause(final RBTException exception) throws ReferenceLinkGarbageException, GarbageException {
        final Throwable cause = exception.getCause();
        if (cause == null) {
            throw new GarbageException(exception);
        } else if (ReferenceLinkGarbageException.class.equals(cause.getClass())) {
            throw (ReferenceLinkGarbageException) cause;
        } else if (GarbageException.class.equals(cause.getClass())) {
            throw (ReferenceLinkGarbageException) cause;
        } else {
            throw new GarbageException(exception);
        }
    }

    /**
	 * for test purpose
	 * 
	 * @return number of link record
	 * @throws GarbageException
	 * @throws ReferenceLinkGarbageException
	 */
    @Fortest
    public int getNumberOfLinkRecord() throws GarbageException, ReferenceLinkGarbageException {
        try {
            return garbageReferenceLinkTree.getNumberOfElement();
        } catch (RBTException exception) {
            rbtExceptionCause(exception);
            throw new InternalError();
        }
    }

    @Fortest
    public ReferenceLink[] getAllReferenceLink() throws GarbageException, ReferenceLinkGarbageException {
        try {
            final List<ReferenceLink> list = new LinkedList<ReferenceLink>();
            IRBTNode<ReferenceLink> node;
            node = garbageReferenceLinkTree.first();
            while (node != null) {
                list.add(node.getElement());
                node = garbageReferenceLinkTree.next(node);
            }
            final ReferenceLink[] referenceLinks = new ReferenceLink[list.size()];
            list.toArray(referenceLinks);
            return referenceLinks;
        } catch (RBTException exception) {
            rbtExceptionCause(exception);
            throw new InternalError();
        }
    }

    @Fortest
    public List<DataRecordIdentifier> getAllDataRecordIdentifiers() throws GarbageException, ReferenceLinkGarbageException {
        final List<DataRecordIdentifier> list = new LinkedList<DataRecordIdentifier>();
        try {
            for (IRBTNode<ReferenceLink> node : garbageReferenceLinkTree) {
                final DataRecordIdentifier dataRecordIdentifier = node.getElement().getNode().getDataRecordIdentifier();
                list.add(dataRecordIdentifier);
            }
        } catch (RBTException exception) {
            rbtExceptionCause(exception);
            throw new InternalError();
        }
        return list;
    }
}

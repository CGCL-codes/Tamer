package org.vramework.vow.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.vramework.commons.config.VConf;
import org.vramework.commons.logging.ICallLevelAwareLog;
import org.vramework.commons.utils.VSert;
import org.vramework.vow.IInverseCollections;
import org.vramework.vow.IVOW;
import org.vramework.vow.InverseEntry;
import org.vramework.vow.collections.ITransactionalCollection;

/**
 * Default implementation for {@link IInverseCollections}. <br />
 * <strong>Note:</strong> The operations are not synchronized. If used with an
 * {@link ITransactionalCollection} and a {@link IVOW}, no synch issue can occur
 * because an {@link IVOW} must only be modified by its owning thread.
 * 
 * @author thomas.mahringer
 */
public class InverseCollections implements IInverseCollections {

    private Map<Collection<?>, InverseEntry> _entries = new IdentityHashMap<Collection<?>, InverseEntry>(20);

    private List<CollectionOperation<? extends Collection<?>>> _changedLog;

    ICallLevelAwareLog _log = VConf.getCallLevelAwareLogger(this);

    /**
   * @param detailedLog -
   *          true: Log each change (addition or removal) in a
   *          {@link CollectionOperation} of its own. The sequence for the
   *          operations will be kept.
   */
    public InverseCollections(boolean detailedLog) {
        if (detailedLog) {
            _changedLog = new ArrayList<CollectionOperation<? extends Collection<?>>>(250);
        }
    }

    /**
   * @see IInverseCollections#registerAdd(Object, String, Collection)
   */
    public void registerAdd(Object root, String property, Collection<? extends Object> collection) {
        VSert.argNotNull("root", root);
        VSert.argNotEmpty("property", property);
        VSert.argNotNull("collection", collection);
        InverseEntry entry = _entries.get(collection);
        if (entry == null) {
            _entries.put(collection, new InverseEntry(root, property, 1));
        } else {
            entry.incCount();
        }
        trackChange(collection, AssociationActionEnum.Insert);
    }

    /**
   * @see IInverseCollections#registerRemove(Object, String, Collection)
   */
    public void registerRemove(Object root, String property, Collection<? extends Object> collection) {
        VSert.argNotNull("root", root);
        VSert.argNotEmpty("property", property);
        VSert.argNotNull("collection", collection);
        InverseEntry entry = _entries.get(collection);
        if (entry == null) {
            _entries.put(collection, new InverseEntry(root, property, -1));
        } else {
            entry.decCount();
        }
        trackChange(collection, AssociationActionEnum.Remove);
    }

    /**
   * @return An iterator over the collections an object has been added to or
   *         removed from. <br />
   *         key == the collection; value = the number of additions minus
   *         removals. <br />
   *         less than 0 means: Object has been removed more often than added
   *         <br />
   *         more than 0 means: Object has been added more often than removed
   *         <br />
   *         0 means: Number of additions == number of removals
   */
    public Iterator<Map.Entry<Collection<?>, InverseEntry>> iterator() {
        return _entries.entrySet().iterator();
    }

    /**
   * @return An iterator of the changed log. Can be null if the "detailedLog"
   *         parameter in the constructor was false.
   */
    public Iterator<CollectionOperation<? extends Collection<?>>> getChangedLog() {
        return _changedLog.iterator();
    }

    /**
   * @param collection
   * @param actionType
   */
    public void trackChange(Collection<? extends Object> collection, AssociationActionEnum actionType) {
        if (_changedLog == null) {
            _log.trace("trackChange", new Object[] { "No detailed tracking wanted: ", actionType });
            return;
        }
        _log.trace("trackChange", new Object[] { "Tracking detailed change long: ", actionType });
        _changedLog.add(new CollectionOperation<Collection<? extends Object>>(actionType, collection));
    }

    /**
   * @see IInverseCollections#getCount(java.util.Collection)
   */
    public int getCount(Collection<?> collection) {
        InverseEntry entry = _entries.get(collection);
        if (entry == null) {
            return 0;
        } else {
            return entry.getCount();
        }
    }

    /**
   * @see org.vramework.vow.IInverseCollections#count()
   */
    public int count() {
        return _entries.size();
    }

    /**
   * @see IInverseCollections#clear()
   */
    public void clear() {
        _entries.clear();
        if (_changedLog != null) {
            _changedLog.clear();
        }
    }
}

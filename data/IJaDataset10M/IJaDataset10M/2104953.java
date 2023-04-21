package org.vramework.vow.collections.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.vramework.commons.collections.CollectionHelper;
import org.vramework.commons.config.VConf;
import org.vramework.commons.datatypes.Dtu;
import org.vramework.commons.datatypes.Reference;
import org.vramework.commons.logging.ICallLevelAwareLog;
import org.vramework.commons.utils.VSert;
import org.vramework.vow.IInverseCollections;
import org.vramework.vow.ITransactionalBean;
import org.vramework.vow.IVOW;
import org.vramework.vow.collections.ITransactionalCollection;
import org.vramework.vow.exceptions.VOWErrors;
import org.vramework.vow.exceptions.VOWException;
import org.vramework.vow.exceptions.VOWNoLongerValidException;
import org.vramework.vow.impl.AssociationActionEnum;
import org.vramework.vow.impl.CollectionOperation;
import org.vramework.vow.impl.VOW;
import org.vramework.vow.impl.VOW.TransitionResult;

/**
 * 
 * Wraps a collection and allows transactional access to it. I.e: All changes
 * are temporary till {@link #commit()}. <br />
 * This implementation creates a copy of the wrapped collection. All changes
 * will go to the copy. The copy has the same type as the wrapped collection.
 * <br />
 * Important terms:
 * <ul>
 * <li> Root object: The object owning or containing the
 * collection. 
 * <li> Associated objects: The objects contained in the collection.
 * <li> Sample: In the 1:n relationship Customer -> Contract, a customer
 * object is the "root" and a contract objects are "associated".
 * <li> Collection property: The property which holds the collection, e.g.
 * "contracts" in a "Customer".
 * </ul>
 * <strong>Note:</strong> This implementation is capable of tracking each
 * operation, e.g. each "add()" and each "remove()". The log of all changes can
 * be retrieved via {@link #getChangeLogIterator()}.
 * 
 * @param <E> -
 *          The type of the contained objects.
 * @param <T> -
 *          The type of the wrapped collection.
 * @author thomas.mahringer
 */
public class TransactionalCollection<E, T extends Collection<E>> implements ITransactionalCollection<E, T> {

    private ICallLevelAwareLog _log = VConf.getCallLevelAwareLogger(this);

    /**
   * The wrapped collection.
   */
    private T _wrapped;

    /**
   * The current state.
   */
    protected T _current;

    /**
   * The {@link VOW} we belong to. Can be null.
   */
    protected IVOW _vow;

    /**
   * The root/owner of the collection. See class comment.
   */
    protected Object _root;

    /**
   * See class comment.
   */
    protected String _collectionProperty;

    /**
   * The log of all changes in the collection.
   */
    private List<CollectionOperation<?>> _changeLog = new ArrayList<CollectionOperation<?>>(100);

    /**
   * true: Track the root/owner object as changed when an object is added or
   * removed. Sample: <code>customer.getContracts().add(contract);</code>
   * would track the customer as changed.
   */
    boolean _trackRootAsChanged;

    /**
   * true: Track the associated object (= the added or removed one) as changed
   * when an object is added or removed. Sample:
   * <code>customer.getContracts().add(contract);</code> would track the
   * contract as changed.
   */
    boolean _trackAssociatedAsChanged;

    /**
   * true: Track each operation and provide the changes in
   * {@link #getChangeLogIterator()}.
   */
    boolean _detailedChangeLog;

    /**
   * true: The {@link IVOW} is no longer valid.
   */
    boolean _vowNoLongerValid = false;

    /**
   * @param uow -
   *          The {@link IVOW} we belong to. May be null if used without a
   *          {@link IVOW}.
   * @param wrapped -
   *          The wrapped collection.
   */
    public TransactionalCollection(IVOW uow, T wrapped) {
        VSert.argNotNull("wrapped", wrapped);
        setWrapped(wrapped);
        T current = CollectionHelper.createShallowCopy(_wrapped);
        setCurrent(current);
        setVow(uow);
    }

    /**
   * @param uow -
   *          The {@link IVOW} we belong to. May be null if used without a
   *          {@link IVOW}.
   * @param wrapped -
   *          The wrapped collection.
   * @param root -
   *          See class description.
   * @param collectionProperty -
   *          See class description.
   * @param trackRoot -
   *          See class description.
   * @param trackAssociated -
   *          See class description.
   * @param detailedChangeLog -
   *          See {@link #isDetailedChangeLog()}
   */
    public TransactionalCollection(IVOW uow, T wrapped, Object root, String collectionProperty, boolean trackRoot, boolean trackAssociated, boolean detailedChangeLog) {
        VSert.argNotNull("wrapped", wrapped);
        VSert.argNotNull("root", root);
        VSert.argNotEmpty("collectionProperty", collectionProperty);
        setWrapped(wrapped);
        T current = CollectionHelper.createShallowCopy(_wrapped);
        setCurrent(current);
        setVow(uow);
        setRoot(root);
        setCollectionProperty(collectionProperty);
        setTrackAssociatedAsChanged(trackAssociated);
        setTrackRootAsChanged(trackRoot);
        setDetailedChangeLog(detailedChangeLog);
    }

    /**
   * This implementation clears the wrapped collection first and then adds all objects of
   * the current state to it.
   * 
   * @see ITransactionalCollection#commit()
   */
    @Override
    public void commit() {
        ensureVOWStillValid();
        if (_current == null) {
            _log.warn("commit", new Object[] { "Collection is already committed. May be a normal behaviour if a VOW commits it and a transactional bean holds a reference to the collection and commits it, too" });
        } else {
            _wrapped.clear();
            _wrapped.addAll(_current);
            rollback();
        }
    }

    @Override
    public void rollback() {
        ensureVOWStillValid();
        _current.clear();
        _current = null;
        _changeLog.clear();
    }

    @Override
    public Iterator<CollectionOperation<?>> getChangeLogIterator() {
        ensureVOWStillValid();
        if (!isDetailedChangeLog()) {
            throw new VOWException(VOWErrors.Collection_DetailedTrackingMustBeTurnedOn, "getChangeLogIterator");
        }
        return _changeLog.iterator();
    }

    @Override
    public int changeLogSize() {
        ensureVOWStillValid();
        return _changeLog.size();
    }

    @Override
    public int changeLogSizeDebug() {
        return _changeLog.size();
    }

    @Override
    public void informVOWNoLongerValid() {
        setVowNoLongerValid(true);
    }

    @Override
    public final boolean isTrackRootAsChanged() {
        return _trackRootAsChanged;
    }

    @Override
    public final boolean isTrackAssociatedAsChanged() {
        return _trackAssociatedAsChanged;
    }

    @Override
    public T getCurrentDebug() {
        return _current;
    }

    /**
   * @see Collection#add(java.lang.Object)
   */
    public boolean add(E e) {
        return addInternally(-1, e);
    }

    /**
   * Adds an element. <br />
   * <strong>Note:</strong> Can also handle additions to {@link List}s in
   * order to avoi code duplication in derived classes. See
   * {@link #addInternallyAndHandleInverse(int, Object)}.
   * 
   * @param index
   * @param e
   * @return See {@link Collection#add(java.lang.Object)}
   */
    protected boolean addInternally(int index, E e) {
        ensureVOWStillValid();
        checkOwner();
        String collectionProperty = getCollectionProperty();
        Object root = getRoot();
        if (_vow != null) {
            TransitionResult tResultRoot = null;
            if (isTrackRootAsChanged()) {
                tResultRoot = _vow.preparePropertySet(root, collectionProperty, null, false);
                _vow.markChanged(root, collectionProperty);
                if (tResultRoot.isTransitionVetoed()) {
                    return false;
                }
            }
            boolean added = addInternallyAndHandleInverse(index, e);
            if (isTrackRootAsChanged()) {
                _vow.completePropertySet(tResultRoot, false);
            }
            return added;
        } else {
            trackChange(e, AssociationActionEnum.Insert);
            return _current.add(e);
        }
    }

    /**
   * @see java.util.Collection#addAll(java.util.Collection)
   */
    public boolean addAll(Collection<? extends E> c) {
        return addAllInternally(-1, c);
    }

    /**
   * Adds all passed elements. <br />
   * <strong>Note:</strong>
   * <ul>
   * <li>Can also handle additions to {@link List}s in order to not to
   * duplicate code in derived classes.</li>
   * </li>
   * <li>If {@code index >= 0} we harcodedly cast to {@link List}. Reason: An
   * int comparison is about 10 times faster than an "{@code instanceof}". We
   * don't expose this method, it is only used by ourselves.</li>
   * </ul>
   * @param index 
   * @param c 
   * @return true: Added auccessfully.
   */
    protected boolean addAllInternally(int index, Collection<? extends E> c) {
        ensureVOWStillValid();
        checkOwner();
        String collectionProperty = getCollectionProperty();
        Object root = getRoot();
        if (_vow != null) {
            TransitionResult tResultRoot = null;
            if (isTrackRootAsChanged()) {
                tResultRoot = _vow.preparePropertySet(root, collectionProperty, null, false);
                _vow.markChanged(root, collectionProperty);
                if (tResultRoot.isTransitionVetoed()) {
                    return false;
                }
            }
            boolean added = false;
            if (isTrackAssociatedAsChanged()) {
                added = false;
                if (index >= 0) {
                    int currentIndex = index;
                    for (E object : c) {
                        added = added | addInternallyAndHandleInverse(currentIndex, object);
                        currentIndex++;
                    }
                } else {
                    for (E object : c) {
                        added = added | addInternallyAndHandleInverse(object);
                    }
                }
            } else {
                if (index >= 0) {
                    added = ((List<E>) _current).addAll(index, c);
                    trackChange(c, AssociationActionEnum.Insert);
                } else {
                    added = _current.addAll(c);
                    trackChange(c, AssociationActionEnum.Insert);
                }
            }
            if (isTrackRootAsChanged()) {
                _vow.completePropertySet(tResultRoot, false);
            }
            return added;
        } else {
            trackChange(c, AssociationActionEnum.Insert);
            return _current.addAll(c);
        }
    }

    /**
   * @see java.util.Collection#clear()
   */
    public void clear() {
        ensureVOWStillValid();
        checkOwner();
        _current.clear();
    }

    /**
   * @see java.util.Collection#contains(java.lang.Object)
   */
    public boolean contains(Object o) {
        ensureVOWStillValid();
        return _current.contains(o);
    }

    /**
   * @see java.util.Collection#containsAll(java.util.Collection)
   */
    public boolean containsAll(Collection<?> c) {
        ensureVOWStillValid();
        return _current.containsAll(c);
    }

    /**
   * @see java.util.Collection#isEmpty()
   */
    public boolean isEmpty() {
        ensureVOWStillValid();
        return _current.isEmpty();
    }

    /**
   * @see java.util.Collection#iterator()
   */
    public Iterator<E> iterator() {
        ensureVOWStillValid();
        return _current.iterator();
    }

    /**
   * @see java.util.Collection#remove(java.lang.Object)
   */
    public boolean remove(Object o) {
        return removeInternally(-1, o, null);
    }

    /**
   * Adds an element. <br />
   * <strong>Note:</strong> Can also handle removals from {@link List}s in
   * order to not to duplicate code in derived classes. See
   * {@link #removeInternallyAndHandleInverse(int, Object, Iterator, Reference)}.
   * 
   * @param index -
   *          The index, at which the object should be removed.
   * @param o -
   *          The object to remove.
   * @param removedObjectRef -
   *          Out-Param in order to return the removed object in case of a
   *          {@List}. Again: This a little bit dirty approach saves code
   *          duplication in derived classes.
   * @return See {@link Collection#remove(java.lang.Object)}
   */
    @SuppressWarnings("unchecked")
    protected boolean removeInternally(int index, Object o, Reference removedObjectRef) {
        ensureVOWStillValid();
        checkOwner();
        String collectionProperty = getCollectionProperty();
        Object root = getRoot();
        if (_vow != null) {
            TransitionResult tResultRoot = null;
            if (isTrackRootAsChanged()) {
                tResultRoot = _vow.preparePropertySet(root, collectionProperty, null, false);
                _vow.markChanged(root, collectionProperty);
                if (tResultRoot.isTransitionVetoed()) {
                    return false;
                }
            }
            boolean removed = removeInternallyAndHandleInverse(index, (E) o, null, removedObjectRef);
            if (isTrackRootAsChanged()) {
                _vow.completePropertySet(tResultRoot, false);
            }
            return removed;
        } else {
            trackChange((E) o, AssociationActionEnum.Remove);
            return _current.remove(o);
        }
    }

    /**
   * @see java.util.Collection#removeAll(java.util.Collection)
   */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean removeAll(Collection c) {
        ensureVOWStillValid();
        checkOwner();
        String collectionProperty = getCollectionProperty();
        Object root = getRoot();
        if (_vow != null) {
            TransitionResult tResultRoot = null;
            if (isTrackRootAsChanged()) {
                tResultRoot = _vow.preparePropertySet(root, collectionProperty, null, false);
                _vow.markChanged(root, collectionProperty);
                if (tResultRoot.isTransitionVetoed()) {
                    return false;
                }
            }
            boolean removed = false;
            if (isTrackAssociatedAsChanged()) {
                for (Object object : c) {
                    removed = removed | removeInternallyAndHandleInverse(object, null, null);
                }
            } else {
                removed = _current.removeAll(c);
                trackChange(c, AssociationActionEnum.Remove);
            }
            if (isTrackRootAsChanged()) {
                _vow.completePropertySet(tResultRoot, false);
            }
            return removed;
        } else {
            trackChange(c, AssociationActionEnum.Remove);
            return _current.removeAll(c);
        }
    }

    /**
   * @see java.util.Collection#retainAll(java.util.Collection)
   */
    public boolean retainAll(Collection<?> c) {
        ensureVOWStillValid();
        checkOwner();
        String collectionProperty = getCollectionProperty();
        Object root = getRoot();
        if (_vow != null) {
            TransitionResult tResultRoot = null;
            if (isTrackRootAsChanged()) {
                tResultRoot = _vow.preparePropertySet(root, collectionProperty, null, false);
                _vow.markChanged(root, collectionProperty);
                if (tResultRoot.isTransitionVetoed()) {
                    return false;
                }
            }
            boolean modified = false;
            if (isTrackAssociatedAsChanged()) {
                Iterator<E> it = iterator();
                while (it.hasNext()) {
                    Object toRemove = it.next();
                    if (!c.contains(toRemove)) {
                        modified = modified | removeInternallyAndHandleInverse(toRemove, it, null);
                    }
                }
            } else {
                Iterator<E> e = iterator();
                while (e.hasNext()) {
                    E toRemove = e.next();
                    if (!c.contains(toRemove)) {
                        e.remove();
                        modified = true;
                        trackChange(toRemove, AssociationActionEnum.Remove);
                    }
                }
            }
            if (isTrackRootAsChanged()) {
                _vow.completePropertySet(tResultRoot, false);
            }
            return modified;
        } else {
            boolean modified = false;
            Iterator<E> e = iterator();
            while (e.hasNext()) {
                E toRemove = e.next();
                if (!c.contains(toRemove)) {
                    modified = modified | _current.remove(toRemove);
                    trackChange(toRemove, AssociationActionEnum.Remove);
                }
            }
            return modified;
        }
    }

    /**
   * @see java.util.Collection#size()
   */
    public int size() {
        ensureVOWStillValid();
        return _current.size();
    }

    /**
   * @see java.util.Collection#toArray()
   */
    public Object[] toArray() {
        ensureVOWStillValid();
        return _current.toArray();
    }

    /**
   * @see java.util.Collection#toArray(Object[])
   */
    public <ET> ET[] toArray(ET[] a) {
        ensureVOWStillValid();
        return _current.toArray(a);
    }

    protected void notifyUOWBeforeAdd(Object toAdd) {
    }

    /**
   * Tracks a change of a single object if {@link #isDetailedChangeLog()} ==
   * true
   * 
   * @param object -
   *          The object which is added or removed.
   * @param actionType -
   *          The action type to track.
   */
    protected void trackChange(E object, AssociationActionEnum actionType) {
        if (isDetailedChangeLog()) {
            _log.trace("trackChange", new Object[] { "Tracking change collection: ", actionType });
            _changeLog.add(new CollectionOperation<E>(actionType, object));
        } else {
            _log.trace("trackChange", new Object[] { "Not tracking change because isDetailedChangeLog() == false: ", actionType });
        }
    }

    /**
   * Tracks a replacement of a single object if {@link #isDetailedChangeLog()} ==
   * true.
   * 
   * @param previousObject -
   *          The object which is replaced.
   * @param newObject -
   *          The object which replaces the "previousObject".
   * @param index -
   *          The index of the "previousObject". Can be
   *          {@link CollectionOperation#NO_INDEX} if there is no index.
   */
    protected void trackReplace(E previousObject, E newObject, int index) {
        if (isDetailedChangeLog()) {
            _log.trace("trackReplace", new Object[] { "Tracking change collection: ", AssociationActionEnum.Replace });
            _changeLog.add(new CollectionOperation<E>(AssociationActionEnum.Replace, previousObject, newObject, index));
        } else {
            _log.trace("trackReplace", new Object[] { "Not tracking change because isDetailedChangeLog() == false: ", AssociationActionEnum.Replace });
        }
    }

    /**
   * Tracks a change of a collection.
   * 
   * @param collection -
   *          The collection which is added or removed.
   * @param actionType -
   *          The action type to track.
   */
    protected void trackChange(Collection<? extends E> collection, AssociationActionEnum actionType) {
        if (isDetailedChangeLog()) {
            _log.trace("trackChange", new Object[] { "Tracking collection change: ", actionType });
            _changeLog.add(new CollectionOperation<Collection<? extends E>>(actionType, collection));
        } else {
            _log.trace("trackChange", new Object[] { "Not tracking collection change because isDetailedChangeLog() == false: ", actionType });
        }
    }

    /**
   * @param e 
   * @return true: Added successfully.
   * @see #addInternallyAndHandleInverse(int, Object)
   */
    protected boolean addInternallyAndHandleInverse(E e) {
        return addInternallyAndHandleInverse(-1, e);
    }

    /**
   * Adds the passed element to the collection. Registers it with the
   * {@link VOW} if {@link #isTrackAssociatedAsChanged()} is true. Tracks the
   * addition in the changed log. <br />
   * <strong>Note:</strong>
   * <ul>
   * <li>Can also handle additions to {@link List}s in order to not to
   * duplicate code in derived classes.</li>
   * </li>
   * <li>If {@code index >= 0} we harcodedly cast to {@link List}. Reason: An
   * int comparison is about 10 times faster than an "{@code instanceof}". We
   * don't expose this method, it is only used by ourselves.</li>
   * </ul>
   * 
   * @param e -
   *          The element to add.
   * @param index -
   *          The index. Used for {@link List}s.
   * @return true if the collection has changed as a result of the call.
   */
    protected boolean addInternallyAndHandleInverse(int index, E e) {
        ensureVOWStillValid();
        checkOwner();
        if (_vow != null) {
            TransitionResult tResultAssociated = null;
            if (isTrackAssociatedAsChanged()) {
                tResultAssociated = _vow.preparePropertySet(e, null, null, true);
                if (tResultAssociated.isTransitionVetoed()) {
                    return false;
                }
                handleInverseAdd(tResultAssociated.getTBean());
            }
            boolean added = false;
            if (index >= 0) {
                ((List<E>) _current).add(index, e);
                added = true;
            } else {
                added = _current.add(e);
            }
            trackChange(e, AssociationActionEnum.Insert);
            if (isTrackAssociatedAsChanged()) {
                _vow.completePropertySet(tResultAssociated, true);
            }
            return added;
        } else {
            return _current.add(e);
        }
    }

    /**
   * @param e 
   * @param it 
   * @param removedObjectRef 
   * @return ture: Removed successfully.
   * @see #removeInternallyAndHandleInverse(int, Object, Iterator, Reference)
   *      with first param == -1.
   */
    protected boolean removeInternallyAndHandleInverse(Object e, Iterator<E> it, Reference removedObjectRef) {
        return removeInternallyAndHandleInverse(-1, e, it, removedObjectRef);
    }

    /**
   * Removes the passed element from the collection. Registers it with the
   * {@link VOW} if {@link #isTrackAssociatedAsChanged()} is true. Tracks the
   * removal in the changed log. <br />
   * <strong>Note:</strong>
   * <ul>
   * <li>Can also handle removals from {@link List}s in order to not to
   * duplicate code in derived classes.</li>
   * <li>If {@code index >= 0}, the passed "e" must be null.</li>
   * <li>If {@code index >= 0} we harcodedly cast to {@link List}. Reason: An
   * int comparison is about 10 times faster than an "{@code instanceof}". We
   * don't expose this method, it is only used by ourselves.</li>
   * <li> Throws {@link VOWException} with
   * {@link VOWErrors#Collection_RemovingFromListDoesNotAllowObjectParam} if ({@code index >= 0}
   * and {@code e != null}) Reason: The {@link List#remove(int)} has only an
   * int parameter.</li>
   * </ul>
   * 
   * 
   * @param e -
   *          The element to remove.
   * @param it -
   *          If not null, {@link Iterator#remove()}} will be called. You must
   *          not specify both: it and index.
   * @param index -
   *          The index. Used for {@link List}s. You must not specify both: it
   *          and index.
   * @param removedObjectRef 
   * 
   * @return true if the collection has changed as a result of the call.
   * @throws VOWException
   *           with {@link VOWErrors#Collection_DontSpecifyIndexAndIterator} if
   *           index and iterator are specified.
   */
    protected boolean removeInternallyAndHandleInverse(int index, Object e, Iterator<E> it, Reference removedObjectRef) {
        ensureVOWStillValid();
        checkOwner();
        if (index >= 0 && e != null) {
            throw new VOWException(VOWErrors.Collection_RemovingFromListDoesNotAllowObjectParam);
        }
        if (index >= 0 && it != null) {
            throw new VOWException(VOWErrors.Collection_DontSpecifyIndexAndIterator);
        }
        if (it != null && e == null) {
            throw new VOWException(VOWErrors.Collection_NeedObjectIfIteratorSpecified);
        }
        if (_vow != null) {
            TransitionResult tResultAssociated = null;
            if (isTrackAssociatedAsChanged()) {
                if (index >= 0) {
                    e = ((List<?>) _current).get(index);
                }
                tResultAssociated = _vow.preparePropertySet(e, null, null, true);
                if (tResultAssociated.isTransitionVetoed()) {
                    return false;
                }
                handleInverseRemove(tResultAssociated.getTBean());
            }
            boolean removed = false;
            Object removedObject = e;
            if (index >= 0) {
                removedObject = ((List<?>) _current).remove(index);
                removed = removedObject != null;
                if (removedObjectRef != null) {
                    removedObjectRef.setReferenced(removedObject);
                }
            } else {
                if (it != null) {
                    it.remove();
                } else {
                    removed = _current.remove(e);
                }
            }
            E removedObjectCasted = Dtu.cast(removedObject);
            trackChange(removedObjectCasted, AssociationActionEnum.Remove);
            if (isTrackAssociatedAsChanged()) {
                _vow.completePropertySet(tResultAssociated, true);
            }
            return removed;
        } else {
            return _current.remove(e);
        }
    }

    /**
   * Handle the added object on the "inverse" side.
   * 
   * @param tBean
   */
    protected void handleInverseAdd(ITransactionalBean<?> tBean) {
        IInverseCollections inverse = tBean.getInverse();
        if (inverse != null) {
            inverse.registerAdd(getRoot(), getCollectionProperty(), _wrapped);
        }
    }

    /**
   * Handle the removed object on the "inverse" side.
   * 
   * @param tBean
   */
    protected void handleInverseRemove(ITransactionalBean<?> tBean) {
        IInverseCollections inverse = tBean.getInverse();
        if (inverse != null) {
            inverse.registerRemove(getRoot(), getCollectionProperty(), _wrapped);
        }
    }

    protected void checkOwner() {
        if (_vow != null) {
            _vow.checkOwner();
        }
    }

    /**
   * Throws exception if {@link #isVowNoLongerValid()} == true;
   */
    protected void ensureVOWStillValid() {
        if (isVowNoLongerValid()) {
            throw new VOWNoLongerValidException(getVow());
        }
    }

    /**
   * @return the changeLog
   */
    protected final List<CollectionOperation<?>> getChangeLog() {
        return _changeLog;
    }

    /**
   * @param trackAssociatedAsChanged
   *          the trackAssociatedAsChanged to set
   */
    protected final void setTrackAssociatedAsChanged(boolean trackAssociatedAsChanged) {
        _trackAssociatedAsChanged = trackAssociatedAsChanged;
    }

    /**
   * @param trackRootAsChanged
   *          the trackRootAsChanged to set
   */
    protected final void setTrackRootAsChanged(boolean trackRootAsChanged) {
        _trackRootAsChanged = trackRootAsChanged;
    }

    /**
   * @return the uow
   */
    public final IVOW getVow() {
        return _vow;
    }

    /**
   * @param uow
   *          the uow to set
   */
    protected final void setVow(IVOW uow) {
        uow.registerCollection(getWrapped(), this);
        _vow = uow;
    }

    /**
   * @return the wrapped
   */
    protected final T getWrapped() {
        return _wrapped;
    }

    /**
   * Only for debugging!
   * @return The wrapped collection.
   */
    public final T getWrappedDebug() {
        return getWrapped();
    }

    /**
   * @param wrapped
   *          the wrapped collection to set
   */
    private final void setWrapped(T wrapped) {
        _wrapped = wrapped;
    }

    /**
   * @param current
   *          the current collection to set
   */
    private final void setCurrent(T current) {
        _current = current;
    }

    /**
   * @return the root
   */
    public final Object getRoot() {
        return _root;
    }

    /**
   * @param root
   *          the root to set
   */
    protected final void setRoot(Object root) {
        _root = root;
    }

    /**
   * @return the collectionProperty
   */
    public final String getCollectionProperty() {
        return _collectionProperty;
    }

    /**
   * @param collectionProperty
   *          the collectionProperty to set
   */
    protected final void setCollectionProperty(String collectionProperty) {
        this._collectionProperty = collectionProperty;
    }

    /**
   * @return true: Track each operation and provide the changes in
   *         {@link #getChangeLogIterator()}.
   */
    public final boolean isDetailedChangeLog() {
        return _detailedChangeLog;
    }

    /**
   * @see #isDetailedChangeLog()
   * @param detailedChangeLog -
   *          true: All changes are logged and can be retrieved via
   *          {@link #getChangeLogIterator()}.
   */
    public final void setDetailedChangeLog(boolean detailedChangeLog) {
        _detailedChangeLog = detailedChangeLog;
    }

    /**
   * @return true: Our {@link IVOW} is no longer valid. See
   *         {@link #informVOWNoLongerValid()}.
   */
    public final boolean isVowNoLongerValid() {
        return _vowNoLongerValid;
    }

    /**
   * @see #informVOWNoLongerValid()
   * @param vowNoLongerValid
   */
    private final void setVowNoLongerValid(boolean vowNoLongerValid) {
        _vowNoLongerValid = vowNoLongerValid;
    }
}

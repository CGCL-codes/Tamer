package org.datanucleus.sco.backed;

import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.ObjectManager;
import org.datanucleus.StateManager;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.FieldPersistenceModifier;
import org.datanucleus.sco.NullsNotAllowedException;
import org.datanucleus.sco.SCOListIterator;
import org.datanucleus.sco.SCOUtils;
import org.datanucleus.sco.queued.AddAtOperation;
import org.datanucleus.sco.queued.AddOperation;
import org.datanucleus.sco.queued.ClearOperation;
import org.datanucleus.sco.queued.QueuedOperation;
import org.datanucleus.sco.queued.RemoveAtOperation;
import org.datanucleus.sco.queued.RemoveOperation;
import org.datanucleus.sco.queued.SetOperation;
import org.datanucleus.state.StateManagerFactory;
import org.datanucleus.store.scostore.ListStore;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.StringUtils;

/**
 * A mutable second-class LinkedList object.
 * This class extends LinkedList, using that class to contain the current objects, and the backing ListStore 
 * to be the interface to the datastore. A "backing store" is not present for datastores that dont use
 * DatastoreClass, or if the container is serialised or non-persistent.
 * 
 * <H3>Modes of Operation</H3>
 * The user can operate the list in 2 modes.
 * The <B>cached</B> mode will use an internal cache of the elements (in the "delegate") reading them at
 * the first opportunity and then using the cache thereafter.
 * The <B>non-cached</B> mode will just go direct to the "backing store" each call.
 *
 * <H3>Mutators</H3>
 * When the "backing store" is present any updates are passed direct to the datastore as well as to the "delegate".
 * If the "backing store" isn't present the changes are made to the "delegate" only.
 *
 * <H3>Accessors</H3>
 * When any accessor method is invoked, it typically checks whether the container has been loaded from its
 * "backing store" (where present) and does this as necessary. Some methods (<B>size()</B>) just check if 
 * everything is loaded and use the delegate if possible, otherwise going direct to the datastore.
 */
public class LinkedList extends org.datanucleus.sco.simple.LinkedList {

    protected transient ListStore backingStore;

    protected transient boolean allowNulls = false;

    protected transient boolean useCache = true;

    protected transient boolean isCacheLoaded = false;

    protected transient boolean queued = false;

    protected transient java.util.ArrayList queuedOperations = null;

    /**
     * Constructor, using the StateManager of the "owner" and the field name.
     * @param ownerSM The owner StateManager
     * @param fieldName The name of the field of the SCO.
     */
    public LinkedList(StateManager ownerSM, String fieldName) {
        super(ownerSM, fieldName);
        this.delegate = new java.util.LinkedList();
        if (ownerSM != null) {
            AbstractMemberMetaData fmd = ownerSM.getClassMetaData().getMetaDataForMember(fieldName);
            this.fieldNumber = fmd.getAbsoluteFieldNumber();
            allowNulls = SCOUtils.allowNullsInContainer(allowNulls, fmd);
            queued = SCOUtils.useContainerQueueing(ownerSM);
            useCache = SCOUtils.useContainerCache(ownerSM, fieldName);
            if (!SCOUtils.collectionHasSerialisedElements(fmd) && fmd.getPersistenceModifier() == FieldPersistenceModifier.PERSISTENT) {
                ClassLoaderResolver clr = ownerSM.getObjectManager().getClassLoaderResolver();
                this.backingStore = (ListStore) ownerSM.getStoreManager().getBackingStoreForField(clr, fmd, java.util.LinkedList.class);
            }
            if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
                NucleusLogger.PERSISTENCE.debug(SCOUtils.getContainerInfoMessage(ownerSM, fieldName, this, useCache, queued, allowNulls, SCOUtils.useCachedLazyLoading(ownerSM, fieldName)));
            }
        }
    }

    /**
     * Method to initialise the SCO from an existing value.
     * @param o The object to set from
     * @param forInsert Whether the object needs inserting in the datastore with this value
     * @param forUpdate Whether to update the datastore with this value
     */
    public void initialise(Object o, boolean forInsert, boolean forUpdate) {
        Collection c = (Collection) o;
        if (c != null) {
            AbstractMemberMetaData fmd = ownerSM.getClassMetaData().getMetaDataForMember(fieldName);
            ObjectManager om = ownerSM.getObjectManager();
            if (SCOUtils.collectionHasSerialisedElements(fmd) && fmd.getCollection().elementIsPersistent()) {
                Iterator iter = c.iterator();
                while (iter.hasNext()) {
                    Object pc = iter.next();
                    StateManager objSM = om.findStateManager(pc);
                    if (objSM == null) {
                        objSM = StateManagerFactory.newStateManagerForEmbedded(om, pc, false);
                        objSM.addEmbeddedOwner(ownerSM, fieldNumber);
                    }
                }
            }
            if (backingStore != null && useCache && !isCacheLoaded) {
                isCacheLoaded = true;
            }
            if (forInsert) {
                if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
                    NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("023007", StringUtils.toJVMIDString(ownerSM.getObject()), fieldName, "" + c.size()));
                }
                addAll(c);
            } else if (forUpdate) {
                if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
                    NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("023008", StringUtils.toJVMIDString(ownerSM.getObject()), fieldName, "" + c.size()));
                }
                clear();
                addAll(c);
            } else {
                if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
                    NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("023007", StringUtils.toJVMIDString(ownerSM.getObject()), fieldName, "" + c.size()));
                }
                delegate.clear();
                delegate.addAll(c);
            }
        }
    }

    /**
     * Method to initialise the SCO for use.
     */
    public void initialise() {
        if (useCache && !SCOUtils.useCachedLazyLoading(ownerSM, fieldName)) {
            loadFromStore();
        }
    }

    /**
     * Accessor for the unwrapped value that we are wrapping.
     * @return The unwrapped value
     */
    public Object getValue() {
        loadFromStore();
        return super.getValue();
    }

    /**
     * Method to effect the load of the data in the SCO.
     * Used when the SCO supports lazy-loading to tell it to load all now.
     */
    public void load() {
        if (useCache) {
            loadFromStore();
        }
    }

    /**
     * Method to return if the SCO has its contents loaded.
     * If the SCO doesn't support lazy loading will just return true.
     * @return Whether it is loaded
     */
    public boolean isLoaded() {
        return useCache ? isCacheLoaded : false;
    }

    /**
     * Method to load all elements from the "backing store" where appropriate.
     */
    protected void loadFromStore() {
        if (backingStore != null && !isCacheLoaded) {
            if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
                NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("023006", StringUtils.toJVMIDString(ownerSM.getObject()), fieldName));
            }
            delegate.clear();
            Iterator iter = backingStore.iterator(ownerSM);
            while (iter.hasNext()) {
                delegate.add(iter.next());
            }
            isCacheLoaded = true;
        }
    }

    /**
     * Method to flush the changes to the datastore when operating in queued mode.
     * Does nothing in "direct" mode.
     */
    public void flush() {
        if (queued) {
            if (queuedOperations != null) {
                if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
                    NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("023005", StringUtils.toJVMIDString(ownerSM.getObject()), fieldName));
                }
                Iterator iter = queuedOperations.iterator();
                while (iter.hasNext()) {
                    QueuedOperation op = (QueuedOperation) iter.next();
                    op.perform(backingStore, ownerSM);
                }
                queuedOperations.clear();
                queuedOperations = null;
            }
        }
    }

    /**
     * Convenience method to add a queued operation to the operations we perform at commit.
     * @param op The operation
     */
    protected void addQueuedOperation(QueuedOperation op) {
        if (queuedOperations == null) {
            queuedOperations = new java.util.ArrayList();
        }
        queuedOperations.add(op);
    }

    /**
     * Method to update an embedded element in this collection.
     * @param element The element
     * @param fieldNumber Number of field in the element
     * @param value New value for this field
     */
    public void updateEmbeddedElement(Object element, int fieldNumber, Object value) {
        if (backingStore != null) {
            backingStore.updateEmbeddedElement(ownerSM, element, fieldNumber, value);
        }
    }

    /**
     * Method to unset the owner and field information.
     */
    public synchronized void unsetOwner() {
        super.unsetOwner();
        if (backingStore != null) {
            backingStore = null;
        }
    }

    /**
     * Clone operator to return a copy of this object.
     * <p>
     * Mutable second-class Objects are required to provide a public
     * clone method in order to allow for copying PersistenceCapable
     * objects. In contrast to Object.clone(), this method must not throw a
     * CloneNotSupportedException.
     * </p>
     *
     * @return The cloned object
     */
    public Object clone() {
        if (useCache) {
            loadFromStore();
        }
        return delegate.clone();
    }

    /**
     * Method to return if the list contains this element.
     * @param element The element
     * @return Whether it is contained
     **/
    public boolean contains(Object element) {
        if (useCache && isCacheLoaded) {
            return delegate.contains(element);
        } else if (backingStore != null) {
            return backingStore.contains(ownerSM, element);
        }
        return delegate.contains(element);
    }

    /**
     * Accessor for whether a collection of elements are contained here.
     * @param c The collection of elements.
     * @return Whether they are contained.
     **/
    public synchronized boolean containsAll(java.util.Collection c) {
        if (useCache) {
            loadFromStore();
        } else if (backingStore != null) {
            java.util.HashSet h = new java.util.HashSet(c);
            Iterator iter = iterator();
            while (iter.hasNext()) {
                h.remove(iter.next());
            }
            return h.isEmpty();
        }
        return delegate.containsAll(c);
    }

    /**
     * Equality operator.
     * @param o The object to compare against.
     * @return Whether this object is the same.
     **/
    public synchronized boolean equals(Object o) {
        if (useCache) {
            loadFromStore();
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof java.util.List)) {
            return false;
        }
        java.util.List l = (java.util.List) o;
        if (l.size() != size()) {
            return false;
        }
        Object[] elements = toArray();
        Object[] otherElements = l.toArray();
        for (int i = 0; i < elements.length; i++) {
            if (!elements[i].equals(otherElements[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to retrieve an element no.
     * @param index The item to retrieve
     * @return The element at that position.
     **/
    public Object get(int index) {
        if (useCache) {
            loadFromStore();
        } else if (backingStore != null) {
            return backingStore.get(ownerSM, index);
        }
        return delegate.get(index);
    }

    /**
     * Method to retrieve the first element.
     * @return The first element
     **/
    public Object getFirst() {
        return get(0);
    }

    /**
     * Method to retrieve the last element.
     * @return The last element
     **/
    public Object getLast() {
        return get(size() - 1);
    }

    /**
     * Hashcode operator.
     * @return The Hash code.
     **/
    public synchronized int hashCode() {
        if (useCache) {
            loadFromStore();
        }
        return delegate.hashCode();
    }

    /**
     * Method to the position of an element.
     * @param element The element.
     * @return The position.
     **/
    public int indexOf(Object element) {
        if (useCache) {
            loadFromStore();
        } else if (backingStore != null) {
            return backingStore.indexOf(ownerSM, element);
        }
        return delegate.indexOf(element);
    }

    /**
     * Accessor for whether the LinkedList is empty.
     * @return Whether it is empty.
     **/
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Method to retrieve an iterator for the list.
     * @return The iterator
     **/
    public Iterator iterator() {
        if (useCache) {
            loadFromStore();
        }
        return new SCOListIterator(this, ownerSM, delegate, backingStore, useCache, -1);
    }

    /**
     * Method to retrieve a List iterator for the list from the index.
     * @param index The start point 
     * @return The iterator
     **/
    public ListIterator listIterator(int index) {
        if (useCache) {
            loadFromStore();
        }
        return new SCOListIterator(this, ownerSM, delegate, backingStore, useCache, index);
    }

    /**
     * Method to retrieve the last position of the element.
     * @param element The element
     * @return The last position of this element in the List.
     **/
    public int lastIndexOf(Object element) {
        if (useCache) {
            loadFromStore();
        } else if (backingStore != null) {
            return backingStore.lastIndexOf(ownerSM, element);
        }
        return delegate.lastIndexOf(element);
    }

    /**
     * Accessor for the size of the LinkedList.
     * @return The size.
     **/
    public int size() {
        if (useCache && isCacheLoaded) {
            return delegate.size();
        } else if (backingStore != null) {
            return backingStore.size(ownerSM);
        }
        return delegate.size();
    }

    /**
     * Accessor for the subList of elements between from and to of the List
     * @param from Start index (inclusive)
     * @param to End index (exclusive) 
     * @return The subList
     **/
    public synchronized java.util.List subList(int from, int to) {
        if (useCache) {
            loadFromStore();
        } else if (backingStore != null) {
            return backingStore.subList(ownerSM, from, to);
        }
        return delegate.subList(from, to);
    }

    /**
     * Method to return the list as an array.
     * @return The array
     **/
    public synchronized Object[] toArray() {
        if (useCache) {
            loadFromStore();
        } else if (backingStore != null) {
            return SCOUtils.toArray(backingStore, ownerSM);
        }
        return delegate.toArray();
    }

    /**
     * Method to return the list as an array.
     * @param a The runtime types of the array being defined by this param
     * @return The array
     **/
    public synchronized Object[] toArray(Object a[]) {
        if (useCache) {
            loadFromStore();
        } else if (backingStore != null) {
            return SCOUtils.toArray(backingStore, ownerSM, a);
        }
        return delegate.toArray(a);
    }

    /**
     * Method to add an element to a position in the LinkedList.
     * @param index The position
     * @param element The new element
     **/
    public void add(int index, Object element) {
        if (element == null && !allowNulls) {
            throw new NullsNotAllowedException(ownerSM, fieldName);
        }
        if (useCache) {
            loadFromStore();
        }
        if (backingStore != null) {
            if (SCOUtils.useQueuedUpdate(queued, ownerSM)) {
                addQueuedOperation(new AddAtOperation(index, element));
            } else {
                try {
                    backingStore.add(ownerSM, element, index, (useCache ? delegate.size() : -1));
                } catch (NucleusDataStoreException dse) {
                    NucleusLogger.PERSISTENCE.warn(LOCALISER.msg("023013", "add", fieldName, dse));
                }
            }
        }
        makeDirty();
        delegate.add(index, element);
    }

    /**
     * Method to add an element to the LinkedList.
     * @param element The new element
     * @return Whether it was added ok.
     **/
    public boolean add(Object element) {
        if (element == null && !allowNulls) {
            throw new NullsNotAllowedException(ownerSM, fieldName);
        }
        if (useCache) {
            loadFromStore();
        }
        boolean backingSuccess = true;
        if (backingStore != null) {
            if (SCOUtils.useQueuedUpdate(queued, ownerSM)) {
                addQueuedOperation(new AddOperation(element));
            } else {
                try {
                    backingSuccess = backingStore.add(ownerSM, element, (useCache ? delegate.size() : -1));
                } catch (NucleusDataStoreException dse) {
                    NucleusLogger.PERSISTENCE.warn(LOCALISER.msg("023013", "add", fieldName, dse));
                    backingSuccess = false;
                }
            }
        }
        makeDirty();
        boolean delegateSuccess = delegate.add(element);
        return (backingStore != null ? backingSuccess : delegateSuccess);
    }

    /**
     * Method to add a Collection to the LinkedList.
     * @param elements The collection
     * @return Whether it was added ok.
     **/
    public boolean addAll(Collection elements) {
        if (useCache) {
            loadFromStore();
        }
        boolean backingSuccess = true;
        if (backingStore != null) {
            if (SCOUtils.useQueuedUpdate(queued, ownerSM)) {
                Iterator iter = queuedOperations.iterator();
                while (iter.hasNext()) {
                    addQueuedOperation(new AddOperation(iter.next()));
                }
            } else {
                try {
                    backingSuccess = backingStore.addAll(ownerSM, elements, (useCache ? delegate.size() : -1));
                } catch (NucleusDataStoreException dse) {
                    NucleusLogger.PERSISTENCE.warn(LOCALISER.msg("023013", "addAll", fieldName, dse));
                    backingSuccess = false;
                }
            }
        }
        makeDirty();
        boolean delegateSuccess = delegate.addAll(elements);
        return (backingStore != null ? backingSuccess : delegateSuccess);
    }

    /**
     * Method to add a Collection to a position in the LinkedList.
     * @param index Position to insert the collection.
     * @param elements The collection
     * @return Whether it was added ok.
     **/
    public boolean addAll(int index, Collection elements) {
        if (useCache) {
            loadFromStore();
        }
        boolean backingSuccess = true;
        if (backingStore != null) {
            if (SCOUtils.useQueuedUpdate(queued, ownerSM)) {
                Iterator iter = elements.iterator();
                int pos = index;
                while (iter.hasNext()) {
                    addQueuedOperation(new AddAtOperation(pos++, iter.next()));
                }
            } else {
                try {
                    backingSuccess = backingStore.addAll(ownerSM, elements, index, (useCache ? delegate.size() : -1));
                } catch (NucleusDataStoreException dse) {
                    NucleusLogger.PERSISTENCE.warn(LOCALISER.msg("023013", "addAll", fieldName, dse));
                    backingSuccess = false;
                }
            }
        }
        makeDirty();
        boolean delegateSuccess = delegate.addAll(index, elements);
        return (backingStore != null ? backingSuccess : delegateSuccess);
    }

    /**
     * Method to add an element as first in the LinkedList.
     * @param element The new element
     **/
    public void addFirst(Object element) {
        add(0, element);
    }

    /**
     * Method to add an element as last in the LinkedList.
     * @param element The new element
     **/
    public void addLast(Object element) {
        add(size(), element);
    }

    /**
     * Method to clear the LinkedList.
     **/
    public synchronized void clear() {
        makeDirty();
        if (backingStore != null) {
            if (SCOUtils.useQueuedUpdate(queued, ownerSM)) {
                addQueuedOperation(new ClearOperation());
            } else {
                backingStore.clear(ownerSM);
            }
        }
        delegate.clear();
    }

    /**
     * Method to remove an element from the LinkedList.
     * @param index The element position.
     * @return The object that was removed
     **/
    public Object remove(int index) {
        makeDirty();
        if (useCache) {
            loadFromStore();
        }
        int size = (useCache ? delegate.size() : -1);
        Object delegateObject = (useCache ? delegate.remove(index) : null);
        Object backingObject = null;
        if (backingStore != null) {
            if (SCOUtils.useQueuedUpdate(queued, ownerSM)) {
                backingObject = delegateObject;
                addQueuedOperation(new RemoveAtOperation(index));
            } else {
                try {
                    backingObject = backingStore.remove(ownerSM, index, size);
                } catch (NucleusDataStoreException dse) {
                    NucleusLogger.PERSISTENCE.warn(LOCALISER.msg("023013", "remove", fieldName, dse));
                    backingObject = null;
                }
            }
        }
        return (backingStore != null ? backingObject : delegateObject);
    }

    /**
     * Method to remove an element from the LinkedList.
     * @param element The element
     * @return Whether the object was removed ok
     */
    public boolean remove(Object element) {
        return remove(element, true);
    }

    /**
     * Method to remove an element from the collection, and observe the flag for whether to allow cascade delete.
     * @param element The element
     * @param allowCascadeDelete Whether to allow cascade delete
     */
    public boolean remove(Object element, boolean allowCascadeDelete) {
        makeDirty();
        if (useCache) {
            loadFromStore();
        }
        int size = (useCache ? delegate.size() : -1);
        boolean contained = delegate.contains(element);
        boolean delegateSuccess = delegate.remove(element);
        boolean backingSuccess = true;
        if (backingStore != null) {
            if (SCOUtils.useQueuedUpdate(queued, ownerSM)) {
                backingSuccess = contained;
                if (backingSuccess) {
                    addQueuedOperation(new RemoveOperation(element, allowCascadeDelete));
                }
            } else {
                try {
                    backingSuccess = backingStore.remove(ownerSM, element, size, allowCascadeDelete);
                } catch (NucleusDataStoreException dse) {
                    NucleusLogger.PERSISTENCE.warn(LOCALISER.msg("023013", "remove", fieldName, dse));
                    backingSuccess = false;
                }
            }
        }
        return (backingStore != null ? backingSuccess : delegateSuccess);
    }

    /**
     * Method to remove a Collection from the LinkedList.
     * @param elements The collection
     * @return Whether it was removed ok.
     **/
    public boolean removeAll(Collection elements) {
        makeDirty();
        if (useCache) {
            loadFromStore();
        }
        if (backingStore != null) {
            boolean backingSuccess = true;
            int size = (useCache ? delegate.size() : -1);
            if (SCOUtils.useQueuedUpdate(queued, ownerSM)) {
                backingSuccess = false;
                Iterator iter = elements.iterator();
                while (iter.hasNext()) {
                    Object element = iter.next();
                    if (contains(element)) {
                        backingSuccess = true;
                        addQueuedOperation(new RemoveOperation(element));
                    }
                }
            } else {
                try {
                    backingSuccess = backingStore.removeAll(ownerSM, elements, size);
                } catch (NucleusDataStoreException dse) {
                    NucleusLogger.PERSISTENCE.warn(LOCALISER.msg("023013", "removeAll", fieldName, dse));
                    backingSuccess = false;
                }
            }
            delegate.removeAll(elements);
            return backingSuccess;
        } else {
            return delegate.removeAll(elements);
        }
    }

    /**
     * Method to remove the first element from the LinkedList.
     * @return The object that was removed
     **/
    public Object removeFirst() {
        return remove(0);
    }

    /**
     * Method to remove the last element from the LinkedList.
     * @return The object that was removed
     **/
    public Object removeLast() {
        return remove(size() - 1);
    }

    /**
     * Method to retain a Collection of elements (and remove all others).
     * @param c The collection to retain
     * @return Whether they were retained successfully.
     **/
    public synchronized boolean retainAll(java.util.Collection c) {
        makeDirty();
        if (useCache) {
            loadFromStore();
        }
        boolean modified = false;
        Iterator iter = iterator();
        while (iter.hasNext()) {
            Object element = iter.next();
            if (!c.contains(element)) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * JPOX wrapper addition that allows turning off of the dependent-field checks
     * when doing the position setting. This means that we can prevent the deletion of
     * the object that was previously in that position. This particular feature is used
     * when attaching a list field and where some elements have changed positions.
     * @param index The position
     * @param element The new element
     * @return The element previously at that position
     */
    public Object set(int index, Object element, boolean allowDependentField) {
        if (element == null && !allowNulls) {
            throw new NullsNotAllowedException(ownerSM, fieldName);
        }
        makeDirty();
        if (useCache) {
            loadFromStore();
        }
        Object delegateReturn = delegate.set(index, element);
        if (backingStore != null) {
            if (SCOUtils.useQueuedUpdate(queued, ownerSM)) {
                addQueuedOperation(new SetOperation(index, element, allowDependentField));
            } else {
                backingStore.set(ownerSM, index, element, allowDependentField);
            }
        }
        return delegateReturn;
    }

    /**
     * Method to set the element at a position in the LinkedList.
     * @param index The position
     * @param element The new element
     * @return The element previously at that position
     **/
    public Object set(int index, Object element) {
        return set(index, element, true);
    }

    /**
     * The writeReplace method is called when ObjectOutputStream is preparing
     * to write the object to the stream. The ObjectOutputStream checks whether
     * the class defines the writeReplace method. If the method is defined, the
     * writeReplace method is called to allow the object to designate its 
     * replacement in the stream. The object returned should be either of the
     * same type as the object passed in or an object that when read and
     * resolved will result in an object of a type that is compatible with
     * all references to the object.
     *
     * @return the replaced object
     * @throws ObjectStreamException
     */
    protected Object writeReplace() throws ObjectStreamException {
        if (useCache) {
            loadFromStore();
            return new java.util.LinkedList(delegate);
        } else {
            return new java.util.LinkedList(delegate);
        }
    }
}

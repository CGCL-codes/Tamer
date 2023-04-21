package org.datanucleus.store.types.sco.simple;

import java.io.ObjectStreamException;
import java.util.AbstractQueue;
import java.util.Comparator;
import java.util.Iterator;
import javax.jdo.spi.PersistenceCapable;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.state.FetchPlanState;
import org.datanucleus.store.ObjectProvider;
import org.datanucleus.store.types.sco.SCOCollection;
import org.datanucleus.store.types.sco.SCOCollectionIterator;
import org.datanucleus.store.types.sco.SCOMtoN;
import org.datanucleus.store.types.sco.SCOUtils;
import org.datanucleus.util.Localiser;
import org.datanucleus.util.NucleusLogger;

/**
 * A mutable second-class Queue object.
 * This is the simplified form that intercepts mutators and marks the field as dirty.
 * It also handles cascade-delete triggering for persistable elements.
 */
public class Queue extends AbstractQueue implements SCOCollection, SCOMtoN, Cloneable, java.io.Serializable {

    protected static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.Localisation", PriorityQueue.class.getClassLoader());

    protected ObjectProvider ownerSM;

    protected String fieldName;

    protected int fieldNumber;

    /** The internal "delegate". */
    protected java.util.Queue delegate;

    /**
     * Constructor. 
     * @param ownerSM The State Manager for this set.
     * @param fieldName Name of the field
     **/
    public Queue(ObjectProvider ownerSM, String fieldName) {
        this.ownerSM = ownerSM;
        this.fieldName = fieldName;
        this.fieldNumber = ownerSM.getClassMetaData().getMetaDataForMember(fieldName).getAbsoluteFieldNumber();
    }

    /**
     * Method to initialise the SCO from an existing value.
     * @param o The object to set from
     * @param forInsert Whether the object needs inserting in the datastore with this value
     * @param forUpdate Whether to update the datastore with this value
     */
    public void initialise(Object o, boolean forInsert, boolean forUpdate) {
        java.util.Collection c = (java.util.Collection) o;
        if (c != null) {
            initialiseDelegate();
            delegate.addAll(c);
        } else {
            initialiseDelegate();
        }
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("023003", ownerSM.toPrintableID(), fieldName, "" + size(), SCOUtils.getSCOWrapperOptionsMessage(true, false, false, false)));
        }
    }

    /**
     * Method to initialise the SCO for use.
     */
    public void initialise() {
        initialiseDelegate();
        if (NucleusLogger.PERSISTENCE.isDebugEnabled()) {
            NucleusLogger.PERSISTENCE.debug(LOCALISER.msg("023003", ownerSM.toPrintableID(), fieldName, "" + size(), SCOUtils.getSCOWrapperOptionsMessage(true, false, false, false)));
        }
    }

    /**
     * Convenience method to set up the delegate respecting any comparator specified in MetaData.
     */
    protected void initialiseDelegate() {
        AbstractMemberMetaData fmd = ownerSM.getClassMetaData().getMetaDataForMember(fieldName);
        Comparator comparator = SCOUtils.getComparator(fmd, ownerSM.getExecutionContext().getClassLoaderResolver());
        if (comparator != null) {
            this.delegate = new java.util.PriorityQueue(5, comparator);
        } else {
            this.delegate = new java.util.PriorityQueue();
        }
    }

    /**
     * Accessor for the unwrapped value that we are wrapping.
     * @return The unwrapped value
     */
    public Object getValue() {
        return delegate;
    }

    /**
     * Method to effect the load of the data in the SCO.
     * Used when the SCO supports lazy-loading to tell it to load all now.
     */
    public void load() {
    }

    /**
     * Method to return if the SCO has its contents loaded. Returns true.
     * @return Whether it is loaded
     */
    public boolean isLoaded() {
        return true;
    }

    /**
     * Method to flush the changes to the datastore when operating in queued mode.
     * Does nothing in "direct" mode.
     */
    public void flush() {
    }

    /**
     * Method to update an embedded element in this collection.
     * @param element The element
     * @param fieldNumber Number of field in the element
     * @param value New value for this field
     */
    public void updateEmbeddedElement(Object element, int fieldNumber, Object value) {
        makeDirty();
    }

    /**
     * Accessor for the field name.
     * @return The field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Accessor for the owner object.
     * @return The owner object
     */
    public Object getOwner() {
        return (ownerSM != null ? ownerSM.getObject() : null);
    }

    /**
     * Method to unset the owner and field information.
     */
    public synchronized void unsetOwner() {
        if (ownerSM != null) {
            ownerSM = null;
        }
    }

    /**
     * Utility to mark the object as dirty
     */
    public void makeDirty() {
        if (ownerSM != null) {
            ((PersistenceCapable) ownerSM.getObject()).jdoMakeDirty(fieldName);
        }
    }

    /**
     * Method to return a detached copy of the container.
     * Recurses through the elements so that they are likewise detached.
     * @param state State for detachment process
     * @return The detached container
     */
    public Object detachCopy(FetchPlanState state) {
        java.util.Collection detached = new java.util.PriorityQueue();
        SCOUtils.detachCopyForCollection(ownerSM, toArray(), state, detached);
        return detached;
    }

    /**
     * Method to return an attached copy of the passed (detached) value. The returned attached copy
     * is a SCO wrapper. Goes through the existing elements in the store for this owner field and
     * removes ones no longer present, and adds new elements. All elements in the (detached)
     * value are attached.
     * @param value The new (collection) value
     */
    public void attachCopy(Object value) {
        java.util.Collection c = (java.util.Collection) value;
        AbstractMemberMetaData fmd = ownerSM.getClassMetaData().getMetaDataForMember(fieldName);
        boolean elementsWithoutIdentity = SCOUtils.collectionHasElementsWithoutIdentity(fmd);
        java.util.Collection attachedElements = new java.util.HashSet(c.size());
        SCOUtils.attachRemoveDeletedElements(ownerSM.getExecutionContext().getApiAdapter(), this, c, elementsWithoutIdentity);
        SCOUtils.attachCopyForCollection(ownerSM, c.toArray(), attachedElements, elementsWithoutIdentity);
        SCOUtils.attachAddNewElements(ownerSM.getExecutionContext().getApiAdapter(), this, attachedElements, elementsWithoutIdentity);
    }

    /**
     * Creates and returns a copy of this object.
     * <P>
     * Mutable second-class Objects are required to provide a public
     * clone method in order to allow for copying PersistenceCapable
     * objects. In contrast to Object.clone(), this method must not throw a
     * CloneNotSupportedException.
     * @return A clone of the object
     */
    public Object clone() {
        return null;
    }

    /**
     * Accessor for whether an element is contained in the Collection.
     * @param element The element
     * @return Whether the element is contained here
     **/
    public synchronized boolean contains(Object element) {
        return delegate.contains(element);
    }

    /**
     * Accessor for whether a collection of elements are contained here.
     * @param c The collection of elements.
     * @return Whether they are contained.
     **/
    public synchronized boolean containsAll(java.util.Collection c) {
        return delegate.containsAll(c);
    }

    /**
     * Equality operator.
     * @param o The object to compare against.
     * @return Whether this object is the same.
     **/
    public synchronized boolean equals(Object o) {
        return delegate.equals(o);
    }

    /**
     * Hashcode operator.
     * @return The Hash code.
     **/
    public synchronized int hashCode() {
        return delegate.hashCode();
    }

    /**
     * Accessor for whether the Collection is empty.
     * @return Whether it is empty.
     **/
    public synchronized boolean isEmpty() {
        return delegate.isEmpty();
    }

    /**
     * Accessor for an iterator for the Collection.
     * @return The iterator
     **/
    public synchronized Iterator iterator() {
        return new SCOCollectionIterator(this, ownerSM, delegate, null, true);
    }

    /**
     * Method to peek at the next element in the Queue.
     * @return The element
     **/
    public synchronized Object peek() {
        return delegate.peek();
    }

    /**
     * Accessor for the size of the Collection.
     * @return The size
     **/
    public synchronized int size() {
        return delegate.size();
    }

    /**
     * Method to return the Collection as an array.
     * @return The array
     **/
    public synchronized Object[] toArray() {
        return delegate.toArray();
    }

    /**
     * Method to return the Collection as an array.
     * @param a The array to write the results to
     * @return The array
     **/
    public synchronized Object[] toArray(Object a[]) {
        return delegate.toArray(a);
    }

    /**
     * Method to add an element to the Collection.
     * @param element The element to add
     * @return Whether it was added successfully.
     */
    public synchronized boolean add(Object element) {
        boolean success = delegate.add(element);
        if (success) {
            makeDirty();
            if (ownerSM != null && !ownerSM.getExecutionContext().getTransaction().isActive()) {
                ownerSM.getExecutionContext().processNontransactionalUpdate();
            }
        }
        return success;
    }

    /**
     * Method to add a collection of elements.
     * @param elements The collection of elements to add.
     * @return Whether they were added successfully.
     */
    public synchronized boolean addAll(java.util.Collection elements) {
        boolean success = delegate.addAll(elements);
        if (success) {
            makeDirty();
            if (ownerSM != null && !ownerSM.getExecutionContext().getTransaction().isActive()) {
                ownerSM.getExecutionContext().processNontransactionalUpdate();
            }
        }
        return success;
    }

    /**
     * Method to clear the Collection.
     */
    public synchronized void clear() {
        if (ownerSM != null && !delegate.isEmpty()) {
            AbstractMemberMetaData mmd = ownerSM.getClassMetaData().getMetaDataForMember(fieldName);
            if (SCOUtils.hasDependentElement(mmd)) {
                Iterator iter = delegate.iterator();
                while (iter.hasNext()) {
                    ownerSM.getExecutionContext().deleteObjectInternal(iter.next());
                }
            }
        }
        delegate.clear();
        makeDirty();
        if (ownerSM != null && !ownerSM.getExecutionContext().getTransaction().isActive()) {
            ownerSM.getExecutionContext().processNontransactionalUpdate();
        }
    }

    /**
     * Method to offer an element to the Queue.
     * @param element The element to offer
     * @return Whether it was added successfully.
     **/
    public synchronized boolean offer(Object element) {
        boolean success = delegate.offer(element);
        if (success) {
            makeDirty();
            if (ownerSM != null && !ownerSM.getExecutionContext().getTransaction().isActive()) {
                ownerSM.getExecutionContext().processNontransactionalUpdate();
            }
        }
        return success;
    }

    /**
     * Method to poll the next element in the Queue.
     * @return The element (now removed)
     **/
    public synchronized Object poll() {
        Object obj = delegate.poll();
        makeDirty();
        if (ownerSM != null && !ownerSM.getExecutionContext().getTransaction().isActive()) {
            ownerSM.getExecutionContext().processNontransactionalUpdate();
        }
        return obj;
    }

    /**
     * Method to remove an element from the List
     * @param element The Element to remove
     * @return Whether it was removed successfully.
     **/
    public synchronized boolean remove(Object element) {
        return remove(element, true);
    }

    /**
     * Method to remove an element from the List
     * @param element The Element to remove
     * @return Whether it was removed successfully.
     **/
    public synchronized boolean remove(Object element, boolean allowCascadeDelete) {
        boolean success = delegate.remove(element);
        if (ownerSM != null && allowCascadeDelete) {
            AbstractMemberMetaData mmd = ownerSM.getClassMetaData().getMetaDataForMember(fieldName);
            if (SCOUtils.hasDependentElement(mmd)) {
                ownerSM.getExecutionContext().deleteObjectInternal(element);
            }
        }
        if (success) {
            makeDirty();
            if (ownerSM != null && !ownerSM.getExecutionContext().getTransaction().isActive()) {
                ownerSM.getExecutionContext().processNontransactionalUpdate();
            }
        }
        return success;
    }

    /**
     * Method to remove a Collection of elements.
     * @param elements The collection to remove
     * @return Whether they were removed successfully.
     **/
    public synchronized boolean removeAll(java.util.Collection elements) {
        boolean success = delegate.removeAll(elements);
        if (ownerSM != null && elements != null && !elements.isEmpty()) {
            AbstractMemberMetaData mmd = ownerSM.getClassMetaData().getMetaDataForMember(fieldName);
            if (SCOUtils.hasDependentElement(mmd)) {
                Iterator iter = elements.iterator();
                while (iter.hasNext()) {
                    ownerSM.getExecutionContext().deleteObjectInternal(iter.next());
                }
            }
        }
        if (success) {
            makeDirty();
            if (ownerSM != null && !ownerSM.getExecutionContext().getTransaction().isActive()) {
                ownerSM.getExecutionContext().processNontransactionalUpdate();
            }
        }
        return success;
    }

    /**
     * Method to retain a Collection of elements (and remove all others).
     * @param c The collection to retain
     * @return Whether they were retained successfully.
     **/
    public synchronized boolean retainAll(java.util.Collection c) {
        boolean success = delegate.retainAll(c);
        if (success) {
            makeDirty();
            if (ownerSM != null && !ownerSM.getExecutionContext().getTransaction().isActive()) {
                ownerSM.getExecutionContext().processNontransactionalUpdate();
            }
        }
        return success;
    }

    /**
     * The writeReplace method is called when ObjectOutputStream is preparing
     * to write the object to the stream. The ObjectOutputStream checks whether
     * the class defines the writeReplace method. If the method is defined, the
     * writeReplace method is called to allow the object to designate its
     * replacement in the stream. The object returned should be either of the
     * same type as the object passed in or an object that when read and
     * resolved will result in an object of a type that is compatible with all
     * references to the object.
     * @return the replaced object
     * @throws ObjectStreamException
     */
    protected Object writeReplace() throws ObjectStreamException {
        return new java.util.PriorityQueue(delegate);
    }
}

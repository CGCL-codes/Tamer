package org.caleigo.core;

import java.util.*;
import org.caleigo.core.event.*;

public class OrderedProxySelection extends ProxySelection {

    private int[] mIndexArray;

    private Comparator mComparator;

    /** Creates new OrderedProxySelection
     */
    public OrderedProxySelection(IEntityDescriptor entityDescriptor) {
        super(entityDescriptor);
    }

    /** Creates new OrderedProxySelection
     */
    public OrderedProxySelection(IEntityDescriptor entityDescriptor, Comparator comparator) {
        this(entityDescriptor);
        mComparator = comparator;
    }

    /** Creates new OrderedProxySelection
     */
    public OrderedProxySelection(ISelection selection) {
        super(selection);
    }

    /** Creates new OrderedProxySelection
     */
    public OrderedProxySelection(ISelection selection, Comparator comparator) {
        this(selection);
        mComparator = comparator;
        this.sort();
    }

    /** Adds the provided IEntity object to at the specified index in the 
     * selection object. If an entity with the same identity already exists 
     * in the selection the requeat is ignored and false is returned.
     */
    public boolean addEntity(int index, IEntity entity) {
        if (this.getRemoteSelection() != null) return this.getRemoteSelection().addEntity(this.getRemoteIndex(index), entity); else return false;
    }

    /** Access method that returns the contained IEntity object with the 
     * specified index.
     */
    public IEntity getEntity(int index) {
        if (this.getRemoteSelection() != null) return this.getRemoteSelection().getEntity(this.getRemoteIndex(index)); else return null;
    }

    /** Mutation method that removes the indexed entity from the selection.
     * Returns the indexed entity if it was found and removed otherwise null  
     * is returned. The entities are not effecte in any other way.
     */
    public IEntity removeEntity(int index) {
        if (this.getRemoteSelection() != null) return this.getRemoteSelection().removeEntity(this.getRemoteIndex(index)); else return null;
    }

    /** Access method that views the selection objct as a grid where row is
     * the entity index and column is the field index for the stored entities.
     */
    public Object getData(int row, int column) {
        if (this.getRemoteSelection() != null) return this.getRemoteSelection().getData(this.getRemoteIndex(row), column); else return this.getEntityDescriptor().getFieldDescriptor(column).getDefaultValue();
    }

    /** Mutation method that views the selection objct as a grid where row is
     * the entity index and column is the field index for the stored entities.
     */
    public void setData(int row, int column, Object dataValue) {
        if (this.getRemoteSelection() != null) this.getRemoteSelection().setData(this.getRemoteIndex(row), column, dataValue);
    }

    /** Creates a sub selection with the indexed entities in the called 
     * selection. The created selection that should be independant of changes 
     * in the source/called selection after the time of creation.
     */
    public ISelection createSubSelection(int[] indexArray) {
        ISelection selection = new Selection(this.getEntityDescriptor());
        for (int j = 0; j < indexArray.length; j++) selection.addEntity(this.getEntity(indexArray[j]));
        return selection;
    }

    /** Help method that returns the index of the provided IEntity object in 
     * the selection if it exists othewise a negative value is returned.
     */
    public int indexOf(IEntity entity) {
        if (this.getRemoteSelection() != null) return this.getOrderedIndex(this.getRemoteSelection().indexOf(entity)); else return -1;
    }

    /** Access method that returns the selections relay listener.
     */
    protected RelayListener createRelayListener() {
        return new OrderedRelayListener();
    }

    /** Overriden to sort the index map for the new remote selection. 
     */
    protected void doAfterRemoteChange() {
        this.sort();
    }

    public Comparator getComparator() {
        return mComparator;
    }

    public void setComparator(Comparator comp) {
        mComparator = comp;
        this.sort();
    }

    public void setCollationField(IFieldDescriptor fieldDescriptor) {
        this.setCollationField(fieldDescriptor, true);
    }

    public void setCollationField(IFieldDescriptor fieldDescriptor, boolean ascending) {
        mComparator = new EntityCollator(this.getEntityDescriptor());
        ((EntityCollator) mComparator).addCollationField(fieldDescriptor, ascending);
        this.sort();
    }

    public void addCollationField(IFieldDescriptor fieldDescriptor) {
        this.addCollationField(fieldDescriptor, true);
    }

    public void addCollationField(IFieldDescriptor fieldDescriptor, boolean ascending) {
        if (!(mComparator instanceof EntityCollator)) mComparator = new EntityCollator(this.getEntityDescriptor());
        ((EntityCollator) mComparator).addCollationField(fieldDescriptor, ascending);
        this.sort();
    }

    public void clearCollationFields() {
        mComparator = null;
    }

    /** Converts an ordered entity index to a remote unordered index.
     */
    public int getRemoteIndex(int orderedIndex) {
        if (mComparator != null && mIndexArray != null && mIndexArray.length > 0) return mIndexArray[orderedIndex]; else return orderedIndex;
    }

    /** Converts a remote unordered entity index to an ordered index.
     */
    public int getOrderedIndex(int remoteIndex) {
        if (mComparator == null || mIndexArray == null) return remoteIndex;
        int orderedIndex = mIndexArray.length - 1;
        while (orderedIndex >= 0 && mIndexArray[orderedIndex] != remoteIndex) orderedIndex--;
        return orderedIndex;
    }

    /** Sorts the selection using the current entity collator. Note that the
     * sort is automatically called when a new collator is set using any of
     * the order changing methods in the object. The method should however be 
     * used if the registered Comperator is changed externally.
     */
    public void sort() {
        if (!this.hasRemoteSelection()) mIndexArray = null;
        if (mComparator == null || !this.hasRemoteSelection()) return;
        ISelection remoteSelection = this.getRemoteSelection();
        if (mIndexArray == null || mIndexArray.length != remoteSelection.size()) {
            if (mIndexArray != null && mIndexArray.length < remoteSelection.size()) {
                int[] oldArray = mIndexArray;
                mIndexArray = new int[remoteSelection.size()];
                System.arraycopy(oldArray, 0, mIndexArray, 0, oldArray.length);
                for (int j = oldArray.length; j < mIndexArray.length; j++) mIndexArray[j] = j;
            } else {
                mIndexArray = new int[remoteSelection.size()];
                for (int j = 0; j < mIndexArray.length; j++) mIndexArray[j] = j;
            }
        }
        boolean orderChanged = false;
        int j, k, tmp;
        for (j = 1; j < mIndexArray.length; j++) {
            k = j;
            while (k > 0 && mComparator.compare(remoteSelection.getEntity(mIndexArray[j]), remoteSelection.getEntity(mIndexArray[k - 1])) < 0) k--;
            if (k < j) {
                tmp = mIndexArray[j];
                System.arraycopy(mIndexArray, k, mIndexArray, k + 1, j - k);
                mIndexArray[k] = tmp;
                orderChanged = true;
            }
        }
        if (orderChanged) this.fireContentsChanged();
    }

    protected class OrderedRelayListener extends RelayListener {

        public void entityRemoved(SelectionEvent event) {
            sort();
            super.entityRemoved(event);
        }

        public void entityAdded(SelectionEvent event) {
            if (mComparator != null && hasRemoteSelection()) {
                ISelection remoteSelection = getRemoteSelection();
                int[] newIndexArray = new int[mIndexArray.length + 1];
                int insertIndex = -1;
                for (int j = 0; j < mIndexArray.length; j++) if (mComparator.compare(remoteSelection.getEntity(mIndexArray[j]), event.getEntity()) > 0) {
                    insertIndex = j;
                    break;
                }
                if (insertIndex == -1) insertIndex = newIndexArray.length - 1;
                newIndexArray[insertIndex] = remoteSelection.indexOf(event.getEntity());
                System.arraycopy(mIndexArray, 0, newIndexArray, 0, insertIndex);
                System.arraycopy(mIndexArray, insertIndex, newIndexArray, insertIndex + 1, mIndexArray.length - insertIndex);
                mIndexArray = newIndexArray;
            }
            super.entityAdded(event);
        }

        public void contentsChanged(SelectionEvent event) {
            sort();
            super.contentsChanged(event);
        }

        public void dataChanged(EntityChangeEvent event) {
            sort();
            super.dataChanged(event);
        }

        public void remoteChanged(ProxyEvent event) {
            sort();
            super.remoteChanged(event);
        }
    }
}

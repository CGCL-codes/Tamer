package jgnash.ui.components.expandingtable;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.prefs.Preferences;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

/**
 * An table model for hierarchical objects of the same type that can be expanded and collapsed
 *
 * @author Craig Cavanaugh
 * @version $Id: AbstractExpandingTableModel.java 3091 2012-01-09 01:11:08Z ccavanaugh $
 */
public abstract class AbstractExpandingTableModel<E extends Comparable<? super E>> extends AbstractTableModel {

    private final Map<E, ExpandingTableNode<E>> objects = new HashMap<E, ExpandingTableNode<E>>();

    private transient List<ExpandingTableNode<E>> visibleObjects;

    /**
     * A list of the key/objects in the model.  It is faster to maintain a list vs return a keySet from the map
     */
    private transient List<E> keys = new ArrayList<E>();

    /**
     * Read write lock for model access
     */
    protected ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(false);

    /**
     * Expansion state preference key
     */
    private static final String EXPANSION_STATE = "ExpansionState";

    private Preferences preferences = Preferences.userNodeForPackage(getClass());

    public AbstractExpandingTableModel() {
        initializeModel();
    }

    protected final void initializeModel() {
        visibleObjects = new ArrayList<ExpandingTableNode<E>>();
        new InitializeModelWorker().execute();
    }

    /**
     * Forces a rebuild of the model when changes are indeterminate
     */
    protected void reloadModel() {
        clear();
        new InitializeModelWorker().execute();
    }

    /**
     * Returns a <code>Collection</code> of objects loaded into the model at the time this method is called.
     *
     * @return <code>Collection</code> of objects
     */
    public List<E> getObjects() {
        ReadLock readLock = rwl.readLock();
        try {
            readLock.lock();
            return new ArrayList<E>(keys);
        } finally {
            readLock.unlock();
        }
    }

    private String getExpansionState() {
        ReadLock readLock = rwl.readLock();
        try {
            readLock.lock();
            StringBuilder builder = new StringBuilder();
            ArrayList<ExpandingTableNode<E>> values = new ArrayList<ExpandingTableNode<E>>(objects.values());
            Collections.sort(values);
            for (ExpandingTableNode<E> node : values) {
                builder.append(node.isExpanded() ? '1' : '0');
            }
            return builder.toString();
        } finally {
            readLock.unlock();
        }
    }

    private void restoreExpansionState(final String state) {
        WriteLock writeLock = rwl.writeLock();
        try {
            writeLock.lock();
            if (state != null && state.length() == objects.size()) {
                ArrayList<ExpandingTableNode<E>> values = new ArrayList<ExpandingTableNode<E>>(objects.values());
                Collections.sort(values);
                for (int i = 0; i < state.length(); i++) {
                    values.get(i).setExpanded(state.charAt(i) == '1');
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    public void toggleExpansion(final E object) {
        WriteLock writeLock = rwl.writeLock();
        try {
            writeLock.lock();
            if (isParent(object)) {
                ExpandingTableNode<E> node = getNode(object);
                node.setExpanded(!node.isExpanded());
                preferences.put(EXPANSION_STATE, getExpansionState());
                fireNodeChanged();
            }
        } finally {
            writeLock.unlock();
        }
    }

    public ExpandingTableNode<E> getNode(final E object) {
        ReadLock readLock = rwl.readLock();
        try {
            readLock.lock();
            return objects.get(object);
        } finally {
            readLock.unlock();
        }
    }

    public void clear() {
        WriteLock writeLock = rwl.writeLock();
        try {
            writeLock.lock();
            visibleObjects.clear();
            objects.clear();
            keys.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public int getObjectIndex(final E object) {
        ReadLock readLock = rwl.readLock();
        try {
            readLock.lock();
            int index = -1;
            for (ExpandingTableNode n : visibleObjects) {
                if (n.getObject().equals(object)) {
                    index = visibleObjects.indexOf(n);
                    break;
                }
            }
            return index;
        } finally {
            readLock.unlock();
        }
    }

    public boolean isExpanded(final E object) {
        return getNode(object).isExpanded();
    }

    @Override
    public int getRowCount() {
        ReadLock readLock = rwl.readLock();
        try {
            readLock.lock();
            return visibleObjects.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        throw new UnsupportedOperationException();
    }

    public void addNode(final E object) {
        WriteLock writeLock = rwl.writeLock();
        try {
            writeLock.lock();
            objects.put(object, new ExpandingTableNode<E>(object));
            keys.add(object);
            buildVisibleModel();
            fireTableDataChanged();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Override the default implementation to push the change onto the EDT
     *
     * @see AbstractTableModel#fireTableDataChanged()
     */
    @Override
    public void fireTableDataChanged() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                AbstractExpandingTableModel.super.fireTableDataChanged();
            }
        });
    }

    /**
     * Force update of the visible model and notify listeners
     */
    public final void fireNodeChanged() {
        WriteLock writeLock = rwl.writeLock();
        try {
            writeLock.lock();
            buildVisibleModel();
            fireTableDataChanged();
        } finally {
            writeLock.unlock();
        }
    }

    public void removeNode(final E object) {
        WriteLock writeLock = rwl.writeLock();
        try {
            writeLock.lock();
            ExpandingTableNode node = getNode(object);
            if (node != null) {
                objects.remove(object);
                visibleObjects.remove(node);
                keys.remove(object);
                fireTableDataChanged();
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Returns the object for the visible row.
     *
     * @param rowIndex visible row index
     * @return object
     */
    public E get(final int rowIndex) {
        ReadLock readLock = rwl.readLock();
        try {
            readLock.lock();
            return getExpandingTableNodeAt(rowIndex).getObject();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Returns the number of visible objects in this model.
     *
     * @return the number of visible objects in this model
     * @see List#size()
     */
    public int size() {
        ReadLock readLock = rwl.readLock();
        try {
            readLock.lock();
            return visibleObjects.size();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Returns the encapsulating object wrapper for the visible row.
     *
     * @param rowIndex visible row index
     * @return node
     */
    public ExpandingTableNode<E> getExpandingTableNodeAt(final int rowIndex) {
        ReadLock readLock = rwl.readLock();
        try {
            readLock.lock();
            return visibleObjects.get(rowIndex);
        } finally {
            readLock.unlock();
        }
    }

    private void loadModel() {
        WriteLock writeLock = rwl.writeLock();
        try {
            writeLock.lock();
            for (E object : getModelObjects()) {
                objects.put(object, new ExpandingTableNode<E>(object));
                keys.add(object);
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void loadVisibleModel(final E object, final List<ExpandingTableNode<E>> model) {
        WriteLock writeLock = rwl.writeLock();
        try {
            writeLock.lock();
            if (isVisible(object)) {
                ExpandingTableNode<E> node = getNode(object);
                if (node != null) {
                    model.add(node);
                }
                if (isParent(object)) {
                    for (E child : getChildren(object)) {
                        loadVisibleModel(child, model);
                    }
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    private synchronized void buildVisibleModel(final E root) {
        WriteLock writeLock = rwl.writeLock();
        try {
            writeLock.lock();
            List<ExpandingTableNode<E>> model = new ArrayList<ExpandingTableNode<E>>();
            for (E child : getChildren(root)) {
                loadVisibleModel(child, model);
            }
            visibleObjects = model;
        } finally {
            writeLock.unlock();
        }
    }

    private synchronized void buildVisibleModel() {
        buildVisibleModel(getRootObject());
    }

    /**
     * Determines the visible depth of the object in the hierarchical structure
     *
     * @param object child object
     * @return depth in the hierarchical structure
     */
    public abstract int getVisibleDepth(E object);

    /**
     * Determines if a given object has children
     *
     * @param object potential parent object
     * @return true if the object is a parent
     */
    public abstract boolean isParent(E object);

    /**
     * Returns a collection objects that are children of a supplied object. If the object does not have children, an
     * empty <code>Collection</code> should be returned instead of null.
     *
     * @param object parent object
     * @return collection of objects.
     */
    public abstract Collection<E> getChildren(E object);

    /**
     * Returns the parent of the given object. May return null if the object does not have a parent
     *
     * @param object parent object
     * @return the parent of the given object.
     */
    public abstract E getParent(E object);

    /**
     * Determines if the object should be visible. This is used to filter the displayed objects. The default
     * implementation checks for visibility based on default expansion state. Overriding implementations should return
     * false if the super returns false.
     *
     * @param object object to check
     * @return true if it should be visible
     */
    public boolean isVisible(final E object) {
        E parent = getParent(object);
        if (parent != null && getNode(parent) != null) {
            return getNode(parent).isExpanded();
        }
        return true;
    }

    /**
     * Returns a collection of all objects that should be loaded into the model.
     * </p>
     * This method can be expense as it is intended for constructing the model, 
     * so usage should be minimal.
     * 
     * @return collection of objects
     * @see AbstractExpandingTableModel#getObjects() 
     */
    protected abstract Collection<E> getModelObjects();

    /**
     * Returns a search string for the given object. Used for keyboard selection and search of the hierarchical
     * structure. Typically, this will be the string returned for the first column.
     *
     * @param Object object to get search string
     * @return a search string
     */
    public abstract String getSearchString(E Object);

    /**
     * Returns the top level object for the hierarchical structure
     *
     * @return root object
     */
    protected abstract E getRootObject();

    private final class InitializeModelWorker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            loadModel();
            buildVisibleModel();
            restoreExpansionState(preferences.get(EXPANSION_STATE, null));
            return null;
        }

        @Override
        protected void done() {
            fireNodeChanged();
        }
    }
}

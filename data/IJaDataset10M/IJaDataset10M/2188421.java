package edu.berkeley.guir.prefuse.focus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusEventMulticaster;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.graph.Entity;

/**
 * Default implementation of the {@link FocusSet FocusSet} interface.
 * This class maintains a <code>java.util.LinkedHashSet</code> of
 * focus entities, supporting quick lookup of entities while
 * maintaining the order in which focus entities are added to the set.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a>
 *         prefuse(AT)jheer.org
 */
public class DefaultFocusSet implements FocusSet {

    private Set m_foci = new LinkedHashSet();

    private ArrayList m_tmp = new ArrayList();

    private FocusListener m_listener;

    /**
     * Adds a listener to monitor changes to this FocusSet.
     * 
     * @param fl
     *            the FocusListener to add
     */
    public void addFocusListener(FocusListener fl) {
        m_listener = FocusEventMulticaster.add(m_listener, fl);
    }

    /**
     * Removes a listener currently monitoring this FocusSet.
     * 
     * @param fl
     *            the FocusListener to remove
     */
    public void removeFocusListener(FocusListener fl) {
        m_listener = FocusEventMulticaster.remove(m_listener, fl);
    }

    /**
     * Adds a new Entity to this FocusSet.
     * 
     * @param focus
     *            the Entity to add
     */
    public void add(Entity focus) {
        boolean add;
        synchronized (this) {
            add = m_foci.add(focus);
        }
        if (add && m_listener != null) {
            m_listener.focusChanged(new FocusEvent(this, FocusEvent.FOCUS_ADDED, new Entity[] { focus }, null));
        }
    }

    /**
     * Adds a Collection of Entity instances to this FocusSet. All
     * members of this Collection should be of type Entity.
     * 
     * @param foci
     *            the Collection of Entity instances to add.
     */
    public void add(Collection foci) {
        synchronized (m_tmp) {
            synchronized (this) {
                Iterator iter = foci.iterator();
                while (iter.hasNext()) {
                    Object o = iter.next();
                    if (!(o instanceof Entity)) {
                        throw new IllegalArgumentException("All foci must be of type Entity");
                    } else if (m_foci.add(o) && m_listener != null) {
                        m_tmp.add(o);
                    }
                }
            }
            if (m_listener != null && m_tmp.size() > 0) {
                Entity[] add = (Entity[]) m_tmp.toArray(FocusEvent.EMPTY);
                m_listener.focusChanged(new FocusEvent(this, FocusEvent.FOCUS_ADDED, add, null));
            }
            m_tmp.clear();
        }
    }

    /**
     * Removes an Entity from this FocusSet.
     * 
     * @param focus
     *            the Entity to remove
     */
    public void remove(Entity focus) {
        boolean remove;
        synchronized (this) {
            remove = m_foci.remove(focus);
        }
        if (remove && m_listener != null) {
            m_listener.focusChanged(new FocusEvent(this, FocusEvent.FOCUS_REMOVED, null, new Entity[] { focus }));
        }
    }

    /**
     * Removes a Collection of Entity instances from this FocusSet.
     * All members of this Collection should already be members of
     * this set.
     * 
     * @param foci
     *            the Collection of Entity instances to remove.
     */
    public void remove(Collection foci) {
        synchronized (m_tmp) {
            synchronized (this) {
                Iterator iter = foci.iterator();
                while (iter.hasNext()) {
                    Object o = iter.next();
                    if (m_foci.remove(o) && m_listener != null) m_tmp.add(o);
                }
            }
            if (m_listener != null && m_tmp.size() > 0) {
                Entity[] rem = (Entity[]) m_tmp.toArray(FocusEvent.EMPTY);
                m_listener.focusChanged(new FocusEvent(this, FocusEvent.FOCUS_REMOVED, null, rem));
            }
            m_tmp.clear();
        }
    }

    /**
     * Sets an Entity as the single focus in this FocusSet. This
     * causes any previous members of the focus set to be removed.
     * 
     * @param focus
     *            the Entity to set
     */
    public void set(Entity focus) {
        Entity[] add = null, rem = null;
        synchronized (this) {
            if (m_foci.size() > 0 && m_listener != null) rem = (Entity[]) m_foci.toArray(FocusEvent.EMPTY);
            m_foci.clear();
            if (m_foci.add(focus) && m_listener != null) add = new Entity[] { focus };
        }
        if (add != null || rem != null) {
            m_listener.focusChanged(new FocusEvent(this, FocusEvent.FOCUS_SET, add, rem));
        }
    }

    /**
     * Sets a Collection of Entity instances as the foci in this
     * FocusSet. All members of this Collection should of type Entity.
     * This method causes any previous members of the focus set to be
     * removed.
     * 
     * @param foci
     *            the Collection of Entity instances to remove.
     */
    public void set(Collection foci) {
        Entity[] add = null, rem = null;
        synchronized (m_tmp) {
            synchronized (this) {
                Iterator iter = foci.iterator();
                while (iter.hasNext()) {
                    Object o = iter.next();
                    if (!(o instanceof Entity)) {
                        throw new IllegalArgumentException("All foci must be of type Entity");
                    }
                }
                if (m_listener != null && m_foci.size() > 0) rem = (Entity[]) m_foci.toArray(FocusEvent.EMPTY);
                m_foci.clear();
                iter = foci.iterator();
                while (iter.hasNext()) {
                    Entity o = (Entity) iter.next();
                    if (m_foci.add(o) && m_listener != null) {
                        m_tmp.add(o);
                    }
                }
            }
            if (m_listener != null && m_tmp.size() > 0) {
                add = (Entity[]) m_tmp.toArray(FocusEvent.EMPTY);
                m_tmp.clear();
            }
        }
        if (add != null || rem != null) {
            m_listener.focusChanged(new FocusEvent(this, FocusEvent.FOCUS_SET, add, rem));
        }
    }

    /**
     * Clears this FocusSet, removing all current members.
     */
    public void clear() {
        Entity[] rem = null;
        synchronized (this) {
            if (m_listener != null && m_foci.size() > 0) rem = (Entity[]) m_foci.toArray(FocusEvent.EMPTY);
            m_foci.clear();
        }
        if (rem != null) {
            m_listener.focusChanged(new FocusEvent(this, FocusEvent.FOCUS_SET, null, rem));
        }
    }

    /**
     * Returns an Iterator over the members of this FocusSet.
     * 
     * @return an Iterator over the members of this FocusSet
     */
    public synchronized Iterator iterator() {
        return m_foci.iterator();
    }

    /**
     * Returns the size of this FocusSet.
     * 
     * @return the number of elements in this FocusSet
     */
    public synchronized int size() {
        return m_foci.size();
    }

    /**
     * Indicates if a given Entity is contained within this FocusSet.
     * 
     * @param entity
     *            the Entity to check for containment
     * @return true if this Entity is in the FocusSet, false otherwise
     */
    public synchronized boolean contains(Entity entity) {
        return m_foci.contains(entity);
    }
}

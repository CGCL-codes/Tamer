package org.zkoss.zk.ui.impl;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.ext.Scope;
import org.zkoss.zk.ui.ext.ScopeListener;

/**
 * Utilities to implement {@link Scope#addScopeListener}
 * and {@link Scope#removeScopeListener}.
 *
 * @author tomyeh
 * @since 5.0.0
 * @see SimpleScope
 * @see Scope
 */
public class ScopeListeners {

    private final Scope _owner;

    private List _listeners;

    /** Constructor.
	 * @param owner the scope that is the owner (i.e., visible to the users).
	 */
    public ScopeListeners(Scope owner) {
        _owner = owner;
    }

    /** Adds a scope listner.
	 */
    public boolean addScopeListener(ScopeListener listener) {
        if (listener == null) throw new IllegalArgumentException("null");
        if (_listeners == null) _listeners = new LinkedList(); else if (_listeners.contains(listener)) return false;
        _listeners.add(listener);
        return true;
    }

    /** Adds a scope listner.
	 */
    public boolean removeScopeListener(ScopeListener listener) {
        return _listeners != null && _listeners.remove(listener);
    }

    /** Returns a ist of all scope listners (never null).
	 */
    public List getListeners() {
        if (_listeners == null) _listeners = new LinkedList();
        return _listeners;
    }

    /** Invokes {@link ScopeListener#attributeAdded} for registered
	 * listeners.
	 *
	 * @see #addScopeListener
	 */
    public void notifyAdded(String name, Object value) {
        if (_listeners != null) for (Iterator it = _listeners.iterator(); it.hasNext(); ) ((ScopeListener) it.next()).attributeAdded(_owner, name, value);
    }

    /** Invokes {@link ScopeListener#attributeReplaced} for registered
	 * listeners.
	 *
	 * @see #addScopeListener
	 */
    public void notifyReplaced(String name, Object value) {
        if (_listeners != null) for (Iterator it = _listeners.iterator(); it.hasNext(); ) ((ScopeListener) it.next()).attributeReplaced(_owner, name, value);
    }

    /** Invokes {@link ScopeListener#attributeRemoved} for registered
	 * listeners.
	 *
	 * @see #addScopeListener
	 */
    public void notifyRemoved(String name) {
        if (_listeners != null) for (Iterator it = _listeners.iterator(); it.hasNext(); ) ((ScopeListener) it.next()).attributeRemoved(_owner, name);
    }

    /** Invokes {@link ScopeListener#parentChanged} for registered
	 * listeners.
	 *
	 * @see #addScopeListener
	 */
    public void notifyParentChanged(Scope newparent) {
        if (_listeners != null) for (Iterator it = _listeners.iterator(); it.hasNext(); ) ((ScopeListener) it.next()).parentChanged(_owner, newparent);
    }

    /** Invokes {@link ScopeListener#idSpaceChanged} for registered
	 * listeners.
	 *
	 * @see #addScopeListener
	 * @since 5.0.1
	 */
    public void notifyIdSpaceChanged(IdSpace newIdSpace) {
        if (_listeners != null) for (Iterator it = _listeners.iterator(); it.hasNext(); ) try {
            ((ScopeListener) it.next()).idSpaceChanged(_owner, newIdSpace);
        } catch (AbstractMethodError ex) {
        }
    }
}

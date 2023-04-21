package org.zkoss.zk.ui.ext;

import org.zkoss.zk.ui.IdSpace;

/**
 * <p>A listener used to listen whether a scope ({@link Scope}) is changed.
 *
 * <p>To add a listener to the scope, invoke
 * {@link Scope#addScopeListener}.
 * 
 * @author tomyeh
 * @since 5.0.0
 */
public interface ScopeListener {

    /** Called when an attribute is going to be added to {@link Scope}.
	 *
	 * @param scope the scope where a new attribute is added
	 * @param value the new value.
	 */
    public void attributeAdded(Scope scope, String name, Object value);

    /** Called when an attribute is going to be replaced in {@link Scope}.
	 *
	 * @param scope the scope where a new attribute is replaced
	 * @param value the new value.
	 */
    public void attributeReplaced(Scope scope, String name, Object value);

    /** Called when an attribute is going to be removed from {@link Scope}.
	 * @param scope the scope where a new attribute is removed
	 */
    public void attributeRemoved(Scope scope, String name);

    /** Called when the parent has been changed.
	 *
	 * @param scope the scope whose parent is changed
	 * @param newparent the new parent.
	 */
    public void parentChanged(Scope scope, Scope newparent);

    /** Called when the ID space of this scope has been changed.
	 * This method is called only if one of its parent implements
	 * {@link IdSpace}, such as components and pages.
	 * @param scope the scope whose ID space is changed
	 * @param newIdSpace the new ID space.
	 * @since 5.0.1
	 */
    public void idSpaceChanged(Scope scope, IdSpace newIdSpace);
}

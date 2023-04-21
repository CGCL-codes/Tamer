package org.zkoss.zk.scripting;

/**
 * A listener used to listen whether {@link Namespace} is changed.
 *
 * <p>To add a listener to the namespace, invoke
 * {@link Namespace#addChangeListener}.
 *
 * @author tomyeh
 */
public interface NamespaceChangeListener {

    /** Called when a variable is set to {@link Namespace}.
	 *
	 * @param value the new value.
	 */
    public void onAdd(String name, Object value);

    /** Called when a variable is removed from {@link Namespace}.
	 */
    public void onRemove(String name);

    /** Called when the parent is changed.
	 *
	 * @param newparent the new parent.
	 */
    public void onParentChanged(Namespace newparent);
}

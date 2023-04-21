package org.jlense.uiworks.workbench;

/**
 * A perspective service tracks the activation and reset of perspectives within a
 * workbench page.
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 *
 * @see IWorkbenchPage
 */
public interface IPerspectiveService {

    /**
     * Adds the given listener for a page's perspective lifecycle events.
     * Has no effect if an identical listener is already registered.
     *
     * @param listener a perspective listener
     */
    public void addPerspectiveListener(IPerspectiveListener listener);

    public IPerspectiveDescriptor getActivePerspective();

    /**
     * Removes the given page's perspective listener.
     * Has no affect if an identical listener is not registered.
     *
     * @param listener a perspective listener
     */
    public void removePerspectiveListener(IPerspectiveListener listener);
}

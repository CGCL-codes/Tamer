package com.vlsolutions.swing.docking.event;

/** This interface describes a listener for dockable selection changes.
 *
 *<p> The notification is currently based on keyboard focus policy (the event is 
 * triggered when a new dockable grabs the keyboard focus).
 *
 * @author Lilian Chamontin, VLSolutions
 * @since 2.0
 * @see DockableSelectionEvent
 * @see com.vlsolutions.swing.docking.DockingDesktop#addDockableSelectionListener(DockableSelectionListener)
 */
public interface DockableSelectionListener {

    /** This method is invoked when a new dockable is selected. */
    public void selectionChanged(DockableSelectionEvent e);
}

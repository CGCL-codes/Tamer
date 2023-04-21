package com.vlsolutions.swing.docking.event;

import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;

/** A DockingActionEvent involving a single dockable as source of the action.
 *
 *
 * @author Lilian Chamontin, VLSolutions
 * @since 2.1 
 */
public abstract class DockingActionDockableEvent extends DockingActionEvent {

    private Dockable dockable;

    public DockingActionDockableEvent(DockingDesktop desktop, Dockable dockable, DockableState.Location initialLocation, DockableState.Location nextLocation, int actionType) {
        super(desktop, initialLocation, nextLocation, actionType);
        this.dockable = dockable;
    }

    public Dockable getDockable() {
        return dockable;
    }

    public void setDockable(Dockable dockable) {
        this.dockable = dockable;
    }
}

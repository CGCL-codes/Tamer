package org.eclipse.gef.handles;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.tools.ConnectionBendpointTracker;

/**
 * A BendpointHandle that is used to create a new bendpoint.
 */
public class BendpointCreationHandle extends BendpointHandle {

    {
        setCursor(Cursors.SIZEALL);
        setPreferredSize(new Dimension(DEFAULT_HANDLE_SIZE - 2, DEFAULT_HANDLE_SIZE - 2));
    }

    /**
 * Creates a new BendpointCreationHandle.
 */
    public BendpointCreationHandle() {
    }

    /**
 * Creates a new BendpointCreationHandle, sets its owner to <code>owner</code>
 * and its index to <code>index</code>, and sets its locator to a new
 * {@link MidpointLocator}.
 * @param owner the ConnectionEditPart owner
 * @param index the index
 */
    public BendpointCreationHandle(ConnectionEditPart owner, int index) {
        setOwner(owner);
        setIndex(index);
        setLocator(new MidpointLocator(getConnection(), index));
    }

    /**
 * Creates a new BendpointCreationHandle, sets its owner to <code>owner</code>
 * and its index to <code>index</code>, and sets its locator to a new
 * {@link MidpointLocator} with the given <code>locatorIndex</code>.
 * @param owner the ConnectionEditPart owner
 * @param index the index
 * @param locatorIndex the locator index
 */
    public BendpointCreationHandle(ConnectionEditPart owner, int index, int locatorIndex) {
        setOwner(owner);
        setIndex(index);
        setLocator(new MidpointLocator(getConnection(), locatorIndex));
    }

    /**
 * Creates a new BendpointCreationHandle and sets its owner to 
 * <code>owner</code>, sets its index to <code>index</code>, and
 * sets its locator to <code>locator</code>.
 * @param owner the ConnectionEditPart owner
 * @param index the index
 * @param locator the Locator
 */
    public BendpointCreationHandle(ConnectionEditPart owner, int index, Locator locator) {
        setOwner(owner);
        setIndex(index);
        setLocator(locator);
    }

    /**
 * Creates and returns a new {@link ConnectionBendpointTracker}.
 * @return the new ConnectionBendpointTracker
 */
    protected DragTracker createDragTracker() {
        ConnectionBendpointTracker tracker;
        tracker = new ConnectionBendpointTracker((ConnectionEditPart) getOwner(), getIndex());
        tracker.setType(RequestConstants.REQ_CREATE_BENDPOINT);
        tracker.setDefaultCursor(getCursor());
        return tracker;
    }
}

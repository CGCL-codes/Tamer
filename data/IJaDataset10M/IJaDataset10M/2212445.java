package com.ivis.xprocess.ui.draganddrop.util;

import java.util.Collection;

public interface IMultipleDragAndDropAction extends IDragAndDropAction {

    /**
     * Can the dragObjects be dragged onto the dropObject?
     *
     * @param dragObject
     *            The object being dragged
     * @param dropObject
     *            The object being dropped onto
     * @return DND.DROP_NONE, DND.DROP_MOVE, DND.DROP_COPY or DND.DROP_LINK
     */
    public boolean canDrop(Collection<?> dragObjects, Object dropObject);

    /**
     * Lightweight checking that the dragObject can be dragged onto the
     * dropObject?
     *
     * @param dragObject
     *            The object being dragged
     * @param dropObject
     *            The object being dropped onto
     * @return DND.DROP_NONE, DND.DROP_MOVE, DND.DROP_COPY or DND.DROP_LINK
     */
    public int lightWeightCanDrop(Collection<?> dragObjects, Object dropObject);

    /**
     * The text shown on the context menu when a drop occurs.<br>
     * This method is only called when canDrop has returned a non DND.DROP_NONE
     * result.
     *
     * @param dragObject
     *            The object being dragged
     * @param dropObject
     *            The object being dropped onto
     * @return The menu item text, or null if there should be no user prompt.
     */
    public String getItemText(Collection<?> dragObjects, Object dropObject);

    /**
     * This method is only called when canDrop has returned a non DND.DROP_NONE
     * result.
     *
     * @param dragObject
     *            The object being dragged
     * @param dropObject
     *            The object being dropped onto
     */
    public void doAction(Collection<?> dragObjects, Object dropObject);
}

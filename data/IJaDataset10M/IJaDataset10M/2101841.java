package org.ufacekit.ui.swing.jface.viewers.internal;

/**
 * Extends <code>IBaseLabelProvider</code> with the methods
 * to update the label for a given element.  The label is represented by a
 * <code>ViewerLabel</code>.
 * Unlike <code>ILabelProvider</code>, this allows the text and image to be
 * set in the same request, rather than via separate requests.
 * <p>
 * It also allows the current values for the text and image to be considered by
 * the label provider, allowing for potential optimizations.
 * For example, decorating label providers that run in the background can hold off
 * applying an update to a previously populated label until the decoration is ready,
 * thereby reducing flicker.
 * </p>
 *
 * @since 3.0
 */
@SuppressWarnings("unchecked")
public interface IViewerLabelProvider extends IBaseLabelProvider {

    /**
     * Updates the label for the given element.
     *
     * @param label the label to update
     * @param element the element
     */
    public void updateLabel(ViewerLabel label, Object element);
}

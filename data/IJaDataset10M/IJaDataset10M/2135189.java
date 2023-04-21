package pl.org.minions.utils.ui.widgets;

import pl.org.minions.utils.logger.Log;

/**
 * Manages focus and modal lock within a {@link Widget
 * widget} hierarchy.
 */
class FocusManager {

    private Widget modalLockingWidget;

    private Widget focusedWidget;

    /**
     * If selected widget is the widget with keyboard focus,
     * remove the focus from it.
     * @param by
     *            the widget releasing focus
     */
    public void focusRelease(Widget by) {
        if (by == focusedWidget) {
            Log.logger.debug("Focus released by " + by);
            focusedWidget = null;
        }
    }

    /**
     * Request keyboard focus for selected widget.
     * @param by
     *            the widget requesting focus
     * @see #getFocused()
     */
    public void focusRequest(Widget by) {
        Log.logger.debug("Focus acquired by " + by);
        focusedWidget = by;
    }

    /**
     * Returns the widget that has keyboard focus. Only the
     * widget with keyboard focus is to receive a keyboard
     * state.
     * @return the focused widget
     * @see Widget#hasFocus()
     */
    public Widget getFocused() {
        return focusedWidget;
    }

    /**
     * Returns the widget that possesses the modal lock.
     * <p>
     * If a widget has a modal lock, only that widget and
     * its descendants will receive keyboard and mouse
     * information.
     * @return the widget possessing the modal lock, or
     *         <code>null</code> if no widget has it
     * @see Widget#hasModalLock()
     */
    public Widget getModalLockWidget() {
        return modalLockingWidget;
    }

    /**
     * Checks id the selected widget has focus.
     * @param w
     *            the selected widget
     * @return <code>true</code> if the selected widget has
     *         keyboard focus, <code>false</code> otherwise
     */
    public boolean hasFocus(Widget w) {
        return focusedWidget == w;
    }

    /**
     * Checks if selected widget possess the modal lock.
     * @param w
     *            the selected widget
     * @return <code>true</code> if the widget has the modal
     *         lock, <code>false</code> otherwise
     */
    public boolean hasModalLock(Widget w) {
        return modalLockingWidget == w;
    }

    /**
     * Release the modal lock if it is owned by selected
     * widget.
     * @param by
     *            widget releasing the modal lock
     */
    public void modalLockRelease(Widget by) {
        if (modalLockingWidget == by) {
            Log.logger.debug("Modal lock released by " + by);
            modalLockingWidget = null;
        }
    }

    /**
     * Requests the modal lock for selected widget.
     * <p>
     * If a widget has keyboard focus and is not a
     * descendant of the widget acquiring the modal lock it
     * loses the keyboard focus.
     * @param by
     *            the widget requesting a modal lock
     * @see #getModalLockWidget()
     */
    public void modalLockRequest(Widget by) {
        if (by == modalLockingWidget) return;
        Log.logger.debug("Modal lock acquired by " + by);
        modalLockingWidget = by;
        if (focusedWidget != null && !modalLockingWidget.isAncestorOf(focusedWidget)) focusRelease(focusedWidget);
    }
}

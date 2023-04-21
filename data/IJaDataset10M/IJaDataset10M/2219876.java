package org.nakedobjects.plugins.dnd;

/**
 * Details where to move the focus to.
 */
public interface FocusManager {

    /**
     * The next view within the container to move the focus to; move to next field.
     */
    void focusNextView();

    /**
     * The previous view within the container to move the focus to; move to previous field.
     */
    void focusPreviousView();

    /**
     * The parent view within the container to move the focus to; move up to containing view in the hierachy.
     */
    void focusParentView();

    /**
     * The first child view within the container to move the focus to; move down to the first view within the
     * current view.
     */
    void focusFirstChildView();

    void focusLastChildView();

    void focusInitialChildView();

    View getFocus();

    void setFocus(View view);
}

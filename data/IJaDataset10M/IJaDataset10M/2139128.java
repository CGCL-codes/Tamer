package org.nakedobjects.viewer.skylark;

import org.nakedobjects.utility.Assert;
import org.nakedobjects.utility.NakedObjectRuntimeException;
import org.nakedobjects.utility.ToString;

/**
 * Abstract focus manager that uses the set of views to move focus between from the concrete subclass.
 * 
 * @see #getChildViews()
 */
public abstract class AbstractFocusManager implements FocusManager {

    protected View container;

    protected View focus;

    private View initialFocus;

    public AbstractFocusManager(View container) {
        this(container, null);
    }

    public AbstractFocusManager(View container, View initalFocus) {
        Assert.assertNotNull(container);
        this.container = container;
        this.initialFocus = initalFocus;
        focus = initalFocus;
    }

    /**
     * Throws a NakedObjectRuntimeException if the specified view is available to this focus manager.
     */
    private void checkCanFocusOn(View view) {
        View[] views = getChildViews();
        boolean valid = view == container.getView();
        for (int j = 0; valid == false && j < views.length; j++) {
            if (views[j] == view) {
                valid = true;
            }
        }
        if (!valid) {
        }
    }

    public void focusFirstChildView() {
        View[] views = getChildViews();
        for (int j = 0; j < views.length; j++) {
            if (views[j].canFocus()) {
                setFocus(views[j]);
                return;
            }
        }
        return;
    }

    public void focusInitialChildView() {
        if (initialFocus == null) {
            focusFirstChildView();
        } else {
            setFocus(initialFocus);
        }
    }

    public void focusLastChildView() {
        View[] views = getChildViews();
        for (int j = views.length - 1; j > 0; j--) {
            if (views[j].canFocus()) {
                setFocus(views[j]);
                return;
            }
        }
        return;
    }

    public void focusNextView() {
        View[] views = getChildViews();
        for (int i = 0; i < views.length; i++) {
            if (views[i] == focus) {
                for (int j = i + 1; j < views.length; j++) {
                    if (views[j].canFocus()) {
                        setFocus(views[j]);
                        return;
                    }
                }
                for (int j = 0; j < i; j++) {
                    if (views[j].canFocus()) {
                        setFocus(views[j]);
                        return;
                    }
                }
                return;
            }
        }
    }

    public void focusParentView() {
        container.getFocusManager().setFocus(container.getFocusManager().getFocus());
    }

    public void focusPreviousView() {
        View[] views = getChildViews();
        for (int i = 0; i < views.length; i++) {
            if (views[i] == focus) {
                for (int j = i - 1; j >= 0; j--) {
                    if (views[j].canFocus()) {
                        setFocus(views[j]);
                        return;
                    }
                }
                for (int j = views.length - 1; j > i; j--) {
                    if (views[j].canFocus()) {
                        setFocus(views[j]);
                        return;
                    }
                }
                return;
            }
        }
        throw new NakedObjectRuntimeException("Can't move to previous peer from " + focus);
    }

    protected abstract View[] getChildViews();

    public View getFocus() {
        return focus;
    }

    public void setFocus(View view) {
        checkCanFocusOn(view);
        if (view != null && view.canFocus()) {
            if ((focus != null) && (focus != view)) {
                focus.focusLost();
                focus.markDamaged();
            }
            focus = view;
            focus.focusReceived();
            view.markDamaged();
        }
    }

    public String toString() {
        ToString str = new ToString(this);
        str.append("container", container);
        str.append("initialFocus", initialFocus);
        str.append("focus", focus);
        return str.toString();
    }
}

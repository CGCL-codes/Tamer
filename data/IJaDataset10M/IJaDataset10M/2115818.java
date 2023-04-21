package org.nakedobjects.plugins.dnd.view.window;

import org.nakedobjects.plugins.dnd.view.View;
import org.nakedobjects.plugins.dnd.view.border.ScrollBorder;

public class DialogBorder extends AbstractWindowBorder {

    public DialogBorder(final View wrappedView, final boolean scrollable) {
        super(scrollable ? new ScrollBorder(wrappedView) : wrappedView);
        setControls(new WindowControl[] { new CloseWindowControl(this) });
    }

    @Override
    protected String title() {
        return getContent().windowTitle();
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/DialogBorder [" + getSpecification() + "]";
    }
}

package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.action.CloseWindowControl;
import org.nakedobjects.plugins.dnd.viewer.action.WindowControl;

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

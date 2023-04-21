package pku.edu.tutor.commands;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Widget;

public class CommandTarget {

    private Widget widget;

    private Object context;

    public CommandTarget(Widget widget, Object context) {
        this.widget = widget;
        this.context = context;
    }

    public void ensureVisible() {
    }

    public Widget getWidget() {
        return widget;
    }

    public Object getContext() {
        return context;
    }

    public void setFocus() {
        ensureVisible();
        Display display = widget.getDisplay();
        if (widget instanceof Control) {
            Control c = (Control) widget;
            if (!c.equals(display.getFocusControl())) c.setFocus();
        }
    }
}

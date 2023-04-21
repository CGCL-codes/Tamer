package org.nakedobjects.plugins.dnd.viewer.lookup;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;

public abstract class DropDownAxis implements ViewAxis {

    private final View view;

    public DropDownAxis(final View originalView) {
        this.view = originalView;
    }

    public View getOriginalView() {
        return view;
    }

    public abstract void setSelection(OptionContent selectedContent);
}

package org.pescuma.jfg.gui.swt;

import static org.eclipse.swt.layout.GridData.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

class SWTUtils {

    public static GridLayout createBorderlessGridLayout(int numColumns, boolean makeColumnsEqualWidth) {
        GridLayout layout = new GridLayout(numColumns, makeColumnsEqualWidth);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        return layout;
    }

    public static Composite setupHorizontalComposite(Composite composite, int horizontalSpan) {
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = horizontalSpan;
        composite.setLayoutData(gd);
        composite.setLayout(createBorderlessGridLayout(1, true));
        return composite;
    }

    public static int layoutHintsToGridDataStyle(int layoutHints) {
        int style = 0;
        if ((layoutHints & JfgFormData.HORIZONTAL_SHRINK) == 0) style += FILL_HORIZONTAL;
        if ((layoutHints & JfgFormData.VERTICAL_FILL) != 0) style += FILL_VERTICAL;
        return style;
    }
}

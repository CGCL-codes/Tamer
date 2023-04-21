package org.designerator.media.util.test.scroller.layout;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

class PictureLabelLayout extends Layout {

    Point iExtent, tExtent;

    protected Point computeSize(Composite composite, int wHint, int hHint, boolean changed) {
        Control[] children = composite.getChildren();
        if (changed || iExtent == null || tExtent == null) {
            iExtent = children[0].computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
            tExtent = children[1].computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
        }
        int width = iExtent.x + 5 + tExtent.x;
        int height = Math.max(iExtent.y, tExtent.y);
        return new Point(width + 2, height + 2);
    }

    protected void layout(Composite composite, boolean changed) {
        Control[] children = composite.getChildren();
        if (changed || iExtent == null || tExtent == null) {
            iExtent = children[0].computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
            tExtent = children[1].computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
        }
        children[0].setBounds(1, 1, iExtent.x, iExtent.y);
        children[1].setBounds(iExtent.x + 5, 1, tExtent.x, tExtent.y);
    }
}

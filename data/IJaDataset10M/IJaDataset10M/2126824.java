package org.nakedobjects.viewer.skylark.tree;

import org.nakedobjects.NakedObjects;
import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Size;
import org.nakedobjects.viewer.skylark.Style;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.Viewer;
import org.nakedobjects.viewer.skylark.special.ResizeBorder;

public class TreeBrowserResizeBorder extends ResizeBorder {

    public static final int BORDER_WIDTH = NakedObjects.getConfiguration().getInteger(Viewer.PROPERTY_BASE + "resize.border", 5);

    public TreeBrowserResizeBorder(View view) {
        super(view, RIGHT, BORDER_WIDTH);
    }

    protected void drawResizeBorder(Canvas canvas, Size size) {
        int y1 = 0;
        int y2 = getSize().getHeight() - 1;
        for (int i = 0; i < getRight(); i++) {
            int x = getSize().getWidth() - i - 1;
            canvas.drawLine(x, y1, x, y2, Style.SECONDARY1);
        }
    }
}

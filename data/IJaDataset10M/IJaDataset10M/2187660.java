package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class BlackHoleTab extends GraphicsTab {

    int size = 1;

    public BlackHoleTab(GraphicsExample example) {
        super(example);
    }

    public String getText() {
        return GraphicsExample.getResourceString("BlackHole");
    }

    public boolean isAnimated() {
        return true;
    }

    public void next(int width, int height) {
        if (size > width * 3 / 2) size = 0; else size += 10;
    }

    public void paint(GC gc, int width, int height) {
        Display display = Display.getCurrent();
        gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.fillOval((width - size) / 2, (height - size) / 2, size, size);
    }
}

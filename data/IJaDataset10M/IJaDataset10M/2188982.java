package com.peterhi.shape;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

public interface Shape {

    String getId();

    Canvas getCanvas();

    void draw(Display d, GC gc);

    void repaint();
}

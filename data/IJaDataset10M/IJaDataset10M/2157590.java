package org.nakedobjects.plugins.dnd.view.debug;

import org.nakedobjects.commons.debug.DebugInfo;
import org.nakedobjects.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.drawing.Bounds;
import org.nakedobjects.plugins.dnd.drawing.DebugCanvas;
import org.nakedobjects.plugins.dnd.view.View;

public class DebugDrawing implements DebugInfo {

    private final View view;

    public DebugDrawing(final View display) {
        this.view = display;
    }

    public void debugData(final DebugString debug) {
        view.draw(new DebugCanvas(debug, new Bounds(view.getBounds())));
    }

    public String debugTitle() {
        return "Drawing";
    }
}

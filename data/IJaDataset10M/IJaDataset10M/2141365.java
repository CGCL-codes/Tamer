package org.nakedobjects.plugins.dnd.view.debug;

import org.nakedobjects.commons.debug.DebugInfo;
import org.nakedobjects.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.drawing.Bounds;
import org.nakedobjects.plugins.dnd.drawing.DebugCanvasAbsolute;
import org.nakedobjects.plugins.dnd.view.View;

public class DebugDrawingAbsolute implements DebugInfo {

    private final View view;

    public DebugDrawingAbsolute(final View display) {
        this.view = display;
    }

    public void debugData(final DebugString debug) {
        view.draw(new DebugCanvasAbsolute(debug, new Bounds(view.getAbsoluteLocation(), view.getSize())));
    }

    public String debugTitle() {
        return "Drawing (Absolute)";
    }
}

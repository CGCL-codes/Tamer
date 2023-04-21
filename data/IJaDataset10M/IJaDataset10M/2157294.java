package org.nakedobjects.plugins.dndviewer.viewer.border;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dndviewer.Canvas;
import org.nakedobjects.plugins.dndviewer.ColorsAndFonts;
import org.nakedobjects.plugins.dndviewer.Toolkit;
import org.nakedobjects.plugins.dndviewer.View;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Color;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Size;

/**
 * A line border draws a simple box around a view of a given width and color.
 */
public class LineBorder extends AbstractBorder {

    private final Color color;

    private final int arcRadius;

    public LineBorder(final View wrappedView) {
        this(1, wrappedView);
    }

    public LineBorder(final int size, final View wrappedView) {
        this(size, 0, Toolkit.getColor(ColorsAndFonts.COLOR_BLACK), wrappedView);
    }

    public LineBorder(final int size, final int arcRadius, final View wrappedView) {
        this(size, arcRadius, Toolkit.getColor(ColorsAndFonts.COLOR_BLACK), wrappedView);
    }

    public LineBorder(final Color color, final View wrappedView) {
        this(1, 0, color, wrappedView);
    }

    public LineBorder(final int width, final Color color, final View wrappedView) {
        this(width, 0, color, wrappedView);
    }

    public LineBorder(final int width, final int arcRadius, final Color color, final View wrappedView) {
        super(wrappedView);
        top = width;
        left = width;
        bottom = width;
        right = width;
        this.arcRadius = arcRadius;
        this.color = color;
    }

    @Override
    protected void debugDetails(final DebugString debug) {
        debug.append("LineBorder " + top + " pixels\n");
    }

    @Override
    public void draw(final Canvas canvas) {
        final Size s = getSize();
        final int width = s.getWidth();
        for (int i = 0; i < left; i++) {
            canvas.drawRoundedRectangle(i, i, width - 2 * i, s.getHeight() - 2 * i, arcRadius, arcRadius, color);
        }
        super.draw(canvas);
    }

    @Override
    public String toString() {
        return wrappedView.toString() + "/LineBorder";
    }
}

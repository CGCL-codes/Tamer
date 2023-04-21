package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;

public class CloseWindow3DRender implements CloseWindowRender {

    public void draw(Canvas canvas, int width, int height, boolean isDisabled, boolean isOver, boolean isPressed) {
        final int x = 0;
        final int y = 0;
        canvas.drawRectangle(x + 1, y + 1, width - 1, height - 1, Toolkit.getColor(ColorsAndFonts.COLOR_WHITE));
        canvas.drawRectangle(x, y, width - 1, height - 1, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1));
        final Color crossColor = Toolkit.getColor(ColorsAndFonts.COLOR_BLACK);
        canvas.drawLine(x + 4, y + 3, x + 10, y + 9, crossColor);
        canvas.drawLine(x + 5, y + 3, x + 11, y + 9, crossColor);
        canvas.drawLine(x + 10, y + 3, x + 4, y + 9, crossColor);
        canvas.drawLine(x + 11, y + 3, x + 5, y + 9, crossColor);
    }
}

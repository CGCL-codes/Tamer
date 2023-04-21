package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Toolkit;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.abstracts.CloseViewOption;
import org.nakedobjects.viewer.skylark.abstracts.WindowControl;
import org.nakedobjects.viewer.skylark.drawing.Color;

public class CloseWindowControl extends WindowControl {

    public CloseWindowControl(final View target) {
        super(new CloseViewOption(), target);
    }

    public void draw(final Canvas canvas) {
        int x = 0;
        int y = 0;
        canvas.drawRectangle(x + 1, y + 1, WIDTH - 1, HEIGHT - 1, Toolkit.getColor("white"));
        canvas.drawRectangle(x, y, WIDTH - 1, HEIGHT - 1, Toolkit.getColor("secondary1"));
        Color crossColor = Toolkit.getColor("black");
        canvas.drawLine(x + 4, y + 3, x + 10, y + 9, crossColor);
        canvas.drawLine(x + 5, y + 3, x + 11, y + 9, crossColor);
        canvas.drawLine(x + 10, y + 3, x + 4, y + 9, crossColor);
        canvas.drawLine(x + 11, y + 3, x + 5, y + 9, crossColor);
    }
}

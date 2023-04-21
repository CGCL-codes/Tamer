package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.utility.ToString;
import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Color;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.Size;
import org.nakedobjects.viewer.skylark.core.AbstractView;

public abstract class DrawingView extends AbstractView {

    private Size requiredSize;

    public DrawingView(Content content) {
        super(content, null, null);
    }

    public void draw(Canvas canvas) {
        final int width = requiredSize.getWidth();
        final int height = requiredSize.getHeight();
        final int left = 0, top = 0;
        final int right = 10 + width - 1 + 10;
        final int bottom = 10 + height - 1 + 10;
        canvas.drawLine(left, top + 10, right, top + 10, Color.GRAY);
        canvas.drawLine(left, bottom - 10, right, bottom - 10, Color.GRAY);
        canvas.drawLine(left + 10, top, left + 10, bottom, Color.GRAY);
        canvas.drawLine(right - 10, top, right - 10, bottom, Color.GRAY);
        canvas.drawRectangle(left + 10, top + 10, width - 1, height - 1, Color.LIGHT_GRAY);
        draw(canvas, left + 10, top + 10);
    }

    protected abstract void draw(Canvas canvas, int x, int y);

    public Size getRequiredSize() {
        Size s = new Size(requiredSize);
        s.extend(20, 20);
        return s;
    }

    public void setRequiredSize(Size size) {
        this.requiredSize = size;
    }

    public String toString() {
        ToString ts = new ToString(this);
        ts.append("size", requiredSize);
        toString(ts);
        return ts.toString();
    }

    protected abstract void toString(ToString ts);
}

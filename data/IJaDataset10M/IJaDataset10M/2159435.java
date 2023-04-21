package com.peterhi.client.ui.widgets.whiteboard.tracks;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import com.peterhi.client.ui.widgets.Whiteboard;
import com.peterhi.client.ui.widgets.whiteboard.Shape;
import com.peterhi.util.Callback;

public class LineTrack extends AbstractTrack {

    protected boolean down;

    protected int downx, downy, movex, movey;

    public LineTrack(Whiteboard w, Shape s, Callback callback) {
        super(w, s, callback);
    }

    public void mouseDown(MouseEvent e) {
        if (e.button == 1) {
            downx = e.x;
            downy = e.y;
            down = true;
        }
    }

    public void mouseMove(MouseEvent e) {
        if (down) {
            movex = e.x;
            movey = e.y;
            getWhiteboard().redraw();
        }
    }

    public void mouseUp(MouseEvent e) {
        if (e.button == 1) {
            callback(new int[] { downx, downy, movex, movey });
            down = false;
            downx = 0;
            downy = 0;
            movex = 0;
            movey = 0;
            getWhiteboard().redraw();
        }
    }

    public void paintControl(PaintEvent e) {
        GC g = e.gc;
        Color back = g.getBackground();
        Color old = g.getForeground();
        g.setForeground(back);
        g.drawLine(downx, downy, movex, movey);
        g.setLineDash(new int[] { 5, 5 });
        g.setForeground(old);
        g.drawLine(downx, downy, movex, movey);
    }
}

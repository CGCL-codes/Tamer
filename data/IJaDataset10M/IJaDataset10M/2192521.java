package com.peterhi.client.ui.widgets.whiteboard.editors;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import com.peterhi.client.ui.widgets.Whiteboard;
import com.peterhi.client.ui.widgets.whiteboard.AbstractElement;

/**
 * An <c>Editor</c> whose visual cue is based on
 * an ellipse.
 * @author YUN TAO HAI (hytparadisee)
 *
 */
public class OvalCreator extends RectangleCreator {

    /**
	 * Constructor.
	 * @param w Owner <c>Whiteboard</c>.
	 * @param s Editing <c>Element</c>.
	 * @param callback Progress <c>Callback</c>.
	 */
    public OvalCreator(Whiteboard w, AbstractElement e) {
        super(w, e);
    }

    @Override
    public void paintControl(PaintEvent e) {
        GC g = e.gc;
        data.x = Math.min(downx, movex);
        data.y = Math.min(downy, movey);
        data.width = Math.abs(downx - movex);
        data.height = Math.abs(downy - movey);
        Color back = g.getBackground();
        Color old = g.getForeground();
        g.setForeground(back);
        g.drawOval(data.x, data.y, data.width, data.height);
        g.setLineDash(new int[] { 5, 5 });
        g.setForeground(old);
        g.drawOval(data.x, data.y, data.width, data.height);
    }
}

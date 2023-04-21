package de.fuh.xpairtise.plugin.ui.xpviews.whiteboard.util;

import java.util.LinkedList;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

/**
 * This class implements the IDrawContext interface for erase draw method.
 * 
 */
public class EraserDrawContext implements IDrawContext {

    private LinkedList<WhiteboardPoint> points = new LinkedList<WhiteboardPoint>();

    private Canvas canvas;

    private Image copy;

    /**
   * Constructs a new EraserDrawContext for drawing eraser elements.
   * 
   * @param canvas 
   *          canvas where to draw on
   * @param img 
   *          copy area
   */
    public EraserDrawContext(Canvas canvas, Image img) {
        this.canvas = canvas;
        this.copy = img;
    }

    /**
   * Draw a new eraser element.
   * @param point  point where to draw
   * @param draw  true drawing is enabled
   */
    public void newPoint(WhiteboardPoint point, boolean draw) {
        if (draw) {
            drawSync(canvas, point);
        }
        synchronized (points) {
            points.addLast(point);
        }
    }

    private void draw(GC gc, WhiteboardPoint p) {
        gc.fillRectangle(p.getX() - 5, p.getY() - 5, 11, 11);
    }

    private void drawSync(final Canvas canvas, final WhiteboardPoint p) {
        Display display = canvas.getDisplay();
        if (display != null && !display.isDisposed()) {
            display.syncExec(new Runnable() {

                public void run() {
                    GC gc = new GC(canvas);
                    draw(gc, p);
                    gc.dispose();
                    if ((copy != null) && !(copy.isDisposed())) {
                        GC gcBuff = new GC(copy);
                        draw(gcBuff, p);
                        gcBuff.dispose();
                    }
                }
            });
        }
    }

    public void redraw(GC gc) {
        synchronized (points) {
            for (WhiteboardPoint x : points) {
                draw(gc, x);
                if ((copy != null) && !(copy.isDisposed())) {
                    GC gcBuff = new GC(copy);
                    draw(gcBuff, x);
                    gcBuff.dispose();
                }
            }
        }
    }

    public void removeAll() {
        synchronized (points) {
            points.clear();
        }
    }
}

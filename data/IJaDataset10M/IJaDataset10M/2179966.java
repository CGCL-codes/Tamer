package view.components;

import view.*;
import eve.fx.Color;
import eve.fx.Graphics;
import eve.fx.Rect;
import eve.sys.Event;
import eve.sys.EventListener;
import eve.ui.event.PenEvent;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author JPEXS
 */
public class ColorPanel extends Panel {

    private Color color;

    private ArrayList<ColorListener> colorListeners = new ArrayList<ColorListener>();

    private boolean hasListener = false;

    public ColorPanel(Color color) {
        setPreferredSize(20, 20);
        this.color = color;
    }

    @Override
    public void doPaint(Graphics g, Rect r) {
        g.setColor(color);
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    public void addColorListener(ColorListener colorListener) {
        colorListeners.add(colorListener);
        if (!hasListener) {
            addListener(new EventListener() {

                public void onEvent(Event ev) {
                    if (ev.type == PenEvent.PEN_DOWN) {
                        for (Iterator<ColorListener> i = colorListeners.iterator(); i.hasNext(); ) {
                            i.next().colorSelected(color);
                        }
                    }
                }
            });
            hasListener = true;
        }
    }

    public void removeColorListener(ColorListener colorListener) {
        colorListeners.remove(colorListener);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }
}

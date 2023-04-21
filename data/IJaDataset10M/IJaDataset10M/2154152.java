package jasci.ui;

import jasci.util.*;
import jasci.ui.event.*;

public class ScrollBar extends Widget {

    int direction;

    int first, last, total;

    public static final int VERTICAL = 1;

    public static final int HORIZONTAL = 2;

    public ScrollBar(int direction) {
        this.direction = direction;
        this.first = 0;
        this.last = 1;
        this.total = 1;
    }

    public Dimension getMinimumSize() {
        return new Dimension(1, 1);
    }

    public Dimension getMaximumSize() {
        return new Dimension(direction == HORIZONTAL ? Dimension.INFINITE : 1, direction == VERTICAL ? Dimension.INFINITE : 1);
    }

    public void set(int first, int last, int total) {
        if (first == this.first && last == this.last && total == this.total) return;
        this.first = first;
        this.last = Math.max(last, first);
        this.total = total > 0 ? total : 1;
        fireChangeEvent(this);
        requestRepaint();
    }

    public void processKeyEvent(KeyEvent e) {
        if ((direction == HORIZONTAL && e.getId() == KeyEvent.KEY_RIGHT) || (direction == VERTICAL && e.getId() == KeyEvent.KEY_DOWN)) {
            if (last < total) {
                last++;
                first++;
                fireChangeEvent(this);
                requestRepaint();
                return;
            }
        } else if ((direction == HORIZONTAL && e.getId() == KeyEvent.KEY_LEFT) || (direction == VERTICAL && e.getId() == KeyEvent.KEY_UP)) {
            if (first > 0) {
                first--;
                last--;
                fireChangeEvent(this);
                requestRepaint();
                return;
            }
        }
        super.processKeyEvent(e);
    }

    public void fireChangeEvent(Widget wid) {
        ChangeEvent e = new ChangeEvent(wid);
        ChangeListener[] l = getListeners(ChangeListener.class);
        for (ChangeListener cl : l) cl.stateChanged(e);
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }

    public int getTotal() {
        return total;
    }

    public void paint(Painter p) {
        Color color = new Color(isFocused() ? Color.RED : Color.WHITE, Color.BLACK);
        Color fcolor = color.reverse();
        int length = direction == HORIZONTAL ? rect.width : rect.height;
        int n1, n2;
        n1 = first * length / total + ((first * length) % total == 0 ? 0 : 1);
        n2 = last * length / total - ((last * length) % total == 0 ? 0 : 1);
        if (n2 < n1) n2 = n1;
        switch(direction) {
            case HORIZONTAL:
                if (n1 > 0) p.drawHorizLine('-', 0, 0, n1, color);
                p.drawHorizLine('-', n1, 0, n2 - n1 + 1, fcolor);
                if (n2 < length) p.drawHorizLine('-', n2 + 1, 0, length - n2 + 1, color);
                break;
            case VERTICAL:
                if (n1 > 0) p.drawVertLine('|', 0, 0, n1, color);
                p.drawVertLine('|', 0, n1, n2 - n1 + 1, fcolor);
                if (n2 < length) p.drawVertLine('|', 0, n2 + 1, length - n2 + 1, color);
                break;
        }
    }
}

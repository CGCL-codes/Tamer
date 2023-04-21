package jasci.ui;

import jasci.util.*;
import jasci.ui.Widget;
import jasci.ui.event.*;

public class ComboBox<T> extends Widget {

    private ListModel<T> model;

    private ListCellRenderer<T> renderer;

    private ComboBoxEditor editor;

    private T prototype;

    private int fixedWidth, fixedHeight;

    private int selection;

    public ComboBox() {
        this(new DefaultListModel<T>());
    }

    public ComboBox(T[] data) {
        DefaultListModel<T> m = new DefaultListModel<T>();
        for (T o : data) m.add(o);
        init(m);
    }

    public ComboBox(ListModel<T> model) {
        init(model);
    }

    private void init(ListModel<T> model) {
        this.model = model;
        this.renderer = new DefaultCellRenderer<T>();
        this.editor = null;
        this.fixedWidth = 0;
        this.fixedHeight = 0;
        this.prototype = null;
        this.selection = model.size() > 0 ? 0 : -1;
        setFocusable(true);
    }

    public void paint(Painter p) {
        boolean focused = isFocused();
        int y;
        Widget w;
        Color c = new Color(focused ? Color.RED : Color.WHITE, Color.BLACK);
        for (y = 0; y < rect.height; y++) {
            p.drawText("[]", 0, y, c);
            p.drawHorizLine(' ', 2, y, rect.width - 3, c);
        }
        if (selection >= 0) {
            w = renderer.getWidget(this, model.elementAt(selection), selection, false, focused);
            w.setRectangle(2, 0, rect.width - 2, rect.height);
            w.paint(p.clip(w.rect));
        }
    }

    public Dimension getMinimumSize() {
        int minx = 0, miny = 0, i;
        Widget w;
        Dimension min;
        if (fixedWidth > 0) minx = fixedWidth + 3;
        if (fixedHeight > 0) miny = fixedHeight;
        if (prototype != null) {
            w = renderer.getWidget(this, prototype, 0, false, false);
            min = w.getMinimumSize();
            if (minx == 0) minx = min.width + 2;
            if (miny == 0) miny = min.height;
        }
        if (minx > 0 && miny > 0) return new Dimension(minx, miny);
        if (model.size() == 0) return new Dimension(3, 1);
        for (i = 0; i < model.size(); i++) {
            w = renderer.getWidget(this, model.elementAt(i), i, false, false);
            min = w.getMinimumSize();
            minx = Math.max(minx, min.width);
            miny = Math.max(miny, min.height);
        }
        return new Dimension(minx + 3, miny);
    }

    public Dimension getMaximumSize() {
        Dimension max = getMinimumSize();
        max.width = Dimension.INFINITE;
        return max;
    }

    public void processKeyEvent(KeyEvent e) {
        if (e.getId() == KeyEvent.KEY_DOWN) {
            selection = Math.min(model.size() - 1, selection + 1);
            requestRepaint();
        } else if (e.getId() == KeyEvent.KEY_UP) {
            if (model.size() == 0) return;
            selection = Math.max(selection - 1, 0);
            requestRepaint();
        } else super.processKeyEvent(e);
    }
}

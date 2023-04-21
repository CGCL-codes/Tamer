package jasci.ui.text;

import jasci.ui.Painter;
import jasci.util.Point;
import jasci.ui.Widget;
import jasci.ui.event.DocumentEvent;

public abstract class View {

    public static final int X_AXIS = 1;

    public static final int Y_AXIS = 2;

    protected Element element;

    protected View parent;

    int columns, rows;

    public View(Element element) {
        this.element = element;
        this.parent = null;
        this.columns = 0;
        this.rows = 0;
    }

    public Element getElement() {
        return element;
    }

    public void setParent(View parent) {
        if (this.parent != null) return;
        this.parent = parent;
        setupChildren();
    }

    public View getParent() {
        return this.parent;
    }

    public Widget getWidget() {
        if (parent == null) return null;
        return parent.getWidget();
    }

    public void invalidate() {
    }

    public abstract int getPreferredSpan(int units, int axis);

    public abstract void paint(Painter p, Highlight[] highlights);

    public void setSize(int width, int height) {
        if (columns == width && rows == height) return;
        columns = width;
        rows = height;
        invalidate();
    }

    public abstract Point getPointByOffset(int offset);

    public abstract int getOffsetByPoint(int x, int y);

    public int getOffsetByPoint(Point p) {
        if (p == null) return 0;
        return getOffsetByPoint(p.x, p.y);
    }

    public void insertNotification(DocumentEvent e) {
    }

    public void removeNotification(DocumentEvent e) {
    }

    public void preferenceChanged(View child, boolean xAxis, boolean yAxis) {
        if (parent == null) return;
        parent.preferenceChanged(child, xAxis, yAxis);
    }

    public void redraw(View child, int x, int y, int width, int height) {
        if (parent == null) return;
        parent.redraw(this, x, y, width, height);
    }

    public int getStartOffset() {
        return element.getStartOffset();
    }

    public int getEndOffset() {
        return element.getEndOffset();
    }

    public View[] breakView(int axis, int len) {
        View[] views = new View[1];
        views[0] = this;
        return views;
    }

    public void setupChildren() {
    }
}

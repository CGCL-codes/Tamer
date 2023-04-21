package org.dyno.visual.swing.widgets.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import org.dyno.visual.swing.base.FieldProperty;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.ILayoutBean;
import org.dyno.visual.swing.plugin.spi.IWidgetPropertyDescriptor;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;

public class BoxLayoutAdapter extends LayoutAdapter implements ILayoutBean {

    private Thumb thumb;

    public void initConainerLayout(Container parent, IProgressMonitor monitor) {
        BoxLayout boxLayout = new BoxLayout(parent, BoxLayout.LINE_AXIS);
        parent.setLayout(boxLayout);
    }

    private boolean isHorizontal() {
        BoxLayout layout = (BoxLayout) container.getLayout();
        int axis = getAxis(layout);
        return axis == BoxLayout.LINE_AXIS || axis == BoxLayout.X_AXIS;
    }

    private int getAxis(BoxLayout layout) {
        Object value = JavaUtil.getField(layout, "axis");
        return value == null ? 0 : ((Integer) value);
    }

    private Thumb getClosetThumb(Point hoverPoint) {
        boolean horizontal = isHorizontal();
        int x = 0, y = 0;
        int w = container.getWidth();
        int h = container.getHeight();
        int size = container.getComponentCount();
        if (size == 0) {
            if (horizontal) {
                x = THUMB_PAD / 2;
                y = h / 2;
            } else {
                x = w / 2;
                y = THUMB_PAD / 2;
            }
            return new Thumb(0, x, y);
        } else {
            double mind = Double.MAX_VALUE;
            int pi = 0;
            for (int i = 0; i < size; i++) {
                Component child = container.getComponent(i);
                Rectangle bounds = child.getBounds();
                int my = bounds.y + THUMB_PAD / 2;
                if (!horizontal) my = bounds.y;
                int mx = bounds.x;
                if (!horizontal) mx = bounds.x + bounds.width / 2;
                double currentd = hoverPoint.distance(mx, my);
                double delta = mind - currentd;
                if (delta > 0) {
                    mind = currentd;
                    x = mx;
                    y = my;
                    pi = i;
                }
            }
            Component child = container.getComponent(size - 1);
            Rectangle bounds = child.getBounds();
            int my = bounds.y + THUMB_PAD / 2;
            if (!horizontal) my = bounds.y + bounds.height;
            int mx = bounds.x + bounds.width;
            if (!horizontal) mx = bounds.x + bounds.width / 2;
            double currentd = hoverPoint.distance(mx, my);
            double delta = mind - currentd;
            if (delta > 0) {
                mind = currentd;
                x = mx;
                y = my;
                pi = size;
            }
            return new Thumb(pi, x, y);
        }
    }

    public boolean dragEnter(Point p) {
        CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
        parent.setMascotLocation(p);
        thumb = getClosetThumb(p);
        return true;
    }

    public boolean dragExit(Point p) {
        thumb = null;
        return true;
    }

    public boolean dragOver(Point p) {
        CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
        parent.setMascotLocation(p);
        thumb = getClosetThumb(p);
        return true;
    }

    public boolean drop(Point p) {
        CompositeAdapter parent = (CompositeAdapter) WidgetAdapter.getWidgetAdapter(container);
        thumb = getClosetThumb(p);
        parent.clearAllSelected();
        for (WidgetAdapter todrop : parent.getDropWidget()) {
            int size = container.getComponentCount();
            if (thumb.pi == size) container.add(todrop.getParentContainer()); else container.add(todrop.getParentContainer(), thumb.pi);
            todrop.setSelected(true);
        }
        parent.getRootAdapter().getWidget().validate();
        thumb = null;
        return true;
    }

    public void paintHovered(Graphics g) {
        if (thumb != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(STROKE);
            g2d.setColor(RED_COLOR);
            g2d.drawRect(thumb.x - THUMB_PAD / 2, thumb.y - THUMB_PAD / 2, THUMB_PAD, THUMB_PAD);
        }
    }

    public boolean cloneLayout(JComponent panel) {
        panel.setLayout(copyLayout(panel));
        int count = container.getComponentCount();
        for (int i = 0; i < count; i++) {
            JComponent child = (JComponent) container.getComponent(i);
            WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
            panel.add(cAdapter.cloneWidget());
        }
        return true;
    }

    protected LayoutManager copyLayout(Container con) {
        BoxLayout layout = (BoxLayout) container.getLayout();
        int axis = getAxis(layout);
        return new BoxLayout(con, axis);
    }

    protected IWidgetPropertyDescriptor[] getLayoutProperties() {
        FieldProperty axisProperty = new FieldProperty("axis", "axis", BoxLayout.class, new BoxLayoutAxisRenderer(), new BoxLayoutAxisEditor());
        return new IWidgetPropertyDescriptor[] { axisProperty };
    }

    public void addChildByConstraints(Component child, Object constraints) {
        container.add(child);
    }

    public Object getChildConstraints(Component child) {
        return null;
    }
}

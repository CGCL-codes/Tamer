package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.util.List;
import javax.swing.JPanel;
import org.dyno.visual.swing.base.Azimuth;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.LayoutAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;

public class JPanelDesignOperation extends CompositeDesignOperation {

    private void resize_widget(Point p) {
        int state = adaptable.getState();
        Dimension min = new Dimension(10, 10);
        List<WidgetAdapter> dropWidgets = adaptable.getDropWidget();
        assert !dropWidgets.isEmpty();
        Component beResized = dropWidgets.get(0).getParentContainer();
        Dimension size = beResized.getSize();
        Point hotspot = adaptable.getMascotLocation();
        int w = min.width;
        int h = min.height;
        switch(state) {
            case Azimuth.STATE_BEAN_RESIZE_RIGHT_BOTTOM:
                w = p.x - hotspot.x;
                h = p.y - hotspot.y;
                break;
            case Azimuth.STATE_BEAN_RESIZE_BOTTOM:
                w = size.width;
                h = p.y - hotspot.y;
                break;
            case Azimuth.STATE_BEAN_RESIZE_LEFT_BOTTOM:
                w = size.width + hotspot.x - p.x;
                h = p.y - hotspot.y;
                hotspot.x = p.x;
                break;
            case Azimuth.STATE_BEAN_RESIZE_LEFT:
                w = size.width + hotspot.x - p.x;
                h = size.height;
                hotspot.x = p.x;
                break;
            case Azimuth.STATE_BEAN_RESIZE_LEFT_TOP:
                w = size.width + hotspot.x - p.x;
                h = size.height + hotspot.y - p.y;
                hotspot.x = p.x;
                hotspot.y = p.y;
                break;
            case Azimuth.STATE_BEAN_RESIZE_TOP:
                w = size.width;
                h = size.height + hotspot.y - p.y;
                hotspot.y = p.y;
                break;
            case Azimuth.STATE_BEAN_RESIZE_RIGHT_TOP:
                w = p.x - hotspot.x;
                h = size.height + hotspot.y - p.y;
                hotspot.y = p.y;
                break;
            case Azimuth.STATE_BEAN_RESIZE_RIGHT:
                w = p.x - hotspot.x;
                h = size.height;
                break;
        }
        if (w <= min.width) w = min.width;
        if (h <= min.height) h = min.height;
        adaptable.setMascotLocation(hotspot);
        beResized.setSize(w, h);
        beResized.doLayout();
    }

    @Override
    public boolean drop(Point p) {
        if (isDroppingPopup() || isDroppingMenuItem() || isDroppingMenuBar()) return super.drop(p);
        JPanel jpanel = (JPanel) adaptable.getWidget();
        LayoutManager layout = jpanel.getLayout();
        if (layout == null) {
            int state = adaptable.getState();
            adaptable.clearAllSelected();
            for (WidgetAdapter adapter : adaptable.getDropWidget()) {
                Component child = adapter.getParentContainer();
                Point htsp = adapter.getHotspotPoint();
                switch(state) {
                    case Azimuth.STATE_BEAN_HOVER:
                        child.setLocation(p.x - htsp.x, p.y - htsp.y);
                        break;
                    default:
                        Point pt = adaptable.getMascotLocation();
                        child.setLocation(pt.x - htsp.x, pt.y - htsp.y);
                        break;
                }
                jpanel.add(child);
                adapter.requestNewName();
                adapter.setSelected(true);
                adapter.setDirty(true);
            }
            adaptable.setDirty(true);
            adaptable.doLayout();
            adaptable.getWidget().validate();
            adaptable.repaintDesigner();
            return true;
        } else {
            LayoutAdapter layoutAdapter = ((CompositeAdapter) adaptable).getLayoutAdapter();
            WidgetAdapter[] copy = new WidgetAdapter[adaptable.getDropWidget().size()];
            adaptable.getDropWidget().toArray(copy);
            if (layoutAdapter.drop(p)) {
                adaptable.clearAllSelected();
                for (WidgetAdapter adapter : copy) {
                    adapter.requestNewName();
                    adapter.setSelected(true);
                }
                adaptable.setDirty(true);
                layoutAdapter.setContainer(jpanel);
                adaptable.doLayout();
                adaptable.getWidget().validate();
                adaptable.repaintDesigner();
                return true;
            } else return false;
        }
    }

    @Override
    public boolean dragEnter(Point p) {
        if (isDroppingMenuItem() || isDroppingMenuBar()) return super.dragEnter(p);
        JPanel jpanel = (JPanel) adaptable.getWidget();
        LayoutManager layout = jpanel.getLayout();
        if (layout != null) {
            LayoutAdapter layoutAdapter = ((CompositeAdapter) adaptable).getLayoutAdapter();
            return layoutAdapter.dragEnter(p);
        } else return true;
    }

    @Override
    public boolean dragExit(Point p) {
        if (isDroppingMenuItem() || isDroppingMenuBar()) return super.dragExit(p);
        JPanel jpanel = (JPanel) adaptable.getWidget();
        LayoutManager layout = jpanel.getLayout();
        if (layout != null) {
            LayoutAdapter layoutAdapter = ((CompositeAdapter) adaptable).getLayoutAdapter();
            return layoutAdapter.dragExit(p);
        } else return true;
    }

    @Override
    public boolean dragOver(Point p) {
        if (isDroppingMenuItem() || isDroppingMenuBar()) return super.dragOver(p);
        JPanel jpanel = (JPanel) adaptable.getWidget();
        LayoutManager layout = jpanel.getLayout();
        if (layout == null) {
            int state = adaptable.getState();
            if (state == Azimuth.STATE_BEAN_HOVER) {
                adaptable.setMascotLocation(p);
            } else {
                resize_widget(p);
            }
            return true;
        } else {
            LayoutAdapter layoutAdapter = ((CompositeAdapter) adaptable).getLayoutAdapter();
            return layoutAdapter.dragOver(p);
        }
    }
}

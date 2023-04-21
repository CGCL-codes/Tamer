package org.dyno.visual.swing.widgets.design;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import org.dyno.visual.swing.base.Azimuth;
import org.dyno.visual.swing.plugin.spi.IPainter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.widgets.painter.JDesktopPanePainter;

public class JDesktopPaneDesignOperation extends CompositeDesignOperation {

    private void setForbid(boolean f) {
        JDesktopPanePainter jdpp = (JDesktopPanePainter) adaptable.getAdapter(IPainter.class);
        jdpp.setForbid(f);
    }

    private boolean isForbid() {
        JDesktopPanePainter jdpp = (JDesktopPanePainter) adaptable.getAdapter(IPainter.class);
        return jdpp.isForbid();
    }

    @Override
    public boolean dragEnter(Point p) {
        for (WidgetAdapter drop : adaptable.getDropWidget()) {
            Component comp = drop.getWidget();
            if (!(comp instanceof JInternalFrame)) {
                setForbid(true);
                return false;
            }
        }
        setForbid(false);
        return true;
    }

    @Override
    public boolean dragExit(Point p) {
        setForbid(false);
        return true;
    }

    @Override
    public boolean dragOver(Point p) {
        int state = adaptable.getState();
        if (state == Azimuth.STATE_BEAN_HOVER) {
            adaptable.setMascotLocation(p);
        } else {
            resize_widget(p);
        }
        return true;
    }

    private void resize_widget(Point p) {
        assert adaptable.getDropWidget().size() == 1;
        int state = adaptable.getState();
        Dimension min = new Dimension(10, 10);
        WidgetAdapter toBeResizedAdapter = adaptable.getDropWidget().get(0);
        Component toBeResized = toBeResizedAdapter.getWidget();
        Dimension size = toBeResized.getSize();
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
        toBeResized.setSize(w, h);
        toBeResizedAdapter.doLayout();
        toBeResizedAdapter.setDirty(true);
    }

    @Override
    public boolean drop(Point p) {
        if (isDroppingPopup()) return super.drop(p);
        if (!isForbid()) {
            JDesktopPane jtp = (JDesktopPane) adaptable.getWidget();
            adaptable.clearAllSelected();
            for (WidgetAdapter drop : adaptable.getDropWidget()) {
                JInternalFrame jif = (JInternalFrame) drop.getWidget();
                Point htsp = drop.getHotspotPoint();
                int state = adaptable.getState();
                switch(state) {
                    case Azimuth.STATE_BEAN_HOVER:
                        jif.setLocation(p.x - htsp.x, p.y - htsp.y);
                        break;
                    default:
                        Point pt = adaptable.getMascotLocation();
                        jif.setLocation(pt.x - htsp.x, pt.y - htsp.y);
                        break;
                }
                jtp.add(jif);
                drop.requestNewName();
                jif.setVisible(true);
                drop.setSelected(true);
                drop.setDirty(true);
                jif.toFront();
            }
            adaptable.getWidget().validate();
            return true;
        } else {
            Toolkit.getDefaultToolkit().beep();
            return false;
        }
    }
}

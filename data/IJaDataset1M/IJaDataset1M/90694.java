package jaxil.control.adapter;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotionControl extends ControllerAdaptater implements MouseMotionListener {

    private static final long serialVersionUID = 728169589697841316L;

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void setView(Container v) {
        v.addMouseMotionListener(this);
    }

    @Override
    public boolean accept(Object o) {
        return o instanceof Container;
    }
}

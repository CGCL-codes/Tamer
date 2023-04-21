package com.memoire.dnd;

import java.awt.Point;
import javax.swing.JComponent;

public class DndRequestObject implements DndRequestData {

    protected Object[] data_;

    public DndRequestObject(Object _data) {
        this(new Object[] { _data });
    }

    public DndRequestObject(Object[] _data) {
        data_ = _data;
    }

    public Object[] request(JComponent _component, Point _location) {
        return data_;
    }
}

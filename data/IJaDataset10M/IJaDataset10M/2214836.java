package com.memoire.dnd;

import java.awt.Point;
import java.io.Serializable;
import javax.swing.JComponent;

public interface DndRequestData extends Serializable {

    Object[] request(JComponent _component, Point _location);
}

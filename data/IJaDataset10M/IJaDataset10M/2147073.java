package edu.whitman.halfway.jigs.gui.desque;

import java.util.EventListener;

public interface ObjectModelListener extends EventListener {

    public void objectChanged(ObjectModelEvent ome);
}

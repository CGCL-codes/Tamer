package com.peterhix.net.client;

import java.awt.Component;
import java.util.Hashtable;
import java.util.Map;
import org.flexdock.docking.DockableFactory.Stub;
import org.flexdock.view.View;

/**
 * @author Administrator
 *
 */
public class GenericViewFactory extends Stub {

    private Map<String, View> views;

    /**
	 * 
	 */
    public GenericViewFactory() {
        views = new Hashtable<String, View>();
    }

    public void put(String id, View view) {
        views.put(id, view);
    }

    public View get(String id) {
        return views.get(id);
    }

    public void remove(String id) {
        views.remove(id);
    }

    @Override
    public Component getDockableComponent(String id) {
        return views.get(id);
    }
}

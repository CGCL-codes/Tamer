package org.dmd.features.extgwt.client.util;

import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;

/**
 * The MultiView class provides the concept of named views so that a
 * controller can manage multiple views of different objects.
 */
public abstract class MultiView extends View {

    String name;

    protected MultiView(String n, Controller controller) {
        super(controller);
        name = n;
    }

    /**
	 * @return the unique name of this view.
	 */
    public String getName() {
        return (name);
    }
}

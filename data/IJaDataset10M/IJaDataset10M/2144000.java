package org.zhouer.zterm.view;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import org.zhouer.zterm.model.Model;

/**
 * ComponentHandler is a component controller for ZTerm applet.
 * 
 * @author h45
 */
public class ComponentHandler extends ComponentAdapter {

    private Model model;

    private ZTerm view;

    public void componentResized(final ComponentEvent ce) {
        this.view.validate();
        this.model.updateSize();
        model.requestFocusToCurrentSession();
    }

    /**
	 * Setter of model
	 * 
	 * @param model
	 *            the model to set
	 */
    public void setModel(final Model model) {
        this.model = model;
    }

    /**
	 * Setter of view
	 * 
	 * @param view
	 *            the view to set
	 */
    public void setView(final ZTerm view) {
        this.view = view;
    }
}

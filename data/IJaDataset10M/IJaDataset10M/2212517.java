package org.qtitools.mathqurate.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.qtitools.mathqurate.model.AbstractModel;
import org.qtitools.mathqurate.view.AbstractApplicationWindow;
import org.qtitools.mathqurate.view.MQMain;

/**
 * The Class AbstractController.
 * 
 */
public abstract class AbstractController implements PropertyChangeListener {

    /** The registered views. */
    private ArrayList<AbstractApplicationWindow> registeredViews;

    /** The registered models. */
    private ArrayList<AbstractModel> registeredModels;

    /**
	 * Instantiates a new abstract controller.
	 */
    public AbstractController() {
        registeredViews = new ArrayList<AbstractApplicationWindow>();
        registeredModels = new ArrayList<AbstractModel>();
    }

    /**
	 * Adds the model.
	 * Binds a model to this controller. Once added, the controller will listen
	 * for all model property changes and propagate them on to registered views.
	 * In addition, it is also responsible for resetting the model properties
	 * when a view changes state.
	 * 
	 * @param model the model
	 */
    public void addModel(AbstractModel model) {
        registeredModels.add(model);
        model.addPropertyChangeListener(this);
    }

    /**
	 * Adds the view.
	 * Binds a view to this controller. The controller will propagate all model
	 * property changes to each view for consideration.
	 * 
	 * @param view the view
	 */
    public void addView(AbstractApplicationWindow view) {
        registeredViews.add(view);
    }

    /**
	 * This method is used to implement the PropertyChangeListener interface.
	 * Any model changes will be sent to this controller through the use of this
	 * method.
	 * 
	 * @param evt
	 *            An object that describes the model's property change.
	 */
    public void propertyChange(PropertyChangeEvent evt) {
        for (AbstractApplicationWindow view : registeredViews) {
            view.modelPropertyChange(evt);
        }
    }

    /**
	 * Removes the model.
	 * Unbinds a model from this controller.
	 * 
	 * @param model the model
	 */
    public void removeModel(AbstractModel model) {
        registeredModels.remove(model);
        model.removePropertyChangeListener(this);
    }

    /**
	 * Removes the view.
	 * Unbinds a view from this controller.
	 * 
	 * @param view the view
	 */
    public void removeView(AbstractApplicationWindow view) {
        registeredViews.remove(view);
    }

    /**
	 * Convenience method that subclasses can call upon to fire off property
	 * changes back to the models. This method used reflection to inspect each
	 * of the model classes to determine if it is the owner of the property in
	 * question. If it isn't, a NoSuchMethodException is throws (which the
	 * method ignores).
	 * 
	 * @param propertyName
	 *            The name of the property
	 * @param newValue
	 *            An object that represents the new value of the property.
	 */
    protected void setModelProperty(String propertyName, Object newValue) {
        for (AbstractModel model : registeredModels) {
            try {
                Method method = model.getClass().getMethod("call" + propertyName, new Class[] { newValue.getClass() });
                method.invoke(model, newValue);
            } catch (Exception ex) {
                MQMain.logger.error("setModelProperty", ex);
            }
        }
    }
}

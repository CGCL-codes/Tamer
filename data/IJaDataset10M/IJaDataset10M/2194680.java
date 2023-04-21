package org.fenggui;

import java.util.Observable;
import java.util.Observer;
import org.fenggui.appearance.DecoratorAppearance;

/**
 * Base class for widgets using a Model to update content. These widgets can be notified on an update of the Model as they use
 * the Observer pattern.
 * 
 * @see java.util.Observer
 * @see java.util.Observable
 * @author Marc Menghin last edited by $Author: marcmenghin $, $Date: 2009-03-13 21:32:56 +0100 (Fr, 06 Mär 2009) $
 * @version $Revision: 603 $
 */
public abstract class ModelWidget<T extends DecoratorAppearance, M extends IModel> extends StatefullWidget<T> implements Observer {

    private M model = null;

    protected abstract void ModelUpdated(M model, boolean newModel);

    /**
   * creates a new ModelWidget
   */
    public ModelWidget() {
    }

    /**
   * copy constructor.
   * 
   * @param widget
   */
    public ModelWidget(ModelWidget<T, M> widget) {
        super(widget);
    }

    /**
   * Adds a Model to this widget. If the Model extends the Observable class it will register itself
   * as a Observer and will refresh the data when its notified of an update. If you add data over
   * other methods to the widget they will most likely be removed by an update.
   * 
   * @param m
   */
    public void setModel(M m) {
        if (model != null && model instanceof Observable) {
            Observable ob = (Observable) model;
            ob.deleteObserver(this);
        }
        model = m;
        if (model != null) {
            if (model instanceof Observable) {
                Observable ob = (Observable) model;
                ob.addObserver(this);
            }
        }
        ModelUpdated(model, true);
    }

    /**
   * Will return true if a model is set to this widget.
   * 
   * @return
   */
    public boolean hasModel() {
        return model != null;
    }

    /**
   * Returns the currently set Model or null.
   * 
   * @return
   */
    public M getModel() {
        return model;
    }

    public void update(Observable o, Object arg) {
        ModelUpdated(model, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ModelWidget<T, M> clone() {
        ModelWidget<T, M> result = (ModelWidget<T, M>) super.clone();
        this.model = null;
        return result;
    }
}

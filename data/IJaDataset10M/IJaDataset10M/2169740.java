package net.sf.ij_plugins.ui;

import net.sf.ij_plugins.beans.Model;
import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Helper for implementing actions that interact with a (presentation) model.
 * The action is disabled when {@code model} is {@code null}.
 * The action listens to model changes and and revalidates enabled state.
 *
 * @author Jarek Sacha
 */
public abstract class AbstractModelAction<M extends Model> extends AbstractAction {

    private M model;

    private final ModelChangeListener changeListener = new ModelChangeListener();

    private static final long serialVersionUID = 1L;

    protected AbstractModelAction(final String name) {
        super(name);
    }

    protected AbstractModelAction(final String name, final M model) {
        this(name);
        setModel(model);
    }

    @Override
    public boolean isEnabled() {
        return model != null;
    }

    public M getModel() {
        return model;
    }

    public void setModel(final M model) {
        if (model == this.model) {
            return;
        }
        if (this.model != null) {
            this.model.removePropertyChangeListener(changeListener);
        }
        firePropertyChange("model", this.model, this.model = model);
        if (this.model != null) {
            this.model.addPropertyChangeListener(changeListener);
        }
        setEnabled(isEnabled());
    }

    private class ModelChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            setEnabled(isEnabled());
        }
    }
}

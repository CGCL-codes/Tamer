package org.hermeneutix.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * manages the communication between the model and listening view parties, when
 * model objects are changed an {@link ModelEvent} is thrown containing the
 * changed object ({@link Pericope} / {@link Proposition} / {@link ClauseItem} /
 * {@link Relation})
 * 
 * @author C. Englert
 */
public class PericopeModel implements Serializable {

    /**
	 * observed {@link Pericope}
	 */
    private final Pericope pericope;

    /**
	 * list of registered {@link ModelChangeListener}s
	 */
    private List<ModelChangeListener> listeners = new ArrayList<ModelChangeListener>();

    /**
	 * creates a new {@link ModelEvent} handling {@link PericopeModel}
	 * 
	 * @param pericope
	 *            observed {@link Pericope} to set
	 */
    protected PericopeModel(final Pericope pericope) {
        this.pericope = pericope;
    }

    /**
	 * @return observed {@link Pericope}
	 */
    public Pericope getPericope() {
        return this.pericope;
    }

    /**
	 * @return list of registered {@link ModelChangeListener}s
	 */
    public List<ModelChangeListener> getListeners() {
        if (this.listeners == null) {
            return null;
        }
        return new ArrayList<ModelChangeListener>(this.listeners);
    }

    /**
	 * adds a {@link ModelChangeListener}, that wants to be informed when a
	 * change happens
	 * 
	 * @param listener
	 *            {@link ModelChangeListener} to add
	 */
    public void addModelChangeListener(final ModelChangeListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    /**
	 * removes a {@link ModelChangeListener}, that wants no further informations
	 * about any change
	 * 
	 * @param listener
	 *            {@link ModelChangeListener} to remove
	 */
    public void removeModelChangeListener(final ModelChangeListener listener) {
        this.listeners.remove(listener);
    }

    /**
	 * requests a whole rebuild of the {@link Pericope}
	 */
    public void fireModelRebuild() {
        fireModelObjectRefresh(this.pericope);
    }

    /**
	 * requests the view refresh of a single model element ({@link Proposition}
	 * / {@link ClauseItem} / {@link Relation})
	 * 
	 * @param target
	 *            edited model element
	 */
    public void fireModelObjectRefresh(final Object target) {
        final ModelEvent event = new ModelEvent(target);
        for (ModelChangeListener singleListener : this.listeners) {
            singleListener.modelChanged(event);
        }
    }
}

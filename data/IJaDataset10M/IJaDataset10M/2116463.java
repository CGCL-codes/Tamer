package org.jsresources.apps.jmvp.model;

import java.util.EventListener;

/**	Listener interface for model changes.
	This interface should be implemented by classes
	that want to receive notification from a model
	on the model's changes.

	Objcts of classes implementing this interface can register
	(and unregister) themselves by calling the model's {@link
	Model#addModelListener addModelListener()} and {@link
	Model#removeModelListener removeModelListener()} methods.

	@see Model

	@author Matthias Pfisterer
 */
public interface ModelListener extends EventListener {

    /**	Model changed.
	 */
    public void modelChanged(ModelEvent event);
}

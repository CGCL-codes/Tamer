package com.greentea.relaxation.jnmf.model.events.listeners;

import com.greentea.relaxation.jnmf.model.Layer;
import java.io.Serializable;

/**
 * Interface <code>ILayerBeforeTranslationListener</code> obligates to implement the method which is
 * called by the layer before translation.
 */
public interface ILayerBeforeTranslationListener extends Serializable {

    /**
    * Listens the <i>beforeLayerTranslation</i> event. I.e. this method is called by the specified
    * layer before translation.
    *
    * @param layer the layer which initiated the event
    */
    void beforeLayerTranslation(Layer layer);
}

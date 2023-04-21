package org.apache.isis.extensions.wicket.model.common;

import java.io.Serializable;
import org.apache.wicket.Component;

/**
 * Decouples the mechanism for letting the user acknowledge that no results returned from action
 * (eg an 'OK' button).
 */
public interface NoResultsHandler extends Serializable {

    void onNoResults(Component context);
}

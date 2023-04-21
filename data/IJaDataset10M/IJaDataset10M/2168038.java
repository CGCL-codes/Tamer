package org.argouml.notation.providers;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;

/**
 * This abstract class forms the basis of all Notation providers
 * for the text shown in the Fig that represents a componentInstance.
 * Subclass this for all languages.
 *
 * @author mvw@tigris.org
 */
public abstract class ComponentInstanceNotation extends NotationProvider {

    /**
     * The constructor.
     *
     * @param componentInstance the componentInstance of which
     *                          we handle the text
     */
    public ComponentInstanceNotation(Object componentInstance) {
        if (!Model.getFacade().isAComponentInstance(componentInstance)) {
            throw new IllegalArgumentException("This is not a ComponentInstance.");
        }
    }
}

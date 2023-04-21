package org.dolmen.container.behaviors;

import org.dolmen.core.container.components.Component;

/**
 * Interface to tell that a {@link Component Component} can be configured
 *
 * @since 0.0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public interface Configurable {

    public void configure(Object aConfiguration) throws Exception;
}

package org.objectwiz.core.facet.session;

import org.objectwiz.core.AbstractFacet;
import org.objectwiz.core.Application;

/**
 * Abstract base implementation of {@link SessionFacet}.
 *
 * @author Vincent Laugier <vincent.laugier at helmet.fr>
 */
public abstract class AbstractSessionFacetImpl extends AbstractFacet implements SessionFacet {

    protected AbstractSessionFacetImpl(Application application) {
        super(application);
    }
}

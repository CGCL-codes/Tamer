package org.spockframework.tapestry;

import org.apache.tapestry5.ioc.internal.services.PerThreadServiceLifecycle;
import org.apache.tapestry5.ioc.services.ClassFactory;

/**
 * Allows a service to exist per feature iteration. Implementation is fully
 * based on Tapestry's (internal) <tt>PerThreadServiceLifecycle</tt>.
 *
 * @author Peter Niederwieser
 */
public class PerIterationServiceLifecycle extends PerThreadServiceLifecycle {

    public PerIterationServiceLifecycle(IPerIterationManager perIterationManager, ClassFactory classFactory) {
        super(perIterationManager, classFactory);
    }
}

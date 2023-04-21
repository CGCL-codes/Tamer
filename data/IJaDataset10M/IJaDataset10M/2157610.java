package org.jplate.tabular.impl.defaults;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jplate.foundation.gof.factory.impl.AbstractFactory;
import org.jplate.tabular.RepoFactoryIfc;
import org.jplate.tabular.RepoIfc;

public class DefaultRepoFactory extends AbstractFactory<RepoIfc> implements RepoFactoryIfc {

    /**
     *
     * Used in serialization.
     *
     */
    private static final long serialVersionUID = -5202291317143823229L;

    /**
     *
     * Used for logging.
     *
     */
    private final transient Log _log = LogFactory.getLog(DefaultRepoFactory.class);

    /**
     *
     * Default constructor.
     *
     */
    public DefaultRepoFactory() {
        if (_log.isDebugEnabled()) {
            _log.debug("New DefaultRepoFactory created");
        }
    }

    /**
     *
     * {@inheritDoc}
     *
     */
    public RepoIfc create() {
        final RepoIfc retVal = new DefaultRepo();
        if (_log.isDebugEnabled()) {
            _log.debug("Created [" + retVal + "]");
        }
        return retVal;
    }
}

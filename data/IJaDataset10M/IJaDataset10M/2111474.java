package org.dwgsoftware.raistlin.composition.model.test.dynamics;

import org.apache.avalon.framework.logger.Logger;

/**
 * A component that implements the Gizmo service.
 *
 * @raistlin.component name="widget" lifestyle="singleton"
 * @raistlin.service type="org.dwgsoftware.raistlin.composition.model.test.dynamics.Widget"
 */
public class DefaultWidget implements Widget {

    /**
   * The logging channel supplied by the container.
   */
    private final Logger m_logger;

    /**
   * Creation of a new hello facility.
   * @param logger a logging channel
   */
    public DefaultWidget(Logger logger) {
        m_logger = logger;
        m_logger.info("I've been created");
    }
}

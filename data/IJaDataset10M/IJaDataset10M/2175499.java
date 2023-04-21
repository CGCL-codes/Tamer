package tutorial;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.activity.Disposable;

/**
 * A component that implements the Widget service.
 *
 * @raistlin.component name="widget" lifestyle="singleton"
 * @raistlin.service type="tutorial.Widget"
 */
public class DefaultWidget implements Widget, Configurable, Disposable {

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
        m_logger.info("hello");
    }

    /**
    * Configuration of the gizmo by the container.
    * @param config the supplied configuration
    */
    public void configure(Configuration config) throws ConfigurationException {
        final String message = config.getChild("message").getValue(null);
        if (null != message) {
            m_logger.info(message);
        }
    }

    /**
   * End-of-life processing initiated by the container.
   */
    public void dispose() {
        m_logger.info("time to die");
    }

    public String toString() {
        return "[widget:" + System.identityHashCode(this) + "]";
    }
}

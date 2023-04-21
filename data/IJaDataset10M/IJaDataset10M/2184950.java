package org.gamegineer.common.core.services.logging;

import java.util.logging.Logger;
import org.osgi.framework.Bundle;

/**
 * A service used to manage the Java core logging facilities in an OSGi
 * environment.
 * 
 * <p>
 * The purpose of this service is to provide individual bundles in the OSGi
 * environment the ability to configure the Java core logging facilities in an
 * isolated manner. The standard logging configuration mechanism is not suitable
 * for OSGi for the following reasons:
 * </p>
 * 
 * <ul>
 * <li>The standard logging configuration is centralized in a single location.
 * Thus, the configuration for a logger associated with a bundle must be located
 * outside the bundle.</li>
 * <li>The standard logging configuration relies heavily on the system class
 * loader. Thus, any object that is instantiated while processing the
 * configuration must be loaded through the system class loader. This is not
 * compatible with the isolation of bundle class loaders in an OSGi environment.</li>
 * </ul>
 * 
 * <p>
 * It is sufficient to use the Java core logging facilities <i>without</i> this
 * service in the following cases:
 * </p>
 * 
 * <ul>
 * <li>It is acceptable to configure all loggers in a single location and your
 * logger configurations only require classes located on the system classpath.</li>
 * <li>You configure all your loggers programatically.</li>
 * </ul>
 * 
 * <p>
 * Each bundle may provide a file named <i>logging.properties</i> at the root
 * of the bundle's location to configure its loggers, handlers, formatters, and
 * filters.
 * </p>
 * 
 * @noextend This interface is not intended to be extended by clients.
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface ILoggingService {

    public Logger getLogger(Bundle bundle);

    public Logger getLogger(Bundle bundle, String name);
}

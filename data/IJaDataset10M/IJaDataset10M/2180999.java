package org.gamegineer.common.internal.core.util.logging;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import org.gamegineer.common.core.services.component.IComponentFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Manages the OSGi services provided by this package.
 * 
 * <p>
 * The {@code close} method should be called before the bundle is stopped.
 * </p>
 */
public final class Services {

    /** The singleton instance. */
    private static final Services c_instance = new Services();

    /** The ConsoleHandlerFactory registration token. */
    private ServiceRegistration m_consoleHandlerFactoryRegistration;

    /** The FrameworkLogHandlerFactory registration token. */
    private ServiceRegistration m_frameworkLogHandlerFactoryRegistration;

    /** The SimpleFormatterFactory registration token. */
    private ServiceRegistration m_simpleFormatterFactoryRegistration;

    /** The StandardOutputHandlerFactory registration token. */
    private ServiceRegistration m_standardOutputHandlerFactoryRegistration;

    /** The XmlFormatterFactory registration token. */
    private ServiceRegistration m_xmlFormatterFactoryRegistration;

    /**
     * Initializes a new instance of the {@code Services} class.
     */
    private Services() {
        super();
    }

    /**
     * Closes the services managed by this object.
     */
    public void close() {
        if (m_xmlFormatterFactoryRegistration != null) {
            m_xmlFormatterFactoryRegistration.unregister();
            m_xmlFormatterFactoryRegistration = null;
        }
        if (m_standardOutputHandlerFactoryRegistration != null) {
            m_standardOutputHandlerFactoryRegistration.unregister();
            m_standardOutputHandlerFactoryRegistration = null;
        }
        if (m_simpleFormatterFactoryRegistration != null) {
            m_simpleFormatterFactoryRegistration.unregister();
            m_simpleFormatterFactoryRegistration = null;
        }
        if (m_frameworkLogHandlerFactoryRegistration != null) {
            m_frameworkLogHandlerFactoryRegistration.unregister();
            m_frameworkLogHandlerFactoryRegistration = null;
        }
        if (m_consoleHandlerFactoryRegistration != null) {
            m_consoleHandlerFactoryRegistration.unregister();
            m_consoleHandlerFactoryRegistration = null;
        }
    }

    public static Services getDefault() {
        return c_instance;
    }

    /**
     * Opens the services managed by this object.
     * 
     * @param context
     *        The execution context of the bundle; must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code context} is {@code null}.
     */
    public void open(final BundleContext context) {
        assertArgumentNotNull(context, "context");
        m_consoleHandlerFactoryRegistration = context.registerService(IComponentFactory.class.getName(), new ConsoleHandlerFactory(), null);
        m_frameworkLogHandlerFactoryRegistration = context.registerService(IComponentFactory.class.getName(), new FrameworkLogHandlerFactory(), null);
        m_simpleFormatterFactoryRegistration = context.registerService(IComponentFactory.class.getName(), new SimpleFormatterFactory(), null);
        m_standardOutputHandlerFactoryRegistration = context.registerService(IComponentFactory.class.getName(), new StandardOutputHandlerFactory(), null);
        m_xmlFormatterFactoryRegistration = context.registerService(IComponentFactory.class.getName(), new XmlFormatterFactory(), null);
    }
}

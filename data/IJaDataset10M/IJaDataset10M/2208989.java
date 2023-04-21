package org.gamegineer.common.internal.core.services.logging;

import net.jcip.annotations.ThreadSafe;
import org.eclipse.osgi.util.NLS;

/**
 * A utility class to manage localized messages for the package.
 */
@ThreadSafe
final class Messages extends NLS {

    /** Logging component creation failed. */
    public static String AbstractLoggingComponentFactory_createLoggingComponent_failed;

    /** The fully-qualified component name must contain at least one dot. */
    public static String AbstractLoggingComponentFactory_createNamedLoggingComponent_nameNoDots;

    /** A component factory is no longer available. */
    public static String AbstractLoggingComponentFactory_createNamedLoggingComponent_noComponentFactoryAvailable;

    /** No component factory is available. */
    public static String AbstractLoggingComponentFactory_findComponentFactory_noComponentFactoryAvailable;

    /** The filter syntax is invalid. */
    public static String AbstractLoggingComponentFactory_getComponentFactory_invalidFilterSyntax;

    /** The property value is illegal. */
    public static String AbstractLoggingComponentFactory_getComponentProperty_illegalPropertyValue;

    /** No component properties specified. */
    public static String AbstractLoggingComponentFactory_newInstance_noComponentProperties;

    /** An illegal component type name was specified. */
    public static String FrameworkLogHandlerFactory_createLoggingComponent_illegalTypeName;

    /** No framework log service is available. */
    public static String FrameworkLogHandlerFactory_createLoggingComponent_noFrameworkLogAvailable;

    /**
     * Initializes the {@code Messages} class.
     */
    static {
        NLS.initializeMessages(Messages.class.getName(), Messages.class);
    }

    /**
     * Initializes a new instance of the {@code Messages} class.
     */
    private Messages() {
        super();
    }

    static String AbstractLoggingComponentFactory_createLoggingComponent_failed(final String typeName) {
        return bind(AbstractLoggingComponentFactory_createLoggingComponent_failed, typeName);
    }

    static String AbstractLoggingComponentFactory_findComponentFactory_noComponentFactoryAvailable(final String typeName) {
        return bind(AbstractLoggingComponentFactory_findComponentFactory_noComponentFactoryAvailable, typeName);
    }

    static String AbstractLoggingComponentFactory_getComponentProperty_illegalPropertyValue(final String propertyName) {
        return bind(AbstractLoggingComponentFactory_getComponentProperty_illegalPropertyValue, propertyName);
    }
}

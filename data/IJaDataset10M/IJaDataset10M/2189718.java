package org.gamegineer.table.internal.ui;

import java.net.URL;
import net.jcip.annotations.ThreadSafe;
import org.eclipse.osgi.util.NLS;
import org.gamegineer.table.core.ComponentSurfaceDesignId;
import org.osgi.framework.Bundle;

/**
 * A utility class to manage non-localized messages for the package.
 */
@ThreadSafe
final class NonNlsMessages extends NLS {

    /** An error occurred while saving the user preferences. */
    public static String Activator_saveUserPreferenecs_error;

    /** An error occurred while reading the image. */
    public static String BundleImages_getImage_readError;

    /**
     * A component surface design user interface is already registered for the
     * specified identifier.
     */
    public static String ComponentSurfaceDesignUIRegistry_registerComponentSurfaceDesignUI_componentSurfaceDesignUI_registered;

    /**
     * The component surface design user interface is not registered for the
     * specified identifier.
     */
    public static String ComponentSurfaceDesignUIRegistry_unregisterComponentSurfaceDesignUI_componentSurfaceDesignUI_unregistered;

    /**
     * The component surface design user interface registry service is already
     * bound.
     */
    public static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_bindComponentSurfaceDesignUIRegistry_bound;

    /** The extension registry service is already bound. */
    public static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_bindExtensionRegistry_bound;

    /** The bundle hosting the component surface design icon was not found. */
    public static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_createComponentSurfaceDesignUIRegistration_iconBundleNotFound;

    /** The component surface design icon file was not found. */
    public static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_createComponentSurfaceDesignUIRegistration_iconFileNotFound;

    /** The component surface design icon path is missing. */
    public static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_createComponentSurfaceDesignUIRegistration_missingIconPath;

    /** The component surface design identifier is missing. */
    public static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_createComponentSurfaceDesignUIRegistration_missingId;

    /** The component surface design name is missing. */
    public static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_createComponentSurfaceDesignUIRegistration_missingName;

    /**
     * An error occurred while parsing the component surface design user
     * interface configuration element.
     */
    public static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_registerComponentSurfaceDesignUI_parseError;

    /**
     * The component surface design user interface registry service is not
     * bound.
     */
    public static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_unbindComponentSurfaceDesignUIRegistry_notBound;

    /** The extension registry service is not bound. */
    public static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_unbindExtensionRegistry_notBound;

    /** The frame window could not be opened. */
    public static String TableRunner_openFrame_error;

    /** The runner is already running or has already finished. */
    public static String TableRunner_state_notPristine;

    /**
     * Initializes the {@code NonNlsMessages} class.
     */
    static {
        NLS.initializeMessages(NonNlsMessages.class.getName(), NonNlsMessages.class);
    }

    /**
     * Initializes a new instance of the {@code NonNlsMessages} class.
     */
    private NonNlsMessages() {
    }

    static String BundleImages_getImage_readError(final URL imageUrl) {
        return bind(BundleImages_getImage_readError, imageUrl);
    }

    static String ComponentSurfaceDesignUIRegistry_registerComponentSurfaceDesignUI_componentSurfaceDesignUI_registered(final ComponentSurfaceDesignId componentSurfaceDesignId) {
        return bind(ComponentSurfaceDesignUIRegistry_registerComponentSurfaceDesignUI_componentSurfaceDesignUI_registered, componentSurfaceDesignId);
    }

    static String ComponentSurfaceDesignUIRegistry_unregisterComponentSurfaceDesignUI_componentSurfaceDesignUI_unregistered(final ComponentSurfaceDesignId componentSurfaceDesignId) {
        return bind(ComponentSurfaceDesignUIRegistry_unregisterComponentSurfaceDesignUI_componentSurfaceDesignUI_unregistered, componentSurfaceDesignId);
    }

    static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_createComponentSurfaceDesignUIRegistration_iconBundleNotFound(final String name) {
        return bind(ComponentSurfaceDesignUIRegistryExtensionPointAdapter_createComponentSurfaceDesignUIRegistration_iconBundleNotFound, name);
    }

    static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_createComponentSurfaceDesignUIRegistration_iconFileNotFound(final Bundle bundle, final String path) {
        return bind(ComponentSurfaceDesignUIRegistryExtensionPointAdapter_createComponentSurfaceDesignUIRegistration_iconFileNotFound, bundle.getSymbolicName(), path);
    }

    static String ComponentSurfaceDesignUIRegistryExtensionPointAdapter_registerComponentSurfaceDesignUI_parseError(final String componentSurfaceDesignId) {
        return bind(ComponentSurfaceDesignUIRegistryExtensionPointAdapter_registerComponentSurfaceDesignUI_parseError, componentSurfaceDesignId);
    }
}

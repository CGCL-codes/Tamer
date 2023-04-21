package org.gamegineer.table.internal.ui;

import net.jcip.annotations.ThreadSafe;

/**
 * Defines useful constants for use by the bundle.
 */
@ThreadSafe
public final class BundleConstants {

    /**
     * The simple identifier of the component surface design user interfaces
     * extension point.
     */
    public static final String COMPONENT_SURFACE_DESIGN_UIS_EXTENSION_POINT_SIMPLE_ID = "componentSurfaceDesignUIs";

    /** The symbolic name of the bundle. */
    public static final String SYMBOLIC_NAME = "org.gamegineer.table.ui";

    /**
     * The unique identifier of the component surface design user interfaces
     * extension point.
     */
    public static final String COMPONENT_SURFACE_DESIGN_UIS_EXTENSION_POINT_UNIQUE_ID = SYMBOLIC_NAME + "." + COMPONENT_SURFACE_DESIGN_UIS_EXTENSION_POINT_SIMPLE_ID;

    /**
     * Initializes a new instance of the {@code BundleConstants} class.
     */
    private BundleConstants() {
    }
}

package org.gamegineer.table.internal.core;

import net.jcip.annotations.ThreadSafe;

/**
 * Debugging utilities for the bundle.
 */
@ThreadSafe
public final class Debug extends org.gamegineer.common.core.runtime.Debug {

    /** The singleton instance of the bundle debug utility. */
    private static final Debug INSTANCE = new Debug();

    /** The name of the top-level debug option. */
    public static final String OPTION_DEFAULT = "/debug";

    /**
     * Initializes a new instance of the {@code Debug} class.
     */
    private Debug() {
        super(BundleConstants.SYMBOLIC_NAME);
    }

    public static Debug getDefault() {
        return INSTANCE;
    }
}

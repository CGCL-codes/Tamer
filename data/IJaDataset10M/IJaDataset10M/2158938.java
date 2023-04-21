package ch.sahits.codegen.core.util;

/**
 * Class containing debug flags
 * @author Andi Hotz, Sahits GmbH
 * @since 2.1.0
 *
 */
public final class Debugging {

    /** Flag indicating the behaviour of overriding existing resources */
    private static boolean forceOverride = false;

    /**
	 * Set the override flag to override existing conflicting ressources.
	 * This flag should only be set to true for debugging purpous and not in
	 * any productive environment.
	 * @param forceOverride
	 */
    public static void setForceOverride(boolean forceOverride) {
        Debugging.forceOverride = forceOverride;
    }

    /**
	 * Retrieve the override flag
	 * @return
	 */
    public static boolean isForceOverride() {
        return forceOverride;
    }
}

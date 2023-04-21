package com.sun.midp.security;

/**
 * Contains methods to get various security state information of the currently
 * running MIDlet suite. It cannot be instantiated.
 */
public final class SecurityDomain {

    public static final String INTERNAL_DOMAIN_NAME = "user";

    /** Enables the first domain be constructed without a domain. */
    private static boolean firstCaller = true;

    /** Domain name. */
    private String name;

    /** Permitted actions. */
    private int actions[] = null;

    /**
     * Creates a security domain with a list of permitted actions or no list
     * to indicate all actions. The caller must be have permission for
     * <code>Actions.DEVICE_CORE_FUNCTION</code> or be the first caller of
     * the method for this instance of the VM.
     * @param securityDomain security domain of the caller, can be null for
     *                       the first caller
     * @param theName domain to create, can be null
     * @exception SecurityException if caller is not permitted to call this
     *            method
     */
    public SecurityDomain(SecurityDomain securityDomain, String theName) {
        if (firstCaller) {
            firstCaller = false;
        } else {
            securityDomain.checkIfPermitted(Actions.DEVICE_CORE_FUNCTION);
        }
        if (theName == null) {
            name = INTERNAL_DOMAIN_NAME;
            return;
        }
        name = theName;
        actions = Actions.forDomain(name);
    }

    /**
     * Check to see the domain permitted to perform a specific action.
     *
     * @param action an action code
     * @exception SecurityException if the domain is not
     *            permitted to perform the specified action.
     */
    public void checkIfPermitted(int action) {
        if (actions == null) {
            return;
        }
        for (int i = 0; i < actions.length; i++) {
            if (actions[i] == action) {
                return;
            }
        }
        throw new SecurityException("Application not authorized " + "to access the restricted API");
    }

    /**
     * Gets the name of this security domain.
     *
     * @return name of this security domain
     */
    public String getName() {
        return name;
    }
}

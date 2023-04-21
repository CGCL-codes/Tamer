package com.sun.midp.push.controller;

/**
 * Unique identification for <code>MIDlet</code> application.
 *
 * <p>
 * Simple, structure like class.
 * </p>
 */
final class MIDPApp {

    /** <code>MIDlet</code> suite ID. */
    public final int midletSuiteID;

    /** <code>MIDlet</code> class name. */
    public final String midlet;

    /**
     * Constructs an instance.
     *
     * @param midletSuiteID <code>MIDlet suite</code> ID
     * @param midlet <code>MIDlet</code> class name
     */
    public MIDPApp(final int midletSuiteID, final String midlet) {
        this.midletSuiteID = midletSuiteID;
        this.midlet = midlet;
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return (midletSuiteID << 3) + midlet.hashCode();
    }

    /** {@inheritDoc} */
    public boolean equals(final Object obj) {
        if (!(obj instanceof MIDPApp)) {
            return false;
        }
        final MIDPApp rhs = (MIDPApp) obj;
        return (midletSuiteID == rhs.midletSuiteID) && midlet.equals(rhs.midlet);
    }
}

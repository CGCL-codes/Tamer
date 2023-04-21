package org.nakedobjects.object.control;

import java.io.Serializable;

public abstract class AbstractConsent implements Serializable, Consent {

    /**
     * Returns an Allow (Allow.DEFAULT) object if true; Veto (Veto.DEFAULT) if
     * false;
     */
    public static final Consent allow(boolean allow) {
        if (allow) {
            return Allow.DEFAULT;
        } else {
            return Veto.DEFAULT;
        }
    }

    /**
     * Returns a new Allow object if <code>allow</code> is true; a new Veto if
     * false. The respective reason is passed to the newly created object.
     */
    public static final Consent create(boolean allow, String reasonAllowed, String reasonVeteod) {
        if (allow) {
            return new Allow(reasonAllowed);
        } else {
            return new Veto(reasonVeteod);
        }
    }

    private String reason;

    protected AbstractConsent() {
    }

    protected AbstractConsent(String reason) {
        this.reason = reason;
    }

    /**
     * Returns the persmission's reason
     */
    public String getReason() {
        return reason == null ? "" : reason;
    }

    /**
     * Returns true if this object is giving permission.
     */
    public abstract boolean isAllowed();

    /**
     * Returns true if this object is NOT giving permission.
     */
    public abstract boolean isVetoed();

    public String toString() {
        return "Permission [type=" + (isVetoed() ? "VETOED" : "ALLOWED") + ", reason=" + reason + "]";
    }
}

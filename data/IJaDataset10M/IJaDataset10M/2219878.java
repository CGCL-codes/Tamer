package org.genxdm.bridgekit.xs.complex;

import org.genxdm.exceptions.PreCondition;

public abstract class LockableImpl {

    private boolean m_isLocked = false;

    public final void lock() {
        m_isLocked = true;
    }

    public final boolean isLocked() {
        return m_isLocked;
    }

    protected final void assertNotLocked() {
        PreCondition.assertFalse(m_isLocked, "isLocked -> true");
    }
}

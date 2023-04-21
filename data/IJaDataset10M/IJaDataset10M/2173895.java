package com.phloc.webbasics.security.role;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IRole} interface.
 * 
 * @author philip
 */
@NotThreadSafe
public final class Role implements IRole {

    private final String m_sID;

    private String m_sName;

    public Role(@Nonnull @Nonempty final String sName) {
        this(GlobalIDFactory.getNewPersistentStringID(), sName);
    }

    Role(@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName) {
        if (StringHelper.hasNoText(sID)) throw new IllegalArgumentException("ID");
        if (StringHelper.hasNoText(sName)) throw new IllegalArgumentException("name");
        m_sID = sID;
        m_sName = sName;
    }

    @Nonnull
    @Nonempty
    public String getID() {
        return m_sID;
    }

    @Nonnull
    @Nonempty
    public String getName() {
        return m_sName;
    }

    @Nonnull
    EChange setName(@Nonnull @Nonempty final String sName) {
        if (StringHelper.hasNoText(sName)) throw new IllegalArgumentException("name");
        if (sName.equals(m_sName)) return EChange.UNCHANGED;
        m_sName = sName;
        return EChange.CHANGED;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Role)) return false;
        final Role rhs = (Role) o;
        return m_sID.equals(rhs.m_sID);
    }

    @Override
    public int hashCode() {
        return new HashCodeGenerator(this).append(m_sID).getHashCode();
    }

    @Override
    public String toString() {
        return new ToStringGenerator(this).append("ID", m_sID).append("name", m_sName).toString();
    }
}

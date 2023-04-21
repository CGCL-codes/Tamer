package org.starobjects.jpa.runtime.persistence.oid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.starobjects.jpa.runtime.persistence.oid.JpaOid;

public class GivenJpaOidWhenMakePersistent {

    @Test
    public void noLongerTransientPersistent() {
        final JpaOid oid = JpaOid.createTransient(Object.class, -1L);
        assertTrue(oid.isTransient());
        oid.setNewId(+1L);
        oid.makePersistent();
        assertFalse(oid.isTransient());
    }

    @Test
    public void getPreviousPopulatedAndIsEqualToCopy() {
        final JpaOid oid = JpaOid.createTransient(Object.class, -1L);
        final JpaOid oidCopy = JpaOid.createPersistent(Object.class, +999L);
        oidCopy.copyFrom(oid);
        assertFalse(oid.hasPrevious());
        oid.setNewId(+999L);
        oid.makePersistent();
        assertTrue(oid.hasPrevious());
        assertEquals(oidCopy, oid.getPrevious());
    }

    @Test
    public void setNewIdIsStored() {
        final JpaOid oid = JpaOid.createTransient(Object.class, -1L);
        assertNull(oid.getNewId());
        oid.setNewId(+999L);
        assertEquals((Long) 999L, oid.getNewId());
    }

    @Test
    public void setNewIdIsClearedAfterMakePersistent() {
        final JpaOid oid = JpaOid.createTransient(Object.class, -1L);
        oid.setNewId(+999L);
        oid.makePersistent();
        assertNull(oid.getNewId());
    }

    @Test
    public void setNewIdBecomesTheIdAfterMakePersistent() {
        final JpaOid oid = JpaOid.createTransient(Object.class, -1L);
        oid.setNewId(+999L);
        oid.makePersistent();
        assertEquals((Long) 999L, oid.getId());
    }

    @Test
    public void equalToExpected() {
        final JpaOid oid = JpaOid.createTransient(Object.class, -1L);
        oid.setNewId(+999L);
        oid.makePersistent();
        final JpaOid expectedPersistent = JpaOid.createPersistent(Object.class, +999L);
        assertEquals(expectedPersistent, oid);
    }
}

package org.sourceforge.jemm.database.components.types;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sourceforge.jemm.database.ClassId;
import org.sourceforge.jemm.database.GetObjectResponse;
import org.sourceforge.jemm.database.ObjectState;
import org.sourceforge.jemm.database.collections.AbstractDatabaseMapTest;
import org.sourceforge.jemm.types.ID;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;

/**
 * Test cases to test modified flag behaviour of StoredMap class.
 * Only the methods that might modify the map are listed here.  All map methods are 
 * given basic functional tests through the {@link AbstractDatabaseMapTest} code.
 * 
 * @author Rory Graves
 */
public class StoredAtomicIntObjectTest {

    ID id = new ID(1);

    ClassId classId = new ClassId(1234);

    StoredAtomicIntObject atomicInt;

    @Before
    public void setup() {
        atomicInt = new StoredAtomicIntObject(id, classId);
    }

    @Test
    public void getAndSetTest() {
        assertEquals(0, atomicInt.get());
        assertFalse(atomicInt.getModified());
        atomicInt.set(57);
        assertTrue(atomicInt.getModified());
        assertEquals(57, atomicInt.get());
        atomicInt.clearModified();
        atomicInt.set(57);
        Assert.assertFalse(atomicInt.getModified());
    }

    @Test
    public void referencedObjectsTest() {
        Assert.assertEquals(0, atomicInt.getReferencedObjects().size());
    }

    @Test
    public void getAndAdd() {
        assertEquals(0, atomicInt.getAndAdd(0));
        assertFalse(atomicInt.getModified());
        assertEquals(0, atomicInt.getAndAdd(10));
        assertEquals(10, atomicInt.get());
        assertTrue(atomicInt.getModified());
    }

    @Test
    public void addAndGet() {
        assertEquals(0, atomicInt.addAndGet(0));
        assertFalse(atomicInt.getModified());
        assertEquals(10, atomicInt.addAndGet(10));
        assertEquals(10, atomicInt.get());
        assertTrue(atomicInt.getModified());
    }

    @Test
    public void getAndSet() {
        assertEquals(0, atomicInt.getAndSet(50));
        assertEquals(50, atomicInt.get());
        assertTrue(atomicInt.getModified());
        atomicInt.clearModified();
        assertEquals(50, atomicInt.getAndSet(50));
        assertEquals(50, atomicInt.get());
        assertFalse(atomicInt.getModified());
    }

    @Test
    public void compareAndSet() {
        assertFalse(atomicInt.compareAndSet(5, 10));
        assertFalse(atomicInt.getModified());
        assertEquals(0, atomicInt.get());
        assertTrue(atomicInt.compareAndSet(0, 10));
        assertTrue(atomicInt.getModified());
        assertEquals(10, atomicInt.get());
        atomicInt.clearModified();
        assertTrue(atomicInt.compareAndSet(10, 10));
        assertFalse(atomicInt.getModified());
        assertEquals(10, atomicInt.get());
    }

    @Test
    public void getObjResp() {
        atomicInt.set(5);
        GetObjectResponse resp = atomicInt.getObjectData();
        Assert.assertNotNull(resp);
        ObjectState state = resp.getObjectState();
        assertEquals(state.getClientVersion(), 0);
        assertEquals(0, state.getFieldValues().size());
        assertEquals(classId, resp.getClassId());
        assertEquals(id, state.getJemmId());
    }
}

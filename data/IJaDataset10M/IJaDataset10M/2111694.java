package com.bluemarsh.jswat.core.runtime;

import java.util.Iterator;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for the RuntimeManager class.
 *
 * @author Nathan Fiedler
 */
public class RuntimeManagerTest {

    @Test
    public void test_RuntimeManager() {
        RuntimeManager rm = RuntimeProvider.getRuntimeManager();
        RuntimeFactory rf = RuntimeProvider.getRuntimeFactory();
        String id = rm.generateIdentifier();
        JavaRuntime rt = rf.createRuntime(System.getProperty("java.home"), id);
        assertNotNull(rt);
        rm.add(rt);
        Iterator<JavaRuntime> iter = rm.iterateRuntimes();
        assertTrue(iter.hasNext());
        boolean found = false;
        while (iter.hasNext()) {
            JavaRuntime rti = iter.next();
            if (rti.equals(rt)) {
                found = true;
                break;
            }
        }
        assertTrue("did not find added runtime", found);
        rm.remove(rt);
        iter = rm.iterateRuntimes();
        assertTrue(iter.hasNext());
        found = false;
        while (iter.hasNext()) {
            JavaRuntime rti = iter.next();
            if (rti.equals(rt)) {
                found = true;
                break;
            }
        }
        assertTrue("found added runtime after removal", !found);
    }

    public void test_RuntimeManager_addRemoveListener() {
        RuntimeManager rm = RuntimeProvider.getRuntimeManager();
        TestListener tl = new TestListener();
        rm.addRuntimeListener(tl);
        JavaRuntime runtime1 = new DummyRuntime();
        rm.add(runtime1);
        JavaRuntime runtime2 = new DummyRuntime();
        rm.add(runtime2);
        rm.remove(runtime1);
        assertTrue("missed add event", tl.added == 2);
        assertTrue("missed remove event", tl.removed == 1);
        rm.removeRuntimeListener(tl);
    }

    private static class TestListener implements RuntimeListener {

        public int added;

        public int removed;

        @Override
        public void runtimeAdded(RuntimeEvent event) {
            added++;
        }

        @Override
        public void runtimeRemoved(RuntimeEvent event) {
            removed++;
        }
    }
}

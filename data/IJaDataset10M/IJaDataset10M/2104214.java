package android.core;

import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.test.suitebuilder.annotation.Suppress;
import dalvik.system.VMRuntime;
import junit.framework.TestCase;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Random;

public class HeapTest extends TestCase {

    private static final String TAG = "HeapTest";

    /**
     * Returns a WeakReference to an object that has no
     * other references.  This is done in a separate method
     * to ensure that the Object's address isn't sitting in
     * a stale local register.
     */
    private WeakReference<Object> newRef() {
        return new WeakReference<Object>(new Object());
    }

    /**
     * Allocates the specified number of bytes. This is done in a separate method
     * to ensure that the Object's address isn't sitting in a stale local register.
     */
    private void allocateMemory(int size) {
        byte[] b = new byte[size];
    }

    @MediumTest
    public void testMinimumHeapSize() throws Exception {
        VMRuntime r = VMRuntime.getRuntime();
        final boolean RUN_FLAKY = false;
        long origSize = r.getMinimumHeapSize();
        if (RUN_FLAKY) {
            assertTrue(origSize == 0);
        }
        long size = 4 * 1024 * 1024;
        long oldSize = r.setMinimumHeapSize(size);
        assertTrue(oldSize == origSize);
        long newSize = r.getMinimumHeapSize();
        assertTrue(newSize == size);
        newSize = r.getMinimumHeapSize();
        assertTrue(newSize == size);
        if (RUN_FLAKY) {
            WeakReference ref = newRef();
            assertNotNull(ref.get());
            r.setMinimumHeapSize(8 * 1024 * 1024);
            allocateMemory(4 * 1024 * 1024);
            assertNotNull(ref.get());
        }
        r.setMinimumHeapSize(origSize);
        newSize = r.getMinimumHeapSize();
        assertTrue(newSize == origSize);
        Runtime.getRuntime().gc();
    }

    private static void makeRefs(Object objects[], SoftReference<Object> refs[]) {
        for (int i = 0; i < objects.length; i++) {
            objects[i] = (Object) new byte[8 * 1024];
            refs[i] = new SoftReference<Object>(objects[i]);
        }
    }

    private static <T> int checkRefs(SoftReference<T> refs[], int last) {
        int i;
        int numCleared = 0;
        for (i = 0; i < refs.length; i++) {
            Object o = refs[i].get();
            if (o == null) {
                numCleared++;
            }
        }
        if (numCleared != last) {
            Log.i(TAG, "****** " + numCleared + "/" + i + " cleared ******");
        }
        return numCleared;
    }

    private static void clearRefs(Object objects[], int skip) {
        for (int i = 0; i < objects.length; i += skip) {
            objects[i] = null;
        }
    }

    private static void clearRefs(Object objects[]) {
        clearRefs(objects, 1);
    }

    private static <T> void checkRefs(T objects[], SoftReference<T> refs[]) {
        boolean ok = true;
        for (int i = 0; i < objects.length; i++) {
            if (refs[i].get() != objects[i]) {
                ok = false;
            }
        }
        if (!ok) {
            throw new RuntimeException("Test failed: soft refs not cleared");
        }
    }

    @MediumTest
    public void testGcSoftRefs() throws Exception {
        final int NUM_REFS = 128;
        Object objects[] = new Object[NUM_REFS];
        SoftReference<Object> refs[] = new SoftReference[objects.length];
        makeRefs(objects, refs);
        Runtime.getRuntime().gc();
        clearRefs(objects, 3);
        VMRuntime.getRuntime().gcSoftReferences();
        Runtime.getRuntime().runFinalization();
        checkRefs(objects, refs);
        clearRefs(objects, 2);
        VMRuntime.getRuntime().gcSoftReferences();
        Runtime.getRuntime().runFinalization();
        checkRefs(objects, refs);
        clearRefs(objects);
        VMRuntime.getRuntime().gcSoftReferences();
        Runtime.getRuntime().runFinalization();
        checkRefs(objects, refs);
    }

    public void xxtestSoftRefPartialClean() throws Exception {
        final int NUM_REFS = 128;
        Object objects[] = new Object[NUM_REFS];
        SoftReference<Object> refs[] = new SoftReference[objects.length];
        makeRefs(objects, refs);
        Runtime.getRuntime().gc();
        clearRefs(objects);
        final int NUM_OBJECTS = 64 * 1024;
        Object junk[] = new Object[NUM_OBJECTS];
        Random random = new Random();
        int i = 0;
        int mod = 0;
        int totalSize = 0;
        int cleared = -1;
        while (i < junk.length && totalSize < 8 * 1024 * 1024) {
            int r = random.nextInt(64 * 1024) + 128;
            Object o = (Object) new byte[r];
            if (++mod % 16 == 0) {
                junk[i++] = o;
                totalSize += r * 4;
            }
            cleared = checkRefs(refs, cleared);
        }
    }

    private static void makeRefs(Object objects[], WeakReference<Object> refs[]) {
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new Object();
            refs[i] = new WeakReference<Object>(objects[i]);
        }
    }

    private static <T> void checkRefs(T objects[], WeakReference<T> refs[]) {
        boolean ok = true;
        for (int i = 0; i < objects.length; i++) {
            if (refs[i].get() != objects[i]) {
                ok = false;
            }
        }
        if (!ok) {
            throw new RuntimeException("Test failed: " + "weak refs not cleared");
        }
    }

    @MediumTest
    public void testWeakRefs() throws Exception {
        final int NUM_REFS = 16;
        Object objects[] = new Object[NUM_REFS];
        WeakReference<Object> refs[] = new WeakReference[objects.length];
        makeRefs(objects, refs);
        Runtime.getRuntime().gc();
        checkRefs(objects, refs);
        for (int i = 0; i < objects.length; i += 2) {
            objects[i] = null;
        }
        Runtime.getRuntime().gc();
        checkRefs(objects, refs);
        for (int i = 0; i < objects.length; i++) {
            objects[i] = null;
        }
        Runtime.getRuntime().gc();
        checkRefs(objects, refs);
    }

    private static void makeRefs(Object objects[], PhantomReference<Object> refs[], ReferenceQueue<Object> queue) {
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new Object();
            refs[i] = new PhantomReference<Object>(objects[i], queue);
        }
    }

    static <T> void checkRefs(T objects[], PhantomReference<T> refs[], ReferenceQueue<T> queue) {
        boolean ok = true;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null && refs[i] != null) {
                if (!refs[i].isEnqueued()) {
                    ok = false;
                }
            }
        }
        if (!ok) {
            throw new RuntimeException("Test failed: " + "phantom refs not marked as enqueued");
        }
        PhantomReference<T> ref;
        while ((ref = (PhantomReference<T>) queue.poll()) != null) {
            int i;
            for (i = 0; i < objects.length; i++) {
                if (refs[i] == ref) {
                    break;
                }
            }
            if (i == objects.length) {
                throw new RuntimeException("Test failed: " + "unexpected ref on queue");
            }
            if (objects[i] != null) {
                throw new RuntimeException("Test failed: " + "reference enqueued for strongly-reachable " + "object");
            }
            refs[i] = null;
            ref.clear();
        }
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null && refs[i] != null) {
                ok = false;
            }
        }
        if (!ok) {
            throw new RuntimeException("Test failed: " + "phantom refs not enqueued");
        }
    }

    @MediumTest
    public void testPhantomRefs() throws Exception {
        final int NUM_REFS = 16;
        Object objects[] = new Object[NUM_REFS];
        PhantomReference<Object> refs[] = new PhantomReference[objects.length];
        ReferenceQueue<Object> queue = new ReferenceQueue<Object>();
        makeRefs(objects, refs, queue);
        Runtime.getRuntime().gc();
        checkRefs(objects, refs, queue);
        for (int i = 0; i < objects.length; i += 2) {
            objects[i] = null;
        }
        Runtime.getRuntime().gc();
        Runtime.getRuntime().runFinalization();
        checkRefs(objects, refs, queue);
        for (int i = 0; i < objects.length; i++) {
            objects[i] = null;
        }
        Runtime.getRuntime().gc();
        Runtime.getRuntime().runFinalization();
        checkRefs(objects, refs, queue);
    }

    private static int sNumFinalized = 0;

    private static final Object sLock = new Object();

    private static class FinalizableObject {

        protected void finalize() {
            Runtime.getRuntime().gc();
            synchronized (sLock) {
                sNumFinalized++;
            }
        }
    }

    private static void makeRefs(FinalizableObject objects[], WeakReference<FinalizableObject> refs[]) {
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new FinalizableObject();
            refs[i] = new WeakReference<FinalizableObject>(objects[i]);
        }
    }

    @LargeTest
    public void testWeakRefsAndFinalizers() throws Exception {
        final int NUM_REFS = 16;
        FinalizableObject objects[] = new FinalizableObject[NUM_REFS];
        WeakReference<FinalizableObject> refs[] = new WeakReference[objects.length];
        int numCleared;
        makeRefs(objects, refs);
        Runtime.getRuntime().gc();
        checkRefs(objects, refs);
        sNumFinalized = 0;
        numCleared = 0;
        for (int i = 0; i < objects.length; i += 2) {
            objects[i] = null;
            numCleared++;
        }
        Runtime.getRuntime().gc();
        Runtime.getRuntime().runFinalization();
        checkRefs(objects, refs);
        if (sNumFinalized != numCleared) {
            throw new RuntimeException("Test failed: " + "expected " + numCleared + " finalizations, saw " + sNumFinalized);
        }
        sNumFinalized = 0;
        numCleared = 0;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                objects[i] = null;
                numCleared++;
            }
        }
        Runtime.getRuntime().gc();
        Runtime.getRuntime().runFinalization();
        checkRefs(objects, refs);
        if (sNumFinalized != numCleared) {
            throw new RuntimeException("Test failed: " + "expected " + numCleared + " finalizations, saw " + sNumFinalized);
        }
    }

    public void testOomeLarge() throws Exception {
        final int SIXTEEN_MB = (16 * 1024 * 1024 - 32);
        Boolean sawEx = false;
        byte a[];
        try {
            a = new byte[SIXTEEN_MB];
        } catch (OutOfMemoryError oom) {
            sawEx = true;
        }
        if (!sawEx) {
            throw new RuntimeException("Test failed: " + "OutOfMemoryError not thrown");
        }
    }

    @Suppress
    public void disableTestOomeSmall() throws Exception {
        final int SIXTEEN_MB = (16 * 1024 * 1024);
        final int LINK_SIZE = 6 * 4;
        Boolean sawEx = false;
        LinkedList<Object> list = new LinkedList<Object>();
        int objSize = 1 * 1024 * 1024;
        while (objSize >= LINK_SIZE) {
            try {
                for (int i = 0; i < SIXTEEN_MB / objSize; i++) {
                    list.add((Object) new byte[objSize]);
                }
            } catch (OutOfMemoryError oom) {
                sawEx = true;
            }
            if (!sawEx) {
                throw new RuntimeException("Test failed: " + "OutOfMemoryError not thrown while filling heap");
            }
            sawEx = false;
            objSize = (objSize * 4) / 5;
        }
    }

    public void testExternalOomeLarge() {
        final int HUGE_SIZE = (16 * 1024 * 1024 - 32);
        assertFalse(VMRuntime.getRuntime().trackExternalAllocation(HUGE_SIZE));
    }

    /**
     * "Allocates" external memory in progressively smaller chunks until there's
     * only roughly 16 bytes left.
     *
     * @return the number of bytes allocated
     */
    private long allocateMaxExternal() {
        final VMRuntime runtime = VMRuntime.getRuntime();
        final int SIXTEEN_MB = (16 * 1024 * 1024);
        final int MIN_SIZE = 16;
        long totalAllocated = 0;
        boolean success;
        success = false;
        try {
            int objSize = 1 * 1024 * 1024;
            while (objSize >= MIN_SIZE) {
                boolean sawFailure = false;
                for (int i = 0; i < SIXTEEN_MB / objSize; i++) {
                    if (runtime.trackExternalAllocation(objSize)) {
                        totalAllocated += objSize;
                    } else {
                        sawFailure = true;
                        break;
                    }
                }
                if (!sawFailure) {
                    throw new RuntimeException("Test failed: " + "no failure while filling heap");
                }
                objSize = (objSize * 4) / 5;
            }
            success = true;
        } finally {
            if (!success) {
                runtime.trackExternalFree(totalAllocated);
                totalAllocated = 0;
            }
        }
        return totalAllocated;
    }

    public void xxtest00ExternalOomeSmall() {
        VMRuntime.getRuntime().trackExternalFree(allocateMaxExternal());
    }

    /**
     * Allocates as much external memory as possible, then allocates from the heap
     * until an OOME is caught.
     *
     * It's nice to run this test while the real heap is small, hence the '00' in its
     * name to force it to run before testOomeSmall().
     */
    public void xxtest00CombinedOomeSmall() {
        long totalAllocated = 0;
        boolean sawEx = false;
        try {
            totalAllocated = allocateMaxExternal();
            LinkedList<Object> list = new LinkedList<Object>();
            try {
                while (true) {
                    list.add((Object) new byte[8192]);
                }
            } catch (OutOfMemoryError oom) {
                sawEx = true;
            }
        } finally {
            VMRuntime.getRuntime().trackExternalFree(totalAllocated);
        }
        assertTrue(sawEx);
    }
}

package sun.management;

import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.GarbageCollectorMXBean;

/**
 * ManagementFactory class provides the methods that the HotSpot VM
 * will invoke. So the class and method names cannot be renamed.
 */
class ManagementFactory {

    private ManagementFactory() {
    }

    ;

    private static MemoryPoolMXBean createMemoryPool(String name, boolean isHeap, long uThreshold, long gcThreshold) {
        return new MemoryPoolImpl(name, isHeap, uThreshold, gcThreshold);
    }

    private static MemoryManagerMXBean createMemoryManager(String name) {
        return new MemoryManagerImpl(name);
    }

    private static GarbageCollectorMXBean createGarbageCollector(String name, String type) {
        return new GarbageCollectorImpl(name);
    }
}

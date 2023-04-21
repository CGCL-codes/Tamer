package net.bull.javamelody;

import java.io.Serializable;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;

/**
 * Informations systèmes sur la mémoire du serveur, sans code html de présentation.
 * L'état d'une instance est initialisé à son instanciation et non mutable;
 * il est donc de fait thread-safe.
 * Cet état est celui d'une instance de JVM java.
 * Les instances sont sérialisables pour pouvoir être transmises au serveur de collecte.
 * @author Emeric Vernat
 */
class MemoryInformations implements Serializable {

    private static final long serialVersionUID = 3281861236369720876L;

    private static final String NEXT = ",\n";

    private static final String MO = " Mo";

    private final long usedMemory;

    private final long maxMemory;

    private final long usedPermGen;

    private final long maxPermGen;

    private final long usedNonHeapMemory;

    private final int loadedClassesCount;

    private final long garbageCollectionTimeMillis;

    private final long usedPhysicalMemorySize;

    private final long usedSwapSpaceSize;

    private final String memoryDetails;

    MemoryInformations() {
        super();
        usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        maxMemory = Runtime.getRuntime().maxMemory();
        final MemoryPoolMXBean permGenMemoryPool = getPermGenMemoryPool();
        if (permGenMemoryPool != null) {
            final MemoryUsage usage = permGenMemoryPool.getUsage();
            usedPermGen = usage.getUsed();
            maxPermGen = usage.getMax();
        } else {
            usedPermGen = -1;
            maxPermGen = -1;
        }
        usedNonHeapMemory = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
        loadedClassesCount = ManagementFactory.getClassLoadingMXBean().getLoadedClassCount();
        garbageCollectionTimeMillis = buildGarbageCollectionTimeMillis();
        final OperatingSystemMXBean operatingSystem = ManagementFactory.getOperatingSystemMXBean();
        if (isSunOsMBean(operatingSystem)) {
            final com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) operatingSystem;
            usedPhysicalMemorySize = osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize();
            usedSwapSpaceSize = osBean.getTotalSwapSpaceSize() - osBean.getFreeSwapSpaceSize();
        } else {
            usedPhysicalMemorySize = -1;
            usedSwapSpaceSize = -1;
        }
        memoryDetails = buildMemoryDetails();
    }

    private static MemoryPoolMXBean getPermGenMemoryPool() {
        for (final MemoryPoolMXBean memoryPool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (memoryPool.getName().endsWith("Perm Gen")) {
                return memoryPool;
            }
        }
        return null;
    }

    private static long buildGarbageCollectionTimeMillis() {
        long garbageCollectionTime = 0;
        for (final GarbageCollectorMXBean garbageCollector : ManagementFactory.getGarbageCollectorMXBeans()) {
            garbageCollectionTime += garbageCollector.getCollectionTime();
        }
        return garbageCollectionTime;
    }

    private String buildMemoryDetails() {
        final DecimalFormat integerFormat = I18N.createIntegerFormat();
        final String nonHeapMemory = "Non heap memory = " + integerFormat.format(usedNonHeapMemory / 1024 / 1024) + MO + " (Perm Gen, Code Cache)";
        final String classLoading = "Loaded classes = " + integerFormat.format(loadedClassesCount);
        final String gc = "Garbage collection time = " + integerFormat.format(garbageCollectionTimeMillis) + " ms";
        final OperatingSystemMXBean operatingSystem = ManagementFactory.getOperatingSystemMXBean();
        String osInfo = "";
        if (isSunOsMBean(operatingSystem)) {
            final com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) operatingSystem;
            osInfo = "Process cpu time = " + integerFormat.format(osBean.getProcessCpuTime() / 1000000) + " ms,\nCommitted virtual memory = " + integerFormat.format(osBean.getCommittedVirtualMemorySize() / 1024 / 1024) + MO + ",\nFree physical memory = " + integerFormat.format(osBean.getFreePhysicalMemorySize() / 1024 / 1024) + MO + ",\nTotal physical memory = " + integerFormat.format(osBean.getTotalPhysicalMemorySize() / 1024 / 1024) + MO + ",\nFree swap space = " + integerFormat.format(osBean.getFreeSwapSpaceSize() / 1024 / 1024) + MO + ",\nTotal swap space = " + integerFormat.format(osBean.getTotalSwapSpaceSize() / 1024 / 1024) + MO;
        }
        return nonHeapMemory + NEXT + classLoading + NEXT + gc + NEXT + osInfo;
    }

    private static boolean isSunOsMBean(OperatingSystemMXBean operatingSystem) {
        final String className = operatingSystem.getClass().getName();
        return "com.sun.management.OperatingSystem".equals(className) || "com.sun.management.UnixOperatingSystem".equals(className);
    }

    long getUsedMemory() {
        return usedMemory;
    }

    long getMaxMemory() {
        return maxMemory;
    }

    double getUsedMemoryPercentage() {
        return 100d * usedMemory / maxMemory;
    }

    long getUsedPermGen() {
        return usedPermGen;
    }

    long getMaxPermGen() {
        return maxPermGen;
    }

    double getUsedPermGenPercentage() {
        if (usedPermGen > 0 && maxPermGen > 0) {
            return 100d * usedPermGen / maxPermGen;
        }
        return -1d;
    }

    long getUsedNonHeapMemory() {
        return usedNonHeapMemory;
    }

    int getLoadedClassesCount() {
        return loadedClassesCount;
    }

    long getGarbageCollectionTimeMillis() {
        return garbageCollectionTimeMillis;
    }

    long getUsedPhysicalMemorySize() {
        return usedPhysicalMemorySize;
    }

    long getUsedSwapSpaceSize() {
        return usedSwapSpaceSize;
    }

    String getMemoryDetails() {
        return memoryDetails;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[usedMemory=" + getUsedMemory() + ", maxMemroy=" + getMaxMemory() + ']';
    }
}

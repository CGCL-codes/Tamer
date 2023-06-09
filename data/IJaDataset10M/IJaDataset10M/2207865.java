package sun.management;

import java.util.List;
import sun.management.counter.Counter;

/**
 * An interface for the monitoring and management of the
 * Java virtual machine.
 */
public interface VMManagement {

    public boolean isCompilationTimeMonitoringSupported();

    public boolean isThreadContentionMonitoringSupported();

    public boolean isThreadContentionMonitoringEnabled();

    public boolean isCurrentThreadCpuTimeSupported();

    public boolean isOtherThreadCpuTimeSupported();

    public boolean isThreadCpuTimeEnabled();

    public boolean isBootClassPathSupported();

    public boolean isObjectMonitorUsageSupported();

    public boolean isSynchronizerUsageSupported();

    public long getTotalClassCount();

    public int getLoadedClassCount();

    public long getUnloadedClassCount();

    public boolean getVerboseClass();

    public boolean getVerboseGC();

    public String getManagementVersion();

    public String getVmId();

    public String getVmName();

    public String getVmVendor();

    public String getVmVersion();

    public String getVmSpecName();

    public String getVmSpecVendor();

    public String getVmSpecVersion();

    public String getClassPath();

    public String getLibraryPath();

    public String getBootClassPath();

    public List<String> getVmArguments();

    public long getStartupTime();

    public int getAvailableProcessors();

    public String getCompilerName();

    public long getTotalCompileTime();

    public long getTotalThreadCount();

    public int getLiveThreadCount();

    public int getPeakThreadCount();

    public int getDaemonThreadCount();

    public String getOsName();

    public String getOsArch();

    public String getOsVersion();

    public long getSafepointCount();

    public long getTotalSafepointTime();

    public long getSafepointSyncTime();

    public long getTotalApplicationNonStoppedTime();

    public long getLoadedClassSize();

    public long getUnloadedClassSize();

    public long getClassLoadingTime();

    public long getMethodDataSize();

    public long getInitializedClassCount();

    public long getClassInitializationTime();

    public long getClassVerificationTime();

    public List<Counter> getInternalCounters(String pattern);
}

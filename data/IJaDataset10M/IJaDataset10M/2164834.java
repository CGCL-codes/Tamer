package org.simpleframework.location;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;

public class ApplicationMonitor extends Thread {

    private static final long KILOBYTE = 1024L;

    private final ClassLoadingMXBean classLoadingBean;

    private final ThreadMXBean threadBean;

    private final MemoryMXBean memoryBean;

    public ApplicationMonitor() {
        this.classLoadingBean = ManagementFactory.getClassLoadingMXBean();
        this.threadBean = ManagementFactory.getThreadMXBean();
        this.memoryBean = ManagementFactory.getMemoryMXBean();
    }

    public static long bytesToKilo(long bytes) {
        return bytes / KILOBYTE;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                long heapUsed = memoryBean.getHeapMemoryUsage().getUsed();
                long heapTotal = memoryBean.getHeapMemoryUsage().getMax();
                long nonHeapUsed = memoryBean.getNonHeapMemoryUsage().getUsed();
                long nonHeapTotal = memoryBean.getNonHeapMemoryUsage().getMax();
                int threadCount = threadBean.getThreadCount();
                int classesLoaded = classLoadingBean.getLoadedClassCount();
                long classesLoadedTotal = classLoadingBean.getTotalLoadedClassCount();
                System.out.println(" threadCount=[" + threadCount + "]" + " heapUsed=[" + bytesToKilo(heapUsed) + "K]" + " heapTotal=[" + bytesToKilo(heapTotal) + "K]" + " nonHeapUsed=[" + bytesToKilo(nonHeapUsed) + "K]" + " nonHeapTotal=[" + bytesToKilo(nonHeapTotal) + "K]" + " classesLoaded=[" + classesLoaded + "]" + " classesLoadedTotal=[" + classesLoadedTotal + "]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

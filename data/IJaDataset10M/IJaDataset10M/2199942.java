package net.sf.profiler4j.console.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.management.MemoryUsage;
import net.sf.profiler4j.agent.ServerUtil;

/**
 * Each instance of <code>MemoryInfo</code> represents a snapshot of the current memory
 * usage, including heap and non-heap.
 * 
 * @author Antonio S. R. Gomes
 */
public class MemoryInfo implements Serializable {

    private MemoryUsage heapUsage;

    private MemoryUsage nonHeapUsage;

    private int objectPendingFinalizationCount;

    public MemoryUsage getHeapUsage() {
        return this.heapUsage;
    }

    public void setHeapUsage(MemoryUsage heapUsage) {
        this.heapUsage = heapUsage;
    }

    public MemoryUsage getNonHeapUsage() {
        return this.nonHeapUsage;
    }

    public int getObjectPendingFinalizationCount() {
        return this.objectPendingFinalizationCount;
    }

    public static MemoryInfo read(ObjectInputStream in) throws IOException {
        MemoryInfo mi = new MemoryInfo();
        mi.heapUsage = ServerUtil.readMemoryUsage(in);
        mi.nonHeapUsage = ServerUtil.readMemoryUsage(in);
        mi.objectPendingFinalizationCount = in.readInt();
        return mi;
    }
}

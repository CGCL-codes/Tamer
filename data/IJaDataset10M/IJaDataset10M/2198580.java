package gov.nasa.jpf.tools;

import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.report.ConsolePublisher;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.report.PublisherExtensionAdapter;
import gov.nasa.jpf.search.Search;

/**
 * Listener that implements various budget constraints
 */
public class BudgetChecker extends ListenerAdapter {

    static final int CHECK_INTERVAL = 10000;

    static final int CHECK_INTERVAL1 = CHECK_INTERVAL - 1;

    class ConsolePublisherExtension extends PublisherExtensionAdapter {

        public void publishFinished(Publisher publisher) {
            if (message != null) {
                PrintWriter pw = publisher.getOut();
                publisher.publishTopicStart("budget exceeded");
                pw.println(message);
            }
        }
    }

    long tStart;

    MemoryUsage muStart;

    long mStart;

    MemoryMXBean mxb;

    JVM vm;

    Search search;

    long insnCount;

    long maxTime;

    long maxState;

    long maxDepth;

    long maxInsn;

    long maxHeap;

    String message;

    public BudgetChecker(Config conf, JPF jpf) {
        maxTime = conf.getDuration("jpf.budget.max_time", -1);
        maxHeap = conf.getMemorySize("jpf.budget.max_heap", -1);
        maxDepth = conf.getLong("jpf.budget.max_depth", -1);
        maxInsn = conf.getLong("jpf.budget.max_insn", -1);
        maxState = conf.getLong("jpf.budget.max_state", -1);
        tStart = System.currentTimeMillis();
        if (maxHeap > 0) {
            mxb = ManagementFactory.getMemoryMXBean();
            muStart = mxb.getHeapMemoryUsage();
            mStart = muStart.getUsed();
        }
        search = jpf.getSearch();
        vm = jpf.getVM();
        jpf.addPublisherExtension(ConsolePublisher.class, new ConsolePublisherExtension());
    }

    public boolean timeExceeded() {
        if (maxTime > 0) {
            long dur = System.currentTimeMillis() - tStart;
            if (dur > maxTime) {
                message = "max time exceeded: " + Publisher.formatHMS(dur) + " >= " + Publisher.formatHMS(maxTime);
                return true;
            }
        }
        return false;
    }

    public boolean heapExceeded() {
        if (maxHeap > 0) {
            MemoryUsage mu = mxb.getHeapMemoryUsage();
            long used = mu.getUsed() - mStart;
            if (used > maxHeap) {
                message = "max heap exceeded: " + (used / (1024 * 1024)) + "MB" + " >= " + (maxHeap / (1024 * 1024)) + "MB";
                return true;
            }
        }
        return false;
    }

    public boolean depthExceeded() {
        if (maxDepth > 0) {
            int d = search.getDepth();
            if (d > maxDepth) {
                message = "max search depth exceeded: " + d + " >= " + maxDepth;
                return true;
            }
        }
        return false;
    }

    public boolean statesExceeded() {
        if (maxState > 0) {
            int stateId = vm.getStateId();
            if (stateId > maxState) {
                message = "max states exceeded: " + stateId + " >= " + maxState;
                ;
                return true;
            }
        }
        return false;
    }

    public boolean insnExceeded() {
        if (maxInsn > 0) {
            if (insnCount > maxInsn) {
                message = "max instruction count exceeded: " + insnCount + " >= " + maxInsn;
                return true;
            }
        }
        return false;
    }

    public void stateAdvanced(Search search) {
        if (timeExceeded() || heapExceeded()) {
            search.terminate();
        }
    }

    public void instructionExecuted(JVM vm) {
        if ((insnCount++ % CHECK_INTERVAL) == CHECK_INTERVAL1) {
            if (timeExceeded() || heapExceeded() || insnExceeded()) {
                vm.getCurrentThread().breakTransition();
                search.terminate();
            }
        }
    }
}

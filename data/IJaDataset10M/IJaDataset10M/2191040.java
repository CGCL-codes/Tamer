package oops.util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import oops.DependencyVisitor;

/**
 * The Concurrent Dependency Visitor is an implementation of the
 * DependencyVisitor that uses Java concurrency utilities to retrieve the list
 * of failures or successes.
 */
public class ConcurrentDependencyVisitor implements DependencyVisitor {

    private CountDownLatch endGate = new CountDownLatch(1);

    private Set<String> failures = new HashSet<String>();

    private Set<String> successes = new HashSet<String>();

    public void end() {
        endGate.countDown();
    }

    public void fail(String name) {
        if (endGate.getCount() == 0) return;
        synchronized (failures) {
            failures.add(name);
        }
    }

    public void success(String name) {
        if (endGate.getCount() == 0) return;
        synchronized (successes) {
            successes.add(name);
        }
    }

    /**
     * Return the Set of classes that could not be found and loaded for
     * bytecode inspection.  This method will block for the completion of 
     * analysis.
     * @return a Set of Strings of class names
     * @throws InterruptedException
     */
    public Set<String> getFailures() throws InterruptedException {
        endGate.await();
        return new HashSet<String>(failures);
    }

    /**
     * Return the Set of classes that could be found and loaded for bytecode
     * inspection.  This method will block for the completion of analysis.
     * @return a Set of Strings of class names
     * @throws InterruptedException
     */
    public Set<String> getSuccesses() throws InterruptedException {
        endGate.await();
        return new HashSet<String>(successes);
    }
}

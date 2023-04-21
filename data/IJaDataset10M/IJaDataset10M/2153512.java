package gloodb;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimeOperationTestBase {

    enum Operation {

        Create("Create"), ContainsNotCached("Contains (not cached)"), ContainsCached("Contains (cached)"), UpdateNotCached("Update (not cached)"), UpdateCached("Update (cached)"), StoreNew("Store new"), StoreExistentNotCached("Store existent (not cached)"), StoreExistentCached("Store existent (cached)"), RestoreNotCached("Restore (not cached)"), RestoreCached("Restore (cached)"), RemoveNotCached("Remove (not cached)"), RemoveCached("Remove (cached)");

        final String reportName;

        final HashMap<String, Long> implementationTimes;

        Operation(String reportName) {
            this.reportName = reportName;
            this.implementationTimes = new HashMap<String, Long>();
        }

        String getReportName() {
            return reportName;
        }

        void putTime(String implementation, long time) {
            Long totalTime = getTime(implementation);
            this.implementationTimes.put(implementation, totalTime + time);
        }

        long getTime(String implementation) {
            Long totalTime = this.implementationTimes.get(implementation);
            return totalTime == null ? Long.valueOf(0) : totalTime;
        }
    }

    private static final HashSet<String> implementations = new HashSet<String>();

    private static final int iterations = 100;

    private final Repository repository;

    private final PersistentFactory factory;

    private final String id = "testId";

    private String implementation;

    public TimeOperationTestBase(String implementation, Repository repository, PersistentFactory factory) {
        super();
        this.repository = repository;
        this.factory = factory;
        this.implementation = implementation;
    }

    @Before
    public void setUp() {
        if (!repository.contains(SimpleInterceptor.class)) repository.create(new SimpleInterceptor());
        implementations.add(this.implementation);
    }

    @After
    public void printReports() {
        for (Operation o : Operation.values()) {
            System.out.println(String.format("[%s] %s: %d microseconds", implementation, o.getReportName(), o.getTime(implementation) / 1000 / iterations));
        }
    }

    @Test
    public void timeOperations() throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (int i = 0; i < iterations; i++) {
            runIteration();
        }
        System.out.print("");
    }

    private void runIteration() throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Serializable object = factory.newObject(id);
        try {
            object = timeCreate(object);
            object = timeStore(object, Operation.StoreExistentCached);
            object = timeUpdate(object, Operation.UpdateCached);
            timeContains(object, Operation.ContainsCached);
            object = timeRestore(object, Operation.RestoreCached);
            timeRemove(object, Operation.RemoveCached);
            object = timeStore(factory.newObject(id), Operation.StoreNew);
            flush(100);
            object = timeStore(object, Operation.StoreExistentNotCached);
            flush(100);
            timeUpdate(object, Operation.UpdateNotCached);
            flush(100);
            timeContains(object, Operation.ContainsNotCached);
            flush(100);
            object = timeRestore(object, Operation.RestoreNotCached);
            flush(100);
            timeRemove(object, Operation.RemoveNotCached);
        } finally {
            this.repository.remove(id);
        }
    }

    private void timeContains(Serializable object, Operation operation) {
        long startTime = System.nanoTime();
        this.repository.contains(object);
        long endTime = System.nanoTime();
        operation.putTime(implementation, endTime - startTime);
    }

    private Serializable timeRestore(Serializable object, Operation operation) {
        long startTime = System.nanoTime();
        object = this.repository.restore(object);
        long endTime = System.nanoTime();
        operation.putTime(implementation, endTime - startTime);
        return object;
    }

    private void timeRemove(Serializable object, Operation operation) {
        long startTime = System.nanoTime();
        object = this.repository.remove(id);
        long endTime = System.nanoTime();
        operation.putTime(implementation, endTime - startTime);
    }

    private Serializable timeStore(Serializable object, Operation operation) {
        long startTime = System.nanoTime();
        object = this.repository.store(object);
        long endTime = System.nanoTime();
        operation.putTime(implementation, endTime - startTime);
        return object;
    }

    private Serializable timeUpdate(Serializable object, Operation operation) {
        long startTime = System.nanoTime();
        object = this.repository.update(object);
        long endTime = System.nanoTime();
        operation.putTime(implementation, endTime - startTime);
        return object;
    }

    private Serializable timeCreate(Serializable object) {
        long startTime = System.nanoTime();
        object = this.repository.create(object);
        long endTime = System.nanoTime();
        Operation.Create.putTime(implementation, endTime - startTime);
        return object;
    }

    private void flush(int precentage) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method flushMethod = repository.getClass().getMethod("flushCache", int.class);
        flushMethod.setAccessible(true);
        flushMethod.invoke(repository, 100);
    }
}

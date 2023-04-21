package correctness.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import correctness.concurrent.util.TestConsumerProcess;
import correctness.concurrent.util.TestProducerProcess;
import junit.framework.TestCase;
import lights.exceptions.TupleSpaceException;
import lights.interfaces.ITuple;
import lights.interfaces.ITupleSpace;
import lights.space.Field;
import lights.space.Tuple;
import lights.space.ReadWriteLockedCopyOnWriteTupleSpace;

;

public class RWLCoWTSConcurrencyTest extends TestCase {

    private ITupleSpace ts;

    private List<Future<Boolean>> producerFutures;

    private List<Future<Boolean>> consumerFutures;

    private ExecutorService threadPool;

    private int invokeCount;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(RWLCoWTSConcurrencyTest.class);
    }

    protected void setUp() {
        this.ts = new ReadWriteLockedCopyOnWriteTupleSpace();
        this.producerFutures = new ArrayList<Future<Boolean>>();
        this.consumerFutures = new ArrayList<Future<Boolean>>();
        this.threadPool = Executors.newCachedThreadPool();
        this.invokeCount = 64;
    }

    protected void reset() {
        this.threadPool.shutdown();
        this.ts = new ReadWriteLockedCopyOnWriteTupleSpace();
        this.producerFutures = new ArrayList<Future<Boolean>>();
        this.consumerFutures = new ArrayList<Future<Boolean>>();
        this.threadPool = Executors.newCachedThreadPool();
    }

    public final void testTupleSpaceAccess() {
        for (int i = 0; i < this.invokeCount; i++) {
            consumerFutures.add(threadPool.submit(new TestConsumerProcess(ts)));
            producerFutures.add(threadPool.submit(new TestProducerProcess(ts)));
        }
        for (Future<Boolean> f : producerFutures) {
            try {
                f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail();
            } catch (ExecutionException e) {
                e.printStackTrace();
                fail();
            }
        }
        for (Future<Boolean> f : consumerFutures) {
            try {
                f.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail();
            } catch (ExecutionException e) {
                e.printStackTrace();
                fail();
            }
        }
        int tupleCount = -1;
        ITuple template = new Tuple();
        template.add(new Field().setType(Float.class));
        try {
            tupleCount = ts.count(template);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
            fail();
        }
        assertFalse("Could not count tuples in tuple space", tupleCount == -1);
        assertEquals("Tuple space is not empty.", tupleCount, 0);
        reset();
    }
}

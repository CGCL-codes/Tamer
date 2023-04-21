package threading.tests;

import org.junit.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import jccr.*;

public class TaskQueueTests extends TestUnitBase {

    @Test
    public void createQueue() {
        ThreadPool pool = new ThreadPool("t", 10);
        new TaskQueue(pool);
        Assert.assertTrue(pool.getQueueCount() == 1);
        new TaskQueue(pool);
        Assert.assertTrue(pool.getQueueCount() == 2);
    }

    @Test
    public void enqueueTask() throws Exception {
        enqueueTasks("enqueueTask", 0);
    }

    @Test
    public void enqueueTask10Child() throws Exception {
        enqueueTasks("enqueueTask10", 10);
    }

    @Test
    public void enqueueAfterPoolIdle() throws Exception {
        ThreadPool pool = new ThreadPool("pt", 5);
        TaskQueue queue = new TaskQueue(pool);
        TraceTask tt = new TraceTask("testTrace", 0);
        queue.enqueue(tt);
        tt.await();
        while (!pool.isIdle()) {
            System.out.println("waiting for idle...");
            Thread.sleep(100);
        }
        tt = new TraceTask("testTrace-2", 0);
        queue.enqueue(tt);
        tt.await();
    }

    void enqueueTasks(String text, int children) throws Exception {
        ThreadPool pool = new ThreadPool("t", 3);
        newLatch();
        TaskQueue q = new TaskQueue(pool);
        TraceTask t = new TraceTask(text, children);
        q.enqueue(t);
        t.await();
        pool.dispose();
    }

    class TraceTask implements Task {

        public TraceTask(String text, int children) {
            _text = text;
            for (int i = 0; i < children; i++) {
                _children.add(new ChildTrace(i));
            }
        }

        public Iterable<Task> execute() {
            System.out.println(_text + " thread id: " + Thread.currentThread().getName());
            _completed.countDown();
            return _children;
        }

        public void await() throws InterruptedException {
            _completed.await();
        }

        String _text;

        ArrayList<Task> _children = new ArrayList<Task>();

        CountDownLatch _completed = new CountDownLatch(1);
    }

    class ChildTrace implements Task {

        public ChildTrace(int i) {
            _i = i;
        }

        public Iterable<Task> execute() {
            System.out.println("Child " + _i + " thread id: " + Thread.currentThread().getName());
            return null;
        }

        int _i;
    }
}

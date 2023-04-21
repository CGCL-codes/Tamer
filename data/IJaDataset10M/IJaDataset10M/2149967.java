package test;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @version $Revision: 1.3 $
 */
public class MultiThreadTestRunner {

    public abstract static class Test {

        private List throwables = new ArrayList();

        public abstract void test() throws Exception;

        private synchronized void addThrowable(Throwable x) {
            throwables.add(x);
        }

        private synchronized Throwable[] getThrowables() {
            return (Throwable[]) throwables.toArray(new Throwable[throwables.size()]);
        }
    }

    private class MultiThrowable extends Exception {

        private final Throwable[] throwables;

        public MultiThrowable(Throwable[] throwables) {
            this.throwables = throwables;
        }

        public void printStackTrace(PrintStream stream) {
            synchronized (stream) {
                stream.println(this);
                for (int i = 0; i < throwables.length; ++i) throwables[i].printStackTrace(stream);
            }
        }

        public void printStackTrace(PrintWriter writer) {
            synchronized (writer) {
                writer.println(this);
                for (int i = 0; i < throwables.length; ++i) throwables[i].printStackTrace(writer);
            }
        }
    }

    private final int threads;

    private final int iterations;

    public MultiThreadTestRunner(int threads, int iterations) {
        this.threads = threads;
        this.iterations = iterations;
    }

    public void run(final Test test) throws Exception {
        Thread[] runners = new Thread[threads];
        for (int i = 0; i < threads; ++i) {
            runners[i] = new Thread(new Runnable() {

                public void run() {
                    for (int i = 0; i < iterations; ++i) {
                        try {
                            test.test();
                        } catch (Throwable x) {
                            test.addThrowable(x);
                        }
                    }
                }
            });
        }
        for (int i = 0; i < threads; ++i) runners[i].start();
        for (int i = 0; i < threads; ++i) runners[i].join();
        Throwable[] failures = test.getThrowables();
        if (failures != null && failures.length > 0) throw new MultiThrowable(failures);
    }
}

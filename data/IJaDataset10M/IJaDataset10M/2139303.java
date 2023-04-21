package foxtrot;

import javax.swing.SwingUtilities;

/**
 * @version $Revision: 263 $
 */
public class AsyncWorkerTest extends FoxtrotTestCase {

    public void testPostAndForget() throws Exception {
        final MutableReference result = new MutableReference(null);
        final WorkerThread workerThread = AsyncWorker.getWorkerThread();
        invokeTest(workerThread, new Runnable() {

            public void run() {
                workerThread.postTask(new Task() {

                    public Object run() throws Exception {
                        sleep(1000);
                        result.set(Boolean.TRUE);
                        return null;
                    }
                });
                sleep(500);
            }
        }, new Runnable() {

            public void run() {
                if (result.get() != Boolean.TRUE) fail("AsyncTask is not executed");
            }
        });
    }

    public void testUsage() throws Exception {
        final MutableReference result = new MutableReference(null);
        invokeTest(AsyncWorker.getWorkerThread(), new Runnable() {

            public void run() {
                AsyncWorker.post(new AsyncTask() {

                    private static final String VALUE = "1000";

                    public Object run() throws Exception {
                        Thread.sleep(1000);
                        return VALUE;
                    }

                    public void success(Object r) {
                        String value = (String) r;
                        if (!VALUE.equals(value)) result.set("AsyncTask.run() does not return the result");
                    }

                    public void failure(Throwable x) {
                        result.set(x.toString());
                    }
                });
                sleep(500);
            }
        }, new Runnable() {

            public void run() {
                if (result.get() != null) fail((String) result.get());
            }
        });
    }

    public void testThreads() throws Exception {
        final MutableReference result = new MutableReference(null);
        invokeTest(AsyncWorker.getWorkerThread(), new Runnable() {

            public void run() {
                AsyncWorker.post(new AsyncTask() {

                    public Object run() throws Exception {
                        if (SwingUtilities.isEventDispatchThread()) {
                            return "Must not be in the Event Dispatch Thread";
                        } else {
                            if (Thread.currentThread().getName().indexOf("Foxtrot") < 0) return "Must be in the Foxtrot Worker Thread";
                        }
                        return null;
                    }

                    public void success(Object r) {
                        String failure = (String) r;
                        if (failure == null) {
                            if (!SwingUtilities.isEventDispatchThread()) result.set("Must be in the Event Dispatch Thread");
                        } else {
                            result.set(failure);
                        }
                    }

                    public void failure(Throwable x) {
                        result.set(x.toString());
                    }
                });
                sleep(500);
            }
        }, new Runnable() {

            public void run() {
                if (result.get() != null) fail((String) result.get());
            }
        });
    }

    public void testTaskException() throws Exception {
        final MutableReference result = new MutableReference(null);
        invokeTest(AsyncWorker.getWorkerThread(), new Runnable() {

            public void run() {
                final IndexOutOfBoundsException ex = new IndexOutOfBoundsException();
                AsyncWorker.post(new AsyncTask() {

                    public Object run() throws Exception {
                        throw ex;
                    }

                    public void success(Object r) {
                        result.set("Expected exception");
                    }

                    public void failure(Throwable x) {
                        if (x instanceof IndexOutOfBoundsException) {
                            if (x != ex) result.set("Expected same exception");
                        } else {
                            result.set("Did not expect checked exception");
                        }
                    }
                });
                sleep(500);
            }
        }, new Runnable() {

            public void run() {
                if (result.get() != null) fail((String) result.get());
            }
        });
    }

    public void testTaskError() throws Exception {
        final MutableReference result = new MutableReference(null);
        invokeTest(AsyncWorker.getWorkerThread(), new Runnable() {

            public void run() {
                final Error ex = new Error();
                AsyncWorker.post(new AsyncTask() {

                    public Object run() throws Exception {
                        throw ex;
                    }

                    public void success(Object r) {
                        result.set("Expected error");
                    }

                    public void failure(Throwable x) {
                        if (x instanceof Error) {
                            if (x != ex) result.set("Expected same error");
                        } else {
                            result.set("Did not expect exception");
                        }
                    }
                });
                sleep(500);
            }
        }, new Runnable() {

            public void run() {
                if (result.get() != null) fail((String) result.get());
            }
        });
    }

    public void testPostFromTask() throws Exception {
        final MutableReference result = new MutableReference(null);
        invokeTest(AsyncWorker.getWorkerThread(), new Runnable() {

            public void run() {
                AsyncWorker.post(new AsyncTask() {

                    public Object run() {
                        AsyncWorker.post(new AsyncTask() {

                            public Object run() {
                                return null;
                            }

                            public void success(Object result) {
                            }

                            public void failure(Throwable x) {
                            }
                        });
                        return null;
                    }

                    public void success(Object r) {
                        result.set("Expected exception");
                    }

                    public void failure(Throwable x) {
                        if (!(x instanceof IllegalStateException)) {
                            result.set("Did not expect exception");
                        }
                    }
                });
            }
        }, new Runnable() {

            public void run() {
                if (result.get() != null) fail((String) result.get());
            }
        });
    }
}

package jp.eflow.hisano.dalvikvm.jvmtests;

public class ThreadWaitWithTimeTest {

    public static void main(String[] args) {
        final Object wait = new Object();
        java.lang.Thread thread = new java.lang.Thread(new Runnable() {

            public void run() {
                synchronized (wait) {
                    wait.notify();
                }
            }
        });
        long start = System.currentTimeMillis();
        synchronized (wait) {
            thread.start();
            try {
                wait.wait(10000);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
        }
        if (System.currentTimeMillis() - start < 10000) {
            System.out.println("notified");
        }
        synchronized (wait) {
            try {
                wait.wait(1000, 1000);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
        }
        System.out.println("timeout");
    }
}

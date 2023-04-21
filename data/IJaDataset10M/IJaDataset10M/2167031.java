package test.org.jikesrvm.basic.core.threads;

/**
 * Test that Thread.sleep() can be interrupted.
 */
public class TestInterruptedSleep {

    private static volatile boolean started;

    private static volatile boolean sleeping;

    public static void main(String[] argv) {
        final Thread main = Thread.currentThread();
        new Thread() {

            public void run() {
                started = true;
                while (!sleeping) Thread.yield();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                main.interrupt();
            }
        }.start();
        while (!started) Thread.yield();
        try {
            sleeping = true;
            Thread.sleep(2000);
            System.out.println("TestInterruptedSleep FAILED");
        } catch (InterruptedException e) {
            System.out.println("TestInterruptedSleep SUCCESS");
        }
    }
}

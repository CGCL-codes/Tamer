package integrationTests;

public final class MultiThreadedCode {

    public Thread nonBlockingOperation() {
        Thread worker = new Thread(new Runnable() {

            public void run() {
                new Object() {
                };
            }
        });
        worker.start();
        return worker;
    }
}

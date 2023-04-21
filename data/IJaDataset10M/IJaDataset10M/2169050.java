package loengud.yksteist;

public class Main implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        Main runnable = new Main();
        Thread t = new Thread(runnable);
        t.start();
        Thread.sleep(100);
        t = new Thread(runnable);
        t.start();
        t.interrupt();
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("thread:" + i);
        }
    }
}

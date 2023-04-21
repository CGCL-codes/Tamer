package aoetec.javalang._331concurrency;

public class TestDeadLock {

    public static void main(String[] args) {
        DeadLock deadLock1 = new DeadLock();
        DeadLock deadLock2 = new DeadLock();
        Thread t1 = new Thread(deadLock1);
        Thread t2 = new Thread(deadLock2);
        deadLock1.flag = 0;
        deadLock2.flag = 1;
        t1.start();
        t2.start();
    }
}

class DeadLock implements Runnable {

    int flag = 0;

    private static Object lockA = new Object();

    private static Object lockB = new Object();

    public void run() {
        if (flag == 0) {
            synchronized (lockA) {
                System.out.println("0 start");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
                synchronized (lockB) {
                    System.out.println("0 end");
                }
            }
        }
        if (flag == 1) {
            synchronized (lockB) {
                System.out.println("1 start");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
                synchronized (lockA) {
                    System.out.println("1 end");
                }
            }
        }
    }
}

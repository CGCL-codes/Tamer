package thread1;

import javax.sql.rowset.spi.SyncResolver;

public class Thredao {

    static final Impressora a = new Impressora();

    static final Impressora b = new Impressora();

    public static class Thread1 implements Runnable {

        @Override
        public void run() {
            synchronized (a) {
                System.out.println("Thread1");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
                synchronized (b) {
                    System.out.println("Thread1");
                }
            }
        }
    }

    public static class Thread2 implements Runnable {

        @Override
        public void run() {
            synchronized (b) {
                System.out.println("Thread2");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
                synchronized (a) {
                    System.out.println("Thread2");
                }
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new Thread1()).start();
        new Thread(new Thread2()).start();
    }
}

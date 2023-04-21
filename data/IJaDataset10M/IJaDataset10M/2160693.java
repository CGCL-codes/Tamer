package thread;

/**
 * @keyword
 */
public class ThreadInterrupt implements Runnable {

    public static boolean doSpin = true;

    public void run() {
        boolean state = true;
        try {
            int s = 0;
            while (doSpin) {
                s++;
            }
            state = false;
            Thread.sleep(3000);
            System.out.println("fail");
        } catch (InterruptedException e) {
            if (state) {
                System.out.println("fail, interrupt should happen in sleep() call");
            } else {
                System.out.println("pass");
            }
        }
    }

    public static void main(String[] args) {
        Thread x = new Thread(new ThreadInterrupt());
        x.start();
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("fail, " + e);
        }
        System.out.println("interrupting...");
        x.interrupt();
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("fail, " + e);
        }
        doSpin = false;
        try {
            x.join();
        } catch (Exception e) {
            System.out.println("fail, " + e);
        }
    }
}

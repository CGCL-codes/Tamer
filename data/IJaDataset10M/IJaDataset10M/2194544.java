package ncr;

public class EraserThread implements Runnable {

    public EraserThread(String prompt) {
        System.out.print("" + prompt + " ");
    }

    public void run() {
        for (stop = true; stop; ) {
            try {
                Thread.currentThread();
                Thread.sleep(1L);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            System.out.print("\b\b  ");
        }
    }

    public void stopMasking() {
        stop = false;
    }

    private boolean stop;
}

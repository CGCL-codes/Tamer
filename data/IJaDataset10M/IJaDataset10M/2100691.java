package dscript;

public class SPThread extends Thread {

    private boolean hibernating;

    public void hibernate() {
        hibernating = true;
    }

    public synchronized boolean hibernating() {
        while (hibernating) {
            try {
                wait(100);
            } catch (Exception e) {
            }
        }
        return hibernating;
    }

    public void wake() {
        hibernating = false;
    }
}

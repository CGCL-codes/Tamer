package pt.helper;

public class Clock implements Runnable {

    private int time = 0;

    public void run() {
        while (true) {
            time++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public int getTime() {
        return time;
    }
}

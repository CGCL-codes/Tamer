package emulator.hardware.clock.test;

import java.util.Observable;
import java.util.Observer;
import emulator.hardware.clock.ClockImplementation;

public class TestClock {

    static final int[] inc_values = { 1, 2, 3, 5, 23 };

    private long clock_speed = 0;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        TestClock test = new TestClock();
        try {
            test.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run() throws Exception {
        ClockImplementation clock = new ClockImplementation();
        clock.addClockSpeedObserver(new ClockSpeedObserver());
        WorkerThread[] threads = new WorkerThread[inc_values.length];
        for (int i = 0; i < inc_values.length; i++) {
            threads[i] = new WorkerThread(clock, inc_values[i]);
            threads[i].start();
        }
        System.out.println(inc_values.length + " threads counting...");
        waitForClockSpeed();
        WorkerThread.stopThread();
        System.out.println("clock speed = " + clock_speed);
        for (int i = 0; i < inc_values.length; i++) {
            threads[i].interrupt();
        }
    }

    public synchronized void waitForClockSpeed() throws InterruptedException {
        clock_speed = 0;
        while (clock_speed == 0) wait();
    }

    public synchronized void notifyClockSpeed(long clock_speed) {
        this.clock_speed = clock_speed;
        notifyAll();
    }

    class ClockSpeedObserver implements Observer {

        @Override
        public void update(Observable arg0, Object clock_speed) {
            notifyClockSpeed((Long) clock_speed);
        }
    }
}

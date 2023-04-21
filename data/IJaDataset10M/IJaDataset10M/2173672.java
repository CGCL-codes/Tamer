package timers;

import my.SimulatorGUI1.Timer;
import my.SimulatorGUI1.Handler;

/**
 *
 * @author jda99448
 */
public class TimerTest00_jda implements Handler {

    public Timer testTimer;

    public static void main(String args[]) {
        System.out.println("Starting test Timer");
        TimerTest00_jda myTest = new TimerTest00_jda();
        myTest.testTimer = new Timer("Hello", 1, 10, myTest);
        Thread runner = new Thread(myTest.testTimer);
        runner.start();
    }

    public void handle(String message) {
        System.out.println("testTimer says: \"" + message + "\"");
    }
}

package bman.game.engine;

/**
 * This is a utility class used to set the frame rate of the GraphicsEngine.
 * First set a desired frame rate. Then call the update() method of the Timer 
 * instance every cycle of a process. For the GraphcisEngine a cycle is after
 * rendering everything that needs to be displayed to the screen. After calling,
 * the update(), call get getSleep() method to get the amount of time that
 * a thread needs to sleep in order to achieve the desired frame rate.
 * 
 * @author MrJacky
 *
 */
public class Timer extends Thread {

    boolean run = true;

    int loops;

    int lastFPS;

    long lastCall = System.currentTimeMillis();

    int fps = 25;

    int pause = 1000 / fps;

    public Timer() {
        start();
    }

    public void update() {
        lastCall = System.currentTimeMillis();
        loops++;
    }

    public long getSleep() {
        long procTime = System.currentTimeMillis() - lastCall;
        long timeToSleep = 0;
        if (procTime < pause) {
            timeToSleep = pause - procTime;
        }
        return timeToSleep;
    }

    /**
	 * Gives the actual frame rate. Acuracy of the value returned is dependent on
	 * how the update() and getSleep() methods are called.
	 */
    public int getFPS() {
        return lastFPS;
    }

    public void run() {
        while (run) {
            try {
                lastFPS = loops / 5;
                loops = 0;
                this.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

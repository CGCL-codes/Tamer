package com.grt192.core;

/**
 *
 * @author anand
 */
public abstract class StepController extends Controller {

    public static final int SLEEP_INTERVAL = 50;

    public void run() {
        running = true;
        while (running) {
            try {
                act();
                sleep(SLEEP_INTERVAL);
            } catch (Exception e) {
                e.printStackTrace();
                stopControl();
            }
        }
    }

    public abstract void act();
}
